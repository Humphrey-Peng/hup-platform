package com.zan.hu.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.zan.hu.common.entity.AccessTokenDto;
import com.zan.hu.common.service.RedisService;
import com.zan.hu.common.utils.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-05-04 12:38
 * @Description 鉴权过滤器
 **/
@Component
@Slf4j
public class AuthorizationFilter implements GlobalFilter {

    @Autowired
    private RedisService redisService;

    private static final String BEARER_TYPE = "Bearer";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Boolean predicate = predicate(exchange);
        if (!predicate) {
            String token = extractHeaderToken(request);
            if (StringUtils.isBlank(token)) {
                log.debug("Token not found in headers. Trying request parameters.");
                return chain.filter(exchange);
            }
            Set<Object> accessTokens = redisService.members(token);
            AccessTokenDto accessTokenDto = getAccessTokenDto(accessTokens);
            boolean expires = isExpires(accessTokenDto.getExpiresIn());
            if (expires) {
                log.debug("Token is expires. log in again.");
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }

    private Boolean predicate(ServerWebExchange serverWebExchange) {
        URI uri = serverWebExchange.getRequest().getURI();
        String requestUri = uri.getPath();
        String authorization = extractHeaderToken(serverWebExchange.getRequest());
        if (isSignIn(requestUri) && StringUtils.isBlank(authorization)) {//登录请求
            return true;
        }
        return false;
    }

    private String extractHeaderToken(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get("Authorization");
        if (Objects.nonNull(headers) && headers.size() > 0) { // typically there is only one (most servers enforce that)
            String value = headers.get(0);
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                // Add this here for the auth details later. Would be better to change the signature of this method.
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }

    private boolean isSignIn(String url) {
        return url.contains("/oauth/token");
    }

    private boolean isLogoutUrl(String url) {
        return url.contains("/login/logout");
    }

    private boolean isExpires(Long expiresIn) {
        Long now = new Date().getTime();
        if (expiresIn < now)
            return true;
        return false;
    }

    private AccessTokenDto getAccessTokenDto(Set<Object> accessTokens) {
        List<AccessTokenDto> accessTokenDtos = null;
        for (Object accessToken : accessTokens) {
            ObjectMapper objectMapper = ObjectMapperUtils.newInstance();
            try {
                accessTokenDtos = objectMapper.readValue(accessToken.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, AccessTokenDto.class));
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return CollectionUtils.isEmpty(accessTokenDtos) ? null : accessTokenDtos.get(0);
    }
}

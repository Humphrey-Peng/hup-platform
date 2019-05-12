package com.zan.hu.gateway.filter;

import com.zan.hu.gateway.exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;

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
    private TokenStore tokenStore;

    private static final String BEARER_TYPE = "Bearer";

    private DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        defaultTokenServices.setTokenStore(tokenStore);
        ServerHttpRequest request = exchange.getRequest();
        String token = extractHeaderToken(request);
        Boolean predicate = predicate(request, token);
        if (predicate) {//如果是登录请求
            log.info(exchange.getAttribute("username") + "正在登录！");
            return chain.filter(exchange);
        }
        if (StringUtils.isBlank(token)) {
            log.error("Token not found in headers. Trying request parameters.");
            throw new ExceptionHandler("Token is empty");
        }
        try {
            OAuth2Authentication oAuth2Authentication = defaultTokenServices.loadAuthentication(token);
            if (!oAuth2Authentication.getUserAuthentication().isAuthenticated()) {
                log.info(oAuth2Authentication.getUserAuthentication().getName() + "未认证用户");
                throw new ExceptionHandler(oAuth2Authentication.getUserAuthentication().getName() + "未认证用户,请登录认证");
            }
            ServerHttpRequest user = exchange.getRequest().mutate().header("user", "").build();
            exchange = exchange.mutate().request(user).build();
        } catch (AuthenticationException e) {
            throw new ExceptionHandler(e.getMessage());
        }
        return chain.filter(exchange);
    }

    private Boolean predicate(ServerHttpRequest request, String token) {
        URI uri = request.getURI();
        String requestUri = uri.getPath();
        if (isSignIn(requestUri) && StringUtils.isBlank(token)) {//登录请求
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

}

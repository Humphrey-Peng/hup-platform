package com.zan.hu.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zan.hu.common.utils.ObjectMapperUtils;
import com.zan.hu.gateway.exception.ExceptionHandler;
import com.zan.hu.gateway.properties.WhiteList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-05-04 12:38
 * @Description 鉴权过滤器
 **/
@EnableConfigurationProperties(value = WhiteList.class)
@Slf4j
@Configuration
public class AuthorizationFilter implements GlobalFilter {

    @Autowired
    private TokenStore tokenStore;


    private WhiteList whiteList;

    public AuthorizationFilter(WhiteList whiteList) {
        this.whiteList = whiteList;
    }

    private static final String BEARER_TYPE = "Bearer";

    private DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        defaultTokenServices.setTokenStore(tokenStore);
        String token = extractHeaderToken(exchange);
        boolean aWhile = isWhile(exchange);
        if (aWhile) {//如果是白名单配置
            return chain.filter(exchange);
        }
        if (StringUtils.isBlank(token)) {
            log.error("Token not found in headers. Trying request parameters.");
            throw new ExceptionHandler("Token is empty");
        }
        OAuth2Authentication oAuth2Authentication = defaultTokenServices.loadAuthentication(token);
        if (!oAuth2Authentication.getUserAuthentication().isAuthenticated()) {
            log.info(oAuth2Authentication.getUserAuthentication().getName() + "未认证用户");
            throw new ExceptionHandler(oAuth2Authentication.getUserAuthentication().getName() + "未认证用户,请登录认证");
        }
        String details = "";
        try {
            details = ObjectMapperUtils.newInstance().writeValueAsString(oAuth2Authentication.getDetails());
        } catch (JsonProcessingException e) {
            log.info("OAuth2Authentication details transfer to json fail");
        }
        String encode = null;
        try {
            encode = URLEncoder.encode(details, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServerHttpRequest tempRequest = exchange.getRequest().mutate().header("details", encode).build();
        exchange = exchange.mutate().request(tempRequest).build();
        return chain.filter(exchange);
    }

    private String extractHeaderToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
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

    private boolean isWhile(ServerWebExchange exchange) {
        Object attribute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        Route currentRoute = (Route) attribute;
        ServerHttpRequest request = exchange.getRequest();
        String methodValue = request.getMethodValue();
        URI uri = request.getURI();
        String path = uri.getPath();
        if (whiteList != null && !CollectionUtils.isEmpty(whiteList.getRouteIds())) {
            for (WhiteList.RouteId routeId : whiteList.getRouteIds()) {
                if (Objects.equals(routeId.getServiceName(), currentRoute.getId()) && !CollectionUtils.isEmpty(routeId.getRoutes())) {
                    for (WhiteList.Route route : routeId.getRoutes()) {
                        if (Objects.equals(route.getHttpMethod(), methodValue.toLowerCase()) && Objects.equals(route.getPath(), path))
                            return true;
                    }
                }
            }
        }
        return false;
    }

}

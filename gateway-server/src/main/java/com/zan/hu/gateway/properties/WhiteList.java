package com.zan.hu.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-05-26 16:02
 * @Description 白名单配置
 **/
@Data
@ConfigurationProperties(prefix = "white.list")
public class WhiteList {

    List<RouteId> routeIds = new ArrayList<>();

    @Data
    public static class RouteId {
        /**
         * 请求的路由id
         */
        private String serviceName;

        private List<Route> routes = new ArrayList<>();
    }

    @Data
    public static class Route {
        /**
         * 请求方式
         */
        String httpMethod;
        /**
         * 请求路径
         */
        String path;
    }
}

package com.zan.hu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-05-12 00:38
 * @Description todo
 **/
@EnableWebFluxSecurity
public class WebSecurityConfig {

    /**
     * 网关引入security 一定要配置disable csrf
     * @param http
     * @return
     */

    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {
        http.cors().and()
                .httpBasic().and()
                .csrf().disable();
        return http.build();
    }
}

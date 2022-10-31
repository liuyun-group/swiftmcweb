package com.flyan.swiftmcweb.core.framework.security.config;

import com.flyan.swiftmcweb.core.framework.security.core.filter.JwtAuthenticationTokenFilter;
import com.flyan.swiftmcweb.core.framework.security.core.bean.JwtEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * @author flyan
 * @version 1.0
 * @date 9/15/22
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static String[] OPEN_PATHS = {
            /* message ipc */
            "/sendrec",

            /* custom */

            /* smart-doc */
            "/doc/*",

            /* swagger */
            "/doc.html",
            "/favicon.ico",
            "/druid/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs/**",
            "/v2/api-docs-ext",
            "/v3/api-docs/**",
            "/v3/api-docs-ext"
    };

    public static String[] BAN_PATHS = {
        "/mipc/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint())
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(OPEN_PATHS).permitAll()
                .antMatchers(BAN_PATHS).denyAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().cacheControl();
    }

    @Bean
    public JwtEntryPoint jwtEntryPoint() {
        return new JwtEntryPoint();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

}

package com.flyan.swiftmcweb.core.framework.security.config;

import com.flyan.swiftmcweb.core.framework.security.core.bean.JwtEntryPoint;
import com.flyan.swiftmcweb.core.framework.security.core.filter.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author flyan
 * @version 1.0
 * @date 9/15/22
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static List<String> OPEN_PATHS = Arrays.stream(new String[] {
            /* message ipc */
            "/sendrec",

            /* custom */

            /* smart-doc */
            "/doc/*",

            /* static files */
            "/static/**",

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
    }).collect(Collectors.toList());

    public static List<String> BAN_PATHS = Arrays.stream(new String[] {
            "/mipc/**"
    }).collect(Collectors.toList());

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint())
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(OPEN_PATHS.toArray(new String[0])).permitAll()
                .antMatchers(BAN_PATHS.toArray(new String[0])).denyAll()
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

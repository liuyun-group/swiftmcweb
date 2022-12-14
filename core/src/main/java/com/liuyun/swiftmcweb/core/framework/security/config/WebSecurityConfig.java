package com.liuyun.swiftmcweb.core.framework.security.config;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.liuyun.swiftmcweb.core.annotation.OriginApi;
import com.liuyun.swiftmcweb.core.framework.security.core.bean.JwtEntryPoint;
import com.liuyun.swiftmcweb.core.framework.security.core.filter.JwtAuthenticationTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsUtils;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

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
        /* ??????????????????????????? */
        String[] controllerBeanNames = applicationContext.getBeanNamesForAnnotation(Controller.class);
        if (ArrayUtil.isNotEmpty(controllerBeanNames)) {
            List<String> openApis = new ArrayList<>();
            for (String controllerBeanName : controllerBeanNames) {
                Object controllerBean = applicationContext.getBean(controllerBeanName);
                String requestPrefixPath = standardApiPath(
                        !controllerBean.getClass().isAnnotationPresent(RequestMapping.class) ? "" :
                                controllerBean.getClass().getAnnotation(RequestMapping.class).value()[0]
                );

                Method[] publicMethods = ReflectUtil.getPublicMethods(controllerBean.getClass());
                for (Method method : publicMethods) {
                    if (method.isAnnotationPresent(OriginApi.class)) {
                        OriginApi originApiAnno = method.getAnnotation(OriginApi.class);
                        if(originApiAnno.auth()) {
                            continue;
                        }

                        for (Annotation annotation : method.getAnnotations()) {
                            if (annotation instanceof RequestMapping mappingAnno) {
                                openApis.addAll(Arrays.stream(mappingAnno.value()).map(api -> requestPrefixPath + standardApiPath(api)).toList());
                            } else if (annotation instanceof GetMapping mappingAnno) {
                                openApis.addAll(Arrays.stream(mappingAnno.value()).map(api -> requestPrefixPath + standardApiPath(api)).toList());
                            } else if (annotation instanceof PostMapping mappingAnno) {
                                openApis.addAll(Arrays.stream(mappingAnno.value()).map(api -> requestPrefixPath + standardApiPath(api)).toList());
                            } else if (annotation instanceof PutMapping mappingAnno) {
                                openApis.addAll(Arrays.stream(mappingAnno.value()).map(api -> requestPrefixPath + standardApiPath(api)).toList());
                            } else if (annotation instanceof DeleteMapping mappingAnno) {
                                openApis.addAll(Arrays.stream(mappingAnno.value()).map(api -> requestPrefixPath + standardApiPath(api)).toList());
                            }
                        }
                    }
                }
            }
            WebSecurityConfig.OPEN_PATHS.addAll(openApis);
            log.info("Open apis has been configured\n{}", openApis);
        }

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


    /* ====================== */

    private String standardApiPath(@NotNull String apiPath) {
        if(apiPath.isBlank()) {
            return "/";
        }
        return apiPath.charAt(0) == '/' ? apiPath : '/' + apiPath;
    }

}

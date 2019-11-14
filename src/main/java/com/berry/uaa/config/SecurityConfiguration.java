package com.berry.uaa.config;

import com.berry.uaa.security.AuthoritiesConstants;
import com.berry.uaa.security.FilterConfigurer;
import com.berry.uaa.security.filter.TokenProvider;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/13 16:47
 * fileName：AuthorizationServerConfig
 * Use：安全配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements InitializingBean {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    public SecurityConfiguration(UserDetailsService userDetailsService,
                                 AuthenticationManagerBuilder authenticationManagerBuilder,
                                 TokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // 安全放行url
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/i18n/**")
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/actuator/health")
                .antMatchers("/content/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                // 自定义表单登录
                .formLogin().loginPage("/auth/login").permitAll()
                .and()
                // url 安全配置
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").hasAuthority(AuthoritiesConstants.ADMIN)
                .anyRequest().authenticated()
                .and()
                .apply(securityConfigurerAdapter());
    }

    private FilterConfigurer securityConfigurerAdapter() {
        return new FilterConfigurer(this.tokenProvider);
    }
}

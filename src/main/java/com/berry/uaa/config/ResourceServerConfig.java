package com.berry.uaa.config;

import com.berry.uaa.security.AuthoritiesConstants;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/13 16:47
 * fileName：AuthorizationServerConfig
 * Use：资源配置
 */
//@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    private final CorsFilter corsFilter;

    ResourceServerConfig(TokenStore tokenStore, CorsFilter corsFilter) {
        this.tokenStore = tokenStore;
        this.corsFilter = corsFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 仅检测 任何以 api开头的 url资源作为保护对象，剩下的 交给 `SecurityConfiguration` 处理
                .requestMatchers()
                .antMatchers("/api/**")
                .and()
                // 禁用 csrf 检测
                .csrf()
                .disable()
                // 添加 用户名密码授权过滤器
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 不允许被 iframe
                .headers()
                .frameOptions()
                .disable()
                .and()
                // 无状态，不保存 任何 session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 被保护的资源 地址
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/activate").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/account/reset-password/init").permitAll()
                .antMatchers("/api/account/reset-password/finish").permitAll()
                .antMatchers("/api/management/health").permitAll()
                .antMatchers("/api/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/**").authenticated()
                .and().httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }
}
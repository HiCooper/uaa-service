package com.berry.authserver.config;

import com.berry.authserver.security.AuthoritiesConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableAuthorizationServer
public class UaaConfiguration extends AuthorizationServerConfigurerAdapter implements ApplicationContextAware {

    private static final String DEMO_RESOURCE_ID = "order";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Configuration
    @EnableResourceServer
    public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        private final TokenStore tokenStore;

        private final CorsFilter corsFilter;

        ResourceServerConfiguration(TokenStore tokenStore, CorsFilter corsFilter) {
            this.tokenStore = tokenStore;
            this.corsFilter = corsFilter;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .and()
                    .csrf()
                    .disable()
                    .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                    .headers()
                    .frameOptions()
                    .disable()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/api/activate").permitAll()
                    .antMatchers("/api/authenticate").permitAll()
                    .antMatchers("/api/account/reset-password/init").permitAll()
                    .antMatchers("/api/account/reset-password/finish").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/management/health").permitAll()
                    .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/v2/api-docs/**").permitAll()
                    .antMatchers("/swagger-resources/configuration/ui").permitAll()
                    .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(DEMO_RESOURCE_ID).tokenStore(tokenStore);
        }
    }

    private final UaaProperties uaaProperties;


    private final RedisConnectionFactory redisConnectionFactory;

    @Resource
    private DataSource dataSource;

    public UaaConfiguration(UaaProperties uaaProperties, RedisConnectionFactory redisConnectionFactory) {
        this.uaaProperties = uaaProperties;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * 注入AuthenticationManager ，密码模式用到
     */
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    /**
     * @return the {@link RedisTokenStore} managing the tokens.
     */
    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * This bean generates an token enhancer, which manages the exchange between JWT access tokens and Authentication
     * in both directions.
     *
     * @return an access token converter configured with the authorization server's public/private keys.
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource(uaaProperties.getKeyStore().getName()), uaaProperties.getKeyStore().getPassword().toCharArray())
                .getKeyPair(uaaProperties.getKeyStore().getAlias());
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //pick up all  TokenEnhancers incl. those defined in the application
        //this avoids changes to this class if an application wants to add its own to the chain
        Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .reuseRefreshTokens(false);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }
}

package com.berry.uaa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2020/11/10 14:27
 * fileName：GlobalProperties
 * Use：
 */
@Component
@ConfigurationProperties(prefix = "global", ignoreUnknownFields = false)
public class GlobalProperties {

    private final CorsConfiguration cors = new CorsConfiguration();

    public CorsConfiguration getCors() {
        return cors;
    }
}

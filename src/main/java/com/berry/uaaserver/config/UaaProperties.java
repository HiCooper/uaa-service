package com.berry.uaaserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties for UAA-based OAuth2 security.
 */
@Component
@ConfigurationProperties(prefix = "uaa", ignoreUnknownFields = false)
public class UaaProperties {
    private KeyStore keyStore = new KeyStore();

    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * Keystore configuration for signing and verifying JWT tokens.
     */
    public static class KeyStore {
        //name of the keystore in the classpath
        private String name = "keystore.p12";
        //password used to access the key
        private String password = "password";
        //name of the alias to fetch
        private String alias = "selfsigned";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }
}

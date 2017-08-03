package com.priceline.cloud.samples.serviceregistryconsul;

import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.priceline.cloud.samples.serviceregistryconsul.ServiceRegistryConsulApplication.HelloProperties;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(HelloProperties.class)
public class ServiceRegistryConsulApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(ServiceRegistryConsulApplication.class, args);
    }

    @Bean
    public HelloProperties helloProperties() {
        return new HelloProperties();
    }

    @ConfigurationProperties
    public static class HelloProperties {
        private String key;

        public String getKey() {
            return key;
        }
    }
}

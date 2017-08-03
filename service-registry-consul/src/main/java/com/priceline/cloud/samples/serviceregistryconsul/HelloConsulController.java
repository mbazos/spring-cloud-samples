package com.priceline.cloud.samples.serviceregistryconsul;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priceline.cloud.samples.serviceregistryconsul.ServiceRegistryConsulApplication.HelloProperties;

@RestController
public class HelloConsulController {
    private final DiscoveryClient discoveryClient;
    private final HelloProperties helloProperties;
    private final String          propValueInject;

    @Autowired
    public HelloConsulController(DiscoveryClient discoveryClient, HelloProperties helloProperties, @Value("${key}") String propValueInject) {
        this.discoveryClient = discoveryClient;
        this.helloProperties = helloProperties;
        this.propValueInject = propValueInject;
    }

    @GetMapping("/get-prop")
    public HelloPropertiesResult getProp() {
        return new HelloPropertiesResult(propValueInject, helloProperties);
    }

    @GetMapping("/consul-info")
    public ConsulInfo getConsulInfo() {
        return new ConsulInfo(discoveryClient);
    }

    static final class HelloPropertiesResult {
        private final String          keyFromValueInject;
        private final HelloProperties keyFromConfigurationProperties;

        HelloPropertiesResult(String keyFromValueInject, HelloProperties keyFromConfigurationProperties) {
            this.keyFromValueInject = keyFromValueInject;
            this.keyFromConfigurationProperties = keyFromConfigurationProperties;
        }

        public String getKeyFromValueInject() {
            return keyFromValueInject;
        }

        public HelloProperties getKeyFromConfigurationProperties() {
            return keyFromConfigurationProperties;
        }
    }

    static class ConsulInfo {
        private final Map<String, List<ServerInstance>> info = new HashMap<>();

        ConsulInfo(DiscoveryClient client) {
            client.getServices().forEach(s -> {
                List<ServerInstance> collect = client.getInstances(s)
                        .stream()
                        .map(ServerInstance::new)
                        .collect(Collectors.toList());
                info.put(s, collect);
            });
        }

        public Map<String, List<ServerInstance>> getInfo() {
            return info;
        }
    }

    static class ServerInstance {
        private final String              serviceId;
        private final String              host;
        private final int                 port;
        private final boolean             secure;
        private final URI                 uri;
        private final Map<String, String> metadata;

        ServerInstance(org.springframework.cloud.client.ServiceInstance instance) {
            this.serviceId = instance.getServiceId();
            this.host = instance.getHost();
            this.port = instance.getPort();
            this.secure = instance.isSecure();
            this.uri = instance.getUri();
            this.metadata = instance.getMetadata();
        }

        public String getServiceId() {
            return serviceId;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public boolean isSecure() {
            return secure;
        }

        public URI getUri() {
            return uri;
        }

        public Map<String, String> getMetadata() {
            return metadata;
        }
    }
}

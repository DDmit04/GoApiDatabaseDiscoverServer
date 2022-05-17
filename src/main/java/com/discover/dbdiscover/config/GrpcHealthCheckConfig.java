package com.discover.dbdiscover.config;

import io.grpc.BindableService;
import io.grpc.services.HealthStatusManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Daniil Dmitrochenkov
 **/
@Configuration
public class GrpcHealthCheckConfig {

    @Bean
    public HealthStatusManager healthStatusManager1() {
        return new HealthStatusManager();
    }
}

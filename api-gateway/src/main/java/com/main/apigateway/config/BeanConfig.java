package com.main.apigateway.config;

import com.main.apigateway.helpers.ServerUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BeanConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new CustomAuditAware();
    }

    public static class CustomAuditAware implements AuditorAware<String> {

        @NonNull
        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of(ServerUtil.LOCAL_HOST_NAME);
        }

    }

}

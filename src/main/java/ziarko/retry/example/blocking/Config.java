package ziarko.retry.example.blocking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.*;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import ziarko.retry.weather.WeatherServiceConfig;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(WeatherServiceConfig.class)
class Config {

    @Bean
    public Map<Class<? extends Throwable>, Boolean> retryableExceptions() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(Exception.class, true);
        return retryableExceptions;
    }

    @Bean
    public RetryPolicy retryPolicy() {
        return new SimpleRetryPolicy(10, retryableExceptions());
    }

    @Bean
    public BackOffPolicy backOffPolicy() {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(3000);
        return fixedBackOffPolicy;
    }

    @Bean
    public LoggingErrorsRetryListener retryListener() {
        return new LoggingErrorsRetryListener();
    }

    @Bean
    public RetryOperations retryOperations() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy());
        retryTemplate.setBackOffPolicy(backOffPolicy());
        retryTemplate.registerListener(retryListener());
        return retryTemplate;
    }

}

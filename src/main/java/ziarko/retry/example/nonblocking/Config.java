package ziarko.retry.example.nonblocking;

import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import ziarko.retry.example.blocking.LoggingErrorsRetryListener;
import ziarko.retry.weather.WeatherServiceConfig;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Import(WeatherServiceConfig.class)
class Config {

    @Bean
    public RetryExecutor retryExecutor() {
        return new AsyncRetryExecutor(scheduler())
                .retryOn(Exception.class)
                .withMaxRetries(5)
                .withExponentialBackoff(1000, 2);
    }

    @Bean(destroyMethod = "shutdownNow")
    public ScheduledExecutorService scheduler() {
        return Executors.newScheduledThreadPool(20);
    }

}

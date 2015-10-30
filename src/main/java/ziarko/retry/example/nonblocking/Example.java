package ziarko.retry.example.nonblocking;

import com.nurkiewicz.asyncretry.RetryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.retry.RetryOperations;
import ziarko.retry.weather.WeatherService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Example {

    static final Logger LOG = LoggerFactory.getLogger(Example.class);
    static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    static final RetryExecutor retryExecutor = applicationContext.getBean(RetryExecutor.class);
    static final WeatherService weatherService = applicationContext.getBean(WeatherService.class);

    public static void main(String[] args) throws Exception {
        String query = "Mumbai";

        CompletableFuture<Optional<String>> future = retryExecutor.getWithRetry(
                context -> weatherService.getConditionsFor(query)
        );

        future.whenComplete((maybeConditions, error) -> {
            if (error != null) {
                LOG.error("Query '{}' failed", query);
            } else {
                if (maybeConditions.isPresent()) {
                    LOG.info("Conditions in '{}' are '{}'", query, maybeConditions.get());
                } else {
                    LOG.info("City '{}' was not found!", query);
                }
            }
        });

        LOG.info("Main thread finished!");
    }


}

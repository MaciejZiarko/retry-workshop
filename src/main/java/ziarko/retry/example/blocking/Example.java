package ziarko.retry.example.blocking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.retry.RetryOperations;
import ziarko.retry.weather.WeatherService;

import java.util.Optional;

public class Example {

    static final Logger LOG = LoggerFactory.getLogger(Example.class);
    static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    static final RetryOperations retryOperations = applicationContext.getBean(RetryOperations.class);
    static final WeatherService weatherService = applicationContext.getBean(WeatherService.class);

    public static void main(String[] args) {
        String query = "Mumbai";

        Optional<String> maybeConditions = retryOperations.execute(
                context -> weatherService.getConditionsFor(query)
        );

        if (maybeConditions.isPresent()) {
            LOG.info("Conditions in '{}' are '{}'", query, maybeConditions.get());
        } else {
            LOG.info("City '{}' was not found!", query);
        }
    }


}

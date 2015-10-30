package ziarko.retry.weather;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherServiceConfig {

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }

    @Bean
    public WeatherService weatherService() {
        return new YahooWeatherService(restOperations());
    }

}

package ziarko.retry.weather;

import java.util.Optional;

public interface WeatherService {

    Optional<String> getConditionsFor(String query);

}

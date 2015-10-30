package ziarko.retry.weather;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;

public class YahooWeatherService implements WeatherService {

    private static final Logger LOG = LoggerFactory.getLogger(YahooWeatherService.class);
    private static final String BASE_URL = "https://query.yahooapis.com/v1/public/yql";
    private final RestOperations restOperations;

    public YahooWeatherService(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Override
    public Optional<String> getConditionsFor(String query) {
        LOG.info("Querying: '{}'", query);
        final String urlForQuery = getUrlForQuery(query);
        final ResponseEntity<String> responseEntity = restOperations.getForEntity(urlForQuery, String.class);
        return extractConditionsFromResponseEntity(responseEntity);
    }

    private Optional<String> extractConditionsFromResponseEntity(ResponseEntity<String> responseEntity) {
        String body = responseEntity.getBody();
        JsonObject jsonObject = new Gson().fromJson(body, JsonObject.class);
        JsonElement results = jsonObject.getAsJsonObject("query").get("results");
        if (results.isJsonNull()) {
            LOG.info("No result found");
            return Optional.empty();
        } else {
            String conditions = results.getAsJsonObject()
                    .getAsJsonObject("channel")
                    .getAsJsonObject("item")
                    .getAsJsonObject("condition")
                    .getAsJsonPrimitive("text").getAsString();
            LOG.info("Conditions: '{}'", conditions);
            return Optional.of(conditions);
        }
    }

    private String getUrlForQuery(String query) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", getQParamValueForQuery(query))
                .queryParam("format", "json");
        return builder.build().toString();
    }

    private String getQParamValueForQuery(String query) {
        return "select item.condition from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + query + "\")";
    }
}

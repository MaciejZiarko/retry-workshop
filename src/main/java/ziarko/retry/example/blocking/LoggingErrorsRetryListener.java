package ziarko.retry.example.blocking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

public class LoggingErrorsRetryListener extends RetryListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingErrorsRetryListener.class);

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        LOG.error("Error occurred on {} attempt", context.getRetryCount() + 1);
    }
}

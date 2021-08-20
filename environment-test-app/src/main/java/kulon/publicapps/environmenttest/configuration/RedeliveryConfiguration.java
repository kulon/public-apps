package kulon.publicapps.environmenttest.configuration;

import org.apache.camel.LoggingLevel;
import org.apache.camel.processor.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedeliveryConfiguration {
	
	@Bean
    public RedeliveryPolicy standardRedeliveryPolicy(
    		@Value("${redelivery.maximumRedeliveries}")	int maximumRedeliveries,
    		@Value("${redelivery.redeliveryDelay}") int redeliveryDelay,
    		@Value("${redelivery.useExponentialBackOff:false}") boolean useExponentialBackOff,
    		@Value("${redelivery.backOffMultiplier:1}") int backOffMultiplier) {
    		
		RedeliveryPolicy policy = new RedeliveryPolicy()
			.maximumRedeliveries(maximumRedeliveries)
			.redeliveryDelay(redeliveryDelay)
			.logExhaustedMessageBody(true)
			.logRetryAttempted(true)
			.retryAttemptedLogLevel(LoggingLevel.WARN);
		
		if (useExponentialBackOff) {
			policy.useExponentialBackOff().backOffMultiplier(backOffMultiplier);
		}

        return policy;
    }
}

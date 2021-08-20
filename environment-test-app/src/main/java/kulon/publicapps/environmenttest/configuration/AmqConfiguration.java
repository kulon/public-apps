package kulon.publicapps.environmenttest.configuration;

import javax.jms.JMSException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqConfiguration {

	public static final String COMPONENT_NAME = "test-amq";
	
	private String brokerURL;
	private int concurrentConsumers;
	private String deliveryQueue;

	public AmqConfiguration(			
			@Value("${ep.amq.brokerURL}") String brokerURL,
			@Value("${ep.amq.concurrentConsumers}") int concurrentConsumers,
			@Value("${ep.amq.queue}") String deliveryQueue) {
		
		this.brokerURL = brokerURL;
		this.concurrentConsumers = concurrentConsumers;
		this.deliveryQueue = deliveryQueue;
	}
	
	@Bean(COMPONENT_NAME)
	public JmsComponent testAmq()
			throws JMSException {

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(getBrokerURL());
		
		JmsConfiguration configuration = new JmsConfiguration();
		configuration.setConnectionFactory(connectionFactory);
		configuration.setConcurrentConsumers(getConcurrentConsumers());

		JmsComponent jmsComponent = new JmsComponent(configuration);

		return jmsComponent;
	}

	public String getBrokerURL() {
		return brokerURL;
	}

	public int getConcurrentConsumers() {
		return concurrentConsumers;
	}

	public String getDeliveryQueue() {
		return deliveryQueue;
	}
}

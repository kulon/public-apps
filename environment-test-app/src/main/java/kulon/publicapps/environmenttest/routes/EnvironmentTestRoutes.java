package kulon.publicapps.environmenttest.routes;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.elasticsearch.ElasticsearchConstants;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kulon.publicapps.environmenttest.components.Constants;
import kulon.publicapps.environmenttest.components.TestReport;
import kulon.publicapps.environmenttest.configuration.AmqConfiguration;
import kulon.publicapps.environmenttest.configuration.ElasticConfiguration;

@Component
public class EnvironmentTestRoutes extends RouteBuilder {

//	@Autowired()
//	private TestSetup testSetup;
	
	@Autowired
	private AmqConfiguration amqConfiguration;
	
	@Autowired
	private ElasticConfiguration elasticConfiguration;
	
	@Override
	public void configure() throws Exception {

		final String ELASTIC_URL = elasticConfiguration.getElasticCluster() + "?operation=Index&indexName=" + elasticConfiguration.getElasticIndex();
		
		rest("/environment-test/{testType}").get()
	    	.id("restRunTest")
	    	.produces("application/json")
		    .description("Send a GET request to start a test")
	    	.param().name("testType")
	    		.description("Specifies which test to run: amq, elastic, kafka, redis, sql etc.")
	    		.type(RestParamType.path).required(true)
	    	.endParam()
		    .outType(TestReport.class)
		.to("direct:startTest");
	
		from ("direct:startTest")
		.choice()
			.when(header("testType").isEqualTo("amq"))
				.process ((Exchange e) -> { e.getIn().setBody("Test message sent to AMQ Broker "  + amqConfiguration.getBrokerURL() +
					" on " + amqConfiguration.getDeliveryQueue() + " queue at " + Calendar.getInstance().getTime());})
				.to(ExchangePattern.InOnly, "seda:amqTest")
		    .endChoice()
			.when(header("testType").isEqualTo("elastic"))
				.process ((Exchange e) -> { e.getIn().setBody("Test message sent to Elastic Cluster "  + ELASTIC_URL + 
						" at " + Calendar.getInstance().getTime());})
				.to(ExchangePattern.InOnly, "seda:elasticTest")
			.endChoice()
			.otherwise()
				.process ((Exchange e) -> { e.getIn().setBody("Unknown type of test: "  + e.getIn().getHeader("testType"));})
			.endChoice()
		.end();

		from ("seda:amqTest")
		.to(AmqConfiguration.COMPONENT_NAME + ":" + amqConfiguration.getDeliveryQueue());
	
		from ("seda:elasticTest")
			.setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, constant(Constants.ELASTIC_INDEX_TYPE))
			.process((Exchange e) -> {
				Map<String, Object> elasticRequest = new HashMap<>();
				elasticRequest.put("message", e.getIn().getBody());
				elasticRequest.put("@timestamp", Calendar.getInstance().getTime());
				e.getIn().setBody(elasticRequest);})
		.to(ElasticConfiguration.COMPONENT_NAME + "://" + ELASTIC_URL);
	}
}

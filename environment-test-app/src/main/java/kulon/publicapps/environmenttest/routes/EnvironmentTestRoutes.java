package kulon.publicapps.environmenttest.routes;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.elasticsearch.ElasticsearchConstants;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kulon.publicapps.environmenttest.components.Constants;
import kulon.publicapps.environmenttest.components.TestReport;
import kulon.publicapps.environmenttest.configuration.AmqConfiguration;
import kulon.publicapps.environmenttest.configuration.ElasticConfiguration;

@Component
public class EnvironmentTestRoutes extends RouteBuilder {

	@Autowired
	private AmqConfiguration amqConfiguration;
	
	@Autowired
	private ElasticConfiguration elasticConfiguration;
	
	@Value("${spring.datasource.url}") 
	private String mysqlUrl;
	
	@Value("${ep.file.folder}") 
	private String fileFolder;
	
	@Override
	public void configure() throws Exception {

		final String ELASTIC_URL = elasticConfiguration.getElasticCluster() + "?operation=Index&indexName=" + elasticConfiguration.getElasticIndex();
		final String HOST_NAME = InetAddress.getLocalHost().getHostName();
		
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
	
		from("direct:startTest")
		.choice()
			.when(header("testType").isEqualTo("amq"))
				.process ((Exchange e) -> { e.getIn().setBody("Test message sent from " + HOST_NAME + " to AMQ Broker "  + amqConfiguration.getBrokerURL() +
					" on " + amqConfiguration.getDeliveryQueue() + " queue at " + Calendar.getInstance().getTime());})
				.to(ExchangePattern.InOnly, "seda:amqTest")
		    .endChoice()
			.when(header("testType").isEqualTo("elastic"))
				.process ((Exchange e) -> { e.getIn().setBody("Test message sent from " + HOST_NAME + " to Elastic Cluster "  + ELASTIC_URL + 
						" at " + Calendar.getInstance().getTime());})
				.to(ExchangePattern.InOnly, "seda:elasticTest")
			.endChoice()
			.when(header("testType").isEqualTo("mysql"))
				.process ((Exchange e) -> { e.getIn().setBody("Test message sent from " + HOST_NAME + " to MySQL " + mysqlUrl + 
						" at " + Calendar.getInstance().getTime());})
				.to(ExchangePattern.InOnly, "seda:mysqlTest")
			.endChoice()
			.when(header("testType").isEqualTo("file"))
				.process ((Exchange e) -> { e.getIn().setBody("Test message sent from " + HOST_NAME + " to the file folder " + fileFolder + 
						" at " + Calendar.getInstance().getTime());})
				.to(ExchangePattern.InOnly, "seda:fileTest")
			.endChoice()
			.otherwise()
				.process ((Exchange e) -> { e.getIn().setBody("Unknown type of test: "  + e.getIn().getHeader("testType"));})
			.endChoice()
		.end();

		from("seda:amqTest")
		.to(AmqConfiguration.COMPONENT_NAME + ":" + amqConfiguration.getDeliveryQueue());
	
		from("seda:elasticTest")
			.setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, constant(Constants.ELASTIC_INDEX_TYPE))
			.process((Exchange e) -> {
				Map<String, Object> elasticRequest = new HashMap<>();
				elasticRequest.put("message", e.getIn().getBody());
				elasticRequest.put("@timestamp", Calendar.getInstance().getTime());
				e.getIn().setBody(elasticRequest);})
		.to(ElasticConfiguration.COMPONENT_NAME + "://" + ELASTIC_URL);

		from("seda:mysqlTest")
		.to("sql:insert into Test (Message) values (:#${body})");

		from("seda:fileTest")
		.to("file:" + fileFolder + "?fileName={{camel.springboot.name}}-${id}.txt");
	}
}

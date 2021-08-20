package kulon.publicapps.environmenttest.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kulon.publicapps.environmenttest.components.TestSetup;

@Configuration
public class TestSetupConfiguration {

	@Bean
	public TestSetup testSetup(
			@Value("${shouldTest.amq}") Boolean amq,
			@Value("${shouldTest.elastic}") Boolean elastic,
			@Value("${shouldTest.kafka}") Boolean kafka,
			@Value("${shouldTest.redis}") Boolean redis,
			@Value("${shouldTest.sql}") Boolean sql
			){

		TestSetup testSetup = new TestSetup(amq, elastic, kafka, redis, sql);
		
		return testSetup;
	}
}

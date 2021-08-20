package kulon.publicapps.environmenttest.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kulon.publicapps.environmenttest.configuration.RestConfiguration;

@Component
public class EnvironmentTestRestApi extends RouteBuilder  {
	
	@Autowired()
	private RestConfiguration configuration;

	@Override
	public void configure() throws Exception {
		
		restConfiguration()
	        .component(configuration.getRestComponent())
	        .scheme(configuration.getRestScheme())
	        .host(configuration.getRestHost())
	        .port(configuration.getRestPort())
	        .bindingMode(RestBindingMode.auto)
	        .dataFormatProperty("prettyPrint", "true")
	        
	        //Swagger
	        .apiContextPath("/environment-test/api-doc")
	        .apiContextRouteId("apiDoc")
	        .apiProperty("api.title", "environment-test-app")
	        .apiProperty("api.description", "This is a REST API to test OpenShift environment setup for the Integration services.")
	        .apiProperty("api.version", "1.0.0");
	}
}

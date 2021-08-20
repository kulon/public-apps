package kulon.publicapps.environmenttest.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfiguration {

	private String restComponent;
	private String restScheme;
	private String restHost;
	private String restPort;
	
	public RestConfiguration(	
			@Value("${ep.rest.component:undertow}") String restComponent,
			@Value("${ep.rest.scheme:http}") String restScheme,
			@Value("${ep.rest.host:localhost}") String restHost,
			@Value("${ep.rest.port:8080}") String restPort) {
		
		this.restComponent = restComponent;
		this.restScheme = restScheme;
		this.restHost = restHost;
		this.restPort = restPort;
	}

	public String getRestComponent() {
		return restComponent;
	}

	public String getRestScheme() {
		return restScheme;
	}

	public String getRestHost() {
		return restHost;
	}

	public String getRestPort() {
		return restPort;
	}
}

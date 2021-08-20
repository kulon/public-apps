package kulon.publicapps.environmenttest.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.camel.CamelContext;
import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.apache.camel.component.elasticsearch.ElasticsearchConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kulon.publicapps.environmenttest.components.NoCertSSL;


/**
 * 
 * This class creates an instance of the Camel ElasticsearchComponent and allows 
 * configuring the RestClient settings used by this component. 
 *
 */
@Configuration
public class ElasticConfiguration {

	public static final String COMPONENT_NAME = "test-elastic";
	
	private String elasticCluster;
	private String elasticHost;
	private String elasticUser;
	private String elasticPassword;
	private String elasticIndex;
	private boolean elasticEnableSSL;
	private boolean ignoreCertificateVerification;
	
	public ElasticConfiguration (
			@Value("${ep.elastic.cluster}") String elasticCluster,
			@Value("${ep.elastic.host}") String elasticHost,
			@Value("${ep.elastic.user}") String elasticUser,
			@Value("${ep.elastic.password}") String elasticPassword,
			@Value("${ep.elastic.index}") String elasticIndex,
			@Value("${ep.elastic.enableSSL}") boolean elasticEnableSSL,
			@Value("${ep.elastic.ignoreCertificateVerification}") boolean ignoreCertificateVerification) {
		
		this.elasticCluster = elasticCluster;
		this.elasticEnableSSL = elasticEnableSSL;
		this.elasticHost = elasticHost;
		this.elasticIndex = elasticIndex;
		this.elasticPassword = elasticPassword;
		this.elasticUser = elasticUser;
		this.ignoreCertificateVerification = ignoreCertificateVerification;
	}
	
	@Bean(COMPONENT_NAME)
	public ElasticsearchComponent testElastic(CamelContext camelContext) {
	
		ElasticsearchComponent component = new ElasticsearchComponent(camelContext);
		RestClient client = buildRestClient(getElasticHost(), getElasticUser(), getElasticPassword(), isElasticEnableSSL(), isIgnoreCertificateVerification());
		component.setClient(client);
		
		return component;
    }
	
	private static RestClient buildRestClient(
			String hostUrl,
			String userName,
			String password,
			boolean enableSSL, 
			boolean ignoreCertificateVerification) {
			
        final RestClientBuilder builder = RestClient.builder(parseHostAddresses(hostUrl, enableSSL));
		final SSLContext sslContext;
		final HostnameVerifier hostnameVerifier;

        try {
			if (enableSSL && ignoreCertificateVerification) {
				sslContext = NoCertSSL.createSSLContext();
				hostnameVerifier = ((hostname, sslSession) -> true);
			} else {
				sslContext = null;
				hostnameVerifier = null;
			}
			
	        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
	        	
	            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

		        builder.setHttpClientConfigCallback((HttpAsyncClientBuilder clientBuilder) -> {
	                clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
	                if (enableSSL) {
			        	clientBuilder.setSSLContext(sslContext).setSSLHostnameVerifier(hostnameVerifier);
		        	}
		        	return clientBuilder;
		        });
	        }
	        else if (enableSSL) {
	        	
		        builder.setHttpClientConfigCallback((HttpAsyncClientBuilder clientBuilder) -> {
		        	clientBuilder.setSSLContext(sslContext).setSSLHostnameVerifier(hostnameVerifier);
		        	return clientBuilder;
		        });
	        }
		} 
        catch (Exception e) {
			LoggerFactory.getLogger(ElasticConfiguration.class).error("Failed to set up RestClientBuilder.", e);
		}

        builder.setMaxRetryTimeoutMillis(ElasticsearchConstants.MAX_RETRY_TIMEOUT);
        builder.setRequestConfigCallback(requestConfigBuilder -> 
        	requestConfigBuilder
        		.setConnectTimeout(ElasticsearchConstants.DEFAULT_CONNECTION_TIMEOUT)
        		.setSocketTimeout(ElasticsearchConstants.DEFAULT_SOCKET_TIMEOUT));
        
        final RestClient restClient = builder.build();
        
        return restClient;
	}
	
	private static HttpHost[] parseHostAddresses(String hostAddresses, boolean enableSSL) {
    	
        if (StringUtils.isEmpty(hostAddresses)) {
            return null;
        }
        List<String> addressList = Arrays.asList(hostAddresses.split(","));
        List<HttpHost> httpHostList = new ArrayList<>(addressList.size());
        
        for (String address : addressList) {
        	
            String[] tokens = address.split(":");
            
            String hostname = tokens[0];
            Integer port = tokens.length > 1 ? Integer.parseInt(tokens[1]) : ElasticsearchConstants.DEFAULT_PORT;
            
            httpHostList.add(new HttpHost(hostname, port, enableSSL ? "HTTPS" : "HTTP"));
        }
        
        return httpHostList.toArray(new HttpHost[0]);
    }

	public String getElasticHost() {
		return elasticHost;
	}

	public String getElasticUser() {
		return elasticUser;
	}

	public String getElasticPassword() {
		return elasticPassword;
	}

	public boolean isElasticEnableSSL() {
		return elasticEnableSSL;
	}

	public boolean isIgnoreCertificateVerification() {
		return ignoreCertificateVerification;
	}

	public String getElasticIndex() {
		return elasticIndex;
	}

	public String getElasticCluster() {
		return elasticCluster;
	}	
}

package kulon.publicapps.environmenttest.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

@Configuration
public class SmbConfiguration {

	private String domain;
	private String username;
	private String password;
	private String server;
	private String port;
	private String share;
	private String dir;
	
	public SmbConfiguration(	
			@Value("${ep.smb.domain}") String domain,
			@Value("${ep.smb.username}") String username,
			@Value("${ep.smb.password}") String password,
			@Value("${ep.smb.server}") String server,
			@Value("${ep.smb.port:445}") String port,
			@Value("${ep.smb.share}") String share,
			@Value("${ep.smb.dir}") String dir
			) {
		
		this.domain = domain;
		this.username = username;
		this.password = password;
		this.server = server;
		this.port = port;
		this.share = share;
		this.dir = dir;
	}

	public String getUrl() {
		
		StringBuilder url = new StringBuilder();
		url.append(domain);
		
		if (url.length() > 0 && StringUtils.isNotEmpty(username)) {
			url.append(";").append(username);
		}
		
		if (StringUtils.isNotEmpty(server)) {
			url.append("@").append(server);
		}
		
		if (StringUtils.isNotEmpty(port)) {
			url.append(":").append(port);
		}
		
		url.append("/").append(share);

		if (StringUtils.isNotEmpty(dir)) {
			url.append("/").append(dir);
		}
		
		return url.toString();
	}

	public String getPassword() {
		return password;
	}
}

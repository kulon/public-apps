package kulon.publicapps.environmenttest.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
		return domain + ";" + username + "@" + server + ":" + port + "/" + share + "/" + dir;
	}

	public String getPassword() {
		return password;
	}
}

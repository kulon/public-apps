package kulon.publicapps.environmenttest.components;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * This class allows creating an instance of SSLContext that ignores SSL certificate verification. 
 * This is to make SSL connections work in non-production environments that use self-signed certificates. 
 *
 */
public class NoCertSSL {
	
	public static SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
		
		TrustManager[] trustAllCerts = new TrustManager[] { new IgnoreCertTrustManager() };
		
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(null, trustAllCerts, new SecureRandom());
		
		return sslContext;
	}

	private static class IgnoreCertTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}

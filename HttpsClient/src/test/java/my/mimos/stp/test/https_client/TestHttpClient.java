/**
 * 
 */
package my.mimos.stp.test.https_client;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ck.lee
 *
 */
public class TestHttpClient {

	
	//javax.net.ssl.SSLException: hostname in certificate didn't match: <10.44.65.141> != <cherry>
	static {
	    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
	        {

				public boolean verify(String arg0, SSLSession arg1) {
					// TODO Auto-generated method stub
					return true;
				}

	        });
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("javax.net.debug", "ssl");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelfSignedCert() throws ClientProtocolException, IOException, KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        HttpClient httpclient = new DefaultHttpClient();
        
        SSLSocketFactory sslsf = new SSLSocketFactory(new TrustStrategy() {

            public boolean isTrusted(
                    final X509Certificate[] chain, String authType) throws CertificateException {
                // Oh, I am easy...
                return true;
            }

			public boolean isTrusted(java.security.cert.X509Certificate[] arg0,
					String arg1) throws java.security.cert.CertificateException {
				return true;
			}

        });
        // javax.net.ssl.SSLException: hostname in certificate didn't match: <10.44.65.141> != <cherry>
		sslsf.setHostnameVerifier(new X509HostnameVerifier() {

			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}

			public void verify(String arg0, SSLSocket arg1) throws IOException {
				// TODO Auto-generated method stub
				
			}

			public void verify(String arg0,
					java.security.cert.X509Certificate arg1)
					throws SSLException {
				// TODO Auto-generated method stub
				
			}

			public void verify(String arg0, String[] arg1, String[] arg2)
					throws SSLException {
				// TODO Auto-generated method stub
				
			}});
        
        
        try {
            HttpGet httpget = new HttpGet("https://10.44.65.141:9031/routerService1");
            httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, sslsf));

            System.out.println("executing request " + httpget.getURI());

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            // Self signed cert error: javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");
            assertThat(responseBody, containsString("wsdl"));

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }	
    }
	
	@Test
	public void testSelfSignedCert2() throws ClientProtocolException, IOException, KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        HttpClient httpclient = new DefaultHttpClient();
        
        SSLSocketFactory sslsf = new SSLSocketFactory(new TrustStrategy() {

            public boolean isTrusted(java.security.cert.X509Certificate[] arg0,
					String arg1) throws java.security.cert.CertificateException {
				return true;
			}
			// javax.net.ssl.SSLException: hostname in certificate didn't match: <10.44.65.141> != <cherry>
        }, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        
        try {
            HttpGet httpget = new HttpGet("https://10.44.65.141:9031/routerService1");
            httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, sslsf));

            System.out.println("executing request " + httpget.getURI());

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            // Self signed cert error: javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");
            assertThat(responseBody, containsString("wsdl"));

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }	
    }
	
	// Need to install cert at JRE/lib/security
	@SuppressWarnings("deprecation")
	@Test
	public void testSelfSignedCert3() throws ClientProtocolException, IOException, KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {

		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		HttpClient httpclient = new DefaultHttpClient();
        
        try {
            HttpGet httpget = new HttpGet("https://10.44.65.141:9031/routerService1");
            httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

            System.out.println("executing request " + httpget.getURI());

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            // Self signed cert error: javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");
            assertThat(responseBody, containsString("wsdl"));

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }	
    }

		// Need to install cert at JRE/lib/security
		@Test
		public void testSelfSignedCert4() throws ClientProtocolException, IOException, KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
			TrustStrategy trustStrategy = new TrustSelfSignedStrategy();
			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStrategy , SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpClient httpclient = new DefaultHttpClient();
	        
	        try {
	            HttpGet httpget = new HttpGet("https://10.44.65.141:9031/routerService1");
	            httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

	            System.out.println("executing request " + httpget.getURI());

	            // Create a response handler
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            String responseBody = httpclient.execute(httpget, responseHandler);
	            // Self signed cert error: javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
	            System.out.println("----------------------------------------");
	            System.out.println(responseBody);
	            System.out.println("----------------------------------------");
	            assertThat(responseBody, containsString("wsdl"));

	        } finally {
	            // When HttpClient instance is no longer needed,
	            // shut down the connection manager to ensure
	            // immediate deallocation of all system resources
	            httpclient.getConnectionManager().shutdown();
	        }	
	    }

	
	@Test
	public void testValidHttps() throws ClientProtocolException, IOException {
	    HttpClient httpclient = new DefaultHttpClient();
	    try {
	        HttpGet httpget = new HttpGet("https://google.com");
	
	        System.out.println("executing request " + httpget.getURI());
	
	        // Create a response handler
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httpget, responseHandler);
	        System.out.println("----------------------------------------");
	        System.out.println(responseBody);
	        System.out.println("----------------------------------------");
	        assertThat(responseBody, containsString("html"));
	
	    } finally {
	        // When HttpClient instance is no longer needed,
	        // shut down the connection manager to ensure
	        // immediate deallocation of all system resources
	        httpclient.getConnectionManager().shutdown();
	    }	
	}
	
	@Test
	public void customProtocol() throws ParseException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            httpclient.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process(
                        final HttpRequest request,
                        final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }

            });

            httpclient.addResponseInterceptor(new HttpResponseInterceptor() {

                public void process(
                        final HttpResponse response,
                        final HttpContext context) throws HttpException, IOException {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        Header ceheader = entity.getContentEncoding();
                        if (ceheader != null) {
                            HeaderElement[] codecs = ceheader.getElements();
                            for (int i = 0; i < codecs.length; i++) {
                                if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                    response.setEntity(
                                            new GzipDecompressingEntity(response.getEntity()));
                                    return;
                                }
                            }
                        }
                    }
                }

            });

            HttpGet httpget = new HttpGet("http://www.apache.org/");

            // Execute HTTP request
            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(response.getLastHeader("Content-Encoding"));
            System.out.println(response.getLastHeader("Content-Length"));
            System.out.println("----------------------------------------");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String content = EntityUtils.toString(entity);
                System.out.println(content);
                System.out.println("----------------------------------------");
                System.out.println("Uncompressed size: "+content.length());
            }

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
	}

	@Test
	public void normalProtocol() throws ParseException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            HttpGet httpget = new HttpGet("http://www.apache.org/");

            // Execute HTTP request
            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(response.getLastHeader("Content-Encoding"));
            System.out.println(response.getLastHeader("Content-Length"));
            System.out.println("----------------------------------------");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String content = EntityUtils.toString(entity);
                System.out.println(content);
                System.out.println("----------------------------------------");
                System.out.println("Uncompressed size: "+content.length());
            }

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
	}
}

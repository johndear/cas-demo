//package com.op;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.security.KeyStore;
//import java.security.SecureRandom;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpVersion;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.conn.ClientConnectionManager;
//import org.apache.http.conn.scheme.PlainSocketFactory;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//
//
//public class Https {
//
//	public static void main(String[] args) throws MalformedURLException {
//		// 证书机构颁发的证书
//		String url = "https://ssouat.oppein.com/cas/serviceValidate?service=http%3A%2F%2Fmkbitest.oppein.com%2Fva&ticket=ST-20446-1Nc0QnhPwDx1gy40STPT-ssouat.oppein.com";
//		String response = getResponseFromServer(new URL(url), HttpsURLConnection.getDefaultHostnameVerifier(), "utf-8");
//		System.out.println(response);
//		
//		// 未经证书机构颁发的临时证书
//		url ="https://127.0.0.1:8443/jenkins/login?from=%2Fjenkins";
////		response = getResponseFromServer(new URL(url), HttpsURLConnection.getDefaultHostnameVerifier(), "utf-8");
////		System.out.println(response);
//		
//		// 忽略证书信任
//		response = getResponseFromServer(new URL(url), "utf-8");
//		System.out.println(response);
//		
//		// httpclient忽略信任证书验证
//		response = retrieveResponseFromServerByHttpClient(new URL(url));
//		System.out.println(response); 
//		
//	}
//	
//	public static String getResponseFromServer(final URL constructedUrl, final String encoding){
//		try {
//			// 创建信任规则列表
//		    // Create a trust manager that does not validate certificate chains
//		    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
//		        public X509Certificate[] getAcceptedIssuers(){return null;}
//		        public void checkClientTrusted(X509Certificate[] certs, String authType){}
//		        public void checkServerTrusted(X509Certificate[] certs, String authType){}
//		    }};
//		    // 信任所有主机-对于任何证书都不做检查
//		    // Install the all-trusting trust manager
//		    SSLContext sc = SSLContext.getInstance("TLS");
//		    sc.init(null, trustAllCerts, new SecureRandom());
//		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//		 
//		    // 不进行主机名确认,对所有主机
//		    HostnameVerifier hv = new HostnameVerifier() {
//		    	// 验证请求域名  跟8443端口绑定的证书域名是否一致
//		        public boolean verify(String urlHostName, SSLSession session) {
//		            System.out.println("Warning: URL Host: " + urlHostName + " vs. "
//		                               + session.getPeerHost());
//		            return urlHostName.equals(session.getPeerHost());
//		        }
//		    };
//		    
//		    return getResponseFromServer(constructedUrl, hv, encoding);
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//		return null;
//	}
//	
//	/**
//     * Contacts the remote URL and returns the response.
//     *
//     * @param constructedUrl the url to contact.
//     * @param hostnameVerifier Host name verifier to use for HTTPS connections.
//     * @param encoding the encoding to use.
//     * @return the response.
//     */
//    public static String getResponseFromServer(final URL constructedUrl, final HostnameVerifier hostnameVerifier, final String encoding) {
//        URLConnection conn = null;
//        try {
//            conn = constructedUrl.openConnection();
//            if (conn instanceof HttpsURLConnection) {
//                ((HttpsURLConnection)conn).setHostnameVerifier(hostnameVerifier);
//            }
//            getCookie(conn);
//            final BufferedReader in;
//
//            if (encoding!=null && !encoding.equals("")) {
//                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
//            }
//
//            String line;
//            final StringBuilder stringBuffer = new StringBuilder(255);
//
//            while ((line = in.readLine()) != null) {
//                stringBuffer.append(line);
//                stringBuffer.append("\n");
//            }
//            return stringBuffer.toString();
//        } catch (final Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (conn != null && conn instanceof HttpURLConnection) {
//                ((HttpURLConnection)conn).disconnect();
//            }
//        }
//
//    }
//    
//    
//    protected static String retrieveResponseFromServerByHttpClient(final URL validationUrl) {
////        logger.info("----- retrieveResponseFromServer ----- validationUrl:" + validationUrl.toString() + ", ticket:" + ticket);
//        DefaultHttpClient httpclient = (DefaultHttpClient) getNewHttpClient();  
//         
//        // httpclient忽略信任证书验证
//        try {  
//            //Secure Protocol implementation.    
//            SSLContext ctx = SSLContext.getInstance("SSL");  
//            //Implementation of a trust manager for X509 certificates    
//            X509TrustManager tm = new X509TrustManager() {  
//   
//                public void checkClientTrusted(X509Certificate[] xcs,  
//                        String string) throws CertificateException {  
//   
//                }  
//   
//                public void checkServerTrusted(X509Certificate[] xcs,  
//                        String string) throws CertificateException {  
//                }  
//   
//                public X509Certificate[] getAcceptedIssuers() {  
//                    return null;  
//                }  
//            };  
//            ctx.init(null, new TrustManager[] { tm }, null);  
//            SSLSocketFactory ssf = new SSLSocketFactory(ctx);  
//            ClientConnectionManager ccm = httpclient.getConnectionManager();  
//            //register https protocol in httpclient's scheme registry    
//            SchemeRegistry sr = ccm.getSchemeRegistry();  
//            sr.register(new Scheme("https", 443, ssf));  
//        } catch (Exception e) {  
////            logger.info(e.getMessage(), e);
//            e.printStackTrace();  
//        }  
//   
//        try {
//            HttpGet httpGet = new HttpGet(validationUrl.toString());  
//            HttpResponse response = httpclient.execute(httpGet);
//            String xml = EntityUtils.toString(response.getEntity());
//            return xml;
//        } catch (Exception e) {
////            logger.info(e.getMessage(), e);
//            e.printStackTrace();
//        }
//        return null;
//         
//    }
//     
//    private static HttpClient getNewHttpClient() {  
//        try {  
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
//            trustStore.load(null, null);  
//            SSLSocketFactory sf = new SSLSocketFactory(trustStore);  
//            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
//            HttpParams params = new BasicHttpParams();  
//            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
//            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);  
//            SchemeRegistry registry = new SchemeRegistry();  
//            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
//            registry.register(new Scheme("https", sf, 443));  
//            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);  
//            return new DefaultHttpClient(ccm, params);  
//        } catch (Exception e) {  
//            return new DefaultHttpClient();  
//        }  
//    }
//    
//    /**
//     * 得到cookie
//     * 
//     */
//    private static void getCookie(URLConnection http) {
//        String cookieVal = null;
//        String key = null;
//        for (int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++) {
//            if (key.equalsIgnoreCase("set-cookie")) {
//                cookieVal = http.getHeaderField(i);
//                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
//                System.out.println("----- cookieVal-----" + cookieVal);
//            }
//        }
//    }
//
//}

package com.misfit.ta.backend.server;

import java.io.IOException;
import java.net.URI;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.graphwalker.Util;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;


public class ServerHelper {

	protected static Logger logger = Util.setupLogger(ServerHelper.class);
	
	// notification endpoint server
	public static void startNotificationEndpointServer(final String url) {
		
		if(url.contains("https://")) {
			logger.info("Starting HTTPS server...");
			startHttpsNotificationEndpointServer(url);
		}
		if(url.contains("http://")) {
			logger.info("Starting HTTP server...");
			startHttpNotificationEndpointServer(url);
		}
	}
	
	private static void startHttpNotificationEndpointServer(final String url) {
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				ResourceConfig rc = new PackagesResourceConfig("com.misfit.ta.backend.server.notificationendpoint");
				try {
					HttpServer server = GrizzlyServerFactory.createHttpServer(url, rc);
					server.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
	}
	
	private static void startHttpsNotificationEndpointServer(final String url) {
			
		Thread thread = new Thread(new Runnable() {
			public void run() {

				ResourceConfig rc = new PackagesResourceConfig("com.misfit.ta.backend.server.notificationendpoint");
//		        SSLContextConfigurator sslCon=new SSLContextConfigurator();
//
//		        sslCon.setKeyStoreFile(NotificationEndpointServer.KeyStoreFilePath);
//		        sslCon.setKeyStorePass(NotificationEndpointServer.KeyStorePassword);
//		        sslCon.setTrustStoreFile(NotificationEndpointServer.KeyStoreFilePath);
//		        sslCon.setTrustStorePass(NotificationEndpointServer.KeyStorePassword);

				TrustManager[] trustAllCerts = new TrustManager[]{
						new X509TrustManager() {
							public java.security.cert.X509Certificate[] getAcceptedIssuers() {
								return null;
							}
							public void checkClientTrusted(
									java.security.cert.X509Certificate[] certs, String authType) {
							}
							public void checkServerTrusted(
									java.security.cert.X509Certificate[] certs, String authType) {
							}
						}
				};
				
				KeyManager[] keyManagers = new KeyManager[1];

				// Install the all-trusting trust manager
				SSLContext sslCon = null;
				try {
					sslCon = SSLContext.getInstance("SSL");
					sslCon.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sslCon.getSocketFactory());
				} catch (Exception e) {
				}
		        
		        SSLEngineConfigurator sslEngine = new SSLEngineConfigurator(sslCon)
		        	.setClientMode(true)
		        	.setNeedClientAuth(false)
		        	.setNeedClientAuth(false);
		        		        

		        URI uri = UriBuilder.fromUri(url).build();		        
		        HttpServer secure;
				try {
					secure = GrizzlyServerFactory.createHttpServer(uri,
					        ContainerFactory.createContainer(HttpHandler.class, rc),
					        true,
					        sslEngine);
					
					secure.start();
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
	}

}

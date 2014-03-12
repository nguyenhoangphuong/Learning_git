package com.misfit.ta.backend.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.graphwalker.Util;

import com.misfit.ta.backend.server.notificationendpoint.NotificationEndpointServer;
import com.misfit.ta.utils.Files;
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
		
		try {
			Files.delete("keys");
			Files.getFile("keys");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		Thread thread = new Thread(new Runnable() {
			public void run() {

				ResourceConfig rc = new PackagesResourceConfig("com.misfit.ta.backend.server.notificationendpoint");
		        SSLContextConfigurator sslCon=new SSLContextConfigurator();

		        sslCon.setKeyStoreFile(NotificationEndpointServer.KeyStoreFilePath);
		        sslCon.setKeyStorePass(NotificationEndpointServer.KeyStorePassword);

		        URI uri = UriBuilder.fromUri(url).build();		        
		        HttpServer secure;
				try {
					secure = GrizzlyServerFactory.createHttpServer(uri,
					        ContainerFactory.createContainer(HttpHandler.class, rc),
					        true,
					        new SSLEngineConfigurator(sslCon).setClientMode(false));
					
					secure.start();
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
	}

}

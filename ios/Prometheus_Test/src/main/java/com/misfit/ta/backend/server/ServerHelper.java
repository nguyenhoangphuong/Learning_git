package com.misfit.ta.backend.server;

import java.io.IOException;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class ServerHelper {

	public static void startNotificationEndpointServer(final String url) {
		
		Thread thread = new Thread(new Runnable() {
			@SuppressWarnings("restriction")
			public void run() {
				ResourceConfig rc = new PackagesResourceConfig("com.misfit.ta.backend.server.notificationendpoint");
				try {
					HttpServerFactory.create(url, rc).start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
	}
}

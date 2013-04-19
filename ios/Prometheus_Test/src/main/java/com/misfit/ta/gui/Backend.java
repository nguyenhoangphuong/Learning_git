package com.misfit.ta.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Backend {

    private static String baseURL = "https://staging-api.misfitwearables.com";
    private static Logger logger = Util.setupLogger(Backend.class);

    public static String submitEmail(String email) {

        SSLContext ctx = null;
        try {
            KeyStore trustStore;
            trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("C:\\truststore_client"), "asdfgh".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
            // } catch (CertificateException e) {
            // e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ClientConfig config = new DefaultClientConfig();
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null, ctx));

        // WebResource r = createWebResource("/mvp1/v1/signup");
        WebResource r = Client.create(config).resource(baseURL + "/" + "/mvp1/v1/signup");
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("email", email);
        logger.info("Submiting email: " + email);
        String response = r.queryParam("email", email).accept(MediaType.APPLICATION_JSON_TYPE,
                MediaType.APPLICATION_XML_TYPE).header("api_key", "76801581").post(String.class);
        logger.info("Response: " + response);
        return response;
    }

    // public static void setupHTTPS() {
    // ClientConfig config = new DefaultClientConfig();
    // SSLContext ctx = SSLContext.getInstance("SSL");
    // ctx.init(null, myTrustManager, null);
    // config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new
    // HTTPSProperties(hostnameVerifier, ctx));
    // Client client = Client.create(config);
    // }
    //

    public static boolean isEmailExisting(String email) {
        WebResource r = createWebResource("/mvp1/v1/all_users");
        logger.info("Checking email existing: " + email);
        String response = r.queryParam("email", email).accept(MediaType.APPLICATION_JSON_TYPE).header("api_key",
                "76801581").get(String.class);
        logger.info("Response: " + response);
        if (response.indexOf(email) >= 0) {
            return true;
        }
        return false;
    }

    private static WebResource createWebResource(String subURL) {
        Client c = Client.create();
        return c.resource(baseURL + "/" + subURL);
    }

}

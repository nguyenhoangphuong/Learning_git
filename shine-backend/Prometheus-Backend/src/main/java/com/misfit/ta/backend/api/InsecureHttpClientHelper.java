package com.misfit.ta.backend.api;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

public class InsecureHttpClientHelper {

    // Dont use static here as we want to send requests in parallel
    public CloseableHttpClient getInsecureCloseableHttpClient() {

        try {
            // build trust all certificates manager
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

            // build allow all hosts verifier
            X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            // build ssl factory
            SSLContext sslContext = builder.build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            
            // create insecure httpclient
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).setRedirectStrategy(new NoRedirectionRedirectStrategy()).build();
            
            return httpclient;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    class NoRedirectionRedirectStrategy implements RedirectStrategy {

        @Override
        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
                throws ProtocolException {
            return false;
        }

        @Override
        public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context)
                throws ProtocolException {
            // TODO Auto-generated method stub
            return null;
        }
        
    }

}

package com.misfit.ta.backend.api.metawatch;

import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class MetawatchApi extends MVPApi {
    // logger
    protected static Logger logger = Util.setupLogger(MetawatchApi.class);

    // fields
    public static String baseAddress = Settings.getValue("MVPMetawatchApiAddress");
    public static Integer port = Settings.getValue("MVPBackendPort") == null ? null : Integer.parseInt(Settings
            .getValue("MVPBackendPort"));

    public static BaseResult registerMetawatch(String token, String userID) {
        String url = baseAddress + "register/metawatch";
        BaseParams requestInfo = new MetawatchBaseParams();

        ServiceResponse response = MVPApi.get(url, port, requestInfo);
        return new BaseResult(response);
    }

    public static ServiceResponse signUpMetawatch(String email, String password) {
        String url = baseAddress + "signup/metawatch";
        BaseParams requestInfo = new MetawatchBaseParams();
        requestInfo.addParam("email", email);
        requestInfo.addParam("password", password);

        // ServiceResponse response = MVPApi.post(url, port, requestInfo);

        EntityBuilder entityBuilder = org.apache.http.client.entity.EntityBuilder.create();
        NameValuePair p1 =  new BasicNameValuePair("email", "thinh@misfit.com");
        NameValuePair p2 = new BasicNameValuePair("password", "misfit1");
        List<org.apache.http.NameValuePair> list = new Vector<NameValuePair>();
        list.add(p1);
        list.add(p2);
        entityBuilder.setParameters(list);
        
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeaders(requestInfo.headers.toArray(new Header[requestInfo.headers.size()]));
        httpPost.setEntity(entityBuilder.build());

        ServiceResponse response = excuteHttpRequest(httpPost);
        System.out.println("LOG [MetawatchApi.enclosing_method]: error code: " + response.getStatusCode());
        return response;
    }   

    public static void main(String[] args) {
//        registerMetawatch("mytoken", "thinh");
         String email = "test"+ System.currentTimeMillis()+ "@misfitqa.com";
         System.out.println("LOG [MetawatchApi.main]: email: " + email);
         signUpMetawatch(email, "misfit1");
        
        // OpenAPI.logIn("thinh@misfitwearables.com", "misfit1");
    }
}

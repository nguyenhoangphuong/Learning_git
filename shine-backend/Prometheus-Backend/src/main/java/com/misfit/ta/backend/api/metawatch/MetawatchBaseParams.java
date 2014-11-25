package com.misfit.ta.backend.api.metawatch;

import com.misfit.ta.backend.data.BaseParams;

public class MetawatchBaseParams extends BaseParams {
    // api key, Content-Type, User-Agent
    
    public MetawatchBaseParams() {
        removeHeader("api_key");
        removeHeader("locale");
        addHeader("api_key", "testautomation_" + Math.random());
        addHeader("signature", MVPApi.generateLocalId());
        String userAgent = "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        addHeader("User-Agent", userAgent);
    }
    
    
    
    
}

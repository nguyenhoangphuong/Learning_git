package com.misfit.ta.backend.api.metawatch;

import com.misfit.ta.backend.data.BaseParams;

public class MetawatchBaseParams extends BaseParams {
    // api key
    
    public MetawatchBaseParams() {
        removeHeader("api_key");
        removeHeader("locale");
        removeHeader("Content-Type");
        addHeader("api_key", "testautomation_" + Math.random());
        addHeader("user_id", "testautomation@misfitqa.com");
        String userAgent = "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        addHeader("User-Agent", userAgent);
    }
    
    
    
    
}

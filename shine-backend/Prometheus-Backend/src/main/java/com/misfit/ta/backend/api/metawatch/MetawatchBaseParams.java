package com.misfit.ta.backend.api.metawatch;

import com.misfit.ta.backend.data.BaseParams;

public class MetawatchBaseParams extends BaseParams {
    // api key
    
    public MetawatchBaseParams() {
        removeHeader("api_key");
        removeHeader("locale");
        removeHeader("Content-Type");
        addHeader("api_key", "testanut\tnomation");
        addHeader("user_id", "testautomation@misfitqa.com");
        
    }
}

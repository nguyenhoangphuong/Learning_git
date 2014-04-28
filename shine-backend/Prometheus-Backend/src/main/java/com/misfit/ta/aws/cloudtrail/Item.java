
package com.misfit.ta.aws.cloudtrail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Item {

    @Expose
    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    
    @Expose
    private String name;
    @Expose
    private ValueSet valueSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValueSet getValueSet() {
        return valueSet;
    }

    public void setValueSet(ValueSet valueSet) {
        this.valueSet = valueSet;
    }

}

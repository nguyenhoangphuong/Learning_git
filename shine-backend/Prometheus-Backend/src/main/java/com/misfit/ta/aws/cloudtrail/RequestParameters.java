
package com.misfit.ta.aws.cloudtrail;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RequestParameters {

    @Expose
    private InstancesSet instancesSet;
    @Expose
    private FilterSet filterSet;
    

    @Expose
    private String instanceId;
    @Expose
    private String attribute;
    
    @Expose
    private String value;
    
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public InstancesSet getInstancesSet() {
        return instancesSet;
    }

    public void setInstancesSet(InstancesSet instancesSet) {
        this.instancesSet = instancesSet;
    }

    public FilterSet getFilterSet() {
        return filterSet;
    }

    public void setFilterSet(FilterSet filterSet) {
        this.filterSet = filterSet;
    }
    
    @Expose
    private String spotPrice;
    @Expose
    private LaunchSpecification launchSpecification;
    @Expose
    private Integer instanceCount;

    public String getSpotPrice() {
        return spotPrice;
    }

    public void setSpotPrice(String spotPrice) {
        this.spotPrice = spotPrice;
    }

    public LaunchSpecification getLaunchSpecification() {
        return launchSpecification;
    }

    public void setLaunchSpecification(LaunchSpecification launchSpecification) {
        this.launchSpecification = launchSpecification;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }


}


package com.misfit.ta.aws.cloudtrail;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.misfit.ta.aws.cloudtrail.filterset.Item1;

@Generated("org.jsonschema2pojo")
public class ValueSet {

    @Expose
    private List<Item1> items = new ArrayList<Item1>();

    public List<Item1> getItems() {
        return items;
    }

    public void setItems(List<Item1> items) {
        this.items = items;
    }

}

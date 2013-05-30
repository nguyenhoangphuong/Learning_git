package com.misfit.ta.backend.data.timeline;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThinhTest {

    public static void main(String[] args) {
        NotableEventItem item = new NotableEventItem(1, null, null, 1, 2);
        TimelineItem timeline = new TimelineItem(TimelineItemBase.TYPE_NOTABLE, null, "clientId", 1234, 4567, item);
        System.out.println("LOG [ThinhTest.main]: " + timeline.toJson());
        
    }
}

package com.misfit.ta.backend.aut.correctness.openapi.notificationserver;

import com.misfit.ta.backend.data.openapi.notification.NotificationMessage;

public interface NotificationReceivedHandler {
    
    public void handleNotification(NotificationMessage message);

}

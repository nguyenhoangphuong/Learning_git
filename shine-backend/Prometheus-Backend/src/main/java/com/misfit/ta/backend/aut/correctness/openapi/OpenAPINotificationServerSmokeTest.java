package com.misfit.ta.backend.aut.correctness.openapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aut.correctness.openapi.notificationserver.NotificationReceivedHandler;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.openapi.notification.NotificationMessage;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.backend.server.notificationendpoint.NotificationEndpointServer;
import com.misfit.ta.base.BasicEvent;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

/*
 * This test cover:
 * profile:
 * - PUT /profile
 * - POST /profile
 * 
 * device:
 * - POST /pedometer
 * - PUT /pedometer
 * - POST /unlink_device
 * 
 * goal
 * - POST /goal
 * - PUT /goal/:id
 * 
 * sessions + sleeps:
 * - POST /timeline_items
 * - POST /timeline_items/batch_insert
 * - PUT /timeline_itmes/:id
 */

public class OpenAPINotificationServerSmokeTest extends BackendAutomation implements NotificationReceivedHandler {

    private static String ClientKey = Settings.getParameter("MVPOpenAPIClientID");
    private static String ClientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
    private static String endpointURL = Settings.getParameter("NotificationEndpoint");
    private static String port = Settings.getParameter("NotificationEndpointPort");
    private static String EPA = endpointURL + ":" + port + "/";
    private static String LocalhostA = "http://0.0.0.0:" + port + "/";

    private static String scope = OpenAPI.allScopesAsString();
    private static String returnUrl = "http://misfit.com/";

    private static ResultLogger resultLogger;
    private static int expectedNotifcations = 0;
    private static List<NotificationMessage> messages = new ArrayList<NotificationMessage>();

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {

        // TODO: map the notifications with a predefined set of notifications
        // and fail the test
        // if they are not matching

        // set up interface to call back when notification is received
        NotificationEndpointServer.onNotificationReceived = new BasicEvent<Void>() {

            @Override
            public Void call(Object sender, Object arguments) {

                // get host and message string
                NotificationEndpointServer server = (NotificationEndpointServer) sender;
                String messageString = (String) arguments;

                // convert from json string to object
                try {

                    JSONArray jarr = new JSONArray(messageString);

                    resultLogger.log("Notification received:");

                    for (int i = 0; i < jarr.length(); i++) {

                        NotificationMessage mess = new NotificationMessage();
                        mess.fromJson(jarr.getJSONObject(i));

                        resultLogger.log("Notification received:");
                        resultLogger.log(String.format("Owner: %s - ObjectId: %s - %s: %s", mess.getOwnerId(), mess
                                .getId(), mess.getType(), mess.getAction()));
                        resultLogger.log("\n");

                        if (NotificationEndpointServer.handler != null) {
                            NotificationEndpointServer.handler.handleNotification(mess);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        // delete log file
        Files.delete("logs/notification_server_light.log");
        resultLogger = ResultLogger.getLogger("notification_server_light.log");

        // start notification endpoint servers EPA and EPB
        resultLogger.log("\n- START NOTIFICATION ENDPOINTS");
        ServerHelper.startNotificationEndpointServer(LocalhostA);
        NotificationEndpointServer.handler = this;
        ShortcutsTyper.delayTime(5000);

    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "openapi_light_smoke_test" })
    public void NotificationServerTestScenerio() {

        subscribe();

        String email = "nhhai16991@gmail.com";
        String password = "qqqqqq";
        String returnUrl = "http://misfit.com/";
        
        String token = MVPApi.signIn(email, password).token;
        String uidA = MVPApi.getUserId(token);
        resultLogger.log("UserA: " + uidA);

        OpenAPI.getAccessToken(email, password, scope, ClientKey, returnUrl);

        // create profiles and pedometers for all users
        resultLogger.log("\n- CREATE PROFILES AND PEDOMETERS");
        ProfileData profileA = createRandomProfile(token);
        expectedNotifcations++;
        Pedometer pedometerA = createRandomPedometer(token);
        expectedNotifcations++;
        ShortcutsTyper.delayTime(5000);

        // create goals, sessions and sleeps for all users
        // all timeline items in order: session, sleep, milestone, achievement,
        // timezone, food
        resultLogger.log("\n- CREATE GOALS, TIMELINE ITEMS USING BOTH SINGLE AND BATCH INSERT");
        resultLogger.log("Expect: 6 messages total from EPB for user A and B");
        Goal goalA = createDefaultGoal(token, 0);
        expectedNotifcations++;
        List<TimelineItem> itemsA = new ArrayList<TimelineItem>();

        itemsA.add(createSessionTimelineItem(token));
        expectedNotifcations++;
        itemsA.addAll(createSleepMilestoneAchievementTimezoneFoodItems(token));
        expectedNotifcations++;
        ShortcutsTyper.delayTime(5000);

        // update goals, profiles and pedometers for all users
        resultLogger.log("\n- UPDATE GOALS, PROFILES AND PEDOMETERS");
        resultLogger.log("Expect: 6 messages total from EPA for user A and B");
        updateGoal(token, goalA.getServerId());
        expectedNotifcations++;
        updateProfile(token, profileA.getServerId());
        expectedNotifcations++;
        updatePedometer(token, pedometerA.getServerId(), pedometerA.getSerialNumberString());
        expectedNotifcations++;

        // update all timeline items
        resultLogger.log("\n- UPDATE ALL TIMELINE ITEMS");
        resultLogger.log("Expect: 4 messages total from EPB for user A and B");
        for (int i = 0; i < itemsA.size(); i++) {
            updateTimelineItem(token, itemsA.get(i).getItemType(), itemsA.get(i).getServerId());
            expectedNotifcations += 1;
        }

        ShortcutsTyper.delayTime(2000);

        // update all timeline item state to 1 (deleted)
        resultLogger.log("\n\nPART 4\n-------------------------------------------------------");
        resultLogger.log("\n- UPDATE ALL TIMELINE ITEMS STATE OF USER A TO 1 (DELETED)");
        resultLogger.log("Expect: 2 delete messages from EPB for user A");
        for (int i = 0; i < itemsA.size(); i++) {
            itemsA.get(i).setState(1);
            MVPApi.updateTimelineItem(token, itemsA.get(i));
            expectedNotifcations++;
        }

        // delay a bit to wait for all the notifications
        ShortcutsTyper.delayTime(100000);
        logger.info("LOG [OpenAPINotificationServerLight.enclosing_method]: number of expected notifications: "
                + expectedNotifcations);
        logger.info("LOG [OpenAPINotificationServerLight.enclosing_method]: number of received notifications: "
                + messages.size());

        cleanUpSubcriptions();
        Assert.assertTrue(messages.size() > 5, "expected= " + expectedNotifcations + " while received="
                + messages.size());
    }
    
    private void failOnError(BaseResult r) {
        if (r.statusCode >=400) {
            Assert.fail("Faild with error code: "+ r.code);
        }
    }

    private void subscribe() {
        // subcribe profiles, devices to EPA
        // subcribe goals, sessions, sleeps to EPB
        // setup steps
        // NOTICE: RUN THIS PART ONCE AND CONFIRM THE SUBSCRIPTIONS MANUALLY DUE
        // TO
        // SOME EXCEPTION WHEN CONFIRMING USING CODE
        resultLogger.log("\n- SUBCRIBE TO EPA - " + EPA);
        BaseResult r;
        r = OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
        failOnError(r);
        r= OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
        failOnError(r);
        r= OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
        failOnError(r);
        r=OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
        failOnError(r);
        r=OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
        failOnError(r);
        ShortcutsTyper.delayTime(10000);
        System.out.println("LOG [OpenAPINotificationServerLight.NotificationServerTestScenerio]: " + EPA);

        // System.exit(1);
    }

    private void cleanUpSubcriptions() {
        OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
        OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
        OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
        OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
        OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);

    }

    private ProfileData createRandomProfile(String token) {

        long timestamp = System.currentTimeMillis() / 1000;
        ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
        ProfileResult result = MVPApi.createProfile(token, profile);
        profile.setServerId(result.profile.getServerId());

        return profile;
    }

    private void updateProfile(String token, String objectId) {

        ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
        profile.setServerId(objectId);
        MVPApi.updateProfile(token, profile);
    }

    private Pedometer createRandomPedometer(String token) {

        long timestamp = System.currentTimeMillis() / 1000;
        Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
        BaseResult result = MVPApi.createPedometer(token, pedometer);
        Pedometer resultPedometer = Pedometer.getPedometer(result.response);
        pedometer.setServerId(resultPedometer.getServerId());

        return pedometer;
    }

    private void updatePedometer(String token, String objectId, String serialNumber) {

        Pedometer pedo = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
        pedo.setServerId(objectId);
        pedo.setSerialNumberString(serialNumber);
        MVPApi.updatePedometer(token, pedo);
    }

    private Goal createDefaultGoal(String token, int diffFromToday) {

        Goal goal = Goal.getDefaultGoal(System.currentTimeMillis() / 1000 - 3600 * 24 * diffFromToday);
        GoalsResult result = MVPApi.createGoal(token, goal);
        goal.setServerId(result.goals[0].getServerId());

        return goal;
    }

    private void updateGoal(String token, String objectId) {

        Goal goal = DataGenerator.generateRandomGoal(System.currentTimeMillis() / 1000, null);
        goal.setServerId(objectId);
        MVPApi.updateGoal(token, goal);
    }

    private TimelineItem createSessionTimelineItem(String token) {

        long timestamp = System.currentTimeMillis() / 1000;
        TimelineItem item = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);
        BaseResult result = MVPApi.createTimelineItem(token, item);
        TimelineItem ritem = TimelineItem.getTimelineItem(result.response);

        item.setServerId(ritem.getServerId());

        return item;
    }

    private List<TimelineItem> createSleepMilestoneAchievementTimezoneFoodItems(String token) {

        long timestamp = System.currentTimeMillis() / 1000;
        List<TimelineItem> items = new ArrayList<TimelineItem>();
        items.add(DataGenerator.generateRandomSleepTimelineItem(timestamp, null));
        items.add(DataGenerator.generateRandomMilestoneItem(timestamp, TimelineItemDataBase.EVENT_TYPE_100_GOAL, null));
        items.add(DataGenerator.generateRandomLifetimeDistanceItem(timestamp, null));
        items.add(DataGenerator.generateRandomTimezoneTimelineItem(timestamp, null));
        items.add(DataGenerator.generateRandomFoodTimelineItem(timestamp, null));

        ServiceResponse response = MVPApi.createTimelineItems(token, items);
        List<TimelineItem> ritems = TimelineItem.getTimelineItems(response);

        for (TimelineItem item : items) {
            for (TimelineItem ritem : ritems) {
                if (ritem.getLocalId().equals(item.getLocalId()))
                    item.setServerId(ritem.getServerId());
            }
        }

        return items;
    }

    private void updateTimelineItem(String token, int itemType, String objectId) {

        long timestamp = System.currentTimeMillis() / 1000;
        TimelineItem item = null;

        switch (itemType) {

        case TimelineItemDataBase.TYPE_SESSION:
            item = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);
            break;

        case TimelineItemDataBase.TYPE_SLEEP:
            item = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);
            break;

        case TimelineItemDataBase.TYPE_MILESTONE:
            item = DataGenerator.generateRandomMilestoneItem(timestamp, TimelineItemDataBase.EVENT_TYPE_100_GOAL, null);
            break;

        case TimelineItemDataBase.TYPE_LIFETIME_DISTANCE:
            item = DataGenerator.generateRandomLifetimeDistanceItem(timestamp, null);
            break;

        case TimelineItemDataBase.TYPE_FOOD:
            item = DataGenerator.generateRandomFoodTimelineItem(timestamp, null);
            break;

        case TimelineItemDataBase.TYPE_TIMEZONE:
            item = DataGenerator.generateRandomTimezoneTimelineItem(timestamp, null);
            break;
        }

        item.setServerId(objectId);
        MVPApi.updateTimelineItem(token, item);
    }

    @Override
    public void handleNotification(NotificationMessage message) {
        messages.add(message);
    }

}

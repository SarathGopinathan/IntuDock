package com.intutrack.intudock.UI;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;

import io.fabric.sdk.android.Fabric;

public class IntuApplication extends Application {


    private static APIUtility apiUtility;

    public static APIUtility getApiUtility() {
        return apiUtility;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    @Override
    public void onCreate() {
        super.onCreate();
       /* try {
            mSocket = IO.socket("http://192.168.0.154:8888/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }*/
        apiUtility = new APIUtility(getApplicationContext());

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                //.debuggable(true)
                .build();
        Fabric.with(fabric);
    }
//    public static void logCustomEvent(String Event) {
//        Answers.getInstance().logCustom(new CustomEvent(Event));
//    }
//    public static void logContentView(String name, String type, String id) {
//        Answers.getInstance().logContentView(new ContentViewEvent()
//                .putContentName(name)
//                .putContentType(type)
//                .putContentId(id));
//    }
//    public static void logLoginEvents(String method, boolean success) {
//        Answers.getInstance().logLogin(new LoginEvent()
//                .putMethod(method)
//                .putSuccess(success));
//    }




}

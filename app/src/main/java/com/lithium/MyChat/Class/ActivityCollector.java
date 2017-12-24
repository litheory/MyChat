package com.lithium.MyChat.Class;

import android.app.Activity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lithium on 2017/12/24.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}

package com.foodsaver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class FoodNotificationManager extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "stored_food_id";
    public static String NOTIFICATION = "notification";

    public static void scheduleNotification(Activity activity, StoredFood food, int delay) {
        Notification.Builder builder = new Notification.Builder(activity);
        builder.setContentTitle("Time to eat some food!");
        builder.setContentText(food.getIngredientsForFoodList() + " " + food.getPlacementForFoodList().toLowerCase());
        builder.setSmallIcon(R.drawable.steak);
        Notification notification = builder.build();

        Intent notificationIntent = new Intent(activity, FoodNotificationManager.class);
        notificationIntent.putExtra(FoodNotificationManager.NOTIFICATION_ID, food.getId());
        notificationIntent.putExtra(FoodNotificationManager.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, food.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Toast.makeText(activity.getApplicationContext(), "Created notification with ID " + Integer.toString(food.getId()), Toast.LENGTH_LONG).show();

        long futureInMillis = SystemClock.elapsedRealtime() + 5000;
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static void cancelNotification(Activity activity, StoredFood food) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(activity.getApplicationContext(), FoodNotificationManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                activity.getApplicationContext(), food.getId(), myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Toast.makeText(activity.getApplicationContext(), "Canceled notification with ID " + Integer.toString(food.getId()), Toast.LENGTH_LONG).show();

        alarmManager.cancel(pendingIntent);
    }

    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

    }
}

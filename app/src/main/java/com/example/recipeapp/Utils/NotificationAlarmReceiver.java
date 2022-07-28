package com.example.recipeapp.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.recipeapp.Activities.DetailsActivity;
import com.example.recipeapp.Models.Recipe;
import com.example.recipeapp.R;

import org.parceler.Parcels;

import java.util.List;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_NAME = "Recipe channel";
    public static final String CHANNEL_ID = "recipe_channel";
    public static final String CHANNEL_DESCRIPTION = "Channel that provides random recipe recommendations";

    @Override
    public void onReceive(Context context, Intent intent) {
        Recommendation.recommend(new RecommendCallback() {
            @Override
            public void OnRecommendationReturned(List<Recipe> recipes) {
                if(recipes == null || recipes.size() == 0) {
                    Log.w(NotificationAlarmReceiver.class.toString(), "No recipes returned or return values is null");
                    return;
                }
                Recipe recipe = recipes.get(0);

                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra(DetailsActivity.RECIPE, Parcels.wrap(recipe));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_coffee_24)
                        .setContentTitle(context.getString(R.string.try_recipe, recipe.getTitle()))
                        .setContentText(Html.fromHtml(recipe.getSummary(),Html.FROM_HTML_MODE_LEGACY))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();

                notificationManager.notify(0, notification);
            }
        });
    }
}

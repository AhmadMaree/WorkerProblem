package com.example.workerproblem;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;

public class NewAppWidget extends AppWidgetProvider {


    public  static ArrayList<String> emails ;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int []appWidgetId , ArrayList<String>Emails) {
        emails=Emails;
        for (int WidgetId : appWidgetId) {
            Intent intent = new Intent(context, listViewsService.class);
            System.out.println("hey"+WidgetId);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget2);
            views.setRemoteAdapter(R.id.list_view_widget, intent);
            ComponentName componentName = new ComponentName(context, NewAppWidget.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(WidgetId, R.id.list_view_widget);
            appWidgetManager.updateAppWidget(componentName, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager,appWidgetIds);

    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

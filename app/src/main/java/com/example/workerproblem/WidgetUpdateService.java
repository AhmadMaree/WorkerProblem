package com.example.workerproblem;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class  WidgetUpdateService extends IntentService {


    private static final String ACTION_FOO = "com.example.workerproblrm.action.FOO";
    private static final String ACTION_BAZ = "com.example.workerproblrm.action.BAZ";
    public static final String WIDGET_UPDATE_ACTION = "com.example.myapplication.update_widget";
    private ArrayList<String> mIngrediants;

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.workerproblrm.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.workerproblrm.extra.PARAM2";




    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction().equals(WIDGET_UPDATE_ACTION)) {
            mIngrediants = intent.getStringArrayListExtra(Intent.EXTRA_TEXT);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
            NewAppWidget.updateAppWidget(this, appWidgetManager, appWidgetIds, mIngrediants);
        }
    }

}

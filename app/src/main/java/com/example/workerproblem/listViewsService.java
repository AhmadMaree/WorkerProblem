package com.example.workerproblem;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.Serializable;
import java.util.ArrayList;

public class listViewsService extends RemoteViewsService implements Serializable
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewsFactory(this.getApplicationContext());
    }
}


class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory,Serializable {
    Context mContext;
    private ArrayList<String> mIngradiants;

    public ListViewsFactory(Context applicationCntext) {
        mContext = applicationCntext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mIngradiants = NewAppWidget.emails;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngradiants == null)
            return 0;
        return mIngradiants.size();

    }
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_text_widget);
        views.setTextViewText(R.id.text_view_recipe_widget,mIngradiants.get(position));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}


package com.ramitsuri.appsearch;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ramitsuri on 11/19/2016.
 */

public class AppLoader extends AsyncTaskLoader<ArrayList<App>>{
    ArrayList<App> installedApps;
    PackageManager packageManager;
    PackageObserver packageObserver;
    public AppLoader(Context context) {
        super(context);
        packageManager = context.getPackageManager();
    }

    @Override
    public ArrayList<App> loadInBackground() {
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);

        if (apps == null) {
            apps = new ArrayList<ApplicationInfo>();
        }
        final Context context = getContext();

        ArrayList<App> items = new ArrayList<App>(apps.size());
        for (int i = 0; i < apps.size(); i++) {
            String pkg = apps.get(i).packageName;

            if (context.getPackageManager().getLaunchIntentForPackage(pkg) != null) {
                App app = new App(context, apps.get(i));
                app.loadLabel(context);
                items.add(app);
            }
        }
        Collections.sort(items, Comparator);
        installedApps = items;
        return items;
    }

    @Override
    public void deliverResult(ArrayList<App> apps) {
        if (isReset()) {
            if (apps != null) {
                onReleaseResources(apps);
            }
        }

        ArrayList<App> oldApps = apps;
        installedApps = apps;

        if (isStarted()) {
            super.deliverResult(apps);
        }

        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }

    @Override
    protected void onStartLoading() {
        if (installedApps != null) {
            deliverResult(installedApps);
        }

        if (packageObserver == null) {
            packageObserver = new PackageObserver(this);
        }

        if (takeContentChanged() || installedApps == null ) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(ArrayList<App> apps) {
        super.onCanceled(apps);
        onReleaseResources(apps);
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (installedApps != null) {
            onReleaseResources(installedApps);
            installedApps = null;
        }
        if (packageObserver != null) {
            getContext().unregisterReceiver(packageObserver);
            packageObserver = null;
        }
    }

    /**
     * Helper method to do the cleanup work if needed, for example if we're
     * using Cursor, then we should be closing it here
     *
     * @param apps
     */
    protected void onReleaseResources(ArrayList<App> apps) {
        // do nothing
    }


    public static final Comparator<App> Comparator = new Comparator<App>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(App object1, App object2) {
            return collator.compare(object1.getLabel(), object2.getLabel());
        }
    };
}

package com.ramitsuri.appsearch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by ramitsuri on 11/19/2016.
 */

public class PackageObserver extends BroadcastReceiver {

    AppLoader loader;
    public PackageObserver(AppLoader loader) {
        this.loader = loader;

        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        this.loader.getContext().registerReceiver(this, filter);

        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        this.loader.getContext().registerReceiver(this, sdFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.loader.onContentChanged();
    }
}

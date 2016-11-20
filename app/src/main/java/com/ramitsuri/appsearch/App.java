package com.ramitsuri.appsearch;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by ramitsuri on 11/19/2016.
 */

public class App {

    private final Context context;
    private final ApplicationInfo applicationInfo;

    private String appLabel;
    private Drawable icon;

    private boolean mounted;
    private final File apkFile;

    public App(Context context, ApplicationInfo info) {
        this.context = context;
        applicationInfo = info;

        apkFile = new File(info.sourceDir);
    }

    public ApplicationInfo getAppInfo() {
        return applicationInfo;
    }

    public String getApplicationPackageName() {
        return getAppInfo().packageName;
    }

    public String getLabel() {
        return appLabel;
    }

    public Drawable getIcon() {
        if (icon == null) {
            if (apkFile.exists()) {
                icon = applicationInfo.loadIcon(context.getPackageManager());
                return icon;
            } else {
                mounted = false;
            }
        } else if (!mounted) {
            // If the app wasn't mounted but is now mounted, reload
            // its icon.
            if (apkFile.exists()) {
                mounted = true;
                icon = applicationInfo.loadIcon(context.getPackageManager());
                return icon;
            }
        } else {
            return icon;
        }

        return context.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }


    void loadLabel(Context context) {
        if (appLabel == null || !mounted) {
            if (!apkFile.exists()) {
                mounted = false;
                appLabel = applicationInfo.packageName;
            } else {
                mounted = true;
                CharSequence label = applicationInfo.loadLabel(context.getPackageManager());
                appLabel = label != null ? label.toString() : applicationInfo.packageName;
            }
        }
    }
}

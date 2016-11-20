package com.ramitsuri.appsearch;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<App>>{

    private ArrayList<App> installedApps;
    private AppListAdapter recyclerViewAdapter;
    RecyclerView recyclerViewApps;
    RecyclerView.LayoutManager recyclerViewLManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewApps = (RecyclerView) findViewById(R.id.recyclerViewApps);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<App>> onCreateLoader(int id, Bundle args) {
        AppLoader loader = new AppLoader(getApplicationContext());
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<App>> loader, ArrayList<App> data) {

        installedApps = data;
        recyclerViewLManager = new LinearLayoutManager(this);
        recyclerViewApps.setHasFixedSize(true);
        recyclerViewApps.setLayoutManager(recyclerViewLManager);
        recyclerViewAdapter = new AppListAdapter(installedApps);
        recyclerViewApps.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<App>> loader) {

    }
}

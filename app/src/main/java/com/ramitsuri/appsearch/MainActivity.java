package com.ramitsuri.appsearch;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<App>>, SearchView.OnQueryTextListener{

    private ArrayList<App> installedApps;
    private AppListAdapter recyclerViewAdapter;
    RecyclerView recyclerViewApps;
    RecyclerView.LayoutManager recyclerViewLManager;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewApps = (RecyclerView) findViewById(R.id.recyclerViewApps);
        searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
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
        recyclerViewLManager = new GridLayoutManager(this, 5);
        recyclerViewApps.setHasFixedSize(true);
        recyclerViewApps.setLayoutManager(recyclerViewLManager);
        recyclerViewAdapter = new AppListAdapter(installedApps);
        recyclerViewApps.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<App>> loader) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<App> originalApps = installedApps;
        installedApps = filter(installedApps, newText);
        recyclerViewAdapter.notifyDataSetChanged();
        recyclerViewAdapter.updateDataSet(installedApps);
        installedApps = originalApps;
        return true;
    }

    private static ArrayList<App> filter(ArrayList<App> apps, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ArrayList<App> filteredApps = new ArrayList<>();
        for (App app : apps) {
            final String text = app.getLabel().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredApps.add(app);
            }
        }
        return filteredApps;
    }
}

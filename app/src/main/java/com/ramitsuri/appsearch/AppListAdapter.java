package com.ramitsuri.appsearch;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ramitsuri on 11/19/2016.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.CustomViewHolder> {

    private ArrayList<App> installedApps;

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView appLabel;
        protected ImageButton appImage;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.appImage = (ImageButton)itemView.findViewById(R.id.appImage);
            this.appLabel = (TextView)itemView.findViewById(R.id.appLabel);
            this.appImage.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            App app = (App) installedApps.get(getAdapterPosition());
            if (app != null) {
                Intent intent = view.getContext().getPackageManager().getLaunchIntentForPackage(app.getApplicationPackageName());

                if (intent != null) {
                    view.getContext().startActivity(intent);
                }
            }

        }
    }

    public AppListAdapter(ArrayList<App> dataset){
        installedApps = dataset;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        App app = installedApps.get(position);
        holder.appLabel.setText(app.getLabel());
        holder.appImage.setBackground(app.getIcon());
    }

    @Override
    public int getItemCount() {
        return installedApps.size();
    }

    public void updateDataSet(ArrayList<App> apps){
        this.installedApps = apps;
        this.notifyDataSetChanged();
    }

}

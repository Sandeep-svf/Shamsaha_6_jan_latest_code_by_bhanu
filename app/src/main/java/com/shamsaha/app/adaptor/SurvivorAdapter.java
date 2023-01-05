package com.shamsaha.app.adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.SurvivorModel;
import com.shamsaha.app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SurvivorAdapter extends RecyclerView.Adapter<SurvivorAdapter.ChannelViewHolder> {

    private final ArrayList<SurvivorModel.Datum> channels;
    private final Context context;

    public SurvivorAdapter(Context context, ArrayList<SurvivorModel.Datum> channels) {
        this.context = context;
        this.channels = channels;
    }

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.survivor_item, parent, false);
        return new ChannelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        holder.channelName.setText(channels.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
//                    Intent intent = new Intent(context, AlertActivity.class);
//                    intent.putExtra("getContentEn", "");
//                    intent.putExtra("getImage", channels.get(position).getPath());
//                    intent.putExtra("getSubjectEn", channels.get(position).getName());
//                    intent.putExtra("getType", "pdf");
//                    intent.putExtra("boolean",true);
//                    context.startActivity(intent);

            Uri webpage = Uri.parse(channels.get(position).getPath());
            Intent i = new Intent(Intent.ACTION_VIEW, webpage);
            //i.setData(Uri.parse(channels.get(position).getPath()));
            Log.d("PAth : ", "onBindViewHolder: " + channels.get(position).getPath());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {

        TextView channelName;
        CardView cv_service;

        ChannelViewHolder(View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.textViewChannel);
            cv_service = itemView.findViewById(R.id.cv_service);
        }
    }

}

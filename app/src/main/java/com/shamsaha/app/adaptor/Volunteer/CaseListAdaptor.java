package com.shamsaha.app.adaptor.Volunteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.OnItemClick;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CaseListAdaptor extends RecyclerView.Adapter<CaseListAdaptor.ViewHolder>{

    private Context context;
    private ArrayList<ChannelListModel.Datum> channels;
    private OnItemClick mCallback;


    public CaseListAdaptor(Context context, ArrayList<ChannelListModel.Datum> channels,OnItemClick listener) {
        this.context = context;
        this.channels = channels;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel, parent, false);
        return new CaseListAdaptor.ViewHolder(itemView);
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }

    private String convertDateFormat(String date) {
        String outputDateStr = date;
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            Date formattedDate = inputFormat.parse(outputDateStr);
            outputDateStr = outputFormat.format(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateStr;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.channelName.setText(channels.get(position).getCaseId());
        holder.tvDate.setText(convertDateFormat(channels.get(position).getYesterdayDate()));
        holder.tvName.setText(channels.get(position).getScreenName());
        holder.cv_service.setTag(position);
        if (channels.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.image_view.setVisibility(View.VISIBLE);
        } else {
            holder.image_view.setVisibility(View.GONE);
        }

        if(isNullOrEmpty(channels.get(position).getScreenName())){
            holder.call_iv.setVisibility(View.VISIBLE);
            holder.chat_iv.setVisibility(View.GONE);
        }else {
            holder.call_iv.setVisibility(View.GONE);
            holder.chat_iv.setVisibility(View.VISIBLE);
        }

        holder.tvName.setVisibility(View.GONE);
        holder.image_view   .setVisibility(View.GONE);
        holder.cv_service.setOnClickListener(view -> {
            mCallback.onClick(channels.get(position).getCaseId());
        });


    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView channelName, tvName, tvDate;
        CardView cv_service;
        ImageView image_view,chat_iv,call_iv;

        ViewHolder(View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.textViewChannel);
            cv_service = itemView.findViewById(R.id.cv_service);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            image_view = itemView.findViewById(R.id.image_view);
            chat_iv = itemView.findViewById(R.id.chat_iv);
            call_iv = itemView.findViewById(R.id.call_iv);
        }
    }


}

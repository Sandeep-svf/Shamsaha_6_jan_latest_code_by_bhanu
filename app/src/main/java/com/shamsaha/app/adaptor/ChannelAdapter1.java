package com.shamsaha.app.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelAdapter1 extends RecyclerView.Adapter {

    private ArrayList<ChannelListModel.Datum> channels;
    private Context context;
    private View.OnClickListener listener;

    public ChannelAdapter1(Context context, ArrayList<ChannelListModel.Datum> channels, View.OnClickListener listener) {
        this.context = context;
        this.channels = channels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel, parent, false);
        return new ChannelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChannelViewHolder) {
            ChannelViewHolder channelViewHolder = (ChannelViewHolder) holder;
            channelViewHolder.channelName.setText(channels.get(position).getCaseId());
            channelViewHolder.tvDate.setText(convertDateFormat(channels.get(position).getYesterdayDate()));
            channelViewHolder.tvName.setText(channels.get(position).getScreenName());
            channelViewHolder.cv_service.setTag(position);
            channelViewHolder.cv_service.setOnClickListener(listener);
            if (channels.get(position).getStatus().equalsIgnoreCase("1")) {
                channelViewHolder.image_view.setVisibility(View.VISIBLE);
            } else {
                channelViewHolder.image_view.setVisibility(View.GONE);
            }

            if (channels.get(position).getConnection_type().equals("CALL") || channels.get(position).getConnection_type().equals("call")){
                channelViewHolder.call_iv.setImageResource(R.drawable.ic_call_black_24dp);
                channelViewHolder.call_iv.setVisibility(View.VISIBLE);
                channelViewHolder.chat_iv.setVisibility(View.GONE);
            }else if (channels.get(position).getConnection_type().equals("CHAT") || channels.get(position).getConnection_type().equals("chat")){
                channelViewHolder.call_iv.setVisibility(View.GONE);
                channelViewHolder.chat_iv.setVisibility(View.VISIBLE);
            }else {
                channelViewHolder.call_iv.setImageResource(R.drawable.old_phone_svg);
                channelViewHolder.call_iv.setVisibility(View.VISIBLE);
                channelViewHolder.chat_iv.setVisibility(View.GONE);
            }

          /*
            if(isNullOrEmpty(channels.get(position).getScreenName())){
                channelViewHolder.call_iv.setVisibility(View.VISIBLE);
                channelViewHolder.chat_iv.setVisibility(View.GONE);
            }else {
                channelViewHolder.call_iv.setVisibility(View.GONE);
                channelViewHolder.chat_iv.setVisibility(View.VISIBLE);
            }*/
        }
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {

        TextView channelName, tvName, tvDate;
        CardView cv_service;
        ImageView image_view,chat_iv,call_iv;

        ChannelViewHolder(View itemView) {
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

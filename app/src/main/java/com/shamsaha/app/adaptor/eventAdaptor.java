package com.shamsaha.app.adaptor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.shamsaha.app.ApiModel.event;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.PublicPart.event.EventViewActivity;
import com.shamsaha.app.databinding.EventCardAdaptorBinding;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.dateConversion;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class eventAdaptor extends RecyclerView.Adapter<eventAdaptor.ViewHolder> {

    private final ArrayList<event> eventsModel;
    private final Context context;

    public eventAdaptor(ArrayList<event> eventsModel, Context context) {
        this.eventsModel = eventsModel;
        this.context = context;
    }

    @NonNull
    @Override
    public eventAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(EventCardAdaptorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull eventAdaptor.ViewHolder holder, int position) {
        holder.rowBinding.root.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
        dateConversion d = new dateConversion();
        Log.d("respCall", "nnn\n" + eventsModel.get(position).getTitleEn());
        try {
            String date = d.dateConversion(eventsModel.get(position).getDate(), "yyyy-MM-dd");
            date = date.replace("-", " ");
            holder.rowBinding.Date.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
            holder.rowBinding.eventTitle.setText(eventsModel.get(position).getTitleEn());
        } else {
            holder.rowBinding.eventTitle.setText(eventsModel.get(position).getTitleAr());
        }

        holder.rowBinding.eventTitle.setText(eventsModel.get(position).getTitleEn());

        Glide.with(context)
                .load(eventsModel.get(position).getImage())
                .into(holder.rowBinding.img);

        String eventTtpe = eventsModel.get(position).getEventType();

        if (eventTtpe.equals("Free")) {
            holder.rowBinding.eventFee.setText(R.string.Free);
        } else if (eventTtpe.equals("No registration")) {
            holder.rowBinding.eventFee.setText(R.string.Free);
            holder.rowBinding.submit.setText(R.string.view_event);
        } else {
            holder.rowBinding.eventFee.setText(String.format("%s BHD", eventsModel.get(position).getPrice()));
        }

        holder.rowBinding.submit.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), EventViewActivity.class);
            intent.putExtra("id", eventsModel.get(position).getWceid());
            intent.putExtra("event_type", eventsModel.get(position).getEventType());
            intent.putExtra("title_en", eventsModel.get(position).getTitleEn());
            intent.putExtra("title_ar", eventsModel.get(position).getTitleAr());
            intent.putExtra("content_en", eventsModel.get(position).getContentEn());
            intent.putExtra("content_ar", eventsModel.get(position).getContentAr());
            intent.putExtra("venu", eventsModel.get(position).getVenu());
            intent.putExtra("venu_time", eventsModel.get(position).getVenuTime());
            intent.putExtra("date", eventsModel.get(position).getDate());
            intent.putExtra("price", eventsModel.get(position).getPrice());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return eventsModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final EventCardAdaptorBinding rowBinding;

        public ViewHolder(@NonNull EventCardAdaptorBinding binding) {
            super(binding.getRoot());
            rowBinding = binding;
        }
    }
}

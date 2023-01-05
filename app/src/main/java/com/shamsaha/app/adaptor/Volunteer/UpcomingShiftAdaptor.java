package com.shamsaha.app.adaptor.Volunteer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shamsaha.app.ApiModel.volunter.UpcomingShift;
import com.shamsaha.app.R;
import com.shamsaha.app.utils.dateConversion;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UpcomingShiftAdaptor extends RecyclerView.Adapter<UpcomingShiftAdaptor.ViewHolder> {

    private ArrayList<UpcomingShift> dataModel;
    private Context context;
    private Dialog dialog;


    public UpcomingShiftAdaptor(ArrayList<UpcomingShift> dataModel, Context context) {
        this.dataModel = dataModel;
        this.context = context;
    }


    @NonNull
    @Override
    public UpcomingShiftAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_shift_adaptor, parent, false);
        return new UpcomingShiftAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingShiftAdaptor.ViewHolder holder, int position) {

        holder.tv_language.setText(dataModel.get(position).getShiftLanguage());

        dateConversion d = new dateConversion();
        String modelDate = dataModel.get(position).getDate();
        if (modelDate!= null){
            try {
                String date = d.dateConversion(modelDate, "yyyy-MM-dd");
                date = date.replace("-", " ");
                holder.tv_deate.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            holder.tv_deate.setText(modelDate);
        }

        holder.upcoming_shifts.setText(dataModel.get(position).getVname());
        holder.tv_time.setText(dataModel.get(position).getShiftTime());

        Glide.with(context).load(dataModel.get(position).getImage()).into(holder.iv_updatedShift);

        String a = dataModel.get(position).getColor();
        String b = "sss";

        if (a!=null){
            if (a.equals("light")) {
                holder.upcoming_shifts.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_deate.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_language.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
            }else{
                holder.upcoming_shifts.setTextColor(Color.parseColor("#474B54"));
                holder.tv_deate.setTextColor(Color.parseColor("#474B54"));
                holder.tv_language.setTextColor(Color.parseColor("#474B54"));
                holder.tv_time.setTextColor(Color.parseColor("#474B54"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView upcoming_shifts, tv_deate, tv_language, tv_time;
        private RoundedImageView iv_updatedShift;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_time = itemView.findViewById(R.id.tv_time);
            upcoming_shifts = itemView.findViewById(R.id.upcoming_shifts);
            tv_deate = itemView.findViewById(R.id.tv_deate);
            tv_language = itemView.findViewById(R.id.tv_language);
//            profilePic3 = itemView.findViewById(R.id.profilePic3);
            iv_updatedShift = itemView.findViewById(R.id.iv_updatedShift);


        }
    }

}

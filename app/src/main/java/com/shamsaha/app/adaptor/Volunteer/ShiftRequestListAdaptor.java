package com.shamsaha.app.adaptor.Volunteer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.ApiModel.volunter.ShiftRequestMode;
import com.shamsaha.app.R;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.dateConversion;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftRequestListAdaptor extends RecyclerView.Adapter<ShiftRequestListAdaptor.ViewHolder> {

    private ArrayList<ShiftRequestMode> dataModel = new ArrayList<>();
    private Context context;
    private String VId;
    private Dialog dialog;


    public ShiftRequestListAdaptor(ArrayList<ShiftRequestMode> dataModel, Context context, String VId) {
        this.dataModel = dataModel;
        this.context = context;
        this.VId = VId;
    }

    @NonNull
    @Override
    public ShiftRequestListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_shift_adaptor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftRequestListAdaptor.ViewHolder holder, int position) {
        holder.tv_language.setText(dataModel.get(position).getShiftLanguage());
        dateConversion d = new dateConversion();
        try {
            if (dataModel.get(position).getDate()!=null) {
                String date = d.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                date = date.replace("-", " ");
                holder.tv_deate.setText(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_time.setText(dataModel.get(position).getShiftTime());
        Glide.with(context).load(dataModel.get(position).getImage()).into(holder.iv_updatedShift);
        String a = dataModel.get(position).getColor();
        String b = "sss";
        if (dataModel.get(position).getColor().equals("light")) {
            holder.upcoming_shifts.setTextColor(Color.parseColor("#ffffff"));
            holder.tv_deate.setTextColor(Color.parseColor("#ffffff"));
            holder.tv_language.setTextColor(Color.parseColor("#ffffff"));
            holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.upcoming_shifts.setTextColor(Color.parseColor("#474B54"));
            holder.tv_deate.setTextColor(Color.parseColor("#474B54"));
            holder.tv_language.setTextColor(Color.parseColor("#474B54"));
            holder.tv_time.setTextColor(Color.parseColor("#474B54"));
        }
        holder.upcoming_shifts.setText("Requested Shift");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (dataModel.get(position).getDate()!=null){
                        String date = d.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                        date = date.replace("-", " ");
//                        openDialog();
                        openDialog(date, dataModel.get(position).getShiftTime(), dataModel.get(position).getShiftName(), dataModel.get(position).getShiftLanguage(), dataModel.get(position).getWSchId());

                        Log.d("positionsss", String.valueOf(position));
                    }

                } catch (ParseException e) {
                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    private void openDialog(String date, String time, String shiftName, String shiftLanguage, String schedule_id) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_shift_reg);

        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        Button btn_accept = dialog.findViewById(R.id.btn_accept);
        TextView tv_time = dialog.findViewById(R.id.tv_time);
        TextView tv_job = dialog.findViewById(R.id.tv_job);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_time.setText(date + ", " + time + " ( " + shiftName + " )");

        tv_job.setText(shiftLanguage + " Advocacy");

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitShiftRegApi(date, VId, schedule_id, time, shiftName);
//                Intent i = new Intent(context, ShiftActivity.class);
//                i.putExtra("vounter_id",VId);
//                context.startActivity(i);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void hitShiftRegApi(String date, String volunteer_id, String schedule_id, String time, String shiftName) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.requwst_shift_accept(date, volunteer_id, schedule_id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getStatus().equals("success")) {

//                        StyleableToast.makeText(context, "Shift successfully registered", Toast.LENGTH_SHORT,R.style.mytoast).show();
                        for (int i = 0; i < 2; i++) {
                            StyleableToast.makeText(context, "Thank you for signing up for " + date + ", " + time + " " + shiftName + ". This shift is now confirmed and is your responsibility. ", Toast.LENGTH_SHORT, R.style.mytoast).show();
                        }
                    } else {
                        StyleableToast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    }
                } else {
                    StyleableToast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT, R.style.mytoast).show();
                }
            }
            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                StyleableToast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView upcoming_shifts, tv_deate, tv_time, tv_language;
        RoundedImageView iv_updatedShift;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_time = itemView.findViewById(R.id.tv_time);
            upcoming_shifts = itemView.findViewById(R.id.upcoming_shifts);
            tv_deate = itemView.findViewById(R.id.tv_deate);
            tv_language = itemView.findViewById(R.id.tv_language);
            iv_updatedShift = itemView.findViewById(R.id.iv_updatedShift);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }


}

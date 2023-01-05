package com.shamsaha.app.adaptor.Volunteer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.ApiModel.volunter.OnDateShift;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.ShiftActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.String2DateConversion;
import com.shamsaha.app.utils.dateConversion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OnDateShiftAdaptor extends RecyclerView.Adapter<OnDateShiftAdaptor.ViewHolder> {

    private ArrayList<OnDateShift> dataModel = new ArrayList<>();
    private Context context;
    private String vID;
    private Dialog dialog;

    public OnDateShiftAdaptor(ArrayList<OnDateShift> dataModel, Context context, String vID) {
        this.dataModel = dataModel;
        this.context = context;
        this.vID = vID;
    }

    @NonNull
    @Override
    public OnDateShiftAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if ((!dataModel.get(position).getVolunteerAssign().equals("")) && !dataModel.get(position).getVolunteerAssign().equals(vID)) {
            dateConversion d = new dateConversion();
            holder.btn_shift.setVisibility(View.INVISIBLE);
            holder.imageView2.setVisibility(View.INVISIBLE);
//            holder.btn_shift.setText("Open Shift");
//            holder.btn_shift.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorYellow));
            try {
                String date = d.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                date = date.replace("-", " ");
                holder.tv_date.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //holder.tv_date.setText(dataModel.get(position).getDate());
            if (dataModel.get(position).getColor().equals("light")) {
                holder.tv_language_lbl.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_date.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_language.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_name.setTextColor(Color.parseColor("#ffffff"));
            }
            holder.tv_language.setText(dataModel.get(position).getShiftLanguage());
            holder.tv_time.setText(dataModel.get(position).getShiftTime());
            holder.tv_name.setText(dataModel.get(position).getVname());

            Glide.with(context).load(dataModel.get(position).getImage()).into(holder.imageView1);

        } else if (dataModel.get(position).getVolunteerAssign().equals(vID)) {
            Glide.with(context).load(R.drawable.reject_btn_round).into(holder.imageView2);

            Calendar cc1 = Calendar.getInstance();
            Date d3 = null;
            Date d2 = null;
            Date datewq = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(datewq);
            Log.d("dateeeeee", strDate);
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                d3 = sdformat.parse(dataModel.get(position).getDate());
                d2 = sdformat.parse(strDate);
                Log.d("dateeeeee", "date= " + d3 + "\n" + d2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (d3.before(d2)) {
                Log.d("dateeeeee", "past");
                holder.imageView2.setVisibility(View.INVISIBLE);
            } else if (d3.after(d2)) {
                Log.d("dateeeeee", "future");
            } else if (d3.equals(d2)) {
                Log.d("dateeeeee", "equal");
                holder.imageView2.setVisibility(View.INVISIBLE);
            }
            dateConversion d1 = new dateConversion();
            String2DateConversion s2d = new String2DateConversion();
            Calendar c = Calendar.getInstance();
            Calendar cc = Calendar.getInstance();
            Date cd = cc.getTime();
            c.add(Calendar.DAY_OF_MONTH, 14);
            Date currentDate = c.getTime();
            Date mDate = null;
            try {
                mDate = s2d.String2DateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (currentDate.before(mDate)) {
                holder.imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String modelDate = d1.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                            modelDate = modelDate.replace("-", " ");
//                        openDialog();
                            cancelDialog(modelDate, dataModel.get(position).getShiftTime(), dataModel.get(position).getShiftName(), dataModel.get(position).getShiftLanguage(), dataModel.get(position).getWSchId());

                            Log.d("positionsss", String.valueOf(position));
                        } catch (ParseException e) {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Glide.with(context).load(R.drawable.ic_request).into(holder.imageView2);
                holder.imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String modelDate = null;
                        try {
                            modelDate = d1.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        modelDate = modelDate.replace("-", " ");
                        requestDialog(modelDate, dataModel.get(position).getShiftTime(), dataModel.get(position).getShiftName(), dataModel.get(position).getShiftLanguage(), dataModel.get(position).getWSchId());

//                        StyleableToast.makeText(context, "The Shift is below 14 days, your not able to cancel.\nPlease contact Admin ", Toast.LENGTH_SHORT,R.style.mytoast).show();
                    }
                });
            }


            dateConversion d = new dateConversion();
            try {
                String date = d.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                date = date.replace("-", " ");
                holder.tv_date.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //holder.tv_date.setText(dataModel.get(position).getDate());
            if (dataModel.get(position).getColor().equals("light")) {
                holder.tv_language_lbl.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_date.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_language.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_name.setTextColor(Color.parseColor("#ffffff"));
            }
            holder.tv_language.setText(dataModel.get(position).getShiftLanguage());
            holder.tv_time.setText(dataModel.get(position).getShiftTime());
            holder.tv_name.setText(dataModel.get(position).getVname());


            Glide.with(context).load(dataModel.get(position).getImage()).into(holder.imageView1);


        } else {
            Calendar cc1 = Calendar.getInstance();
            Date d1 = null;
            Date d2 = null;
            Date datewq = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(datewq);
            Log.d("dateeeeee", strDate);
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                d1 = sdformat.parse(dataModel.get(position).getDate());
                d2 = sdformat.parse(strDate);
                Log.d("dateeeeee", "date= " + d1 + "\n" + d2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (d1.before(d2)) {
                Log.d("dateeeeee", "past");
                holder.imageView2.setVisibility(View.INVISIBLE);
            } else if (d1.after(d2)) {
                Log.d("dateeeeee", "future");
            } else if (d1.equals(d2)) {
                Log.d("dateeeeee", "equal");
            }

            holder.btn_shift.setText("Open Shift");
            //holder.btn_shift.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorYellow));
            holder.btn_shift.setBackgroundResource(R.drawable.ic_open_shift_btn);
            dateConversion d = new dateConversion();
            try {
                String date = d.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                date = date.replace("-", " ");
                holder.tv_date.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //holder.tv_date.setText(dataModel.get(position).getDate());
            if (dataModel.get(position).getColor().equals("light")) {
                holder.tv_language_lbl.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_date.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_language.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_name.setTextColor(Color.parseColor("#ffffff"));
            }
            holder.tv_language.setText(dataModel.get(position).getShiftLanguage());
            holder.tv_time.setText(dataModel.get(position).getShiftTime());
            holder.tv_name.setText(" ");

            holder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        String date = d.dateConversion(dataModel.get(position).getDate(), "yyyy-MM-dd");
                        date = date.replace("-", " ");
//                        openDialog();
                        openDialog(date, dataModel.get(position).getShiftTime(), dataModel.get(position).getShiftName(), dataModel.get(position).getShiftLanguage(), dataModel.get(position).getWSchId());

                        Log.d("positionsss", String.valueOf(position));
                    } catch (ParseException e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }
            });

            Glide.with(context).load(dataModel.get(position).getImage()).into(holder.imageView1);


        }

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
                hitShiftRegApi(date, vID, schedule_id, time, shiftName);
                Intent i = new Intent(context, ShiftActivity.class);
                i.putExtra("vounter_id", vID);
                context.startActivity(i);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    private void cancelDialog(String date, String time, String shiftName, String shiftLanguage, String schedule_id) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_shift_cancel);

        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        Button btn_accept = dialog.findViewById(R.id.btn_accept);
        TextView tv_time = dialog.findViewById(R.id.tv_time);
        TextView tv_job = dialog.findViewById(R.id.tv_job);
        EditText editTextTextPersonName = dialog.findViewById(R.id.editTextReason);
        TextView tv_reason_title = dialog.findViewById(R.id.tv_reason_title);
        tv_reason_title.setVisibility(View.GONE);
        editTextTextPersonName.setVisibility(View.GONE);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitShiftCancelApi(vID, schedule_id);
                Intent i = new Intent(context, ShiftActivity.class);
                i.putExtra("vounter_id", vID);
                context.startActivity(i);
            }
        });

        tv_time.setText(date + ", " + time + " ( " + shiftName + " )");

        tv_job.setText(shiftLanguage + " Advocacy");

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void requestDialog(String date, String time, String shiftName, String shiftLanguage, String schedule_id) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_shift_cancel);

        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        Button btn_accept = dialog.findViewById(R.id.btn_accept);
        TextView tv_time = dialog.findViewById(R.id.tv_time);
        TextView tv_job = dialog.findViewById(R.id.tv_job);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        EditText editTextTextPersonName = dialog.findViewById(R.id.editTextReason);

        tv_title.setText("Shift Swap Request ");
        btn_accept.setText("Send Request");

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(context, "Enter the reason" + editTextTextPersonName.getText().toString(), Toast.LENGTH_SHORT).show();

                if(editTextTextPersonName.getText().toString().length() != 0){
                    hitShiftRequestApi(vID, schedule_id, editTextTextPersonName.getText().toString());
                    Intent i = new Intent(context, ShiftActivity.class);
                    i.putExtra("vounter_id", vID);
                    context.startActivity(i);
                }else{
                    Toast.makeText(context, "Enter the reason", Toast.LENGTH_SHORT).show();
                }


            }
        });

        tv_time.setText(date + ", " + time + " ( " + shiftName + " )");
        tv_job.setText(shiftLanguage + " Advocacy");
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void hitShiftRegApi(String date, String volunteer_id, String schedule_id, String time, String shiftName) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.shift_accept(date, volunteer_id, schedule_id);
        Log.d("ressssssssss", "onResponse: " + date + "   " + volunteer_id + "   " + schedule_id);

        call.enqueue(new Callback<MessageModel>() {

            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("valid")) {
//                        StyleableToast.makeText(context, "Shift successfully registered", Toast.LENGTH_SHORT,R.style.mytoast).show();
                        for (int i = 0; i < 2; i++) {
                            StyleableToast.makeText(context, "Thank you for signing up for " + date + ", " + time + " " + shiftName + ". This shift is now confirmed and is your responsibility. ", Toast.LENGTH_SHORT, R.style.mytoast).show();
                        }
                    } else {
                        StyleableToast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                        Log.d("ressssssssss", "onResponse: " + response.body().getStatus());
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

    private void hitShiftCancelApi(String volunteer_id, String schedule_id) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.shift_cancel(volunteer_id, schedule_id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    StyleableToast.makeText(context, "Shift successfully canceled", Toast.LENGTH_SHORT, R.style.mytoast).show();
                } else {
                    StyleableToast.makeText(context, "err: " + response.errorBody(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                StyleableToast.makeText(context, "err: " + t.getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        });
    }

    private void hitShiftRequestApi(String volunteer_id, String schedule_id, String Reason) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.shift_request(volunteer_id, schedule_id, Reason);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("valid")) {
                        StyleableToast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    } else {
                        StyleableToast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    }
                } else {
                    StyleableToast.makeText(context, "err: " + response.errorBody(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                }

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                StyleableToast.makeText(context, "err: " + t.getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_date, tv_time, tv_language, tv_language_lbl;
        private Button btn_shift;
        private ImageView imageView2;
        private RoundedImageView imageView1;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_time = itemView.findViewById(R.id.tv_time);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_language = itemView.findViewById(R.id.tv_language);
            btn_shift = itemView.findViewById(R.id.btn_shift);
            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView1 = itemView.findViewById(R.id.imageView1);
            tv_language_lbl = itemView.findViewById(R.id.tv_language_lbl);

        }
    }

}

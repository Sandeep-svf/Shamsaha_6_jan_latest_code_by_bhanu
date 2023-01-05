package com.shamsaha.app.adaptor.Volunteer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.ShiftActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.String2DateConversion;
import com.shamsaha.app.utils.dateConversion;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ShiftListChieldAdaptor extends RecyclerView.Adapter<ShiftListChieldAdaptor.ViewHolder> {

    JSONArray items;
    Context context;
    private Dialog dialog;
    private String vID;


    String sProfilePic;
    String sProfileName;
    String sProfileLanguage;
    String sProfileMobile;
    String sProfileEmail;


    public ShiftListChieldAdaptor(JSONArray items,Context context,String vID) {
        this.items = items;
        this.context = context;
        this.vID = vID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shift_list_chield_adaptor,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Log.d(TAG, "onBindViewHolder: "+items.getJSONObject(position).getString("w_sch_id"));
            Log.d(TAG, "onBindViewHolder: ");

//            holder.itemTextView.setText(items.getJSONObject(position).getString("w_sch_id"));

            Glide.with(context).load(items.getJSONObject(position).getString("image")).into(holder.iv_updatedShift);
            String proPic = items.getJSONObject(position).getString("profile_pic");
            String vname = items.getJSONObject(position).getString("vname");
            String volunteer_assign = items.getJSONObject(position).getString("volunteer_assign");
            String color = items.getJSONObject(position).getString("color");
            String dateRes = items.getJSONObject(position).getString("date");
            String shift_time = items.getJSONObject(position).getString("shift_time");
            String shift_language = items.getJSONObject(position).getString("shift_language");
            String shift_name = items.getJSONObject(position).getString("shift_name");
            String w_sch_id = items.getJSONObject(position).getString("w_sch_id");

            dateConversion d = new dateConversion();
            try {
                String date = d.dateConversion(dateRes, "yyyy-MM-dd");
                date = date.replace("-", " ");
                holder.tv_deate.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (color.equals("light")) {
                holder.upcoming_shifts.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_deate.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_language.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
            }
            holder.upcoming_shifts.setText("Open Shift");
            holder.tv_language.setText(shift_language);
            holder.tv_time.setText(shift_time);
            Glide.with(context).load(R.drawable.ic_smalllogo).into(holder.profilePic);

            if(volunteer_assign.isEmpty()){
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            String date = d.dateConversion(dateRes, "yyyy-MM-dd");
                            date = date.replace("-", " ");
//                          openDialog();
                            openDialog(date,shift_time , shift_name , shift_language , w_sch_id);
                            Log.d("positionsss",String.valueOf(position));
                        } catch (ParseException e) {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }


            if(!volunteer_assign.isEmpty()){
                holder.upcoming_shifts.setText(vname);
                Glide.with(context).load(proPic).into(holder.profilePic);

                dateConversion d1 = new dateConversion();

                holder.cardView.setOnClickListener(view -> hitProfileApi(volunteer_assign));
            }

             if (volunteer_assign.equals(vID)) {

                 dateConversion d1 = new dateConversion();
                 String2DateConversion s2d = new String2DateConversion();

                 Calendar c = Calendar.getInstance();
                 c.add(Calendar.DAY_OF_MONTH, 14);
                 Date currentDate = c.getTime();
                 Date mDate = null;
                 try {
                     mDate =  s2d.String2DateConversion(dateRes,"yyyy-MM-dd");
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }

                 if(currentDate.before(mDate)){
                     holder.cardView.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             try {
                                 String modelDate = d1.dateConversion(dateRes, "yyyy-MM-dd");
                                 modelDate = modelDate.replace("-", " ");
//                        openDialog();
                                 cancelDialog(modelDate , shift_time , shift_name , shift_language , w_sch_id);

                                 Log.d("positionsss",String.valueOf(position));
                             } catch (ParseException e) {
                                 Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                 e.printStackTrace();
                             }
                         }
                     });
                 }else{

                     holder.cardView.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                             String modelDate = null;
                             try {
                                 modelDate = d1.dateConversion(dateRes, "yyyy-MM-dd");
                             } catch (ParseException e) {
                                 e.printStackTrace();
                             }
                             modelDate = modelDate.replace("-", " ");

                             requestDialog(modelDate , shift_time , shift_name , shift_language , w_sch_id);

//                        StyleableToast.makeText(context, "The Shift is below 14 days, your not able to cancel.\nPlease contact Admin ", Toast.LENGTH_SHORT,R.style.mytoast).show();
                         }
                     });
                 }

             }
            } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView upcoming_shifts, tv_deate , tv_language, tv_time;
        RoundedImageView iv_updatedShift;
        CardView cardView;
        CircleImageView profilePic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            upcoming_shifts = itemView.findViewById(R.id.upcoming_shifts);
            tv_deate = itemView.findViewById(R.id.tv_deate);
            tv_language = itemView.findViewById(R.id.tv_language);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_updatedShift = itemView.findViewById(R.id.iv_updatedShift);
            cardView = itemView.findViewById(R.id.cardView);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }

    private void hitShiftRegApi(String date, String volunteer_id, String schedule_id) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.shift_accept(date,volunteer_id,schedule_id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if(response.isSuccessful()){

                    if(response.body().getStatus().equals("valid")){
                        StyleableToast.makeText(context, "Shift successfully registered", Toast.LENGTH_SHORT,R.style.mytoast).show();
                    }else {
                        StyleableToast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT,R.style.mytoast).show();
                    }
                }
                else {
                    StyleableToast.makeText(context, "err: "+response.errorBody(), Toast.LENGTH_SHORT,R.style.mytoast).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                StyleableToast.makeText(context, "err: "+t.getMessage(), Toast.LENGTH_SHORT,R.style.mytoast).show();}
        });
    }

    private void hitShiftCancelApi(String volunteer_id, String schedule_id) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.shift_cancel(volunteer_id,schedule_id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if(response.isSuccessful()){
                    StyleableToast.makeText(context, "Shift successfully canceled", Toast.LENGTH_SHORT,R.style.mytoast).show();
                }
                else {
                    StyleableToast.makeText(context, "err: "+response.errorBody(), Toast.LENGTH_SHORT,R.style.mytoast).show();}

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                StyleableToast.makeText(context, "err: "+t.getMessage(), Toast.LENGTH_SHORT,R.style.mytoast).show();}
        });
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

        tv_time.setText(date+", "+time+" ( "+shiftName+" )");

        tv_job.setText(shiftLanguage+" Advocacy");

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hitShiftRegApi(date,vID,schedule_id);
                Intent i = new Intent(context, ShiftActivity.class);
                i.putExtra("vounter_id",vID);
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
        TextView tv_reason_title = dialog.findViewById(R.id.tv_reason_title);
        EditText editTextTextPersonName = dialog.findViewById(R.id.editTextReason);

        editTextTextPersonName.setVisibility(View.GONE);
        tv_reason_title.setVisibility(View.GONE);


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
                i.putExtra("vounter_id",vID);
                context.startActivity(i);
            }
        });
        tv_time.setText(date+", "+time+" ( "+shiftName+" )");
        tv_job.setText(shiftLanguage+" Advocacy");
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

        tv_title.setText("Shift Change Request ");
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
                hitShiftCancelApi(vID, schedule_id);
                Intent i = new Intent(context, ShiftActivity.class);
                i.putExtra("vounter_id",vID);
                context.startActivity(i);
            }
        });

        tv_time.setText(date+", "+time+" ( "+shiftName+" )");
        tv_job.setText(shiftLanguage+" Advocacy");
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void openProfileDialog(String sProfilePic, String sProfileName, String sProfileLanguage, String sProfileMobile, String sProfileEmail) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.other_volunteer_profile);
        ImageView i = dialog.findViewById(R.id.closeBtn2);
        CircleImageView profilePic = dialog.findViewById(R.id.profilePic);
        TextView profileName = dialog.findViewById(R.id.profileName);
        TextView profileLanguage = dialog.findViewById(R.id.profileLanguage);
        TextView profileMobile = dialog.findViewById(R.id.profileMobile);
        TextView profileEmail = dialog.findViewById(R.id.profileEmail);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        profileName.setText(sProfileName);
        profileLanguage.setText(sProfileLanguage);
        profileEmail.setText(sProfileEmail);
        profileMobile.setText(sProfileMobile);

        profileEmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + sProfileMobile));
            context.startActivity(intent);
        });

        profileEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "info@shamsaha.org", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });

        Glide.with(context).load(sProfilePic).placeholder(R.drawable.ic_smalllogo).into(profilePic);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void hitProfileApi(String vounter_id) {
        api api = retrofit.retrofit.create(api.class);
        Call<List<ProfileInfoModel>> call = api.volunteer_info(vounter_id);
        Log.d("sssss", "vounter_id : " + vounter_id);
        call.enqueue(new Callback<List<ProfileInfoModel>>() {
            @Override
            public void onResponse(Call<List<ProfileInfoModel>> call, Response<List<ProfileInfoModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<ProfileInfoModel> pro = response.body();
                    for (ProfileInfoModel infoModel : pro) {
                        sProfileName = infoModel.getVname();
                        sProfilePic = infoModel.getProfilePic();
                        sProfileEmail = infoModel.getVemail();
                        sProfileLanguage = infoModel.getLanguageKnown();
                        sProfileMobile = infoModel.getVmobile();
                    }
                    openProfileDialog(sProfilePic,sProfileName,sProfileLanguage,sProfileMobile,sProfileEmail);
                } else {
                    Log.d("sssss", "err " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<ProfileInfoModel>> call, Throwable t) {
                Log.d("sssss", "err " + t.getMessage());
            }
        });
    }
}

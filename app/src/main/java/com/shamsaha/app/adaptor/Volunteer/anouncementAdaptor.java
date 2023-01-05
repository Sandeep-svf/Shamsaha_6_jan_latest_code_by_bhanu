package com.shamsaha.app.adaptor.Volunteer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.volunter.Announcement;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.AlertActivity;
import com.shamsaha.app.utils.dateConversion;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class anouncementAdaptor extends RecyclerView.Adapter<anouncementAdaptor.ViewHolder> {

    private ArrayList<Announcement> AnouncementModel = new ArrayList<>();
    private Context context;
    private Dialog dialog;


    public anouncementAdaptor(ArrayList<Announcement> AnouncementModel, Context context) {
        this.AnouncementModel = AnouncementModel;
        this.context = context;
    }

    @NonNull
    @Override
    public anouncementAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anouncement_holder, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull anouncementAdaptor.ViewHolder holder, int position) {

        dateConversion d = new dateConversion();
        try {
            String date = d.dateConversion(AnouncementModel.get(position).getDate(),"yyyy-MM-dd");
            date = date.replace("-"," ");
            holder.tv_dare.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tv_announce.setText(AnouncementModel.get(position).getSubjectEn());

        holder.ll_announcemet_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                openDialog(AnouncementModel.get(position).getSubjectEn(),AnouncementModel.get(position).getContentEn());
                Intent intent = new Intent(context, AlertActivity.class);
                intent.putExtra("getContentEn",AnouncementModel.get(position).getContentEn());
                intent.putExtra("getImage",AnouncementModel.get(position).getImage());
                intent.putExtra("getSubjectEn",AnouncementModel.get(position).getSubjectEn());
                intent.putExtra("getType",AnouncementModel.get(position).getType());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return AnouncementModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_dare, tv_announce;
        private LinearLayout ll_announcemet_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dare = itemView.findViewById(R.id.tv_dare);
            tv_announce = itemView.findViewById(R.id.tv_announce);
            ll_announcemet_root = itemView.findViewById(R.id.ll_announcemet_root);


        }
    }

    private void openDialog(String Subject, String Message) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        TextView subject = dialog.findViewById(R.id.tv_title);
        TextView message = dialog.findViewById(R.id.tv_message);

        subject.setText(Subject);
        message.setText(Message);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    private void openDialogImage(String Subject, String Message) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        TextView subject = dialog.findViewById(R.id.tv_title);
        TextView message = dialog.findViewById(R.id.tv_message);
        WebView webView = dialog.findViewById(R.id.webViewAlert);

        subject.setText(Subject);
        message.setText(Message);


        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}

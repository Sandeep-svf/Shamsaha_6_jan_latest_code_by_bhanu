package com.shamsaha.app.adaptor.Volunteer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.volunter.HelpRequestModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Channel.ChannelManager;
import com.shamsaha.app.activity.ChatHelper.Message.ChatClientManager;
import com.twilio.chat.Channels;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class HelpRequestListAdaptor extends RecyclerView.Adapter<HelpRequestListAdaptor.ViewHolder> {

    private static final String TAG = "abc" ;
    private List<HelpRequestModel.Datum> helpRequestModels = new ArrayList<>();
    private Context context;
    private Dialog dialog,dialog1;
    private String volID;
    private static ChannelManager channelManager;
    private Channels channelsObject;
    private ChatClientManager chatClientManager;

    public HelpRequestListAdaptor(List<HelpRequestModel.Datum> helpRequestModels, Context context, Dialog dialog, String volID) {
        this.helpRequestModels = helpRequestModels;
        this.context = context;
        this.dialog = dialog;
        this.volID = volID;
    }



    @NonNull
    @Override
    public HelpRequestListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_req_list_holder, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HelpRequestListAdaptor.ViewHolder holder, int position) {

        String getSupportType = "";

        if(helpRequestModels.get(position).getSupportType().endsWith(",")){
            getSupportType = helpRequestModels.get(position).getSupportType().substring(0,helpRequestModels.get(position).getSupportType().length() - 1);
        }

        holder.caseID.setText(helpRequestModels.get(position).getCaseId());
        holder.supportType.setText(getSupportType+" Support");
        holder.message.setText(helpRequestModels.get(position).getMessage());
        if(helpRequestModels.get(position).getLanguage().length() > 3){
            holder.language.setText(" ("+helpRequestModels.get(position).getLanguage()+")");
        }else {
            holder.language.setVisibility(View.GONE);
        }

        holder.ll_root.setOnClickListener(view -> {
            dialog.dismiss();
            dialog1 = new Dialog(context);
            openHelpListDialog(dialog1,helpRequestModels.get(position).getCaseId(),volID,helpRequestModels.get(position).getId());
        });

    }

    @Override
    public int getItemCount() {
        return helpRequestModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView caseID, supportType, message, language;
        private LinearLayout ll_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseID = itemView.findViewById(R.id.caseID);
            supportType = itemView.findViewById(R.id.supportType);
            message = itemView.findViewById(R.id.message);
            language = itemView.findViewById(R.id.language);
            ll_root = itemView.findViewById(R.id.ll_root);
        }
    }

    private void openHelpListDialog(Dialog dialog1,String CaseID,String VolID,String id) {
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.help_request_list_dialog);
        ImageView closeImg2 = dialog1.findViewById(R.id.closeBtn2);
        LinearLayout list_items = dialog1.findViewById(R.id.list_items);
        LinearLayout accept = dialog1.findViewById(R.id.accept);
        TextView alertText = dialog1.findViewById(R.id.alertText);
        CardView cardAcceptBtn = dialog1.findViewById(R.id.cardAcceptBtn);
        closeImg2.setOnClickListener(view -> {dialog1.dismiss();});
        alertText.setText("Are you sure wants to join with the case id ("+CaseID+") ?");

        cardAcceptBtn.setOnClickListener(view -> {
            dialog1.dismiss();
            joinChannel(CaseID,id);
        });

        list_items.setVisibility(View.GONE);
        accept.setVisibility(View.VISIBLE);
        closeImg2.setOnClickListener(v -> dialog1.dismiss());
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog1.show();
    }








    private void joinChannel(String caseID,String id ) {
        Intent intent = new Intent("custom-message");
        intent.putExtra("caseID",caseID);
        intent.putExtra("id",id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }



}

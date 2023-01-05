package com.shamsaha.app.adaptor.Volunteer.resources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.resource;
import com.shamsaha.app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ResourceContactVolAdaptor extends RecyclerView.Adapter<ResourceContactVolAdaptor.ViewHolder> {

    public String wcrcid, category_name, category_icon;
    String Title, Jdate, JobType, Brief;
    private ArrayList<resource> dataModels = new ArrayList<>();
    private Context context;


    public ResourceContactVolAdaptor(Context context, ArrayList<resource> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ResourceContactVolAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vol_resource_contact_adaptor, parent, false);
        return new ViewHolder(view) {

            @Override
            public void setFixedHeight() {
                //  magic happening here
                ViewGroup.LayoutParams parentParams = parent.getLayoutParams();
                parentParams.height =
                        ((RecyclerView) parent).computeVerticalScrollRange()
                                + parent.getPaddingTop()
                                + parent.getPaddingBottom();
                parent.setLayoutParams(parentParams);
            }
        };

    }

    @Override
    public void onBindViewHolder(@NonNull ResourceContactVolAdaptor.ViewHolder holder, final int position) {

        //holder.setFixedHeight();
//        Log.d("hold___", "nnn\n" + dataModels.get(position).getEmailInfo());

        String em1 = dataModels.get(position).getEmailInfo1();
        String em2 = dataModels.get(position).getEmailInfo2();
        String em3 = dataModels.get(position).getEmailInfo3();

        String c1 = dataModels.get(position).getContactInfo1();
        String c2 = dataModels.get(position).getContactInfo2();
        String c3 = dataModels.get(position).getContactInfo3();
        String c4 = dataModels.get(position).getContactInfo4();

        String w1 = dataModels.get(position).getWebInfo1();
        String w2 = dataModels.get(position).getWebInfo2();

//        String emSize = String.valueOf(em.length());
//        Log.d("hold___", "nnn\n" + emSize);

        if(c1.equals("") && c2.equals("") && c3.equals("")  && c4.equals("") ){
            holder.contact.setVisibility(View.GONE);
        }else {
            if(c1.equals("")){
                holder.contact_info_1.setVisibility(View.GONE);
            }else {
                holder.contact_info_1.setText(c1);
            }

            if(c2.equals("")){
                holder.contact_info_2.setVisibility(View.GONE);
            }else {
                holder.contact_info_2.setText(c2);
            }

            if(c3.equals("")){
                holder.contact_info_3.setVisibility(View.GONE);
            }else {
                holder.contact_info_3.setText(c3);
            }

            if(c4.equals("")){
                holder.contact_info_4.setVisibility(View.GONE);
            }else {
                holder.contact_info_4.setText(c4);
            }
        }

        holder.contact_info_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dataModels.get(position).getContactInfo1()));
                context.startActivity(intent);
            }
        });
        holder.contact_info_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dataModels.get(position).getContactInfo2()));
                context.startActivity(intent);
            }
        });
        holder.contact_info_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dataModels.get(position).getContactInfo3()));
                context.startActivity(intent);
            }
        });
        holder.contact_info_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dataModels.get(position).getContactInfo4()));
                context.startActivity(intent);
            }
        });




        if(em1.equals("") && em2.equals("") && em3.equals("") ){
            holder.email.setVisibility(View.GONE);
        }else {
            if(em1.equals("")){
                holder.email_info_1.setVisibility(View.GONE);
            }else {
                holder.email_info_1.setText(em1);
            }

            if(em2.equals("")){
                holder.email_info_2.setVisibility(View.GONE);
            }else {
                holder.email_info_2.setText(em2);
            }

            if(em3.equals("")){
                holder.email_info_3.setVisibility(View.GONE);
            }else {
                holder.email_info_3.setText(em3);
            }
        }


        holder.email_info_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("hold___", "" + dataModels.get(position).getContactInfo());

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", dataModels.get(position).getEmailInfo1(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });

        holder.email_info_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("hold___", "" + dataModels.get(position).getContactInfo());

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", dataModels.get(position).getEmailInfo2(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });

        holder.email_info_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("hold___", "" + dataModels.get(position).getContactInfo());

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", dataModels.get(position).getEmailInfo3(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });

        if(w1.equals("") && w2.equals("") ){
            holder.web.setVisibility(View.GONE);
        }else {
            if(w1.equals("")){
                holder.web_info_1.setVisibility(View.GONE);
            }else {
                holder.web_info_1.setText(w1);
            }

            if(w2.equals("")){
                holder.web_info_2.setVisibility(View.GONE);
            }else {
                holder.web_info_2.setText(w2);
            }

        }
        holder.web_info_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hold___", "" + dataModels.get(position).getWebInfo1());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+dataModels.get(position).getWebInfo1()));
                context.startActivity(intent);
            }
        });

        holder.web_info_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hold___", "" + dataModels.get(position).getWebInfo2());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+dataModels.get(position).getWebInfo2()));
                context.startActivity(intent);
            }
        });




        holder.nameTV.setText(dataModels.get(position).getName());
        holder.address_info.setText(dataModels.get(position).getAddressInfo());


        if(dataModels.get(position).getAddressInfo().equals("")){
            holder.address.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV,
                contact_info_1,
                contact_info_2,
                contact_info_3,
                contact_info_4,
                email_info_1,
                email_info_2,
                email_info_3,
                address_info,
                web_info_1,
                web_info_2;
        private CardView Root_layout;
        private ImageView Cat_img;
        private LinearLayout email,contact,address,web;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Root_layout = itemView.findViewById(R.id.Root_layout);
            nameTV = itemView.findViewById(R.id.nameTV);
            contact_info_1 = itemView.findViewById(R.id.contact_info_1);
            contact_info_2 = itemView.findViewById(R.id.contact_info_2);
            contact_info_3 = itemView.findViewById(R.id.contact_info_3);
            contact_info_4 = itemView.findViewById(R.id.contact_info_4);
            email_info_1 = itemView.findViewById(R.id.email_info_1);
            email_info_2 = itemView.findViewById(R.id.email_info_2);
            email_info_3 = itemView.findViewById(R.id.email_info_3);
            address_info = itemView.findViewById(R.id.address_info);
            web_info_1 = itemView.findViewById(R.id.web_info_1);
            web_info_2 = itemView.findViewById(R.id.web_info_2);
            email = itemView.findViewById(R.id.email);
            contact = itemView.findViewById(R.id.contact);
            address = itemView.findViewById(R.id.address);
            web = itemView.findViewById(R.id.web);
        }

        public abstract void setFixedHeight();

    }
}
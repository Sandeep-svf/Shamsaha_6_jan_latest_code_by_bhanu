package com.shamsaha.app.adaptor.PublicPart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.shamsaha.app.ApiModel.PublicPart.AboutModel.Partner;
import com.shamsaha.app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PartnerAdaptor extends RecyclerView.Adapter<PartnerAdaptor.ViewHolder> {

    public String wcrcid, category_name, category_icon;
    String Title, Jdate, JobType, Brief;
    private ArrayList<Partner> dataModels = new ArrayList<>();
    private Context context;


    public PartnerAdaptor(Context context, ArrayList<Partner> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PartnerAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_partners, parent, false);
        return new ViewHolder(view){

        };

    }

    @Override
    public void onBindViewHolder(@NonNull PartnerAdaptor.ViewHolder holder, final int position) {

        Glide.with(context)
                .load(dataModels.get(position).getImage())
                .centerInside()
                .into(holder.image1);

//        holder.name1.setText(dataModels.get(position).getPname());
//
//        holder.tv_url.setText(dataModels.get(position).getWebsite());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+dataModels.get(position).getWebsite()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

//        private TextView name1,tv_url;
        private ImageView image1;
        private LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            image1 = itemView.findViewById(R.id.image1);
//            name1 = itemView.findViewById(R.id.name1);
//            tv_url = itemView.findViewById(R.id.tv_url);
        }


    }
}
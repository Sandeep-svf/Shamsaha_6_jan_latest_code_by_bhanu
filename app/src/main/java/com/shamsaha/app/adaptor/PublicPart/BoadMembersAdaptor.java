package com.shamsaha.app.adaptor.PublicPart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shamsaha.app.ApiModel.PublicPart.AboutModel.BordMembers;
import com.shamsaha.app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class BoadMembersAdaptor extends RecyclerView.Adapter<BoadMembersAdaptor.ViewHolder> {

    public String wcrcid, category_name, category_icon;
    String Title, Jdate, JobType, Brief;
    private ArrayList<BordMembers> dataModels = new ArrayList<>();
    private Context context;


    public BoadMembersAdaptor(Context context, ArrayList<BordMembers> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BoadMembersAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_board, parent, false);
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
    public void onBindViewHolder(@NonNull BoadMembersAdaptor.ViewHolder holder, final int position) {

        Glide.with(context)
                .load(dataModels.get(position).getImage())
                .centerInside()
                .into(holder.advocacy_board_img);

        holder.name.setText(dataModels.get(position).getBname());

        holder.designation.setText(dataModels.get(position).getDesignation());


    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,designation;
        private CircleImageView advocacy_board_img;
        private LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            advocacy_board_img = itemView.findViewById(R.id.advocacy_board_img);
            name = itemView.findViewById(R.id.name);
            designation = itemView.findViewById(R.id.designation);
//            tv_url = itemView.findViewById(R.id.tv_url);
        }

        public abstract void setFixedHeight();

    }
}
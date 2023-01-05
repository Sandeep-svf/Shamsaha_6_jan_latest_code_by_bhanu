package com.shamsaha.app.adaptor.Volunteer.resources;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shamsaha.app.ApiModel.ResourceCategory;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.Resources.VolResourceContactsActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ResourceCategoryVolAdaptor extends RecyclerView.Adapter<ResourceCategoryVolAdaptor.ViewHolder> {

    public String wcrcid, category_name, category_icon;
    String Title, Jdate, JobType, Brief;
    private ArrayList<ResourceCategory> dataModels = new ArrayList<>();
    private Context context;


    public ResourceCategoryVolAdaptor(Context context, ArrayList<ResourceCategory> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ResourceCategoryVolAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_category_layout, parent, false);
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
    public void onBindViewHolder(@NonNull ResourceCategoryVolAdaptor.ViewHolder holder, final int position) {

        //holder.setFixedHeight();
        Log.d("hold__",""+dataModels.get(position).getCategoryName());
        category_name = dataModels.get(position).getCategoryName();
        category_icon = dataModels.get(position).getCategoryIcon();
        holder.Cat_id.setText(category_name);


        Glide.with(context)
                .load(category_icon)
                .centerInside()
                .into(holder.Cat_img);

        holder.Rootlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "ID: " + dataModels.get(position).getWcrcid() + "\nWcrid: " + dataModels.get(position).getWcrid(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, VolResourceContactsActivity.class);
                i.putExtra("Location", dataModels.get(position).getWcrid());
                i.putExtra("Category",dataModels.get(position).getWcrcid());
                i.putExtra("CategoryName",dataModels.get(position).getCategoryName());

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Cat_id;
        private CardView Rootlayout;
        private ImageView Cat_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Rootlayout = itemView.findViewById(R.id.Root_layout);
            Cat_id = itemView.findViewById(R.id.nameTV);
            Cat_img = itemView.findViewById(R.id.Cat_img);
            Cat_img.setScaleType(ImageView.ScaleType.FIT_XY);
            //Cat_img.setAdjustViewBounds(true);
        }

        public abstract void setFixedHeight();

    }
}
package com.shamsaha.app.adaptor.Volunteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.CustomMessageItem;
import com.shamsaha.app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomMessageAdaptor extends RecyclerView.Adapter<CustomMessageAdaptor.ViewHolder> {

    private Context context;
    CustomMessageItem customMessageItem;
    private ArrayList<CustomMessageItem> dataModels = new ArrayList<>();


    public CustomMessageAdaptor(Context context,ArrayList<CustomMessageItem> dataModels) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new ViewHolder(view) {
            @Override
            public void setFixedHeight() {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private CardView Rootlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Rootlayout = itemView.findViewById(R.id.Root_layout);
            messageText = itemView.findViewById(R.id.messageText);
            //Cat_img.setAdjustViewBounds(true);
        }

        public abstract void setFixedHeight();

    }
}

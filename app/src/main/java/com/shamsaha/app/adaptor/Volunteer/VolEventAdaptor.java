package com.shamsaha.app.adaptor.Volunteer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shamsaha.app.ApiModel.event;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventViewActivity;
import com.shamsaha.app.utils.dateConversion;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VolEventAdaptor extends RecyclerView.Adapter<VolEventAdaptor.ViewHolder> {


    private ArrayList<event> eventsModel = new ArrayList<>();
    private Context context;

    public VolEventAdaptor(ArrayList<event> eventsModel, Context context) {
        this.eventsModel = eventsModel;
        this.context = context;
    }

    @NonNull
    @Override
    public VolEventAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_adaptor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolEventAdaptor.ViewHolder holder, int position) {



        dateConversion d = new dateConversion();
        Log.d("respCall", "nnn\n" + eventsModel.get(position).getTitleEn());
        try {
            String date = d.dateConversion(eventsModel.get(position).getDate(), "yyyy-MM-dd");
            date = date.replace("-"," ");
            holder.Date.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.eventTitle.setText(eventsModel.get(position).getTitleEn());
        String api_data = eventsModel.get(position).getShortEn();
        api_data = api_data.replace("<p>", "<p style=\"font-family:avenir;text-align: justify; \">");
//        holder.setwebView(api_data);
        //holder.descriptionBrief.setText(eventsModel.get(position).getContentEn());
        Glide.with(context)
                .load(eventsModel.get(position).getImage())
                .into(holder.img);

        String eventTtpe = eventsModel.get(position).getEventType();

        if(eventTtpe.equals("No registration")){
            holder.eventFee.setText("Free");
        }else if(eventTtpe.equals("Free")){
            holder.eventFee.setText("Free");
            holder.submit.setText("View Event");
        }else{
            holder.eventFee.setText(eventsModel.get(position).getPrice()+" BHD");
        }

        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), VolEventViewActivity.class);
                intent.putExtra("id",eventsModel.get(position).getWceid());
                intent.putExtra("event_type",eventsModel.get(position).getEventType());
                intent.putExtra("title_en",eventsModel.get(position).getTitleEn());
                intent.putExtra("title_ar",eventsModel.get(position).getTitleAr());
                intent.putExtra("content_en",eventsModel.get(position).getContentEn());
                intent.putExtra("content_ar",eventsModel.get(position).getContentAr());
                intent.putExtra("venu",eventsModel.get(position).getVenu());
                intent.putExtra("venu_time",eventsModel.get(position).getVenuTime());
                intent.putExtra("date",eventsModel.get(position).getDate());
                intent.putExtra("price",eventsModel.get(position).getPrice());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return eventsModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventTitle, eventFee,  Date;
        private ImageView img;
        private Button submit;
        private WebView webView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventFee = itemView.findViewById(R.id.eventFee);
            Date = itemView.findViewById(R.id.Date);
            img = itemView.findViewById(R.id.img);
            submit = itemView.findViewById(R.id.submit);

        }

        private void setwebView(String data) {
            webView2.setBackgroundColor(Color.TRANSPARENT);

            webView2.loadData(data, "text/html", "UTF-8");
            //webView.loadUrl(data);
            Log.d("apidataa", data);
        }

    }
}

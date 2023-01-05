package com.shamsaha.app.adaptor.Volunteer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.volunter.SheduleListModel;
import com.shamsaha.app.R;
import com.shamsaha.app.utils.dateConversion;

import org.json.JSONArray;

import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class ShiftListParentAdaptor extends RecyclerView.Adapter<ShiftListParentAdaptor.ViewHolder> {


    List<SheduleListModel>   sectionList;
    Context context;
    private String vID;


    public ShiftListParentAdaptor(List<SheduleListModel> sectionList,Context context,String vID) {
        this.sectionList = sectionList;
        this.context = context;
        this.vID = vID;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shift_list_parent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dateConversion d = new dateConversion();

        SheduleListModel section = sectionList.get(position);
        String sectionName = section.getDate();

        try {
            String date = d.dateConversion(sectionName, "yyyy-MM-dd");
            date = date.replace("-", " ");
            holder.sectionNameTextView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<JSONArray> items = section.getJsonArray();

        JSONArray item = null;
        for (int i = 0; i < items.size(); i++){
            item = items.get(i);
            Log.d(TAG, "onBindViewHolder: ");
        }


        ShiftListChieldAdaptor shiftListChieldAdaptor = new ShiftListChieldAdaptor(item,context,vID);
        holder.chieldRecyclerView.setAdapter(shiftListChieldAdaptor);
        holder.chieldRecyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView sectionNameTextView;
        RecyclerView chieldRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionNameTextView = itemView.findViewById(R.id.sectionNameTextView);
            chieldRecyclerView = itemView.findViewById(R.id.chieldRecyclerView);

        }
    }

}

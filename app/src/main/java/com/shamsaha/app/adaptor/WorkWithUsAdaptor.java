package com.shamsaha.app.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.shamsaha.app.ApiModel.job;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.PublicPart.GetInvolve.WorkWithUs.workFormActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

    public class WorkWithUsAdaptor extends RecyclerView.Adapter<WorkWithUsAdaptor.ViewHolder> {

    private ArrayList<job> dataModels = new ArrayList<>();
    private Context context;
    public String Title,Jdate,JobType,Brief,Edate;


    public WorkWithUsAdaptor(Context context, ArrayList<job> dataModels){
        this.dataModels = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkWithUsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_with_us_adaptor, parent,false);
        return new ViewHolder(view)
        {

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
    public void onBindViewHolder(@NonNull WorkWithUsAdaptor.ViewHolder holder, final int position) {

        holder.setFixedHeight();

        holder.Rootlayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.slide_in_bottom_custom));

        Title = dataModels.get(position).getTitle();
        Title = Title.replace("<p>","");
        Title = Title.replace("</p>","");

        Jdate = dataModels.get(position).getJdate();
        Jdate = Jdate.replace("<p>","");
        Jdate = Jdate.replace("</p>","");

        Edate = dataModels.get(position).getEdate();
        Edate = Edate.replace("<p>","");
        Edate = Edate.replace("</p>","");

        JobType = dataModels.get(position).getJobType();
        JobType = JobType.replace("<p>","");
        JobType = JobType.replace("</p>","");

        Brief = dataModels.get(position).getBrief();
        Brief = Brief.replace("<p>","");
        Brief = Brief.replace("</p>","");
        //dynamic Hight

        holder.JobTitle.setText(Title);
        holder.type.setText(JobType);
        holder.jobDate.setText(Jdate);
        holder.jobEndDate.setText(Edate);
        holder.jobData.setText(Brief);

        holder.more_detail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = dataModels.get(position).getTitle();
                Title = Title.replace("<p>","");
                Title = Title.replace("</p>","");

                Jdate = dataModels.get(position).getJdate();
                Jdate = Jdate.replace("<p>","");
                Jdate = Jdate.replace("</p>","");

                JobType = dataModels.get(position).getJobType();
                JobType = JobType.replace("<p>","");
                JobType = JobType.replace("</p>","");

                Brief = dataModels.get(position).getBrief();
                Brief = Brief.replace("<p>","");
                Brief = Brief.replace("</p>","");

                String detail = dataModels.get(position).getDetail();
                detail = detail.replace("<p>","");
                detail = detail.replace("</p>","");

                Intent intent = new Intent(v.getContext(), workFormActivity.class);
                intent.putExtra("JobType",JobType);
                intent.putExtra("Title",Title);
                intent.putExtra("detail",detail);
                intent.putExtra("job_id",dataModels.get(position).getJobId());
                context.startActivity(intent);
                //Toast.makeText(context, dataModels.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.Rootlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = dataModels.get(position).getTitle();
                Title = Title.replace("<p>","");
                Title = Title.replace("</p>","");

                Jdate = dataModels.get(position).getJdate();
                Jdate = Jdate.replace("<p>","");
                Jdate = Jdate.replace("</p>","");

                Edate = dataModels.get(position).getEdate();
                Edate = Edate.replace("<p>","");
                Edate = Edate.replace("</p>","");

                JobType = dataModels.get(position).getJobType();
                JobType = JobType.replace("<p>","");
                JobType = JobType.replace("</p>","");

                Brief = dataModels.get(position).getBrief();
                Brief = Brief.replace("<p>","");
                Brief = Brief.replace("</p>","");

                String detail = dataModels.get(position).getDetail();

                Intent intent = new Intent(v.getContext(),workFormActivity.class);
                intent.putExtra("JobType",JobType);
                intent.putExtra("Title",Title);
                intent.putExtra("detail",detail);
                intent.putExtra("job_id",dataModels.get(position).getJobId());
                context.startActivity(intent);
                //Toast.makeText(context, dataModels.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        private TextView JobTitle,type,jobDate,jobData,jobEndDate;
        private CardView Rootlayout;
        private Button more_detail1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Rootlayout = itemView.findViewById(R.id.Rootlayout);
            JobTitle = itemView.findViewById(R.id.JobTitle);
            type = itemView.findViewById(R.id.type);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobData = itemView.findViewById(R.id.jobData);
            more_detail1 = itemView.findViewById(R.id.more_detail1);
            jobEndDate = itemView.findViewById(R.id.jobEndDate);


        }

        public abstract void setFixedHeight();

    }
}
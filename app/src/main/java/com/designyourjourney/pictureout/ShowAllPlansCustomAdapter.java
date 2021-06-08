package com.designyourjourney.pictureout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.designyourjourney.pictureout.db.AppRepository;
import com.designyourjourney.pictureout.db.City;
import com.designyourjourney.pictureout.db.Plan;

import java.util.List;

public class ShowAllPlansCustomAdapter extends
        RecyclerView.Adapter<ShowAllPlansCustomAdapter.ViewHolder> {

    private List<Plan> myPlans;
    Context context;

    // Click listener object created for last plan deletion
    private onLastPlanDeleteListener listener;

    // Click listener object created for recycler view item click
    private onRecyclerViewItemClickListener itemListener;

    /*
    * Provide a reference to the type of views that you are using
    * (custom ViewHolder).
    */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView planImage;
        TextView planName;
        TextView destinations;
        TextView dateRange;
        ImageView deleteButton;
        ImageView copyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.planImage=(ImageView)itemView.findViewById(R.id.planImage);
            this.planName=(TextView)itemView.findViewById(R.id.cardPlanName);
            this.destinations=(TextView)itemView.findViewById(R.id.cardDestinations);
            this.dateRange=(TextView)itemView.findViewById(R.id.cardDateRange);
            this.deleteButton=(ImageView) itemView.findViewById(R.id.cardDelete);
            this.copyButton=(ImageView)itemView.findViewById(R.id.cardCopy);
        }
    }

    public ShowAllPlansCustomAdapter (Context context,List<Plan> myPlans) {
        this.context=context;
        this.myPlans=myPlans;
    }

    public void setWhenLastPlanDeletedEvent(onLastPlanDeleteListener listener) {
        this.listener=listener;
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener itemListener) {
        this.itemListener=itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_card_view,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView planImage=holder.planImage;
        TextView planName=holder.planName;
        TextView destinations=holder.destinations;
        TextView dateRange=holder.dateRange;
        ImageView deleteButton=holder.deleteButton;
        ImageView copyButton=holder.copyButton;

        Plan plan = myPlans.get(position);

        // Set image to plan

        // Set plan name to planName
        planName.setText(plan.getPlanName());

        // Set destinations list to destinations
        List<City> dest = plan.getDestinations();
        StringBuilder word = new StringBuilder();
        for (int i=0;i<dest.size();i++) {
            word.append(dest.get(i).getName());
            word.append(" ,");
        }
        destinations.setText(word.toString().substring(0,word.length()-1)); //Does not include last comma

        // Set start-date to end-date to dateRange
        dateRange.setText(String.format("%s-%s", plan.getStartDate(), plan.getEndDate()));

        // Set on click listener to delete button i.e delete the plan
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateDeleteDialog(myPlans.get(position));
            }
        });

        // Set on click listener to copy button i.e copy the plan
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyPlan(myPlans.get(position));
            }
        });

        // Add click event listener to holder view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPlans.size();
    }

    private void generateDeleteDialog(Plan plan) {
        final Plan planToBeDeleted = plan;

        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);
        deleteBuilder.setTitle("Warning");
        deleteBuilder.setMessage("Are you sure you want to delete your plan ?");

        deleteBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete the plan
                AppRepository repository = new AppRepository(context);
                repository.deletePlan(planToBeDeleted);
                Toast.makeText(context, "Plan deleted successfully", Toast.LENGTH_SHORT).show();
                myPlans.remove(plan);
                if (myPlans.size()==0 && listener!=null) {
                    listener.onLastPlanDeleted();
                }
                notifyDataSetChanged();
            }
        });

        deleteBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });

        AlertDialog dialog = deleteBuilder.create();
        dialog.show();
    }

    private void copyPlan(Plan plan) {
        AppRepository repository = new AppRepository(context);
        myPlans.add(0,repository.copyPlan(plan));
        notifyDataSetChanged();
    }

    // Interface to perform action on all plans deleted event
    public interface onLastPlanDeleteListener {
        void onLastPlanDeleted();
    }

    // Interface to perform action for click on item in recycler view
    public interface onRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

}

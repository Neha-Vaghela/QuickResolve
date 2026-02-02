package com.quickresolve.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quickresolve.ModelClass.ComplaintModel;
import com.quickresolve.R;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    Context context;
    ArrayList<ComplaintModel> list;

    public ComplaintAdapter(Context context, ArrayList<ComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.single_complaint_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComplaintModel c = list.get(position);

        holder.name.setText("Name: " + c.getFullName());
        holder.category.setText("Category: " + c.getCategory());
        holder.branch.setText("Branch: " + c.getBranch());
        holder.enrollment.setText("Enrollment: " + c.getEnrollment());
        holder.mobile.setText("Mobile: " + c.getMobile());
        holder.description.setText("Description: " + c.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, category, branch, enrollment, mobile, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            category = itemView.findViewById(R.id.tvCategory);
            branch = itemView.findViewById(R.id.tvBranch);
            enrollment = itemView.findViewById(R.id.tvEnrollment);
            mobile = itemView.findViewById(R.id.tvMobile);
            description = itemView.findViewById(R.id.tvDescription);
        }
    }
}

package com.example.gradle.Survey;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradle.R;

import java.util.List;

public class ReAdapter extends RecyclerView.Adapter<ReAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Context context;

    public ReAdapter(Context context, List<ListItem> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        holder.use.setText(listItem.getUser());
        holder.stats.setText(listItem.getStatistics());
        holder.ends.setText(listItem.getExpired());
        holder.start.setText(listItem.getUpdated());
        holder.stat.setText("Pending");
        if ((holder.stat.getText()).equals("Pending")) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,SurveyForm1.class);
                    context.startActivity(intent);
                    Toast.makeText(context, "YouViewed" + listItem.getUser(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(context, "You had completed the survey", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
public TextView use;
public TextView stats;
public TextView start;
public TextView ends;
public TextView stat;
public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            use=itemView.findViewById(R.id.user);
            stat=itemView.findViewById(R.id.status);
            stats=itemView.findViewById(R.id.stats);
            start=itemView.findViewById(R.id.updateon);
            ends=itemView.findViewById(R.id.endon);
            linearLayout=itemView.findViewById(R.id.linear);
        }
    }
}

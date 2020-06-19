package com.example.gradle.Survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradle.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SuAdapter extends RecyclerView.Adapter<SuAdapter.ViewHolder> {
    private List<SurveyItem> listItems;
    private Context context;
    private int count=0;
    public SuAdapter(Context context, List<SurveyItem> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SurveyItem listItem = listItems.get(position);
        holder.question.setText(listItem.getQuestion());
        holder.submi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=count+1;
                String ans=holder.answer.getText().toString();
               DatabaseReference mRef= FirebaseDatabase.getInstance().getReference("answers");
               mRef.child(String.valueOf(count)).setValue(ans);
            }
        });

    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
public TextView question;
public EditText answer;
private Button submi;
//public TextView stats;
//public TextView start;
//public TextView ends;
//public TextView stat;
public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.questions);
            answer=itemView.findViewById(R.id.answer);
            submi=itemView.findViewById(R.id.submit);
            linearLayout=itemView.findViewById(R.id.linear);
        }
    }
}

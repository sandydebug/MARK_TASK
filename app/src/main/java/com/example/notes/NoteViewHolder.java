package com.example.notes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView textTitle,textTime,textContent;
    CardView cardView;
    TextView textView2;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
        textTitle=view.findViewById(R.id.textTitle);
        textView2=view.findViewById(R.id.textTask);
        textContent=view.findViewById(R.id.textContent);
        textTime=view.findViewById(R.id.textTime);
        cardView=view.findViewById(R.id.card_view);

    }

    public void setTitle(String title){
        textTitle.setText(title);
    }
    public void setTextView2(String task){textView2.setText(task);}

    public void setContent(String content) {
        textContent.setText(content);
    }



    public void setTime(String time){
        textTime.setText(time);
    }
}

package com.croissant.croissant.ActivityEvent;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.croissant.croissant.Question;
import com.croissant.croissant.R;

import java.util.List;

/**
 * Created by santiago on 15/06/2016.
 */
public class RecyclerAdapterEvent  extends RecyclerView.Adapter<RecyclerAdapterEvent.ViewHolder>{

    private List<Question> items;
    private int itemLayout;
    private Context context;

    /**
     * this is the constructor of perceptions
     * @param items this items are all the perceptions from the API
 */
    public RecyclerAdapterEvent(List<Question> items, Context context) {
        this.items = items;
        this.context = context;
    }


    /*
    * this method just inflate a element in the recyclerview
    */

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.acceptedmessageevent, parent, false);
        return new ViewHolder(v);
    }

    /**
     * this method append the one item<deduction> into a layout of amountmessage
     * @param holder this contain the elements of the layout
     * @param position this is the position of the element that is in use
     */
    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Question item = items.get(position);
        //display in controls
        holder.date.setText(item.getDateOfQuestion());
        holder.question.setText(item.getQuestion());
        holder.name.setText(item.getUser());
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView question;
        public TextView name;
        public TextView id;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.dateOfQues);
            question = (TextView)itemView.findViewById(R.id.contentQuestion);
            name = (TextView)itemView.findViewById(R.id.msName);
            id = (TextView)itemView.findViewById(R.id.idOfQuestion);

        }
    }

}






package com.croissant.croissant;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Santiago on 19/03/2016.
 */
public class ListAdapter_aceptMesage extends BaseAdapter{
    private Activity activity;
    private ArrayList<Question> data;
    private static LayoutInflater inflater;

    public ListAdapter_aceptMesage(Activity activity, ArrayList<Question> data)
    {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return this.data.size();
    }
    @Override
    public Object getItem(int itemId) {return itemId;}
    @Override
    public long getItemId(int position) {return position;}
    @Override
    public View getView(int position, View view, ViewGroup parentView)
    {
        View v = view;

        //layout of list adapter
        if (v == null) v = this.inflater.inflate(R.layout.acceptedmessage,null);
        //layout Controls

        TextView date = (TextView)v.findViewById(R.id.dateOfQues);
        TextView question = (TextView)v.findViewById(R.id.contentQuestion);
        TextView name = (TextView)v.findViewById(R.id.msName);
        TextView id = (TextView)v.findViewById(R.id.idOfQuestion);
        //read data item
        Question item = this.data.get(position);
        //display in controls
        date.setText(item.getDateOfQuestion());
        question.setText(item.getQuestion());
        name.setText(item.getUser());
        //id.setText(item.getId());
        //return view
        return  v;
    }
}

package com.croissant.croissant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by brandon on 30/03/16.
 */
public class ListAdapter_question_answer_trivia extends BaseAdapter {
    private Activity activity;
    private ArrayList<Question> data;
    private static LayoutInflater inflater;

    public ListAdapter_question_answer_trivia(Activity activity, ArrayList<Question> data)
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
        if (v == null) v = this.inflater.inflate(R.layout.normal_comment,null);
        //layout Controls
        TextView date = (TextView)v.findViewById(R.id.dateOfQues);
        EditText question = (EditText)v.findViewById(R.id.contentQuestion);
        TextView name = (TextView)v.findViewById(R.id.msName);
        TextView id = (TextView)v.findViewById(R.id.idOfQuestion);
        RelativeLayout btnSend = (RelativeLayout)v.findViewById(R.id.contentsend);

        // agregamos el evento que hara el 'boton' al momento de hacer clic
        //read data item
        Question item = this.data.get(position);
        //display in controls
        int i = item.getId();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(i);
        String strI = sb.toString();
        id.setText(strI);
        date.setText(item.getDateOfQuestion());
        question.setText(item.getQuestion());
        name.setText(item.getUser() + ", says :");
        //id.setText(item.getId());
        //return view
        return  v;
    }
}

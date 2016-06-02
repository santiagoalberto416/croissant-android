package com.croissant.croissant;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter_conference extends BaseAdapter
{
    private Activity activity;
    private ArrayList<Conference> data;
    private static LayoutInflater inflater;

    public ListAdapter_conference(Activity activity, ArrayList<Conference> data)
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
        if (v == null) v = this.inflater.inflate(R.layout.conference_layout,null);
        //layout Controls
        RelativeLayout contentConference = (RelativeLayout)v.findViewById(R.id.contentConference);

        TextView tvName = (TextView)v.findViewById(R.id.nameConference);
        TextView tvSpeaker = (TextView)v.findViewById(R.id.nameSpeaker);
        TextView tvPlace = (TextView)v.findViewById(R.id.placeConference);
        TextView tvTime = (TextView)v.findViewById(R.id.timeConference);
        //read data item
        Conference item = this.data.get(position);
        //display in controls
        contentConference.setTag(item.getId());
        tvName.setText(item.getName());
        tvSpeaker.setText(item.getSpeaker());
        tvPlace.setText(parentView.getResources().getString(R.string.room_style) + "\n" + item.getPlace());
        tvTime.setText(item.getDatetime());
        //return view
        return  v;
    }
}

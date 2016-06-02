package com.croissant.croissant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by brandon on 14/03/16.
 */
public class ReverseChronometer extends TextView implements Runnable {
   long startTime=0L;
   long overallDuration=0L;
   long warningDuration=0L;
    public static final String DEFAULT = "N/A";
    private static Activity activity;

    public ReverseChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        reset();
    }

    public static void setActivity(Activity activity) {
        ReverseChronometer.activity = activity;
    }


    @Override
    public void run() {
        long elapsedSeconds= (SystemClock.elapsedRealtime() - startTime) / 1000;
        if (elapsedSeconds < overallDuration) {
            long remainingSeconds=overallDuration - elapsedSeconds;
            long minutes=remainingSeconds / 60;
            long seconds=remainingSeconds - (60 * minutes);
            setText(String.format("%d:%02d", minutes, seconds));
            if (warningDuration > 0 && remainingSeconds < warningDuration) {
                setTextColor(0xFFFF6600); // orange
            }
            else {
                setTextColor(getResources().getColor(R.color.infoConference));
            }
            postDelayed(this, 1000);
        }
        else {
            setText("0:00");
            setTextColor(getResources().getColor(R.color.infoConference));
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("horaEntrada", "");
            editor.putString("intentQstns", "3");
            editor.commit();
            stop();
            ((ViewGroup) this.getParent()).removeView(this);
            addTxtQuestion(activity);
        }
    }

    public void reset() {
        startTime=SystemClock.elapsedRealtime();
        setText("--:--");
        setTextColor(getResources().getColor(R.color.infoConference));
    }
    public void stop() {
        removeCallbacks(this);
    }
    public void setOverallDuration(long overallDuration) {
        this.overallDuration=overallDuration;
    }
    public void setWarningDuration(long warningDuration) {
        this.warningDuration=warningDuration;
    }

    public static void addTxtQuestion(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("myData", activity.MODE_PRIVATE);
        String counts = sharedPreferences.getString("countChrono", DEFAULT);

        if(counts.equals("") || counts == "N/A") {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("countChrono", "1");
            editor.commit();
            RelativeLayout contentAskQstn = (RelativeLayout) activity.findViewById(R.id.contentAskQuestion);
            LinearLayout contentBtn = (LinearLayout) activity.findViewById(R.id.contentBtnsAskQuestion);
            Log.e("strDate", "1");
            LayoutInflater inflater = LayoutInflater.from(activity); // 1
            ((ViewGroup) contentBtn.getParent()).removeView(contentBtn);

            TextView titleAskQstn = (TextView) activity.findViewById(R.id.titleAskQuestion);
            titleAskQstn.setText("Write your question :");
            titleAskQstn.setGravity(Gravity.NO_GRAVITY);
            View theInflatedView = inflater.inflate(R.layout.txtaskquestion, null);
            View btn = inflater.inflate(R.layout.btn_send, null);
            Log.e("strDate", "2");
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.titleAskQuestion);
            theInflatedView.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0,0,0,10);
            btn.setLayoutParams(params2);

            Log.e("strDate", "3");
            contentAskQstn.addView(theInflatedView);
            contentAskQstn.addView(btn);
        }

    }
}

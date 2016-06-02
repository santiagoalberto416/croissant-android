package com.croissant.croissant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by brandon on 30/03/16.
 */
public class ReverseChronometerTrivia extends TextView implements Runnable {
    long startTime=0L;
    long overallDuration=0L;
    long warningDuration=0L;
    private static long seconds;
    public static final String DEFAULT = "N/A";
    private static Activity activity;

    public ReverseChronometerTrivia(Context context, AttributeSet attrs) {
        super(context, attrs);
        reset();
    }

    public static void setActivity(Activity activity) {
        ReverseChronometerTrivia.activity = activity;
    }

    public static long getSeconds() {
        return seconds;
    }


    @Override
    public void run() {
        long elapsedSeconds= (SystemClock.elapsedRealtime() - startTime) / 1000;
        if (elapsedSeconds < overallDuration) {
            long remainingSeconds=overallDuration - elapsedSeconds;
            long minutes=remainingSeconds / 60;
            seconds=remainingSeconds - (60 * minutes);
            setText(String.format("%d:%02d", minutes, getSeconds()));
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
            setTextColor(ContextCompat.getColor(activity, R.color.alert));
            TextView info = (TextView)activity.findViewById(R.id.infoTrivia);
            info.setText("Sorry, your time is over :(");
            info.setGravity(Gravity.CENTER);
            info.setTextSize(25);
            info.setTextColor(ContextCompat.getColor(activity, R.color.btnLogout));
            Button button = (Button) activity.findViewById(R.id.btnSendAnswer);
            button.setText("Back to Conference");
            button.setEnabled(true);
            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, GoAndShowConference.class));
                }
            });
            View contentButtons = activity.findViewById(R.id.contentRadioButtons);
            ((ViewGroup) contentButtons.getParent()).removeView(contentButtons);
            ((ViewGroup) this.getParent()).removeView(this);
        }
    }

    public void reset() {
        startTime=SystemClock.elapsedRealtime();
        setText("--:--");
        setTextColor(getResources().getColor(R.color.infoConference));
    }

    public void remove(){
        stop();
        ((ViewGroup) this.getParent()).removeView(this);
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

}

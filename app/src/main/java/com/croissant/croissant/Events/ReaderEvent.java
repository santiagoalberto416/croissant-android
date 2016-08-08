package com.croissant.croissant.Events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.croissant.croissant.GoAndShowConferences;
import com.croissant.croissant.Graphics.utils.Constants;
import com.croissant.croissant.MainActivity;
import com.croissant.croissant.R;
import com.example.santiago.data.Event.EventInfo;
import com.example.santiago.data.GraphicsAllPersons.Graphics;
import com.example.santiago.data.services.EventService;
import com.example.santiago.data.services.QuestionScoreService;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReaderEvent extends Activity implements ZXingScannerView.ResultHandler  {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_event);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(mScannerView!=null)mScannerView.startCamera();

    }

    public void QrScanner(View view){
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        mScannerView.startCamera();         // Start camera<br />
    }
   @Override
    public void onPause(){
            super.onPause();
       if(mScannerView!=null)mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult){
        getEvent(rawResult.getText());
    }

    public void getEvent(String urlget){


        if(mScannerView!=null)mScannerView.stopCamera();
        ReaderEvent.this.setContentView(R.layout.activity_reader_event);
        boolean isUrlValid = true;
        int id = 0;
        String base = getResources().getString(R.string.baseurl);
        String apiurl = getResources().getString(R.string.eventapiurl);

        //Validations of the getting url
        if(urlget.length()>base.length()) {
            try {
            if(!base.equals(urlget.substring(0, base.length())))   {
                isUrlValid = false;
            }
            id = Integer.parseInt(urlget.substring((base.length() + apiurl.length() +4), urlget.length() ));
            Log.d("id of event", id+"");

            }catch (NumberFormatException e){
                isUrlValid = false;
            }
        }else {
            isUrlValid = false;
        }
        //end of validations

        if(isUrlValid || id!=0) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EventService service = retrofit.create(EventService.class);
            Call<EventInfo> call = service.getEvent(id);

            call.enqueue(new Callback<EventInfo>() {
                @Override
                public void onResponse(Call<EventInfo> call, Response<EventInfo> response) {
                    if(response.body().getStatus()==0)
                    showInfoEvent(response.body());
                }

                @Override
                public void onFailure(Call<EventInfo> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReaderEvent.this);
                    builder.setTitle("error");
                    builder.setMessage("Server internal error");
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                }
            });
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("error");
            builder.setMessage("url is not valid");
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
    }

    private void showInfoEvent(EventInfo eventInfo){
        if(eventInfo!=null){
            TextView title = (TextView)findViewById(R.id.titlescan);
            title.setText("Welcome to the event:"+eventInfo.getName());
            ImageView imgConf = (ImageView)findViewById(R.id.imgEvent);
            Picasso.with(this)
                    .load(getResources().getString(R.string.imgUrl)+eventInfo.getLogo())
                    .placeholder(R.id.imgLogo)
                    .error(R.drawable.ic_mood_bad_white_48dp)
                    .into(imgConf)
            ;
            findViewById(R.id.ScanButton).setVisibility(View.GONE);

            SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.ID_EVENT,eventInfo.getId()+"");
            editor.putString(Constants.LOGO, eventInfo.getLogo());
            editor.putString(Constants.NAME_EVENT,eventInfo.getName());
            editor.putString(Constants.PRIMARY,eventInfo.getTheme().getPrimary());
            editor.putString(Constants.DARK,eventInfo.getTheme().getPrimaryDark());
            editor.putString(Constants.LIGHT,eventInfo.getTheme().getPrimaryLight());
            editor.putString(Constants.ASCENT,eventInfo.getTheme().getAscent());
            editor.putString(Constants.ICONS,eventInfo.getTheme().getIcons());
            editor.commit();

            Log.d("primary from db", eventInfo.getTheme().getPrimaryDark());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(ReaderEvent.this, GoAndShowConferences.class));
                }
            }, 2000);
        }
    }
}

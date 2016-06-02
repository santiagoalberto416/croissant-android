package com.croissant.croissant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

/**
 * Created by brandon on 12/03/16.
 */
public class GoAndShowAskQuestion extends Activity  {

    public static final String DEFAULT = "N/A";
    private ProgressDialog pd;
    EditText txtEmail, txtPassword;
    InputStream data = null; //raw data received from api
    JSONObject result = null; //JSON Object
    String resultText = ""; //result as string
    String domain = "";
    String urlAPI = ""; //API Url
    //Json Nodes
    private static final String LABEL_STATUS ="status";
    private static final String LABEL_IDUSER ="id";
    private static final String LABEL_FIRSTNAME ="firstname";
    private static final String LABEL_LASTNAME ="lastname";
    private static final String LABEL_USERSTATUS ="userstatus";
    private static final String LABEL_TYPEUSER ="type";
    private final String USER_AGENT = "Mozilla/5.0";

    private Socket socket;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_question);
        ShowInfoUser.show(this);
        ShowInfoConference.show(this);
        domain = getResources().getString(R.string.domainsite);
        urlAPI = domain + "/croissant/addquestion.php"; //API Url

        try{
            socket = IO.socket(domain + ":8081");
        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
        socket.connect();

        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        String horaEntrada="";
        horaEntrada = sharedPreferences.getString("horaEntrada", DEFAULT);


        if(!horaEntrada.equals("") && horaEntrada != "N/A"){
            chronometerProcess();
        }
        else{
        }


    }

    public class SendQuestion extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog(new ContextThemeWrapper(GoAndShowAskQuestion.this, android.R.style.Theme_Holo_Light_Dialog));
            pd.setMessage("Sending your question, please wait a moment...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            //empty list
            JSONObject result = new JSONObject();

            //connect to API
            Log.d("Connecting to API", "Connecting");
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(urlAPI); //create url object
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("txtquestion", args[0])
                        .appendQueryParameter("txtiduser", args[1])
                        .appendQueryParameter("txtidconf", args[2])
                        .appendQueryParameter("txtstatus", "w");
                String query = builder.build().getEncodedQuery();

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                Log.d("Connecting to API", "Connection Established");
                data = connection.getInputStream(); //read Stream
                StringBuilder rawData = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(data));
                String dataLine = "";
                while ((dataLine = reader.readLine()) != null) {
                    rawData.append(dataLine);
                }
                resultText = rawData.toString();
                Log.d("Raw Data Received", resultText);
            } catch (MalformedURLException e) {
                Log.e("URL Exception", e.toString());
            } catch (IOException e) {
                Log.e("IO Exception", e.toString());
            } catch (Exception e) {
                Log.e("Generic Exception", e.toString());
            } finally {
                if (connection != null) connection.disconnect();
            }

            try {
                result = new JSONObject(resultText);
                Log.d("Data in JSON Format", result.toString());
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            } catch (Exception e) {
                Log.e("Generic Exception", e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            //close progress dialog
            pd.dismiss();
            try {
                if (result.getInt(LABEL_STATUS) == 0) {
                    socket.emit("sendMessage", result);
                    View v = findViewById(R.id.btnSend);
                    Button cancel = (Button)findViewById(R.id.btnCancel);
                    cancel.setText("Back");
                    //v.setBackgroundResource(R.drawable.disable_buttons);
                    //v.setEnabled(false);
                    ((Button)v).setText("New question");
                    ((Button)v).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cleanTxtQstn(v);
                        }
                    });
                    View editText = findViewById(R.id.txtAskQuestion);
                    editText.setEnabled(false);
                    View scroll = findViewById(R.id.scrollAskQuestion);
                    scroll.setBackgroundResource(R.drawable.disable_scroll);
                    SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
                    int intents = Integer.valueOf(sharedPreferences.getString("intentQstns", DEFAULT));
                    intents -= 1;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("intentQstns", String.valueOf(intents));
                    editor.commit();
                    if(intents == 0)
                    {
                        chronometerProcess();
                    }
                    /*((ViewGroup) namebar.getParent()).removeView(namebar);
                    TextView titleAskQstn = (TextView)findViewById(R.id.titleAskQuestion);
                    titleAskQstn.setText("You've");*/
                    Toast toast = Toast.makeText(GoAndShowAskQuestion.this, "Your question was sent.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(GoAndShowAskQuestion.this, "ERROR: Question not sent.", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            }
        }
    }

    public void showConference(View v) {
        startActivity(new Intent(this, GoAndShowConference.class));
    }

    public void sendQuestion(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        int intents = Integer.valueOf(sharedPreferences.getString("intentQstns", DEFAULT));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("countChrono", "");
        editor.commit();
        if(intents > 0) {
            EditText etQuestion = (EditText) findViewById(R.id.txtAskQuestion);
            String txtQuestion = etQuestion.getText().toString();
            if (!txtQuestion.equals("")) {
                String txtIdUser = sharedPreferences.getString("idUser", DEFAULT);
                String txtIdConference = sharedPreferences.getString("idConference", DEFAULT);
                new SendQuestion().execute(txtQuestion, txtIdUser, txtIdConference);
            } else {
                Toast toast = Toast.makeText(GoAndShowAskQuestion.this, "First, write your question.", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(GoAndShowAskQuestion.this, "You must wait 2 minutes, to another 3 questions.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void cleanTxtQstn(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        int intents = Integer.valueOf(sharedPreferences.getString("intentQstns", DEFAULT));
        if(intents > 0) {
            Button cancel = (Button) findViewById(R.id.btnCancel);
            cancel.setText("Cancel");
            //v.setBackgroundResource(R.drawable.disable_buttons);
            //v.setEnabled(false);
            ((Button) v).setText("Send");
            ((Button) v).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sendQuestion(v);
                }
            });
            View editText = findViewById(R.id.txtAskQuestion);
            editText.setEnabled(true);
            ((EditText) editText).setText("");
            View scroll = findViewById(R.id.scrollAskQuestion);
            scroll.setBackgroundResource(R.drawable.stroke_scroll_askqstn);
        }
        else{
            Toast toast = Toast.makeText(GoAndShowAskQuestion.this, "You must wait 2 minutes, to another 3 questions.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

   /* protected void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }*/

    public void chronometerProcess()
    {
        ReverseChronometer chrono=null;
        View scroll = findViewById(R.id.scrollAskQuestion);
        ((ViewGroup) scroll.getParent()).removeView(scroll);
        RelativeLayout contentAskQstn = (RelativeLayout)findViewById(R.id.contentAskQuestion);

        LayoutInflater inflater = LayoutInflater.from(GoAndShowAskQuestion.this); // 1
        View theInflatedView = inflater.inflate(R.layout.chronometer, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.titleAskQuestion);
        theInflatedView.setLayoutParams(params);

        contentAskQstn.addView(theInflatedView);

        TextView titleAskQstn = (TextView)findViewById(R.id.titleAskQuestion);
        titleAskQstn.setText("You must wait 2 minutes for another 3 questions.");
        titleAskQstn.setGravity(Gravity.CENTER_HORIZONTAL);

        chrono=(ReverseChronometer)findViewById(R.id.chrono);
        chrono.setActivity(GoAndShowAskQuestion.this);
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        String reEntrada="";
        reEntrada = sharedPreferences.getString("horaEntrada", DEFAULT);

        Button send = (Button) findViewById(R.id.btnSend);
        ((ViewGroup) send.getParent()).removeView(send);

        Button cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setText("Back");

        if(!reEntrada.equals("") && reEntrada != "N/A"){
            String horaEntrada = sharedPreferences.getString("horaEntrada", DEFAULT);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            Calendar c = Calendar.getInstance();
            String strDate2 = format.format(c.getTime());

            try {
                Date reEntradaNow = format.parse(strDate2);
                Date entrada = format.parse(horaEntrada);
                long seconds = reEntradaNow.getTime() - entrada.getTime();
                //long res = 120000 - seconds;
                double total = seconds * 0.001;
                if(total < 10)
                {
                    chrono.setOverallDuration(10 - (int) total);
                    chrono.setWarningDuration(10);
                    chrono.run();
                }
                else
                {
                    chrono.stop();
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            SharedPreferences sharedPreferences1 = getSharedPreferences("myData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String strDate = sdf.format(c.getTime());
            editor.putString("horaEntrada", strDate);
            editor.commit();
            chrono.setOverallDuration(10);
            chrono.setWarningDuration(10);
            chrono.reset();
            chrono.run();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GoAndShowConference.class));
    }

}

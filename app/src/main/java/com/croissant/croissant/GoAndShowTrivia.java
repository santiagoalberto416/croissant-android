package com.croissant.croissant;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by brandon on 27/03/16.
 */
public class GoAndShowTrivia extends Activity {

    ListView lvQuestionsTrivia;
    TextView txtNameUser;
    public static final String DEFAULT = "N/A";
    private ProgressDialog pd;
    InputStream data = null; //raw data received from api
    JSONObject result = null; //JSON Object
    String resultText = ""; //result as string
    String domain = "";
    String urlAPI = ""; //API Url
    String urlAPI2 = "";
    String idQuestion;
    String userId;
    String answerId;
    String timeStarted ="";
    SimpleDateFormat format;
    long seconds;
    private ReverseChronometerTrivia chrono;
    //Json Nodes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trivia);
        format = new SimpleDateFormat("HH:mm:ss");

        Calendar c = Calendar.getInstance();
        timeStarted = format.format(c.getTime());
        ShowInfoUser.show(this);
        domain = getResources().getString(R.string.domainsite);
        urlAPI = domain +  "/croissant/getquestionanswer.php"; //API Url
        urlAPI2 = domain + "/croissant/addscoreuser.php";
        destroyNotifications();
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        idQuestion = sharedPreferences.getString("questionId", DEFAULT);
        userId = sharedPreferences.getString("idUser", DEFAULT);
        String conference = sharedPreferences.getString("nameConference", DEFAULT);
        TextView tv = (TextView) findViewById(R.id.conferenceTitle);
        tv.setText(conference);
        RadioGroup g = (RadioGroup)findViewById(R.id.contentRadioButtons);
        g.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                group.getCheckedRadioButtonId();
                RadioButton b1 = (RadioButton)group.findViewById(R.id.firstAnswer);
                RadioButton b2 = (RadioButton)group.findViewById(R.id.secondAnswer);
                RadioButton b3 = (RadioButton)group.findViewById(R.id.thirdAnswer);
                b1.setTextColor(ContextCompat.getColor(GoAndShowTrivia.this, R.color.infoConference));
                b2.setTextColor(ContextCompat.getColor(GoAndShowTrivia.this, R.color.infoConference));
                b3.setTextColor(ContextCompat.getColor(GoAndShowTrivia.this, R.color.infoConference));

                RadioButton selected = (RadioButton)group.findViewById(checkedId);
                selected.setTextColor(ContextCompat.getColor(GoAndShowTrivia.this, R.color.backgroundColor));
            }
        });
        new GetQuestion().execute(idQuestion);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GoAndShowConference.class));
        super.onBackPressed();
    }

    public void chronometerProcess(double added)
    {
        chrono=(ReverseChronometerTrivia)findViewById(R.id.chrono);
        chrono.setActivity(this);
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        String reEntrada="";
        reEntrada = sharedPreferences.getString("horaLlegadaNotificacion", DEFAULT);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        Calendar c = Calendar.getInstance();
        String strDate2 = format.format(c.getTime());
        try {
            Date reEntradaNow = format.parse(strDate2);
            Date entrada = format.parse(reEntrada);
            long seconds = reEntradaNow.getTime() - entrada.getTime();
            //long res = 120000 - seconds;
            double total = seconds * 0.001;
            added += 15;
            Double time = new Double(added);
            if(total < 15)
            {
                chrono.setOverallDuration(time.intValue() - (int) total);
                chrono.setWarningDuration(5);
                chrono.run();
            }
            else
            {
                chrono.stop();
            }
        }catch(Exception ex){}


    }

    private void destroyNotifications()
    {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    public void sendAnswer(View v)
    {
        RadioGroup g = (RadioGroup)findViewById(R.id.contentRadioButtons);
        int id = g.getCheckedRadioButtonId();
        if(id != -1)
        {
            v.setEnabled(false);
            RadioButton rb = (RadioButton)findViewById(id);
            String idAnswer = rb.getTag().toString();
            new SendAnswer().execute(userId, idQuestion, idAnswer);
        }
        else
        {
            Toast toast = Toast.makeText(GoAndShowTrivia.this, "Select some option", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public class SendAnswer extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            seconds = chrono.getSeconds();
            chrono.stop();
            //progress dialog
            pd = new ProgressDialog(new ContextThemeWrapper(GoAndShowTrivia.this, android.R.style.Theme_Holo_Light_Dialog));
            pd.setMessage("Sending your answer...");
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
                url = new URL(urlAPI2); //create url object
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("txtiduser", args[0])
                        .appendQueryParameter("txtquestion", args[1])
                        .appendQueryParameter("txtanswer", args[2])
                        .appendQueryParameter("txttime", String.valueOf(15 - seconds));
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
                if (result.getInt("status") == 0) {
                    Toast toast = Toast.makeText(GoAndShowTrivia.this, "Answer sent", Toast.LENGTH_LONG);
                    toast.show();
                    chrono.remove();
                    View contentButtons = findViewById(R.id.contentRadioButtons);
                    ((ViewGroup) contentButtons.getParent()).removeView(contentButtons);
                    TextView title = (TextView) findViewById(R.id.infoTrivia);
                    title.setText("Thanks for answer! :)");
                    title.setTextSize(25);
                    title.setTextColor(ContextCompat.getColor(GoAndShowTrivia.this, R.color.infoConference));
                    title.setGravity(Gravity.CENTER);
                    Button button = (Button) findViewById(R.id.btnSendAnswer);
                    button.setText("Back to Conference");
                    button.setEnabled(true);
                    button.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            startActivity(new Intent(GoAndShowTrivia.this, GoAndShowConference.class));
                        }
                    });


                }
                else
                {
                    Toast toast = Toast.makeText(GoAndShowTrivia.this, "ERROR: Answer not sent", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            }
        }
    }


    public class GetQuestion extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog(new ContextThemeWrapper(GoAndShowTrivia.this, android.R.style.Theme_Holo_Light_Dialog));
            pd.setMessage("Loading question...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            //empty list
            JSONObject result = new JSONObject();

            //connect to API
            //Log.d("Connecting to API", "Connecting");
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(urlAPI); //create url object
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", args[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                //Log.d("Connecting to API", "Connection Established");
                data = connection.getInputStream(); //read Stream
                StringBuilder rawData = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(data));
                String dataLine = "";
                while ((dataLine = reader.readLine()) != null) {
                    rawData.append(dataLine);
                }
                resultText = rawData.toString();
                //Log.d("Raw Data Received", resultText);
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
                //Log.d("Data in JSON Format", result.toString());
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
                if (result.getInt("status") == 0) {

                    TextView infotrivia = (TextView)findViewById(R.id.infoTrivia);
                    infotrivia.setText(result.getString("question"));
                    infotrivia.setTag(result.get("correctAnswer"));
                    int i = 0;
                    int[] indexs = new int[3];
                    int aux = 0;
                    indexs[0] = 0;
                    indexs[1] = 1;
                    indexs[2] = 2;

                    Random r = new Random();

                    for(int x = 0; x < 3; x++)
                    {
                        i = r.nextInt(3 - 0);
                        aux = indexs[x];
                        indexs[x] = indexs[i];
                        indexs[i] = aux;
                    }

                    JSONArray answers = result.getJSONArray("answerOptions");
                    JSONObject answer = answers.getJSONObject(indexs[0]);

                    RadioButton option1 = (RadioButton) findViewById(R.id.firstAnswer);
                    option1.setText("- " + answer.getString("answer"));
                    option1.setTag(answer.getString("idAnswer"));


                    answer = answers.getJSONObject(indexs[1]);

                    RadioButton option2 = (RadioButton) findViewById(R.id.secondAnswer);
                    option2.setText("- " + answer.getString("answer"));
                    option2.setTag(answer.getString("idAnswer"));

                    answer = answers.getJSONObject(indexs[2]);

                    RadioButton option3 = (RadioButton) findViewById(R.id.thirdAnswer);
                    option3.setText("- " + answer.getString("answer"));
                    option3.setTag(answer.getString("idAnswer"));

                    Calendar d = Calendar.getInstance();
                    String timeEnd = format.format(d.getTime());
                    try {
                        Date dateStarted = format.parse(timeStarted);
                        Date dateEnd = format.parse(timeEnd);
                        long seconds = dateEnd.getTime() - dateStarted.getTime();
                        double total = seconds * 0.001;
                        chronometerProcess(total);

                    }catch (Exception e){}

                }
                else
                {
                    Toast toast = Toast.makeText(GoAndShowTrivia.this, "ERROR: Question not load.", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            }
        }
    }

}

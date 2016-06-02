package com.croissant.croissant;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.*;

import android.app.Activity;


//necesarios para consumir API
import android.os.AsyncTask;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.*;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {

    private ProgressDialog pd;
    EditText txtEmail, txtPassword;
    InputStream data = null; //raw data received from api
    JSONObject result = null; //JSON Object
    String resultText = ""; //result as string
    String domain = "";
    String urlAPI = "";
    public static final String DEFAULT = "N/A";
    //Json Nodes
    private static final String LABEL_STATUS ="status";
    private static final String LABEL_IDUSER ="id";
    private static final String LABEL_FIRSTNAME ="firstname";
    private static final String LABEL_LASTNAME ="lastname";
    private static final String LABEL_USERSTATUS ="userstatus";
    private static final String LABEL_TYPEUSER ="type";
    private final String USER_AGENT = "Mozilla/5.0";
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        ImageView usa = (ImageView)findViewById(R.id.lenguageUsa);
        usa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguageMex("us");
            }
        });

        ImageView mex = (ImageView)findViewById(R.id.lenguageMex);
        mex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguageMex("es");
            }
        });

        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        domain = getResources().getString(R.string.domainsite);
        urlAPI = domain + "/croissant/getusers.php"; //API Url
        try{
            socket = IO.socket(domain + ":8081");
        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
        socket.on("newQuestion", showNotificationNewQuestion);
        socket.connect();

    }

    public class LoadInfoUser extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog(new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog));
            pd.setMessage("Loading, please wait a moment...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            //empty list
            JSONObject infoUser = new JSONObject();

            //connect to API
            Log.d("Connecting to API", "Connecting");
            URL url;
            HttpURLConnection connection = null;
            try {
                String email = URLEncoder.encode(args[0], "UTF-8");
                String password = URLEncoder.encode(args[1], "UTF-8");

                url = new URL(urlAPI + "?email=" + email + "&password=" + password); //create url object
                connection = (HttpURLConnection) url.openConnection();
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
                infoUser = new JSONObject(resultText);
                Log.d("Data in JSON Format", infoUser.toString());
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            } catch (Exception e) {
                Log.e("Generic Exception", e.toString());
            }

            //parse JSON to classes
            try {

                if (infoUser.getInt(LABEL_STATUS) == 0 && infoUser.getInt(LABEL_USERSTATUS) == 0) {
                    Log.d("Status: ", " ENTRO!!!!!!!!!!1");
                    String userName = infoUser.getString(LABEL_FIRSTNAME) + " " + infoUser.getString(LABEL_LASTNAME);
                    SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nameUser", userName);
                    editor.putString("idUser", infoUser.getString(LABEL_IDUSER));
                    editor.putString("typeUser", infoUser.getString(LABEL_TYPEUSER));
                    editor.commit();
                }
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            }

            return infoUser;
        }

        @Override
        protected void onPostExecute(JSONObject infoUser) {
            //close progress dialog
            pd.dismiss();
            try {
                if (infoUser.getInt(LABEL_STATUS) == 0) {

                    if(infoUser.getInt(LABEL_USERSTATUS) == 0) {

                        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("intentQstns", "3");
                        editor.commit();
                        startActivity(new Intent(MainActivity.this, GoAndShowConferences.class));

                    }
                    else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Message")
                                .setMessage("You are banned, sorry.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(MainActivity.this, "Email or password incorrect or maybe you're not registered.", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            }
        }
    }

    public void showOther(View v) {
        String password = txtPassword.getText().toString();
        String email = txtEmail.getText().toString();
        new LoadInfoUser().execute(email, password);
    }

    private Emitter.Listener showNotificationNewQuestion = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
                    String idConference = sharedPreferences.getString("idConference", DEFAULT);
                    String typeUser = sharedPreferences.getString("typeUser", DEFAULT);
                    JSONObject data = (JSONObject) args[0];
                    try {
                        int idCon = data.getInt("idConference");
                        if (Integer.valueOf(idConference) == idCon) {
                            if (Integer.valueOf(typeUser) == 4) {
                                Notification not = new Notification();
                                not.setActivityToShowNotification(MainActivity.this);
                                not.setTitleNotification(data.getString("nameConference"));
                                not.setTextNotification("New question! Go! The time is running!");
                                not.setTickerNotification("New question!");
                                not.showNotification();
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                String strDate = sdf.format(c.getTime());

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("questionId", data.getString("id"));
                                editor.putString("horaLlegadaNotificacion", strDate);
                                editor.commit();
                            }
                        }


                    } catch (JSONException ex) {
                    }

                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("newQuestion", showNotificationNewQuestion);
    }

    @Override
    public void onBackPressed() {
        socket.disconnect();
        socket.off("newQuestion", showNotificationNewQuestion);
        super.onBackPressed();
    }

    public void changeLanguageMex(String country)
    {
        Toast toast = Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT);
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String lenguage = sharedPreferences.getString("lenguage", "");
        if(country.equals("")||country==null){
            toast = Toast.makeText(getApplicationContext(), "lenguage is empty", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lenguage", country);
            editor.commit();
        }
        toast.show();
        Locale locale2 = new Locale(country);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        getApplicationContext().getResources().updateConfiguration(config2, null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.lenguageUsa)
    public void changeLanguageUsa()
    {

        Toast toast = Toast.makeText(getApplicationContext(), "usa", Toast.LENGTH_SHORT);

        toast.show();
        Locale locale2 = new Locale("us");
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        getApplicationContext().getResources().updateConfiguration(config2, null);
    }
}

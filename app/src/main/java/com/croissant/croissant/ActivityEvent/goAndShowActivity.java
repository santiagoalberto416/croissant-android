package com.croissant.croissant.ActivityEvent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.croissant.croissant.GoAndShowQuestionsManage;
import com.croissant.croissant.ListAdapter_aceptMesage;
import com.croissant.croissant.Question;
import com.croissant.croissant.R;
import com.croissant.croissant.ShowInfoConference;
import com.croissant.croissant.ShowInfoUser;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;

/**
 * Created by santiago on 14/06/2016.
 */
public class goAndShowActivity extends Activity{

    RecyclerView lvQuestionWaiting;
    private static ProgressDialog pd;
    private static InputStream data = null; //raw data received from api
    private static JSONObject result = null; //JSON Object
    private static String resultText = ""; //result as string
    private static String domain = "https://croissant-santy-ruler.c9users.io";
    String urlAPI = ""; //API Url
    RelativeLayout tipoDiv;
    int initialHeight;

    RelativeLayout infoSpeaker;

    int marginTopRecycler;
    public static String mode = "";

    public static ArrayList<Question> list2;

    public static RecyclerAdapterEvent adapter2;

    private static final String LABEL_ASKS ="asks";
    private static final String LABEL_ASKSA ="asksA";
    private static final String LABEL_ASKSD ="asksD";
    private static final String LABEL_ASKST ="asksT";
    private static final String LABEL_ID ="id";
    private static final String LABEL_QUESTION ="question";
    private static final String LABEL_DATE ="date";
    private static final String LABEL_USER ="user";
    public static final String DEFAULT = "N/A";

    public static Activity activity;

    private static Question questioninuse;
    private static int positionofquestion;

    private static Socket socket;
    {
        try {
            socket = IO.socket(domain + ":8081");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Emitter.Listener showNewQuestions = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            goAndShowActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    addQuestionFromServer(data);
                    try {
                        Log.d("info", data.getString("question"));
                    } catch (JSONException ex) {
                    }
                }
            });
        }
    };

    private void addQuestionFromServer(JSONObject data)
    {
        try {
            Question question = new Question(
                    data.getInt("id"),
                    data.getString("question"),
                    data.getString("date"),
                    data.getString("user"));
            list2.add(0, question);
            adapter2.animateTo(list2);
        }catch (JSONException ex){}
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        activity = this;
        ShowInfoConference.show(this);
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String idConference = sharedPreferences.getString("idConference", DEFAULT);
        domain = getResources().getString(R.string.domainsite);
        urlAPI = domain +  "/croissant/getallsasks.php"; //API Url
        new LoadWaitQuestions().execute(idConference);

        socket.on("newSpeaker", showNewQuestions);
        socket.connect();

    }

    public void removeQuestion(View v)
    {
        socket.emit("removeQuestion", result);
    }

    public goAndShowActivity()
    {

    }

    public class LoadWaitQuestions extends AsyncTask<String, Void, ArrayList<ArrayList<Question>>>
    {
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog( goAndShowActivity.this);
            pd.setMessage("Loading Questions...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }
        @Override
        protected ArrayList<ArrayList<Question>> doInBackground(String...args)
        {
            //empty list
            list2 = new ArrayList<Question>();

            ArrayList<ArrayList<Question>> ArrayAlCuadrado = new ArrayList<ArrayList<Question>>();
            //connect to API

            Log.d("Connecting to API", "Connecting");
            URL url;
            HttpURLConnection connection = null;
            try
            {
                url = new URL(urlAPI); //create url object
                connection = (HttpURLConnection) url.openConnection();


                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
                //String idConference = sharedPreferences.getString("idConference", DEFAULT);
                //Log.d("idConference ",idConference);
                // no sale en mode default
                Log.d("entra al modo","modo "+ mode);
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("txtidconference", args[0]);
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
                while ((dataLine = reader.readLine())!= null)
                {
                    rawData.append(dataLine);
                }
                resultText = rawData.toString();
                Log.d("Raw Data Received", resultText);
            }
            catch(MalformedURLException e)
            {
                Log.e("URL Exception",e.toString());
            }
            catch (IOException e)
            {
                Log.e("IO Exception", e.toString());
            }
            catch (Exception e)
            {
                Log.e("Generic Exception", e.toString());
            }
            finally
            {
                if (connection != null) connection.disconnect();
            }
            try {
                result = new JSONObject(resultText);
                Log.d("Data in JSON Format", result.toString());
            }
            catch (JSONException e) { Log.e("JSON Exception", e.toString()); }
            catch (Exception e) { Log.e("Generic Exception", e.toString()); }

            //parse JSON to classes
            try{


                JSONArray jsonArray2 = result.getJSONArray(LABEL_ASKSA);
                for(int i = 0; i <jsonArray2.length(); i++)
                {
                    JSONObject jsonItem = jsonArray2.getJSONObject(i);
                    Log.d("JSON item", jsonItem.toString());
                    //read key-values
                    int id = jsonItem.getInt(LABEL_ID);
                    String user = jsonItem.getString(LABEL_USER);
                    String date = jsonItem.getString(LABEL_DATE);
                    String question = jsonItem.getString(LABEL_QUESTION);
                    //Create object
                    try {
                        Question os = new Question(id, question, date, user);
                        //add object to list
                        list2.add(os);
                    }catch (Exception e)
                    {
                        Log.d("error ", "no se pudo crear el objeto");
                    }

                }







            }
            catch (JSONException e) { Log.e("JSON Exception", e.toString()); }

            ArrayAlCuadrado.add(list2);
            //return populated list
            return ArrayAlCuadrado;
        }
        @Override
        protected void onPostExecute(ArrayList<ArrayList<Question>> list)
        {
            //close progress dialog
            pd.dismiss();
            //show list

            /*
            este si
             */

            lvQuestionWaiting = (RecyclerView) findViewById(R.id.lvAsksAceptedOfConference);

            adapter2 = new RecyclerAdapterEvent( list.get(0), getApplicationContext());
            lvQuestionWaiting.setHasFixedSize(true);


            lvQuestionWaiting.setAdapter(adapter2);
            lvQuestionWaiting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            infoSpeaker = (RelativeLayout)findViewById(R.id.contenttoHide);
            HidingScrollListener hidingScrollListener = new HidingScrollListener(infoSpeaker, lvQuestionWaiting);
            lvQuestionWaiting.addOnScrollListener(hidingScrollListener);
        }
    }



    public void ShowDeclined(View v)
    {

        ListView Wait = (ListView)findViewById(R.id.lvAsksOfConference);
        ListView Acept = (ListView)findViewById(R.id.lvAsksAceptedOfConference);
        ListView Declined = (ListView)findViewById(R.id.lvAsksDeclinedOfConference);

        ListView trivia = (ListView)findViewById(R.id.lvtrivia);
        trivia.setVisibility(View.INVISIBLE);


        Wait.setVisibility(View.INVISIBLE);

        Acept.setVisibility(View.INVISIBLE);

        Declined.setVisibility(View.VISIBLE);

        Button onHold = (Button)findViewById(R.id.btnonhold);
        Button onSent = (Button)findViewById(R.id.btnSendQuestion);
        Button onDeclined = (Button)findViewById(R.id.btnDeclined);
        Button onTrivia = (Button)findViewById(R.id.btntrivia);


        onHold.setBackgroundResource(R.drawable.layer_list2);
        onSent.setBackgroundResource(R.drawable.layer_list2);
        onDeclined.setBackgroundResource(R.drawable.layer_list);

        onTrivia.setBackgroundResource(R.drawable.layer_list2);
    }

    public void ShowAcepted(View v)
    {

        ListView Wait = (ListView)findViewById(R.id.lvAsksOfConference);
        ListView Acept = (ListView)findViewById(R.id.lvAsksAceptedOfConference);
        ListView Declined = (ListView) findViewById(R.id.lvAsksDeclinedOfConference);

        ListView trivia = (ListView)findViewById(R.id.lvtrivia);
        trivia.setVisibility(View.INVISIBLE);

        Wait.setVisibility(View.INVISIBLE);

        Acept.setVisibility(View.VISIBLE);

        Declined.setVisibility(View.INVISIBLE);

        Button onHold = (Button)findViewById(R.id.btnonhold);
        Button onSent = (Button)findViewById(R.id.btnSendQuestion);
        Button onDeclined = (Button)findViewById(R.id.btnDeclined);
        Button onTrivia = (Button)findViewById(R.id.btntrivia);

        onHold.setBackgroundResource(R.drawable.layer_list2);
        onSent.setBackgroundResource(R.drawable.layer_list);
        onSent.setHeight(94);
        onDeclined.setBackgroundResource(R.drawable.layer_list2);
        onTrivia.setBackgroundResource(R.drawable.layer_list2);
    }

    public void ShowWait(View v)
    {

        ListView Wait = (ListView)findViewById(R.id.lvAsksOfConference);
        ListView Acept = (ListView)findViewById(R.id.lvAsksAceptedOfConference);
        ListView Declined = (ListView)findViewById(R.id.lvAsksDeclinedOfConference);


        ListView trivia = (ListView)findViewById(R.id.lvtrivia);
        trivia.setVisibility(View.INVISIBLE);


        Wait.setVisibility(View.VISIBLE);

        Acept.setVisibility(View.INVISIBLE);

        Declined.setVisibility(View.INVISIBLE);

        Button onHold = (Button)findViewById(R.id.btnonhold);
        Button onSent = (Button)findViewById(R.id.btnSendQuestion);
        Button onDeclined = (Button)findViewById(R.id.btnDeclined);
        Button onTrivia = (Button)findViewById(R.id.btntrivia);

        onHold.setBackgroundResource(R.drawable.layer_list);
        onSent.setBackgroundResource(R.drawable.layer_list2);
        onDeclined.setBackgroundResource(R.drawable.layer_list2);
        onTrivia.setBackgroundResource(R.drawable.layer_list2);
    }

    public void ShowTrivia(View v)
    {

        ListView Wait = (ListView)findViewById(R.id.lvAsksOfConference);
        ListView Acept = (ListView)findViewById(R.id.lvAsksAceptedOfConference);
        ListView Declined = (ListView)findViewById(R.id.lvAsksDeclinedOfConference);


        ListView trivia = (ListView)findViewById(R.id.lvtrivia);
        trivia.setVisibility(View.VISIBLE);


        Wait.setVisibility(View.INVISIBLE);

        Acept.setVisibility(View.INVISIBLE);

        Declined.setVisibility(View.INVISIBLE);

        Button onHold = (Button)findViewById(R.id.btnonhold);
        Button onSent = (Button)findViewById(R.id.btnSendQuestion);
        Button onDeclined = (Button)findViewById(R.id.btnDeclined);
        Button onTrivia = (Button)findViewById(R.id.btntrivia);

        onHold.setBackgroundResource(R.drawable.layer_list2);
        onSent.setBackgroundResource(R.drawable.layer_list2);
        onDeclined.setBackgroundResource(R.drawable.layer_list2);
        onTrivia.setBackgroundResource(R.drawable.layer_list);
    }

    //future socket
    /*


    private Emitter.Listener showNewQuestions = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            goAndShowActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    addQuestionFromServer(data);
                    try {
                        Log.d("info", data.getString("question"));
                    } catch (JSONException ex) {
                    }
                }
            });
        }
    };



    public static void sendAceptedQuestion(int position){

        questioninuse = (Question)list.get(position);
        String idofquestion = String.valueOf(questioninuse.getId());
        positionofquestion = position;
        new ChangeStatus().execute("/croissant/aceptquestion.php", idofquestion, "send");
    }



    @Override
    public void onBackPressed() {
        socket.disconnect();
        socket.off("newMessage", showNewQuestions);
        startActivity(new Intent(this, GoAndShowManage.class));
    }

    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("newMessage", showNewQuestions);
    }
    */



}

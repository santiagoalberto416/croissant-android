package com.croissant.croissant;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Santiago on 13/03/2016.
 */

public class GoAndShowQuestionsManage extends Activity{


    ListView lvQuestionWaiting;
    private static ProgressDialog pd;
    private static InputStream data = null; //raw data received from api
    private static JSONObject result = null; //JSON Object
    private static String resultText = ""; //result as string
    private static String domain = "https://croissant-santy-ruler.c9users.io";
    String urlAPI = ""; //API Url
    RelativeLayout tipoDiv;

    public static String mode = "";

    public static ArrayList<Question> list;
    public static ArrayList<Question> list2;
    public static ArrayList<Question> list3;
    public static ArrayList<Question> list4;

    public static ListAdapter_mesage adapter;
    public static ListAdapter_aceptMesage adapter2;
    public static ListAdapter_declined adapter3;
    public static ListAdapter_questions_trivia adapter4;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_conference);
        activity = this;
        ShowInfoConference.show(this);
        ShowInfoUser.show(this);
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String idConference = sharedPreferences.getString("idConference", DEFAULT);
        domain = getResources().getString(R.string.domainsite);
        urlAPI = domain +  "/croissant/getallsasks.php"; //API Url
        new LoadWaitQuestions().execute(idConference);

        socket.on("newMessage", showNewQuestions);
        socket.connect();
        //registerClickCallback();
        /*
        mode = "3";
        new LoadWaitQuestions().execute(idConference, mode);*/

    }

    public void removeQuestion(View v)
    {
        socket.emit("removeQuestion", result);
    }

    public GoAndShowQuestionsManage()
    {

    }

    public class LoadWaitQuestions extends AsyncTask<String, Void, ArrayList<ArrayList<Question>>>
    {
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog(GoAndShowQuestionsManage.this);
            pd.setMessage("Loading Questions...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }
        @Override
        protected ArrayList<ArrayList<Question>> doInBackground(String...args)
        {
            //empty list
            list = new ArrayList<Question>();
            list2 = new ArrayList<Question>();
            list3 = new ArrayList<Question>();

            //a;adi ontro array
            list4 = new ArrayList<Question>();

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
                JSONArray jsonArray = result.getJSONArray(LABEL_ASKS);
                for(int i = 0; i <jsonArray.length(); i++)
                {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
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
                        list.add(os);
                    }catch (Exception e)
                    {
                        Log.d("error ", "no se pudo crear el objeto");
                    }

                }

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
                JSONArray jsonArray3 = result.getJSONArray(LABEL_ASKSD);
                for(int i = 0; i <jsonArray3.length(); i++)
                {
                    JSONObject jsonItem = jsonArray3.getJSONObject(i);
                    //Log.d("JSON item", jsonItem.toString());
                    //read key-values
                    int id = jsonItem.getInt(LABEL_ID);
                    String user = jsonItem.getString(LABEL_USER);
                    String date = jsonItem.getString(LABEL_DATE);
                    String question = jsonItem.getString(LABEL_QUESTION);
                    //Create object
                    try {
                        Question os = new Question(id, question, date, user);
                        //add object to list
                        Log.d("denied item added", jsonItem.toString());
                        list3.add(os);
                    }catch (Exception e)
                    {
                        Log.d("error ", "no se pudo crear el objeto");
                    }

                }



                JSONArray jsonArray4 = result.getJSONArray(LABEL_ASKST);
                Log.d("length ","esta madre fueron"+ jsonArray4.length());
                for(int i = 0; i <jsonArray4.length(); i++)
                {
                    JSONObject jsonItem = jsonArray4.getJSONObject(i);
                    Log.d("JSON item trivia", jsonItem.toString());
                    //read key-values
                    int id = jsonItem.getInt(LABEL_ID);
                    String user = jsonItem.getString(LABEL_USER);
                    String date = jsonItem.getString(LABEL_DATE);
                    String question = jsonItem.getString(LABEL_QUESTION);
                    //Create object
                    try {
                        Question os = new Question(id, question, date, user);
                        //add object to list
                        Log.d("trivia item added", jsonItem.toString());
                        list4.add(os);
                    }catch (Exception e)
                    {
                        Log.d("error ", "no se pudo crear el objeto");
                    }

                }




            }
            catch (JSONException e) { Log.e("JSON Exception", e.toString()); }
            ArrayAlCuadrado.add(list);
            ArrayAlCuadrado.add(list2);
            ArrayAlCuadrado.add(list3);
            ArrayAlCuadrado.add(list4);
            //return populated list
            return ArrayAlCuadrado;
        }
        @Override
        protected void onPostExecute(ArrayList<ArrayList<Question>> list)
        {
            //close progress dialog
            pd.dismiss();
            //show list

            lvQuestionWaiting = (ListView) findViewById(R.id.lvAsksOfConference);
            adapter = new ListAdapter_mesage(GoAndShowQuestionsManage.this, list.get(0));
            lvQuestionWaiting.setAdapter(adapter);



            lvQuestionWaiting = (ListView) findViewById(R.id.lvAsksAceptedOfConference);
            adapter2 = new ListAdapter_aceptMesage(GoAndShowQuestionsManage.this, list.get(1));
            lvQuestionWaiting.setAdapter(adapter2);


            lvQuestionWaiting = (ListView) findViewById(R.id.lvAsksDeclinedOfConference);
            adapter3 = new ListAdapter_declined(GoAndShowQuestionsManage.this, list.get(2));
            lvQuestionWaiting.setAdapter(adapter3);

            lvQuestionWaiting = (ListView) findViewById(R.id.lvtrivia);
            adapter4 = new ListAdapter_questions_trivia(GoAndShowQuestionsManage.this, list.get(3));
            lvQuestionWaiting.setAdapter(adapter4);
            Log.d("entro?","entro en el modo 3");


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

    private Emitter.Listener showNewQuestions = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            GoAndShowQuestionsManage.this.runOnUiThread(new Runnable() {
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
            list.add(question);
            adapter.notifyDataSetChanged();
        }catch (JSONException ex){}
    }

    public static void sendAceptedQuestion(int position){
        /* Guardamos la pregunta a la que le dimos click.
            Para esto, utilizamos la variable 'position' que contiene la posicion del item, del ListView,
            al que le dimos click y luego utilizamos esa misma posicion para acceder al item pero
            ahora de la lista que contiene los datos de tipo Question (lista: list).
        */
        questioninuse = (Question)list.get(position);
        String idofquestion = String.valueOf(questioninuse.getId());
        positionofquestion = position;
        new ChangeStatus().execute("/croissant/aceptquestion.php", idofquestion, "send");
    }

    public static void sendQuestionToTrivia(int position){
        /* Guardamos la pregunta a la que le dimos click.
            Para esto, utilizamos la variable 'position' que contiene la posicion del item, del ListView,
            al que le dimos click y luego utilizamos esa misma posicion para acceder al item pero
            ahora de la lista que contiene los datos de tipo Question (lista: list).
        */
        questioninuse = (Question)list4.get(position);
        String idofquestion = String.valueOf(questioninuse.getId());
        positionofquestion = position;
        new ChangeStatus().execute("/croissant/playquestion.php", idofquestion, "trivia");
    }

    public static void sendDeclinedQuestion(int position){
        questioninuse = (Question)list.get(position);
        String idofquestion = String.valueOf(questioninuse.getId());
        positionofquestion = position;
        new ChangeStatus().execute("/croissant/declinequestion.php", idofquestion, "declined");
    }

    public static void showEditQuestion(final int position, final Activity activity) {

        Question questioninuse1 = list.get(position);
        LayoutInflater inflater = LayoutInflater.from(activity); // 1
        final View theInflatedView = inflater.inflate(R.layout.edit_question, null);
        EditText txt = (EditText)theInflatedView.findViewById(R.id.txtEditQuestion);
        TextView tvUser = (TextView)theInflatedView.findViewById(R.id.userEditQuestion);
        TextView tvDate = (TextView)theInflatedView.findViewById(R.id.timeEditQuestion);
        txt.setText(questioninuse1.getQuestion());
        tvUser.setText(questioninuse1.getUser());
        tvDate.setText(questioninuse1.getDateOfQuestion());


        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(theInflatedView);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Button btn_cancel = (Button) theInflatedView.findViewById(R.id.btnCancelEditQuestion);
        Button btn_edit = (Button) theInflatedView.findViewById(R.id.btnEditQuestion);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editQuestion(position, theInflatedView, dialog);
            }
        });
    }

    public static void editQuestion(int position, View view, Dialog dialog)
    {
        EditText txt = (EditText)view.findViewById(R.id.txtEditQuestion);
        String newQuestion = txt.getText().toString();
        list.get(position).setQuestion(newQuestion);
        String idofquestion = String.valueOf(list.get(position).getId());
        new ChangeStatus().execute("/croissant/editquestion.php", idofquestion, "edit", newQuestion);
        dialog.dismiss();

    }

    public static void returnQuestion(int position)
    {

        questioninuse = (Question)list3.get(position);
        String idofquestion = String.valueOf(questioninuse.getId());
        positionofquestion = position;
        new ChangeStatus().execute("/croissant/returnquestion.php", idofquestion, "return");

    }

    public static void deleteQuestion(int position)
    {
        questioninuse = (Question)list3.get(position);
        String idofquestion = String.valueOf(questioninuse.getId());
        positionofquestion = position;
        new ChangeStatus().execute("/croissant/deletequestion.php", idofquestion, "delete");

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

    public static class ChangeStatus extends AsyncTask<String,Void, JSONObject>
    {
        String mode;
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog(activity);
            pd.setMessage("Loading Questions...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }
        @Override
        protected JSONObject doInBackground(String...args)
        {
            //connect to API
            Boolean response = false;

            Log.d("Connecting to API", "Connecting");
            URL url;
            HttpURLConnection connection = null;
            try
            {
                url = new URL(domain+args[0]); //create url object
                //aqui le ingresare el domain mas un argumento que le ingrese por ejemplo domain+ "acceptquestion"
                connection = (HttpURLConnection) url.openConnection();


                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                SharedPreferences sharedPreferences = activity.getSharedPreferences("myData", Context.MODE_PRIVATE);
                //String idConference = sharedPreferences.getString("idConference", DEFAULT);
                //Log.d("idConference ",idConference);
                // no sale en mode default
                Uri.Builder builder;
                if(args[2].equals("edit")) {
                    builder = new Uri.Builder().appendQueryParameter("idquestion", args[1]).appendQueryParameter("txtquestion", args[3]);

                }
                else{
                    builder = new Uri.Builder().appendQueryParameter("idquestion", args[1]);
                }
                mode = args[2];
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

            //return populated list
            return result;
        }
        @Override
        protected void onPostExecute(JSONObject result)
        {
            //close progress dialog

            pd.dismiss();
            //show list
            try{

                int id = result.getInt("status");

                if(id==0)
                {


                    if(mode.equals("send")) {

                        Toast toast = Toast.makeText(activity, "The question was accepted", Toast.LENGTH_LONG);
                        toast.show();

                        // añadimos la pregunta a la lista de preguntas aceptadas
                        list2.add(questioninuse);

                        // le decimos al adaptador de la 'list2', que un elemento a sido modificado (en este caso añadimos la pregunta a la lista)
                        adapter2.notifyDataSetChanged();

                        // ahora removemos esa pregunta de la lista de preguntas en espera
                        list.remove(positionofquestion);

                        // le decimos al adaptador de la 'list', que un elemento a sido modificado (en este caso eliminamos un elemento)
                        adapter.notifyDataSetChanged();
                        socket.emit("sendSpeaker", result);
                    }
                    if(mode.equals("declined")) {

                        Toast toast = Toast.makeText(activity, "The question was declined", Toast.LENGTH_LONG);
                        toast.show();
                        // añadimos la pregunta a la lista de preguntas aceptadas
                        list3.add(questioninuse);

                        // le decimos al adaptador de la 'list2', que un elemento a sido modificado (en este caso añadimos la pregunta a la lista)
                        adapter3.notifyDataSetChanged();

                        // ahora removemos esa pregunta de la lista de preguntas en espera
                        list.remove(positionofquestion);

                        // le decimos al adaptador de la 'list', que un elemento a sido modificado (en este caso eliminamos un elemento)
                        adapter.notifyDataSetChanged();
                    }
                    if(mode.equals("edit")){

                        Toast toast = Toast.makeText(activity, "The question was edited", Toast.LENGTH_LONG);
                        toast.show();
                        //list.set(positionofquestion, questioninuse);
                        adapter.notifyDataSetChanged();
                        //questioninuse = null;

                    }
                    if(mode.equals("delete")){

                        Toast toast = Toast.makeText(activity, "The question was deleted", Toast.LENGTH_LONG);
                        toast.show();
                        list3.remove(positionofquestion);
                        adapter3.notifyDataSetChanged();

                    }
                    if(mode.equals("return")) {

                        Toast toast = Toast.makeText(activity, "The question was returned", Toast.LENGTH_LONG);
                        toast.show();
                        list3.remove(positionofquestion);
                        adapter3.notifyDataSetChanged();
                        list.add(questioninuse);
                        adapter.notifyDataSetChanged();
                    }
                    if(mode.equals("trivia")){
                        socket.emit("sendQuestion", result);
                        Toast toast = Toast.makeText(activity, "The question was send to the asistant", Toast.LENGTH_LONG);
                        toast.show();


                        // ahora removemos esa pregunta de la lista de preguntas en espera
                        list4.remove(positionofquestion);

                        // le decimos al adaptador de la 'list', que un elemento a sido modificado (en este caso eliminamos un elemento)
                        adapter4.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(activity, "The question couldn't change of status", Toast.LENGTH_LONG);
                    toast.show();
                }


            }
            catch (JSONException e) { Log.e("JSON Exception", e.toString()); }


        }
    }//aqui

}


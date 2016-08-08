package com.croissant.croissant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.croissant.croissant.Graphics.utils.Constants;
import com.croissant.croissant.utilities.CustomActivity;

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
import java.util.ArrayList;

/**
 * Created by brandon on 9/03/16.
 */
public class GoAndShowConferences extends CustomActivity {

    ListView lvConferences;
    TextView txtNameUser;
    public static final String DEFAULT = "N/A";
    private ProgressDialog pd;
    InputStream data = null; //raw data received from api
    JSONObject result = null; //JSON Object
    String resultText = ""; //result as string
    String domain = "";
    String urlAPI = ""; //API Url
    //Json Nodes
    private static final String LABEL_CONFERENCES ="conferences";
    private static final String LABEL_TITLE ="title";
    private static final String LABEL_DATE ="date";
    private static final String LABEL_TIME ="time";
    private static final String LABEL_PLACE ="Place";
    private static final String LABEL_ROOM ="Room";
    private static final String LABEL_FIRSTNAME_SPEAKER ="name";
    private static final String LABEL_LASTNAME_SPEAKER ="lastname";
    private static final String LABEL_ID = "id";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conferences);
        domain = getResources().getString(R.string.domainsite);
        urlAPI = domain +  "/croissant/getconferences.php"; //API Url
        new LoadConferences().execute();
        registerClickCallback();
    }

    public class LoadConferences extends AsyncTask<String, Void, ArrayList<Conference>>
    {
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog(new ContextThemeWrapper(GoAndShowConferences.this, android.R.style.Theme_Holo_Light_Dialog));
            pd.setMessage("Loading Conferences...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }
        @Override
        protected ArrayList<Conference> doInBackground(String...args)
        {
            //empty list
            ArrayList<Conference> list = new ArrayList<Conference>();

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

                //shaHe

                SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("txtnickname", "213")
                        .appendQueryParameter("id_event", sharedPreferences.getString(Constants.ID_EVENT, "0"));

                String query = builder.build().getEncodedQuery();

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                data = connection.getInputStream(); //read Stream
                StringBuilder rawData = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(data));
                String dataLine = "";
                while ((dataLine = reader.readLine())!= null)
                {
                    rawData.append(dataLine);
                }
                resultText = rawData.toString();
                rawData = null;
                os = null;
                writer = null;
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
                //Log.d("Data in JSON Format", result.toString());
            }
            catch (JSONException e) { Log.e("JSON Exception", e.toString()); }
            catch (Exception e) { Log.e("Generic Exception", e.toString()); }

            //parse JSON to classes
            try{
                JSONArray jsonArray = result.getJSONArray(LABEL_CONFERENCES);
                for(int i = 0; i <jsonArray.length(); i++)
                {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    //Log.d("JSON item", jsonItem.toString());
                    //read key-values
                    int id = jsonItem.getInt(LABEL_ID);
                    String title = jsonItem.getString(LABEL_TITLE);
                    String speaker = jsonItem.getJSONObject("speaker").getString(LABEL_FIRSTNAME_SPEAKER) + " " + jsonItem.getJSONObject("speaker").getString(LABEL_LASTNAME_SPEAKER);
                    String date = jsonItem.getString(LABEL_DATE) + "\n" + jsonItem.getString(LABEL_TIME);
                    String place = jsonItem.getString(LABEL_PLACE) + ", " + jsonItem.getString(LABEL_ROOM);
                    //Create object
                    Conference conference = new Conference(id, title, speaker, date, place);
                    //add object to list
                    list.add(conference);
                }
            }
            catch (JSONException e) { Log.e("JSON Exception", e.toString()); }

            //return populated list
            return list;
        }
        @Override
        protected void onPostExecute(ArrayList<Conference> list)
        {
            //close progress dialog
            pd.dismiss();
            //show list
            lvConferences = (ListView)findViewById(R.id.lvConferences);
            ListAdapter_conference adapter = new ListAdapter_conference(GoAndShowConferences.this, list);
            lvConferences.setAdapter(adapter);

        }
    }//aqui

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.lvConferences);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                RelativeLayout relativeLayout = (RelativeLayout) viewClicked;
                TextView tvNameConference = (TextView)relativeLayout.findViewById(R.id.nameConference);
                TextView tvSpeaker = (TextView)relativeLayout.findViewById(R.id.nameSpeaker);
                TextView tvPlace = (TextView)relativeLayout.findViewById(R.id.placeConference);
                TextView tvDate = (TextView)relativeLayout.findViewById(R.id.timeConference);
                SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idConference", relativeLayout.getTag().toString());
                editor.putString("nameConference", tvNameConference.getText().toString());
                editor.putString("nameSpeaker", tvSpeaker.getText().toString());
                editor.putString("placeConference", tvPlace.getText().toString());
                editor.putString("timeConference", tvDate.getText().toString());
                editor.commit();

                String typeUser = sharedPreferences.getString("typeUser", DEFAULT);

                if(Integer.valueOf(typeUser) == 4) //asistente
                    startActivity(new Intent(GoAndShowConferences.this, GoAndShowConference.class));
                if(Integer.valueOf(typeUser) == 3) //moderador
                    startActivity(new Intent(GoAndShowConferences.this, GoAndShowManage.class));


            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}

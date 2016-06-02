package com.croissant.croissant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;

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
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by brandon on 2/04/16.
 */
public class GoAndShowScoresByConference extends Activity{
    int height1;

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
    String idConference = "";
    String idUser = "";
    int count=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        ShowInfoUser.show(this);

        domain = getResources().getString(R.string.domainsite);
        urlAPI = domain + "/croissant/getscoresbyconference.php"; //API Url
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        idConference = sharedPreferences.getString("idConference", DEFAULT);
        idUser = sharedPreferences.getString("idUser", DEFAULT);
        String conference = sharedPreferences.getString("nameConference", DEFAULT);
        TextView tv = (TextView) findViewById(R.id.conferenceTitle);
        tv.setText(conference);
        new LoadScores().execute(idConference);

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GoAndShowConference.class));
    }

    public class LoadScores extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress dialog
            pd = new ProgressDialog(new ContextThemeWrapper(GoAndShowScoresByConference.this, android.R.style.Theme_Holo_Light_Dialog));
            pd.setMessage("Loading scores, please wait a moment...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            //empty list
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
                        .appendQueryParameter("idconference", args[0]);
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
            JSONObject score;
            JSONObject user;
            TextView tvFirstName;
            TextView tvSecondName;
            TextView tvThirdName;
            RelativeLayout contentGraphsUsers = (RelativeLayout) findViewById(R.id.contentBarFirst);
            RelativeLayout contentGraphUser = (RelativeLayout) findViewById(R.id.contentBarsYourScore);
            int widthUsers = contentGraphsUsers.getWidth();
            int heightUser = contentGraphUser.getHeight();
            try {
                if (result.getInt(LABEL_STATUS) == 0 && result.getInt("countQuestions") > 0) {
                    try
                    {
                        TextView totalPreguntas = (TextView) findViewById(R.id.tvTotalQuestions);
                        totalPreguntas.setText("Total of questions: " + result.getString("countQuestions"));
                        Log.d("Entro","si");
                        JSONArray arrayscores  = result.getJSONArray("scores");
                        count = arrayscores.length();
                        for(int x=0; x < count; x++)
                        {
                            score = arrayscores.getJSONObject(x);
                            user = score.getJSONObject("user");
                            if(x==0)
                            {
                                ((TextView)findViewById(R.id.tvFirstName)).setText(user.getString("name"));
                                ((TextView)findViewById(R.id.pointsFirst)).setText(score.getString("points"));
                                ((TextView)findViewById(R.id.pointsBadFirst)).setText(String.valueOf(result.getInt("countQuestions") - score.getInt("points")));
                                ((RelativeLayout) findViewById(R.id.graphRight1)).getLayoutParams().width = (widthUsers * score.getInt("points")) / result.getInt("countQuestions");
                                ((TextView)findViewById(R.id.averageFirst)).setText(String.valueOf("Average: " + score.getString("averageTime")));
                            }
                            if(x==1)
                            {
                                ((TextView)findViewById(R.id.tvSecondName)).setText(user.getString("name"));
                                ((TextView)findViewById(R.id.pointsSecond)).setText(score.getString("points"));
                                ((TextView)findViewById(R.id.pointsBadSecond)).setText(String.valueOf(result.getInt("countQuestions") - score.getInt("points")));
                                ((RelativeLayout) findViewById(R.id.graphRight2)).getLayoutParams().width = (widthUsers * score.getInt("points")) / result.getInt("countQuestions");
                                ((TextView)findViewById(R.id.averageSecond)).setText(String.valueOf("Average: " + score.getString("averageTime")));
                            }
                            if(x==2)
                            {
                                ((TextView)findViewById(R.id.tvThirdName)).setText(user.getString("name"));
                                ((TextView)findViewById(R.id.pointsThird)).setText(score.getString("points"));
                                ((TextView)findViewById(R.id.pointsBadThird)).setText(String.valueOf(result.getInt("countQuestions") - score.getInt("points")));
                                ((RelativeLayout) findViewById(R.id.graphRight3)).getLayoutParams().width = (widthUsers * score.getInt("points")) / result.getInt("countQuestions");
                                ((TextView)findViewById(R.id.averageThird)).setText(String.valueOf("Average: " + score.getString("averageTime")));
                            }

                            if(user.getString("id").equals(idUser))
                            {

                                ((TextView)findViewById(R.id.positionScore)).setText(String.valueOf(x + 1));
                                ((TextView)findViewById(R.id.pointsGood)).setText(score.getString("points"));
                                ((TextView)findViewById(R.id.pointsBad)).setText(String.valueOf(result.getInt("countQuestions") - score.getInt("points")));
                                ((TextView)findViewById(R.id.tvBestTime)).setText(String.valueOf(score.getString("bestTime")));
                                ((TextView)findViewById(R.id.tvWorstTime)).setText(String.valueOf(score.getString("worstTime")));
                                ((TextView)findViewById(R.id.tvAverage)).setText(String.valueOf(score.getString("averageTime")));

                                ((RelativeLayout) findViewById(R.id.rightQuestionsBar)).getLayoutParams().height = (heightUser * score.getInt("points")) / result.getInt("countQuestions");
                                ((RelativeLayout) findViewById(R.id.badQuestionsBar)).getLayoutParams().height = heightUser - ((RelativeLayout) findViewById(R.id.rightQuestionsBar)).getLayoutParams().height;


                            }

                        }
                        ((ScrollView)findViewById(R.id.scrollScoresConference)).setVisibility(View.VISIBLE);

                    }catch (Exception ex) {}

                }
                else
                {
                    ((TextView)findViewById(R.id.tvTotalQuestions)).setText("Scores are not available for this conference.");
                }
            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            }
        }
    }

    public class Vista extends View
    {

        public Vista(Context c)
        {
            super(c);
        }
        @Override
        public void onDraw(Canvas c)
        {
            super.onDraw(c);
            //tamaño del canvas
            int anchoCanvas = c.getWidth();
            int arriba = 0;
            int puntoInicioBarras = 360;
            int puntosAsistente = 0;
            int totalPreguntas = 0;
            int margin = 10;
            int areaBarras = (anchoCanvas - puntoInicioBarras) - 10;
            int anchoRectangulo = 0;
            JSONArray arrayscores;
            JSONObject score;
            JSONObject user;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 10;
            Bitmap userPicture = BitmapFactory.decodeResource(getResources(), R.drawable.user_black, bitmapOptions);
            bitmapOptions.inSampleSize = 2;
            Bitmap firstPlace = BitmapFactory.decodeResource(getResources(), R.drawable.firstplace, bitmapOptions);
            Bitmap secondPlace = BitmapFactory.decodeResource(getResources(), R.drawable.secondplace, bitmapOptions);
            Bitmap thirdPlace = BitmapFactory.decodeResource(getResources(), R.drawable.thirdplace, bitmapOptions);

            //Pintura
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            Paint alphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            alphaPaint.setAlpha(70);

            try {
                totalPreguntas = result.getInt("countQuestions");
                arrayscores = result.getJSONArray("scores");

                p.setColor(Color.parseColor("#cccccc"));
                c.drawRect(margin, 0, anchoCanvas - margin, 1, p);

                for(int i = 0; i < count; i++) {

                    //User picture
                    c.drawBitmap(userPicture, margin, arriba + 30, alphaPaint); //77
                    p.setColor(Color.parseColor("#cccccc"));

                    //User name
                    score = arrayscores.getJSONObject(i);
                    user = score.getJSONObject("user");
                    p.setColor(Color.parseColor("#9c9c9c"));
                    p.setTextSize(24);
                    p.setTextAlign(Paint.Align.LEFT);
                    c.drawText(user.getString("name"), 120 + margin, arriba + 76, p);

                    //Premios
                    if(i == 0) c.drawBitmap(firstPlace, 40 + margin, arriba + 58, null);


                    if(i == 1) c.drawBitmap(secondPlace, 40 + margin, arriba + 58, null);

                    if(i == 2) c.drawBitmap(thirdPlace, 40 + margin, arriba + 58, null);


                    //Rectangulo Aciertos
                    puntosAsistente = score.getInt("points");
                    anchoRectangulo = ((puntosAsistente * areaBarras) / totalPreguntas) + puntoInicioBarras;
                    p.setColor(Color.parseColor("#FF397BAE"));
                    c.drawRect(puntoInicioBarras, arriba + 36, anchoRectangulo, arriba + 103, p);
                    p.setColor(Color.parseColor("#FFFFFFFF"));
                    p.setTextSize(30);
                    p.setTextAlign(Paint.Align.CENTER);
                    c.drawText(String.valueOf(puntosAsistente), ((anchoRectangulo - puntoInicioBarras) / 2) + puntoInicioBarras, arriba + 82, p);


                    //Rectangulo Errores
                    p.setColor(Color.parseColor("#E47F52"));
                    c.drawRect(anchoRectangulo, arriba + 36, (areaBarras - (anchoRectangulo - puntoInicioBarras)) + anchoRectangulo, arriba + 103, p);
                    p.setColor(Color.parseColor("#FFFFFFFF"));
                    if((totalPreguntas - puntosAsistente) != 0)
                    c.drawText(String.valueOf(totalPreguntas - puntosAsistente), ((areaBarras - (anchoRectangulo - puntoInicioBarras)) / 2) + anchoRectangulo, arriba + 82, p);


                    arriba += 133;
                    p.setColor(Color.parseColor("#cccccc"));
                    c.drawRect(margin, arriba, anchoCanvas - margin, arriba + 1, p);

                    //Color
                    //p.setColor(Color.parseColor("#505050"));
                }

            }catch (JSONException ex) {}
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // Compute the height required to render the view
            // Assume Width will always be MATCH_PARENT.
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = height1; // Since 3000 is bottom of last Rect to be drawn added and 50 for padding.
            setMeasuredDimension(width, height);
        }

    }
}

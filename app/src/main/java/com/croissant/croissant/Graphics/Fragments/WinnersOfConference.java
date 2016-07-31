package com.croissant.croissant.Graphics.Fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.croissant.croissant.Graphics.utils.Constants;
import com.croissant.croissant.R;
import com.croissant.croissant.ShowInfoConference;
import com.croissant.croissant.ShowInfoUser;
import com.croissant.croissant.adapters.QuestionsAdapterGraphics;
import com.example.santiago.data.GraphicsAllPersons.Graphics;
import com.example.santiago.data.GraphicsConference.GraphicsFistPlaces;
import com.example.santiago.data.GraphicsConference.Score;
import com.example.santiago.data.services.QuestionScoreService;
import com.example.santiago.data.services.WinnerOfConferenceService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by santiago on 30/07/2016.
 */
public class WinnersOfConference extends Fragment {

    private int txtIdConference;
    RelativeLayout progressBar;
    // TODO: Rename and change types of parameters
    private int mParam1;

    private int points = 0;


    protected TextView points1;
    protected TextView points2;
    protected TextView points3;
    protected TextView titlefitst;
    protected TextView titleSecond;
    protected TextView titleThird;
    protected RelativeLayout firstBar;
    protected RelativeLayout secondBar;
    protected RelativeLayout thirdBar;
    protected RelativeLayout firstEmptyBar;
    protected RelativeLayout secondEmptyBar;
    protected RelativeLayout thirdEmptyBar;

    public WinnersOfConference() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GeneralInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static WinnersOfConference newInstance() {
        WinnersOfConference fragment = new WinnersOfConference();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.winners_of_conference_fragment, container, false);
        bindViews(v);
        progressBar.setVisibility(View.VISIBLE);
        return v;
    }

    @Override
    public void onResume(){

        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", getContext().MODE_PRIVATE);
        txtIdConference = Integer.parseInt(sharedPreferences.getString("idConference", Constants.DEFAULT));
        getWinners();
    }

    private void getWinners(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://croissant-santy-ruler.c9users.io/croissant/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WinnerOfConferenceService service = retrofit.create(WinnerOfConferenceService.class);
        Call<GraphicsFistPlaces> call = service.GetWinners(txtIdConference, 0);

        call.enqueue(new Callback<GraphicsFistPlaces>() {
            @Override
            public void onResponse(Call<GraphicsFistPlaces> call, Response<GraphicsFistPlaces> response) {
                Resources r = getContext().getResources();
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
                List<Score> scores = response.body().getScores();
                getTotalPoints(scores);
                Score score;
                String conectorword = getContext().getResources().getString(R.string.pointsof);
                if(scores.size()>0) {
                    score = scores.get(0);
                    titlefitst.setText("1. "+score.getUser().getName());
                    points1.setText(score.getPoints()+" "+conectorword+" "+points);
                    firstBar.setLayoutParams(
                            new TableLayout.LayoutParams(0, Math.round(px), points-score.getPoints()));
                    firstEmptyBar.setLayoutParams(
                            new TableLayout.LayoutParams(0, Math.round(px), score.getPoints()));
                }
                if(scores.size()>1) {
                    score = scores.get(1);
                    titleSecond.setText("2. "+score.getUser().getName());
                    points2.setText(score.getPoints()+" "+conectorword+" "+points);
                    secondBar.setLayoutParams(
                            new TableLayout.LayoutParams(0, Math.round(px), points-score.getPoints()));
                    secondEmptyBar.setLayoutParams(
                            new TableLayout.LayoutParams(0, Math.round(px), score.getPoints()));
                }
                if(scores.size()>2) {
                    score = scores.get(2);
                    titleThird.setText("3. "+score.getUser().getName());
                    points3.setText(score.getPoints()+" "+conectorword+" "+points);
                    thirdBar.setLayoutParams(
                            new TableLayout.LayoutParams(0, Math.round(px), points-score.getPoints()));
                    thirdEmptyBar.setLayoutParams(
                            new TableLayout.LayoutParams(0, Math.round(px), score.getPoints()));
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GraphicsFistPlaces> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("response", "failure");
            }
        });

    }

    private void bindViews(View v){
        progressBar = (RelativeLayout)v.findViewById(R.id.progressQuestions);
        titlefitst = (TextView)v.findViewById(R.id.nameWinner);
        titleSecond =  (TextView)v.findViewById(R.id.nameWinner2);
        titleThird =  (TextView)v.findViewById(R.id.nameWinner3);
        points1 = (TextView)v.findViewById(R.id.points1);
        points2 =  (TextView)v.findViewById(R.id.points2);
        points3 =  (TextView)v.findViewById(R.id.points3);
        firstBar =  (RelativeLayout)v.findViewById(R.id.incorrectBar);
        secondBar =  (RelativeLayout)v.findViewById(R.id.incorrectBar2);
        thirdBar =  (RelativeLayout)v.findViewById(R.id.incorrectBar3);
        firstEmptyBar  = (RelativeLayout)v.findViewById(R.id.emptyBar1);
        secondEmptyBar = (RelativeLayout)v.findViewById(R.id.emptyBar2);
        thirdEmptyBar = (RelativeLayout) v.findViewById(R.id.emptyBar3);
    }

    private void getTotalPoints(List<Score> scores){
        for (Score sc: scores) {
            points = sc.getPoints() + points;
        }
    }

}

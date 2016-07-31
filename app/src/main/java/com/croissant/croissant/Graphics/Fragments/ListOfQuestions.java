package com.croissant.croissant.Graphics.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.croissant.croissant.Graphics.utils.Constants;
import com.croissant.croissant.R;
import com.croissant.croissant.adapters.QuestionsAdapterGraphics;
import com.example.santiago.data.GraphicsAllPersons.Graphics;
import com.example.santiago.data.services.QuestionScoreService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListOfQuestions extends Fragment {

    private int txtIdConference;
    RelativeLayout messageNoQuestion;
    RelativeLayout progressBar;
    RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private int mParam1;

    public ListOfQuestions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GeneralInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfQuestions newInstance() {
        ListOfQuestions fragment = new ListOfQuestions();
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
        View v = inflater.inflate(R.layout.fragment_list_of_questions, container, false);
        recyclerView= (RecyclerView)v.findViewById(R.id.recyclerQuestions);
        progressBar = (RelativeLayout) v.findViewById(R.id.progressQuestions);
        messageNoQuestion = (RelativeLayout) v.findViewById(R.id.messageContainer);
        progressBar.setVisibility(View.VISIBLE);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", getContext().MODE_PRIVATE);
        txtIdConference = Integer.parseInt(sharedPreferences.getString("idConference", Constants.DEFAULT));
            getQuestions();

    }

    private void getQuestions(){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://croissant-santy-ruler.c9users.io/croissant/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        QuestionScoreService service = retrofit.create(QuestionScoreService.class);
        Call<Graphics> call = service.GetQuestions(txtIdConference);

        call.enqueue(new Callback<Graphics>() {
            @Override
            public void onResponse(Call<Graphics> call, Response<Graphics> response) {

                    QuestionsAdapterGraphics adapter = new QuestionsAdapterGraphics(response.body().getAsks(), getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    if(response.body().getAsks().size()>0){
                        progressBar.setVisibility(View.GONE);
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                    }

            }

            @Override
            public void onFailure(Call<Graphics> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("response", "failure");
            }
        });

    }


}

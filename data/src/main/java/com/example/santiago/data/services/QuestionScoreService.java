package com.example.santiago.data.services;

import com.example.santiago.data.GraphicsAllPersons.Graphics;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by santiago on 11/07/2016.
 */
public interface QuestionScoreService {

    final String END_POINT = "https://croissant-santy-ruler.c9users.io/croissant";
    @FormUrlEncoded
    @POST("getScoresGodAndBad.php")
    Call<Graphics> GetQuestions(@Field("txtidconference") int id);
}

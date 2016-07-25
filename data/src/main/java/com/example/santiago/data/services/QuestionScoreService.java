package com.example.santiago.data.services;

import com.example.santiago.data.Graphics;
import com.example.santiago.data.IdUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by santiago on 11/07/2016.
 */
public interface QuestionScoreService {

    final String END_POINT = "https://croissant-santy-ruler.c9users.io/croissant";
    @FormUrlEncoded
    @POST("getScoresGodAndBad.php")
    Call<Graphics> GetQuestions(@Field("txtidconference") int id);
}

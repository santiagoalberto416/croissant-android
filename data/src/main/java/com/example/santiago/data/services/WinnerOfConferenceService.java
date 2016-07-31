package com.example.santiago.data.services;

import com.example.santiago.data.GraphicsAllPersons.Graphics;
import com.example.santiago.data.GraphicsConference.GraphicsFistPlaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by santiago on 30/07/2016.
 */
public interface WinnerOfConferenceService {
    @FormUrlEncoded
    @POST("getscoresbyconference.php")
    Call<GraphicsFistPlaces> GetWinners(@Field("idconference") int idConference, @Field("idquestion") int idQuestion);
}

package com.cos.phoneapp.service;

import com.cos.phoneapp.CMRespDto;
import com.cos.phoneapp.model.Phone;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PhoneService {

    @GET("phone")
    Call<CMRespDto<List<Phone>>> findAll();

    @GET("phone/{id}")
    Call<CMRespDto<Phone>> findById(@Path("id") Long id);

    @POST("phone")
    Call<CMRespDto<Phone>> save(@Body Phone phone);

    @DELETE("phone/{id}")
    Call<CMRespDto<Phone>> deleteById(@Path("id") Long id);

    @PUT("phone/{id}")
    Call<CMRespDto<Phone>> update(@Path("id") Long id ,@Body Phone phone);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://112.162.114.11:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
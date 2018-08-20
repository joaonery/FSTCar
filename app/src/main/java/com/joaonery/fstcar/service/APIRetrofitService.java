package com.joaonery.fstcar.service;

import com.joaonery.fstcar.model.Address;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIRetrofitService {

    @GET("{CEP}/json")
    Call<Address> getCEP(@Path("CEP") String CEP);
}

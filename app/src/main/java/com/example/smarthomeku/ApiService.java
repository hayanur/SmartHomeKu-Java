package com.example.smarthomeku;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("smarthomeku.php")
    Call<List<RelayResponse>> getRelayStatus();

    @GET("on_relay1.php")
    Call<RelayResponse> turnOnRelay1();

    @GET("off_relay1.php")
    Call<RelayResponse> turnOffRelay1();

    @GET("on_relay2.php")
    Call<RelayResponse> turnOnRelay2();

    @GET("off_relay2.php")
    Call<RelayResponse> turnOffRelay2();
}

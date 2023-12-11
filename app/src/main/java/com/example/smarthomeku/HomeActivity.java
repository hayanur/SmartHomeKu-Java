package com.example.smarthomeku;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private String currentRelay1Value = "";
    private String previousSuhuValue = null;
    private ApiService apiService;
    private final Handler handler = new Handler();
    private ImageView imageView1;

    private final Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            // Call API and update response here
            updateApiStatus();
            handler.postDelayed(this, pollingIntervalMillis); // Schedule repeated call
        }
    };

    private final long pollingIntervalMillis = 100; // Every 2 seconds

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        //tanggal
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.tanggal);
        textViewDate.setText(currentDate);

        //imageslider
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.cctv, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.cctv2, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        if (isInternetConnected()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.smarthomeku2.mwebs.id/") // Replace with your API URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
            loadRelayStatus();

            CardView btn1 = findViewById(R.id.imagebutton1);
            CardView btn2 = findViewById(R.id.imagebutton2);
            CardView btn3 = findViewById(R.id.imagebutton3);
            CardView btn4 = findViewById(R.id.imagebutton4);
            CardView btn5 = findViewById(R.id.imagebutton5);
            CardView btn6 = findViewById(R.id.imagebutton6);
            CardView btn7 = findViewById(R.id.imagebutton7);
            CardView btn8 = findViewById(R.id.imagebutton8);
            CardView btn9 = findViewById(R.id.imagebutton9);
            Animation zoomOutAnimation = AnimationUtils.loadAnimation(this, R.anim.animbtn);

            btn1.setOnClickListener(v -> btn1.startAnimation(zoomOutAnimation));
            btn2.setOnClickListener(v -> btn2.startAnimation(zoomOutAnimation));
            btn3.setOnClickListener(v -> btn3.startAnimation(zoomOutAnimation));
            btn4.setOnClickListener(v -> btn4.startAnimation(zoomOutAnimation));
            btn5.setOnClickListener(v -> btn5.startAnimation(zoomOutAnimation));
            btn6.setOnClickListener(v -> btn6.startAnimation(zoomOutAnimation));
            btn7.setOnClickListener(v -> btn7.startAnimation(zoomOutAnimation));
            btn8.setOnClickListener(v -> btn8.startAnimation(zoomOutAnimation));
            btn9.setOnClickListener(v -> btn9.startAnimation(zoomOutAnimation));
        } else {
            // Display message to the user if there is no internet connection
            Toast.makeText(this, "Tidak ada koneksi internet. Silakan cek koneksi Anda!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start the repeated call schedule when the activity is resumed
        handler.postDelayed(pollingRunnable, pollingIntervalMillis);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the repeated call schedule when the activity is paused
        handler.removeCallbacks(pollingRunnable);
    }

    private void loadRelayStatus() {
        Call<List<RelayResponse>> call = apiService.getRelayStatus();
        if (call != null) {
            call.enqueue(new Callback<List<RelayResponse>>() {
                @Override
                public void onResponse(Call<List<RelayResponse>> call, Response<List<RelayResponse>> response) {
                    if (response.isSuccessful()) {
                        RelayResponse relayResponse = response.body().get(0);
                        String relay1Value = relayResponse.getRelay1();
                        String relay2Value = relayResponse.getRelay2();
                        String suhuvalue = relayResponse.suhu();

                        // Update gambar sesuai dengan nilai relay1
                        updateImageView(relay1Value);
                        updateImageView2(relay2Value);
                        updateTextView(suhuvalue);

                    }
                }

                @Override
                public void onFailure(Call<List<RelayResponse>> call, Throwable t) {
                    // Handle failure of API call here
                }
            });
        }
    }

    private void updateImageView(String relay1Value) {
        ImageView imageView1 = findViewById(R.id.imageView1);

        if ("1".equals(relay1Value)) {
            // Ganti gambar sesuai dengan relay1=0 (ataskirion)
            imageView1.setImageResource(R.drawable.atastengahon);
        } else {
            // Ganti gambar sesuai dengan relay1=1 (ataskirioff)
            imageView1.setImageResource(R.drawable.atastengahoff);
        }
        // Tambahkan logika lain jika diperlukan
    }

    private void updateImageView2(String relay2Value) {
        ImageView imageView2 = findViewById(R.id.imageView2);

        if ("1".equals(relay2Value)) {
            // Ganti gambar sesuai dengan relay1=0 (ataskirion)
            imageView2.setImageResource(R.drawable.ataskananon);
        } else {
            // Ganti gambar sesuai dengan relay1=1 (ataskirioff)
            imageView2.setImageResource(R.drawable.ataskananoff);
        }
        // Tambahkan logika lain jika diperlukan
    }

    private void updateTextView(String suhuValue) {
        TextView textSuhu = findViewById(R.id.suhu);
        // Ensure UI updates on the main thread
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (suhuValue != null && !suhuValue.equals(previousSuhuValue)) {
                    // Update the TextView with the retrieved 'suhu' value
                    textSuhu.setText("Suhu ruangan: " + suhuValue + " °C");

                    // Update the previousSuhuValue
                    previousSuhuValue = suhuValue;
                } else {
                    // If suhuValue is null, set the text to "--"
                    textSuhu.setText("Suhu ruangan: --");
                }
            }
        });
    }


    private void updateApiStatus() {
        CardView btn1 = findViewById(R.id.imagebutton1);
        CardView btn2 = findViewById(R.id.imagebutton2);
        CardView btn3 = findViewById(R.id.imagebutton3);
        CardView btn4 = findViewById(R.id.imagebutton4);
        CardView btn5 = findViewById(R.id.imagebutton5);
        CardView btn6 = findViewById(R.id.imagebutton6);
        CardView btn7 = findViewById(R.id.imagebutton7);
        CardView btn8 = findViewById(R.id.imagebutton8);
        CardView btn9 = findViewById(R.id.imagebutton9);
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);
        ImageView imageView5 = findViewById(R.id.imageView5);
        ImageView imageView6 = findViewById(R.id.imageView6);
        ImageView imageView7 = findViewById(R.id.imageView7);
        ImageView imageView8 = findViewById(R.id.imageView8);
        ImageView imageView9 = findViewById(R.id.imageView9);
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(this, R.anim.animbtn);

        btn1.setOnClickListener(v -> {
            btn1.startAnimation(zoomOutAnimation);
            ApiService apiService = ApiClient.getApiService();
            Call<List<RelayResponse>> call = apiService.getRelayStatus();
            if (call != null) {
                call.enqueue(new Callback<List<RelayResponse>>() {
                    @Override
                    public void onResponse(Call<List<RelayResponse>> call, Response<List<RelayResponse>> response) {
                        if (response.isSuccessful()) {
                            RelayResponse relayResponse = response.body().get(0);
                            String relay1Value = relayResponse.getRelay1();
                            String relay2Value = relayResponse.getRelay2();
                            if ("1".equals(relay1Value)) {
                                // Ganti gambar sesuai dengan relay1 aktif
                                imageView1.setImageResource(R.drawable.atastengahoff);
                                Toast.makeText(HomeActivity.this, "Lampu Mati", Toast.LENGTH_SHORT).show();
                                turnOffRelay1();
                            } else {
                                // Ganti gambar sesuai dengan relay1 non-aktif
                                imageView1.setImageResource(R.drawable.atastengahon);
                                Toast.makeText(HomeActivity.this, "Lampu Hidup", Toast.LENGTH_SHORT).show();
                                turnOnRelay1();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RelayResponse>> call, Throwable t) {
                        // Handle failure of API call here
                    }
                });
            }
        });

        btn2.setOnClickListener(v -> {
            btn2.startAnimation(zoomOutAnimation);
            ApiService apiService = ApiClient.getApiService();
            Call<List<RelayResponse>> call = apiService.getRelayStatus();
            if (call != null) {
                call.enqueue(new Callback<List<RelayResponse>>() {
                    @Override
                    public void onResponse(Call<List<RelayResponse>> call, Response<List<RelayResponse>> response) {
                        if (response.isSuccessful()) {
                            RelayResponse relayResponse = response.body().get(0);
                            String relay1Value = relayResponse.getRelay1();
                            String relay2Value = relayResponse.getRelay2();
                            if ("1".equals(relay2Value)) {
                                // Ganti gambar sesuai dengan relay1 aktif
                                imageView2.setImageResource(R.drawable.ataskananoff);
                                Toast.makeText(HomeActivity.this, "Lampu Mati", Toast.LENGTH_SHORT).show();
                                turnOffRelay2();
                            } else {
                                // Ganti gambar sesuai dengan relay1 non-aktif
                                imageView2.setImageResource(R.drawable.ataskananon);
                                Toast.makeText(HomeActivity.this, "Lampu Hidup", Toast.LENGTH_SHORT).show();
                                turnOnRelay2();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RelayResponse>> call, Throwable t) {
                        // Handle failure of API call here
                    }
                });
            }
        });


        btn3.setOnClickListener(v -> {
            btn3.startAnimation(zoomOutAnimation);
            // Similar logic as above for button 3
        });

        btn4.setOnClickListener(v -> {
            btn4.startAnimation(zoomOutAnimation);
            // Similar logic as above for button 2
        });

        btn5.setOnClickListener(v -> {
            btn5.startAnimation(zoomOutAnimation);
            // Similar logic as above for button 2
        });

        btn6.setOnClickListener(v -> {
            btn6.startAnimation(zoomOutAnimation);
            // Similar logic as above for button 2
        });

        btn7.setOnClickListener(v -> {
            btn7.startAnimation(zoomOutAnimation);
            // Similar logic as above for button 2
        });

        btn8.setOnClickListener(v -> {
            btn8.startAnimation(zoomOutAnimation);
            // Similar logic as above for button 2
        });

        btn9.setOnClickListener(v -> {
            btn9.startAnimation(zoomOutAnimation);
            // Similar logic as above for button 2
        });
    }

    private void turnOffRelay1() {
        ApiService apiService = ApiClient.getApiService();
        Call<RelayResponse> onCall = apiService.turnOffRelay1();

        onCall.enqueue(new Callback<RelayResponse>() {
            @Override
            public void onResponse(Call<RelayResponse> call, Response<RelayResponse> response) {
                if (response.isSuccessful()) {
                    RelayResponse relayResponse = response.body();
                    // Handle respons dari panggilan API on_relay1.php
                    // Misalnya, perbarui tampilan atau lakukan tindakan lain
                } else {
                    // Handle respons tidak berhasil
                }
            }

            @Override
            public void onFailure(Call<RelayResponse> call, Throwable t) {
                // Handle kegagalan panggilan API
            }
        });
    }

    private void turnOnRelay1() {
        ApiService apiService = ApiClient.getApiService();
        Call<RelayResponse> onCall = apiService.turnOnRelay1();

        onCall.enqueue(new Callback<RelayResponse>() {
            @Override
            public void onResponse(Call<RelayResponse> call, Response<RelayResponse> response) {
                if (response.isSuccessful()) {
                    RelayResponse relayResponse = response.body();
                    // Handle respons dari panggilan API on_relay1.php
                    // Misalnya, perbarui tampilan atau lakukan tindakan lain
                } else {
                    // Handle respons tidak berhasil
                }
            }

            @Override
            public void onFailure(Call<RelayResponse> call, Throwable t) {
                // Handle kegagalan panggilan API
            }
        });
    }

    private void turnOffRelay2() {
        ApiService apiService = ApiClient.getApiService();
        Call<RelayResponse> onCall = apiService.turnOffRelay2();

        onCall.enqueue(new Callback<RelayResponse>() {
            @Override
            public void onResponse(Call<RelayResponse> call, Response<RelayResponse> response) {
                if (response.isSuccessful()) {
                    RelayResponse relayResponse = response.body();
                    // Handle respons dari panggilan API on_relay1.php
                    // Misalnya, perbarui tampilan atau lakukan tindakan lain
                } else {
                    // Handle respons tidak berhasil
                }
            }

            @Override
            public void onFailure(Call<RelayResponse> call, Throwable t) {
                // Handle kegagalan panggilan API
            }
        });
    }

    private void turnOnRelay2() {
        ApiService apiService = ApiClient.getApiService();
        Call<RelayResponse> onCall = apiService.turnOnRelay2();

        onCall.enqueue(new Callback<RelayResponse>() {
            @Override
            public void onResponse(Call<RelayResponse> call, Response<RelayResponse> response) {
                if (response.isSuccessful()) {
                    RelayResponse relayResponse = response.body();
                    // Handle respons dari panggilan API on_relay1.php
                    // Misalnya, perbarui tampilan atau lakukan tindakan lain
                } else {
                    // Handle respons tidak berhasil
                }
            }

            @Override
            public void onFailure(Call<RelayResponse> call, Throwable t) {
                // Handle kegagalan panggilan API
            }
        });
    }

    public void alamatrumah(View view) {
        String url="https://maps.app.goo.gl/vAa3HWuRCN4ubFdx6";
        Intent bukamaps= new Intent(Intent.ACTION_VIEW);
        bukamaps.setData(Uri.parse(url));
        startActivity(bukamaps);
    }

    public void suhu(View view) {
        String url="https://www.bmkg.go.id/cuaca/prakiraan-cuaca.bmkg?Kec=Colomadu&kab=Kab._Karanganyar&Prov=Jawa_Tengah&AreaID=5010028";
        Intent bukacuaca= new Intent(Intent.ACTION_VIEW);
        bukacuaca.setData(Uri.parse(url));
        startActivity(bukacuaca);
    }
}
package com.example.whether_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.whether_application.api.ApiClient;
import com.example.whether_application.api.WeatherService;
import com.example.whether_application.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText city;
    Button get_bt;
    TextView result;
    private String appid="63c4de9592f2a31608e2c829ca32f099";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=findViewById(R.id.city);
        get_bt=findViewById(R.id.get_bt);
        result=findViewById(R.id.result);

        get_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityy=city.getText().toString();
                getWeatherData(cityy);
            }
        });


    }

    private void getWeatherData(String cityy) {
        WeatherService service= ApiClient.getClient().create(WeatherService.class);
        //api call
        Call<WeatherResponse> call=service.getCurrentWeather(cityy,"63c4de9592f2a31608e2c829ca32f099");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful()){
                     WeatherResponse weatherResponse=response.body();
                     if(weatherResponse!=null){
                         float kelvin_to_celsius= weatherResponse.main.temp;
                         float celsi=kelvin_to_celsius-273.15f;


                         String weatherInfo="\n city: "+weatherResponse.name+"\n"+
                                 "Temperature: "+weatherResponse.main.temp+"°K \n"+
                                 "Temperature in Celsuis : "+ String.format("%.2f", celsi) +"\n"+
                                 "Humidity is "+weatherResponse.main.humidity+"\n";



                         result.setText(weatherInfo);
                     }
                     else {
                         result.setText("Error:"+response.message());
                     }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                result.setText("Error :"+t.getMessage());
            }
        });


    }
}
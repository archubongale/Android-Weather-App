package com.example.archanabongale.stormy;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "43fb13aef43ad25a0935007a1a87bdde";
        double latitude = 37.8267;
        double longitude = -122.423;

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/"
                + latitude + "," + longitude;
        if(isNetworkAvailabel()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather.getCurrentDetails(jsonData);
                            Log.v(TAG, response.body().string());

                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, R.string.network_unavailable_message ,
                    Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "Main UI code is running");

    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forcast = new JSONObject(jsonData);

    }

    private boolean isNetworkAvailabel() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;

        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");

    }


}

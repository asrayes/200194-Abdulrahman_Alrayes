package com.example.se328project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.Preferences;

import es.dmoral.toasty.Toasty;

public class PopUpWindow extends AppCompatActivity {

    ImageView weatherBackground;
    // Textview to show temperature and description
    TextView temperature, humidity;

    SharedPreferences sharedPreferences;
    String loc;
    String weatherWebserviceURL;
    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);

        weatherBackground = (ImageView) findViewById(R.id.weatherbackground);
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        EditText city = findViewById(R.id.edittext123);
        Button button = findViewById(R.id.buttonCity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loc = ((sharedPreferences.getString("city",null) != null && count++ != 0)  ? sharedPreferences.getString("city",null) : "berlin");
        // we"ll make HTTP request to this URL to retrieve weather conditions
        weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q="+loc+"&appid=5d56b24d1108b763e9381132bc443b43&units=metric";


        setPopUpWindow();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loc = city.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("city", loc);
                editor.commit();
                weatherWebserviceURL = "https://api.openweathermap.org/data/2.5/weather?q=" + loc + "&appid=5d56b24d1108b763e9381132bc443b43&units=metric";
                weather(weatherWebserviceURL);
            }
        });
        weather(weatherWebserviceURL);
    }

    public void weather(String url) {
        Log.d("rayes", url);
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("rayes","Response Received.");
                Log.d("rayes",response.toString());

                try {
                    JSONObject jsonMain = response.getJSONObject("main");
                    temperature.setText("Temperature: " + String.valueOf(jsonMain.getDouble("temp")));
                    humidity.setText("Humidity: " + String.valueOf(jsonMain.getDouble("humidity")));

                    /* sub categories as JSON arrays */
                    JSONArray jsonArray = response.getJSONArray("weather");
                    for (int i=0; i < jsonArray.length();i++){
                        Log.d("rayes",jsonArray.getString(i));
                        JSONObject oneObject = jsonArray.getJSONObject(i);
                        String weather = oneObject.getString("main");
                        Log.d("rayes",weather);

                        if (weather.equals("Clear")) {
                            Glide.with(PopUpWindow.this).load("https://i.ibb.co/Rcbv1Zc/sunny.jpg").into(weatherBackground);
                        }
                        else if (weather.equals("Clouds")) {
                            Glide.with(PopUpWindow.this).load("https://i.ibb.co/RbzxxRw/mostlycloudy.jpg").into(weatherBackground);
                        }
                        else if (weather.equals("Rain")) {
                            Glide.with(PopUpWindow.this).load("https://i.ibb.co/0jDN5cV/rainy.jpg").into(weatherBackground);
                        }
                    }
                    Toasty.success(PopUpWindow.this, "City " + loc + ". Successfully Updated.", Toast.LENGTH_LONG, true).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) {
            Log.d("Rayes","Error retrieving URL.");
            Log.d("Rayes",error.toString());
            Toasty.error(PopUpWindow.this, "City " + loc + ". Failed to Updated.", Toast.LENGTH_LONG, true).show();
        }});
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);

    }

    private void setPopUpWindow() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.75));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
}
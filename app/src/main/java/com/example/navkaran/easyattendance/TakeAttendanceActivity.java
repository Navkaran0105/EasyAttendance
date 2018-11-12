package com.example.navkaran.easyattendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaoyutian on 2018-11-06.
 */

public class TakeAttendanceActivity extends AppCompatActivity {

    Button stop_btn;
    private TextView class_number;
    private TextView class_name;
    private TextView register_number;
    private TextView check_number;
    private String course_id;
    private String course_name;
    private int student_num;
    private Runnable runnable;
    private Handler handler;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_class_attendance);
        getSupportActionBar().setTitle("Class Attendance");

        stop_btn = findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(stop);
        check_number = findViewById(R.id.check_number);
        class_number = findViewById(R.id.class_number);
        class_name = findViewById(R.id.class_name);
        register_number = findViewById(R.id.register_number);
        //Intent intent = getIntent();
        //course_id = intent.getStringExtra("COURSE_ID");
        //course_name = intent.getStringExtra("COURSE_NAME");
        //student_num = intent.getIntExtra("STUDENT_NUMBER",-1);
        course_id = "CSCI-5708";
        course_name = "Advanced topic in Github";
        student_num = 23;

        class_number.setText(course_id);
        class_name.setText(course_name);
        register_number.setText(student_num+" Students Registered");

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        checkAttendance();
                    }
                };
                Thread thread = new Thread(null, runnable, "background");
                thread.start();
                handler.postDelayed(this, 2000);
            }
        }, 0);

    }

    View.OnClickListener stop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            handler.removeCallbacksAndMessages(null);

            startNewIntend();
        }
    };

    private void startNewIntend(){
        Intent intent = new Intent(this, AttendanceDetailsActivity.class);

        //intent.putExtra("LONGITUDE", longitude);
        //intent.putExtra("LATITUDE", latitude);
        startActivity(intent);
    }

    public void checkAttendance(){
        final String url = "https://web.cs.dal.ca/~stang/csci5708/count.php?class_id="+course_id;
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            check_number.setText(response.getString("students_count")
                                    +" of 102 checked in");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

            }
        }
        );
        RequestQueueSingleton.getmInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
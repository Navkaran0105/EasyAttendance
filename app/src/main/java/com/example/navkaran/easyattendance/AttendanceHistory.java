package com.example.navkaran.easyattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class AttendanceHistory extends AppCompatActivity {
    ListView datelist;
    private CourseAdapter adapter;
    ArrayList dummy = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        datelist = findViewById(R.id.lvdatelist);
        adapter = new CourseAdapter(this, R.layout.course_list_item, dummy);
        datelist.setAdapter(adapter);
        registerForContextMenu(datelist);
    }
}

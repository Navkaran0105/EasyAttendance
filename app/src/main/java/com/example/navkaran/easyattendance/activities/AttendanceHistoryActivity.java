package com.example.navkaran.easyattendance.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navkaran.easyattendance.adapters.AttendanceHistoryAdapter;
import com.example.navkaran.easyattendance.utils.EasyAttendanceConstants;
import com.example.navkaran.easyattendance.models.Lecture;
import com.example.navkaran.easyattendance.models.LectureRepository;
import com.example.navkaran.easyattendance.R;

import java.util.List;

/**
 * @author Navkaran Kumar
 * Nov 2018
 * This activity handles the display of
 * all historical lectures for a course.
 */
public class AttendanceHistoryActivity extends AppCompatActivity {
    ListView dateList;
    private AttendanceHistoryAdapter adapter;
    private LectureRepository lectureRepository;
    private LiveData<List<Lecture>> lectures;
    private Intent intent;
    private String courseId,courseName;
    private TextView className,classNumber,registeredStudents;
    private int courseKey, studentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        getSupportActionBar().setTitle(R.string.title_lecture_list_hist);

        className = findViewById(R.id.tvClassName);
        classNumber = findViewById(R.id.tvClassNumber);
        registeredStudents = findViewById(R.id.tvRegisterNumber);
        dateList = findViewById(R.id.lvDateList);

        intent = getIntent();
        courseKey = intent.getIntExtra(EasyAttendanceConstants.COURSE_KEY, -1);
        courseId = intent.getStringExtra(EasyAttendanceConstants.COURSE_ID);
        courseName = intent.getStringExtra(EasyAttendanceConstants.COURSE_NAME);
        studentCount = intent.getIntExtra(EasyAttendanceConstants.COURSE_STUDENT_COUNT, 0);

        classNumber.setText(String.valueOf(courseId));
        className.setText(courseName);
        registeredStudents.setText(String.format(getString(R.string.formatString_students_registered), studentCount));

        // uses live data for better performance, so when lecture and attendances are
        // deleted in HistoryCheckedActivity, the list auto refreshes
        lectureRepository = new LectureRepository(getApplication());
        lectures = lectureRepository.getLiveLecturesByCourseKey(courseKey);
        adapter= new AttendanceHistoryAdapter(this, R.layout.attendance_history_list_item, lectures.getValue());
        dateList.setAdapter(adapter);

        lectures.observe(this, new Observer<List<Lecture>>() {
            @Override
            public void onChanged(@Nullable List<Lecture> lectures) {
                adapter.setLectures(lectures);
            }
        });

        //listens to which item in the listView the user clicks
        dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), HistoryCheckedActivity.class);

                // this lec is the one clicked by user
                Lecture lecClicked = adapter.getLectureList().get(i);

                // course id, name, student count, are from previous intents
                intent.putExtra(EasyAttendanceConstants.COURSE_ID, courseId);
                intent.putExtra(EasyAttendanceConstants.COURSE_NAME, courseName);
                intent.putExtra(EasyAttendanceConstants.COURSE_STUDENT_COUNT, studentCount);

                // get lecture id from lec clicked
                intent.putExtra(EasyAttendanceConstants.LECTURE_ID, lecClicked.getLectureId());
                intent.putExtra(EasyAttendanceConstants.ATTENDANCE_COUNT, lecClicked.getNumAttendee());

                startActivity(intent);
            }
        });
    }
}

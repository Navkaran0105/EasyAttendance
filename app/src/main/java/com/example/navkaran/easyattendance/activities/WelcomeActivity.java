package com.example.navkaran.easyattendance.activities;

// Author: Lan Chen, B00809814
// activity for one-time login
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navkaran.easyattendance.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomeActivity extends AppCompatActivity {

    public TextView tvUserId;
    public EditText etUserId;
    public Button btSave;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tvUserId = findViewById(R.id.tvUserId);
        etUserId = findViewById(R.id.etUserId);
        btSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        role = intent.getStringExtra("userRole");
        if (role != null) {
            if (role.equals("student")) {
                tvUserId.setText(R.string.label_student_id);
                btSave.setBackgroundResource(R.drawable.rounded_rect_button_orange_selector);
            } else if (role.equals("teacher")) {
                tvUserId.setText(R.string.label_staff_id);
                btSave.setBackgroundResource(R.drawable.rounded_rect_button_blue_selector);
            }
        }


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // save the data for studentID/staffID
                String id = etUserId.getText().toString();

                //Judging the accuracy of input
                if (id.equals("")) {
                    // if the user enter nothing
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_welcome_enter_id), Toast.LENGTH_SHORT).show();
                } else {
                    if (!isInputRight(id)) {
                        //if the user not only enter letter or number
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_welcome_only_enter_letter_number), Toast.LENGTH_SHORT).show();
                    } else {

                        //if the user enter right, save the ID locally
                        SharedPreferences sp = getSharedPreferences("CONTAINER",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userID", id);
                        editor.putString("userRole", role);
                        if(editor.commit()){
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_welcome_save), Toast.LENGTH_SHORT).show();
                        }

                        //close the current activity and open next activity based on which role
                        Intent intent1 = new Intent();
                        intent1.setClass(getApplicationContext(), MainActivity.class);

                        //send the data to the next activity
                        Bundle bundle = new Bundle();
                        bundle.putString("userRole", role);
                        bundle.putString("userID", id);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                    }
                }
            }
        });
    }


    /**
     * judge the accuracy of the input
     * only if user enter letters or numbers, no matter what length
     *
     * @param str
     * @return
     */
    public boolean isInputRight(String str) {
        String regExpression = "^[0-9a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regExpression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}

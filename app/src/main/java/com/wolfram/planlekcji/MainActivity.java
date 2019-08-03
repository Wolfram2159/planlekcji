package com.wolfram.planlekcji;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.wolfram.planlekcji.database.AppDatabase;
import com.wolfram.planlekcji.database.UserDao;
import com.wolfram.planlekcji.database.entities.Grade;
import com.wolfram.planlekcji.database.entities.Subject;
import com.wolfram.planlekcji.dialogs.SimpleDialog;

import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserDao dao = AppDatabase.getInstance(getApplicationContext()).getUserDao();

        Date d = new Date();

        Button btn = findViewById(R.id.button);

        d.setHours(15);
        d.setMinutes(0);

        AsyncTask.execute(()->{
            dao.setSubject(new Subject(1));
            dao.setSubject(new Subject(3));
            dao.setGrade(new Grade(1));
            dao.setGrade(new Grade(2));
            dao.setGrade(new Grade(2));
            //dao.setGrade(new Grade(10));
        });

        btn.setOnClickListener((v) -> {
            Log.w("date", ""+d+"");
            LiveData<List<Subject>> sub = dao.getSubjects();
            sub.observe(this, subjects -> {
                Log.e("subject_size", "" + subjects.size() + "");
                for (Subject subject : subjects) {
                    Log.e("subject", subject.toString());
                }
            });
            LiveData<List<Grade>> gr = dao.getGrades();
            gr.observe(this, grades->{
                Log.e("grade_size", ""+grades.size() + "");
                for (Grade grade : grades) {
                    Log.e("grade", grade.toString());
                }
            });
            SimpleDialog dialog = new SimpleDialog();
            dialog.show(getSupportFragmentManager(), "Dialog");
        });


        /*sub.observe(this, (list) -> {
            for (Subject subject : list) {
                Log.e("Subject", subject.toString());
            }
        });*/

        //dao.setGrade();
    }
}
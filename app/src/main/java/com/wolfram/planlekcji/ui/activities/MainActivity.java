package com.wolfram.planlekcji.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectAdapter;
import com.wolfram.planlekcji.database.Database;
import com.wolfram.planlekcji.database.mock.MockDatabase;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private LayoutInflater layoutInflater;
    private SubjectAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects_activity);

        recycler = findViewById(R.id.subjects_recycler);
        layoutInflater = getLayoutInflater();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recycler.setLayoutManager(layoutManager);

        Database db = new MockDatabase();

        List<Subject> data = db.getSubjectList();

        adapter = new SubjectAdapter(layoutInflater, data);

        recycler.setAdapter(adapter);
        /*UserDao dao = AppDatabase.getInstance(getApplicationContext()).getUserDao();

        Date d = new Date();

        Button btn = findViewById(R.id.button);

        d.setHours(15);
        d.setMinutes(0);
*/
        /*AsyncTask.execute(()->{
            dao.setSubject(new Subject(1));
            dao.setSubject(new Subject(3));
            dao.setGrade(new Grade(1));
            dao.setGrade(new Grade(2));
            dao.setGrade(new Grade(2));
            //dao.setGrade(new Grade(10));
        });*/

        /*btn.setOnClickListener((v) -> {
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
        });*/


        /*sub.observe(this, (list) -> {
            for (Subject subject : list) {
                Log.e("Subject", subject.toString());
            }
        });*/

        //dao.setGrade();
    }
}
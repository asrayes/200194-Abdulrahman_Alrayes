package com.example.se328project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class StudentDetailsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_view);

        TextView id = findViewById(R.id.studentIDView);
        TextView name = findViewById(R.id.studentNameView);
        TextView fatherName = findViewById(R.id.studentFatherNameView);
        TextView surname = findViewById(R.id.studentSurnameView);
        TextView nid = findViewById(R.id.studentNIDView);
        TextView dob = findViewById(R.id.studentDOBView);
        TextView gender = findViewById(R.id.studentGenderView);
        Button insert = findViewById(R.id.insertIntoSQLI);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ref = sharedPreferences.getString("ref",null);
        Firebase myDB = new Firebase();
        DatabaseReference reference = myDB.getReference();
        
        reference.child(ref).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Student student = snapshot.getValue(Student.class);
                    id.setText(student.getID());
                    name.setText(student.getName());
                    fatherName.setText(student.getFatherName());
                    surname.setText(student.getSurname());
                    nid.setText(student.getNID());
                    dob.setText(student.getDOB());
                    gender.setText(student.getGender());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper sqli = new DatabaseHelper(StudentDetailsView.this);
                System.out.println(id.getText().toString());
                if(sqli.insertStudent(Integer.parseInt(id.getText().toString()), name.getText().toString(), fatherName.getText().toString(),
                        surname.getText().toString(), nid.getText().toString(), dob.getText().toString(), gender.getText().toString())) {
                    Toasty.success(StudentDetailsView.this, "Student " + name.getText().toString() + " " + surname.getText().toString() + ". Successfully Inserted into SQL Database.", Toast.LENGTH_LONG, true).show();
                }
                else {
                    Toasty.error(StudentDetailsView.this, "Student " + name.getText().toString() + " " + surname.getText().toString() + ". Is already in SQL Database.", Toast.LENGTH_LONG, true).show();
                }
            }
        });
        
        
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent popUpWindow = new Intent(StudentDetailsView.this, PopUpWindow.class);
        switch(item.getItemId()) {
            case R.id.cloud:
                startActivity(popUpWindow);
        }
        return super.onOptionsItemSelected(item);
    }
}
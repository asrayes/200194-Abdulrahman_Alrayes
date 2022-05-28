package com.example.se328project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class StudentInteraction extends AppCompatActivity {

    DatabaseReference reference;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Spinner spinner;
    EditText name;
    EditText fatherName;
    EditText surname;
    EditText NID;
    DataSnapshot chosen;
    Firebase myDB = new Firebase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_interaction);

        name = findViewById(R.id.studentNameInteraction);
        surname = findViewById(R.id.surnameInteraction);
        fatherName = findViewById(R.id.fatherNameInteraction);
        NID = findViewById(R.id.NID);
        Button updateFirebase = findViewById(R.id.updateFirebase);
        Button deleteFirebase = findViewById(R.id.deleteFirebase);
        spinner = findViewById(R.id.spinner);


        setSpinner();
        setUI();

        updateFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosen != null) {
                    if ((name.getText().toString().isEmpty() || name.getText().toString().charAt(0) == ' ') && (fatherName.getText().toString().isEmpty() || fatherName.getText().toString().charAt(0) == ' ') &&
                            (surname.getText().toString().isEmpty() || surname.getText().toString().charAt(0) == ' ') && (NID.getText().toString().isEmpty() || NID.getText().toString().charAt(0) == ' ')) {
                        Toasty.error(StudentInteraction.this, "Please Fill All Inputs.", Toast.LENGTH_LONG, true).show();
                    } else if(chosen == null) {
                        Toasty.error(StudentInteraction.this, "Please Choose a Student.", Toast.LENGTH_LONG, true).show();
                    }
                    else {
                        Student student = chosen.getValue(Student.class);
                        String newName = ((!(name.getText().toString().isEmpty()) && name.getText().toString().charAt(0) != ' ') ? name.getText().toString() : student.getName());
                        String newFatherName = ((!(fatherName.getText().toString().isEmpty()) && fatherName.getText().toString().charAt(0) != ' ') ? fatherName.getText().toString() : student.getFatherName());
                        String newSurname = ((!(surname.getText().toString().isEmpty()) && surname.getText().toString().charAt(0) != ' ') ? surname.getText().toString() : student.getSurname());
                        String newNID = ((!(NID.getText().toString().isEmpty()) && NID.getText().toString().charAt(0) != ' ') ? NID.getText().toString() : student.getNID());

                        myDB.updateStudent(StudentInteraction.this, chosen.getKey(), newName, newFatherName, newSurname, newNID);
                    }
                }
                else {
                    Toasty.error(StudentInteraction.this, "Please Choose a Student.", Toast.LENGTH_LONG, true).show();
                }
            }
        });

        deleteFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosen == null) {
                    Toasty.error(StudentInteraction.this, "Please Choose a Student.", Toast.LENGTH_LONG, true).show();
                }
                else {
                    myDB.removeStudent(StudentInteraction.this, chosen.getKey());
                }
            }
        });
    }

    private void setUI() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosen = myDB.studentList.get(i);
                Student student = chosen.getValue(Student.class);
                name.setHint(student.getName());
                fatherName.setHint(student.getFatherName());
                surname.setHint(student.getSurname());
                NID.setHint(student.getNID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinner() {
        reference = myDB.getReference();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    arrayList.clear();
                    for (DataSnapshot child: snapshot.getChildren()) {
                        Student student = child.getValue(Student.class);
                        String value = student.getID();
                        arrayList.add(value);
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent popUpWindow = new Intent(StudentInteraction.this, PopUpWindow.class);
        switch(item.getItemId()) {
            case R.id.cloud:
                startActivity(popUpWindow);
        }
        return super.onOptionsItemSelected(item);
    }
}
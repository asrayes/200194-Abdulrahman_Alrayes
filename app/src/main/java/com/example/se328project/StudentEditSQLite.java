package com.example.se328project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
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

public class StudentEditSQLite extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Spinner spinner;
    EditText name;
    EditText fatherName;
    EditText surname;
    EditText NID;
    Cursor chosen;
    DatabaseHelper sqlite = new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_sqlite);

        name = findViewById(R.id.studentNameEditSQLite);
        surname = findViewById(R.id.surnameEditSQLite);
        fatherName = findViewById(R.id.fatherNameEditSQLite);
        NID = findViewById(R.id.NIDEditSQLite);
        Button updateSQLite = findViewById(R.id.updateSQLite);
        Button deleteSQLite = findViewById(R.id.deleteSQLite);
        spinner = findViewById(R.id.spinnerSQLite);


        setSpinner();
        setUI();

        updateSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((name.getText().toString().isEmpty() || name.getText().toString().charAt(0) == ' ') && (fatherName.getText().toString().isEmpty() || fatherName.getText().toString().charAt(0) == ' ') &&
                        (surname.getText().toString().isEmpty() || surname.getText().toString().charAt(0) == ' ') && (NID.getText().toString().isEmpty() || NID.getText().toString().charAt(0) == ' ')) {
                    Toasty.error(StudentEditSQLite.this, "Please Fill All Inputs.", Toast.LENGTH_LONG, true).show();
                } else if(chosen == null) {
                    Toasty.error(StudentEditSQLite.this, "Please Choose a Student.", Toast.LENGTH_LONG, true).show();
                }
                else {
                    chosen.moveToFirst();
                    String newName = ((!(name.getText().toString().isEmpty()) && name.getText().toString().charAt(0) != ' ')  ? name.getText().toString() : chosen.getString(1));
                    String newFatherName = ((!(fatherName.getText().toString().isEmpty()) && fatherName.getText().toString().charAt(0) != ' ')  ? fatherName.getText().toString() : chosen.getString(2));
                    String newSurname = ((!(surname.getText().toString().isEmpty()) && surname.getText().toString().charAt(0) != ' ')  ? surname.getText().toString() : chosen.getString(3));
                    String newNID = ((!(NID.getText().toString().isEmpty()) && NID.getText().toString().charAt(0) != ' ')  ? NID.getText().toString() : chosen.getString(4));

                    if(sqlite.updateStudent(Integer.parseInt(chosen.getString(0)), newName, newFatherName, newSurname, newNID)) {
                        Toasty.success(StudentEditSQLite.this, "Student " + newName + " " + newSurname + ". Updated Successfully.", Toast.LENGTH_LONG, true).show();
                    }
                    else {
                        Toasty.error(StudentEditSQLite.this, "Student " + newName + " " + newSurname + ". Update Failure.", Toast.LENGTH_LONG, true).show();
                    }
                }
            }
        });

        deleteSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosen != null) {
                    if (sqlite.deleteStudent(Integer.parseInt(chosen.getString(0)))) {
                        Toasty.success(StudentEditSQLite.this, "Student " + chosen.getString(1) + " " + chosen.getString(3) + ". Deleted Successfully.", Toast.LENGTH_LONG, true).show();
                    } else {
                        Toasty.error(StudentEditSQLite.this, "Student " + chosen.getString(1) + " " + chosen.getString(3) + ". Delete Failure.", Toast.LENGTH_LONG, true).show();
                    }
                }
                else {
                    Toasty.error(StudentEditSQLite.this, "Please Choose a Student.", Toast.LENGTH_LONG, true).show();
                }
            }
        });
    }

    private void setUI() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedID = adapterView.getItemAtPosition(i).toString();
                Log.d("rayes","SQLi : " + selectedID);
                chosen = sqlite.getSpecificStudent(selectedID);
                chosen.moveToFirst();
                Log.d("rayes","SQLi : " + chosen.toString());
                name.setHint(chosen.getString(1));
                fatherName.setHint(chosen.getString(2));
                surname.setHint(chosen.getString(3));
                NID.setHint(chosen.getString(4));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinner() {
        Cursor cursor = sqlite.getListStudent();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(arrayAdapter);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent popUpWindow = new Intent(StudentEditSQLite.this, PopUpWindow.class);
        switch(item.getItemId()) {
            case R.id.cloud:
                startActivity(popUpWindow);
        }
        return super.onOptionsItemSelected(item);
    }
}
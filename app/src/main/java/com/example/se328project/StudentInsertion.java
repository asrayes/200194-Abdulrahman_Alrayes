package com.example.se328project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class StudentInsertion extends AppCompatActivity {

    Spinner spinner;
    String gender;
    EditText dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_insertion);

        EditText id = findViewById(R.id.idInsertion);
        EditText name = findViewById(R.id.nameInsertion);
        EditText fatherName = findViewById(R.id.fatherInsertion);
        EditText surname = findViewById(R.id.surnameInsertion);
        EditText nid = findViewById(R.id.nidInsertion);
        dob = findViewById(R.id.dobInsertion);
        spinner = findViewById(R.id.genderInsertion);
        Button insert = findViewById(R.id.add);
        Firebase myDB = new Firebase();
        ArrayList<DataSnapshot> students = myDB.studentList;

        setSpinner();
        setDOB();

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < students.size(); i++) {
                    Student student = students.get(i).getValue(Student.class);
                    if (id.getText().toString().equals(student.getID())) {
                        Toasty.error(StudentInsertion.this, "The Student ID inserted is already used.", Toast.LENGTH_LONG, true).show();
                        return;
                    }
                }
                    if ((id.getText().toString().isEmpty() || id.getText().toString().charAt(0) == ' ') || (name.getText().toString().isEmpty() || name.getText().toString().charAt(0) == ' ') || (fatherName.getText().toString().isEmpty() || fatherName.getText().toString().charAt(0) == ' ') ||
                            (surname.getText().toString().isEmpty() || surname.getText().toString().charAt(0) == ' ') || (nid.getText().toString().isEmpty() || nid.getText().toString().charAt(0) == ' ') ||
                            (dob.getText().toString().isEmpty() || dob.getText().toString().charAt(0) == ' ') || (gender.isEmpty() || gender.charAt(0) == ' ')) {
                        Toasty.error(StudentInsertion.this, "Please Fill All Inputs.", Toast.LENGTH_LONG, true).show();
                    }
                    else if(id.getText().toString().length() != 6) {
                        Toasty.error(StudentInsertion.this, "Student ID must be 6 digits Long.", Toast.LENGTH_LONG, true).show();
                    } else {
                        String newID = id.getText().toString();
                        String newName = name.getText().toString();
                        String newFatherName = fatherName.getText().toString();
                        String newSurname = surname.getText().toString();
                        String newNID = nid.getText().toString();
                        String newDOB = dob.getText().toString();
                        String newGender = gender;
                        myDB.insertStudent(StudentInsertion.this, newID, newName, newFatherName, newSurname, newNID, newDOB, newGender);
                    }
                }
        });
    }

    private void setDOB() {
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(StudentInsertion.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        Log.e("Date Selected", "Month: " + selectedMonth + " Day: " + selectedDay + " Year: " + selectedYear);
                        dob.setText(selectedMonth + "/" + selectedDay + "/" + selectedYear);
                    }
                },year, month, day);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();
            }
        });
    }

    private void setSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        gender = adapterView.getItemAtPosition(i).toString();
                        break;
                    case 1:
                        gender = adapterView.getItemAtPosition(i).toString();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent popUpWindow = new Intent(StudentInsertion.this, PopUpWindow.class);
        switch(item.getItemId()) {
            case R.id.cloud:
                startActivity(popUpWindow);
        }
        return super.onOptionsItemSelected(item);
    }
}
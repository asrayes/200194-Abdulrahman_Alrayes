package com.example.se328project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class StudentDetailsViewSQLite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_view_sqlite);

        TextView id = findViewById(R.id.studentIDViewSQLite);
        TextView name = findViewById(R.id.studentNameViewSQLite);
        TextView fatherName = findViewById(R.id.studentFatherNameViewSQLite);
        TextView surname = findViewById(R.id.studentSurnameViewSQLite);
        TextView nid = findViewById(R.id.studentNIDViewSQLite);
        TextView dob = findViewById(R.id.studentDOBViewSQLite);
        TextView gender = findViewById(R.id.studentGenderViewSQLite);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        DatabaseHelper myDB = new DatabaseHelper(this);

                    Cursor chosen = myDB.getSpecificStudent(sharedPreferences.getString("id", null));
                    chosen.moveToFirst();
                    id.setText(chosen.getString(0));
                    name.setText(chosen.getString(1));
                    fatherName.setText(chosen.getString(2));
                    surname.setText(chosen.getString(3));
                    nid.setText(chosen.getString(4));
                    dob.setText(chosen.getString(5));
                    gender.setText(chosen.getString(6));
                    Toasty.info(StudentDetailsViewSQLite.this,  chosen.getString(1) + " " + chosen.getString(3), Toast.LENGTH_LONG, true).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent popUpWindow = new Intent(StudentDetailsViewSQLite.this, PopUpWindow.class);
        switch(item.getItemId()) {
            case R.id.cloud:
                startActivity(popUpWindow);
        }
        return super.onOptionsItemSelected(item);
    }
}
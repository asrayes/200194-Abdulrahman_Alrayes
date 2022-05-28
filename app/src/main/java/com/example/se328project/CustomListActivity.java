package com.example.se328project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomListActivity extends AppCompatActivity {

    DatabaseReference reference;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Firebase myDB = new Firebase();
    ArrayList<DataSnapshot> studentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        reference = myDB.getReference();
        listView = findViewById(R.id.studentList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        setListView();
        onListItemClick();
    }

    protected void onListItemClick() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int chosen = Integer.parseInt(adapterView.getItemAtPosition(i).toString().substring(0,6));
                for (DataSnapshot child : studentList) {
                    Student student = child.getValue(Student.class);
                    if(chosen == Integer.parseInt(student.getID())) {
                        chosen = Integer.parseInt(child.getKey());
                    }
                }
                System.out.println(chosen);
                editor.putString("ref", String.valueOf(chosen));
                editor.commit();
                startActivity(new Intent(CustomListActivity.this, StudentDetailsView.class));
            }
        });
    }

    private void setListView() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    arrayList.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        studentList.add(child);
                        Student student = child.getValue(Student.class);
                        String value = student.getID();
                        value += " - " + student.getName();
                        value += " " + student.getSurname();
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
        Intent popUpWindow = new Intent(CustomListActivity.this, PopUpWindow.class);
        switch(item.getItemId()) {
            case R.id.cloud:
                startActivity(popUpWindow);
        }
        return super.onOptionsItemSelected(item);
    }
}

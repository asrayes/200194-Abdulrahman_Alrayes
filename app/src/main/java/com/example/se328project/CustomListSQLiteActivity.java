package com.example.se328project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class CustomListSQLiteActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    DatabaseHelper myDB = new DatabaseHelper(this);
    Cursor list;
    Cursor chosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_sqlite);

        listView = findViewById(R.id.studentListSQLite);
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
                chosen = myDB.getSpecificStudent(adapterView.getItemAtPosition(i).toString().substring(0,6));
                chosen.moveToFirst();
                System.out.println(chosen.getString(0));
                editor.putString("id", chosen.getString(0));
                editor.commit();
                startActivity(new Intent(CustomListSQLiteActivity.this, StudentDetailsViewSQLite.class));
            }
        });
    }

    private void setListView() {
        list = myDB.getListStudent();
        list.moveToFirst();
        arrayList.clear();
        if (list.getCount() > 0) {
            do {
                String value = list.getString(0);
                value += " - " + list.getString(1);
                value += " " + list.getString(3);
                arrayList.add(value);
            } while (list.moveToNext());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent popUpWindow = new Intent(CustomListSQLiteActivity.this, PopUpWindow.class);
        switch(item.getItemId()) {
            case R.id.cloud:
                startActivity(popUpWindow);
        }
        return super.onOptionsItemSelected(item);
    }
}

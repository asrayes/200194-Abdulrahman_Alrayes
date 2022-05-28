package com.example.se328project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Firebase extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static int count;
    public ArrayList<DataSnapshot> studentList = new ArrayList<>();

    public Firebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Students");
        setCount();
        setStudentArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void insertStudent(Context context, String ID, String name, String fatherName, String surname, String NID, String DOB, String gender) {
        Student student = new Student(ID, name, fatherName, surname, NID, DOB, gender);
        reference.child(String.valueOf(count)).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("rayes", "Insertion Success " + count);
                Toasty.success(context, "Student " + name + " " + surname + ". Insertion Successful.", Toast.LENGTH_LONG, true).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("rayes", "Insertion Failure: " + e);
                Toasty.error(context, "Insertion Failure.", Toast.LENGTH_LONG, true).show();
            }
        });
    }

    public void updateStudent(Context context,String ref, String newName, String newFatherName, String newSurname, String newNID) {
        HashMap<String, Object> update = new HashMap<>();
        update.put("name",newName);
        update.put("fatherName",newFatherName);
        update.put("surname",newSurname);
        update.put("nid",newNID);

        reference.child(ref).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("rayes","Update Success");
                Toasty.success(context, "Student " + newName + " " + newSurname + ". Update Successful.", Toast.LENGTH_LONG, true).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("rayes","Update Failure: " + e);
                Toasty.error(context, "Update Failure.", Toast.LENGTH_LONG, true).show();
            }
        });
    }

    public void removeStudent(Context context, String ref) {
        reference.child(ref).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("rayes","Remove Success");
                count--;
                Toasty.success(context, "Student. Removed Successfully.", Toast.LENGTH_LONG, true).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("rayes","Remove Failure: " + e);
                Toasty.success(context, "Student. Removed Failure.", Toast.LENGTH_LONG, true).show();
            }
        });
    }

    public void getStudent(String ref) {
        reference.child(ref).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot data: snapshot.getChildren()) {
                        Log.d("rayes", data.getKey() + ": " + data.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("rayes","Get Singular Failure: " + error);
            }
        });
    }

    private void setStudentArray() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    studentList.clear();
                    for (DataSnapshot child: snapshot.getChildren()) {
                        studentList.add(child);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public static int getCount() {
        return count;
    }

    private void setCount() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int high = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (Integer.parseInt(child.getKey()) > high) {
                        high = Integer.parseInt(child.getKey());
                    }
                }
                count = high + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
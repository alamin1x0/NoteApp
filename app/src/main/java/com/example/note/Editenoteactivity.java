package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Editenoteactivity extends AppCompatActivity {

    Intent data;
    private EditText editTitleNote, editContentofNote;
    FloatingActionButton saveEditNote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editenoteactivity);

        editTitleNote = findViewById(R.id.editTitleNote);
        editContentofNote = findViewById(R.id.editContentofNote);
        saveEditNote = findViewById(R.id.saveEditNote);

        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbarofNoteEdit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Save Button Click", Toast.LENGTH_SHORT).show();

                String newtitle = editTitleNote.getText().toString();
                String newcontent = editContentofNote.getText().toString();

                if (newtitle.isEmpty() || newcontent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Something is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", newtitle);
                    note.put("content", newcontent);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Note is Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Editenoteactivity.this,notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To Update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        String notetitle = data.getStringExtra("title");
        String newcontent = data.getStringExtra("content");
        editTitleNote.setText(notetitle);
        editContentofNote.setText(newcontent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
}
package com.k2.notetaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.k2.notetaker.RoomDB.*;
import com.k2.notetaker.RoomDB.NoteModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditNote extends AppCompatActivity implements View.OnClickListener {
    AppDatabase mydb;
    NoteModel currentNote;

    EditText ETTitle;
    EditText ETDesc;
    EditText ETUpdatedBy;
    TextView results;
    TextView TVTags;
    TextView createDate;
    TextView modifiedDate;
    Button bSave;
    Button bTag;
    Button bCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_note);
        mydb = DatabaseHelperSingleton.createAppDatabase(getApplicationContext());

        ETTitle = (EditText) findViewById(R.id.ETTitle);
        ETDesc = (EditText) findViewById(R.id.ETDesc);
        //ETTag = (EditText) findViewById(R.id.ETTag);
        ETUpdatedBy = (EditText) findViewById(R.id.ETUpdatedBy);
        results = (TextView) findViewById(R.id.results);
        TVTags = (TextView) findViewById(R.id.TVTags);
        createDate = (TextView) findViewById(R.id.createdate);
        modifiedDate = (TextView) findViewById(R.id.modifieddate);
        bTag = (Button) findViewById(R.id.bAddTag);
            bTag.setOnClickListener(this);
        bSave = (Button) findViewById(R.id.bSave);
            bSave.setOnClickListener(this);
        bCancel = (Button) findViewById(R.id.bCancel);
            bCancel.setOnClickListener(this);

        int id = getIntent().getExtras().getInt("noteID");
        if (id > 0 ) {
            currentNote = mydb.getNoteById(id);
        }
        else {
            currentNote = new NoteModel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bAddTag:
                Intent myintent = new Intent(EditNote.this, EditTags.class);
                buildNoteFromScreen();
                Bundle basket = new Bundle();
                basket.putIntegerArrayList("tagIDs",currentNote.tagIDs);
                myintent.putExtras(basket);

                startActivityForResult(myintent, 0);
                break;
            case R.id.bSave:
                buildNoteFromScreen();
                mydb.saveNote(currentNote);
            case R.id.bCancel:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK) {
            Bundle basket = data.getExtras();
            currentNote.tagIDs = basket.getIntegerArrayList("tagIDs");
            updateTagNames(currentNote.tagIDs);
        }
    }

    private void updateView() {
        ETTitle.setText(currentNote.noteTitle);
        ETDesc.setText(currentNote.noteDescription);
        ETUpdatedBy.setText(currentNote.updatedBy);
        createDate.setText  ("created:   " + currentNote.createDate);
        modifiedDate.setText("modified: " + currentNote.modifiedDate);
        if(currentNote.tagIDs.size() > 0) {
            updateTagNames(currentNote.tagIDs);
        }
    }

    private void updateTagNames(ArrayList<Integer> tagIDs) {
        String s= "";
        ArrayList<String> list = mydb.getTagNamesByTagIDs(tagIDs);
        for (String i : list){
            s += i + ", ";
        }
        s = NotesHelper.removeComma(s);

        TVTags.setText(s);

    }
    private void buildNoteFromScreen(){
        currentNote.noteTitle = ETTitle.getText().toString();
        currentNote.noteDescription = ETDesc.getText().toString();
        currentNote.updatedBy = ETUpdatedBy.getText().toString();
        currentNote.modifiedDate = "todo";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentNote.modifiedDate = sdf.format(Calendar.getInstance().getTime());

    }

}


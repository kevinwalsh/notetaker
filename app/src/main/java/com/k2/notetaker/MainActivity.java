package com.k2.notetaker;

import android.content.Intent;
//  import android.support.v7.app.AppCompatActivity;            // not applicable w/ androidx
                    // see https://stackoverflow.com/questions/30803405/cannot-resolve-symbol-appcompatactivity-support-v7-libraries-arent-recognized
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.bLogin);
        b.setOnClickListener(this);
        Button bClearData = (Button) findViewById(R.id.bClearData);
        bClearData.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogin:
                Intent myintent = new Intent(MainActivity.this, NotesList.class);
                finish();
                startActivity(myintent);
                break;
            case R.id.bClearData:
                this.deleteDatabase("notesRoom.db");
                            // manual way: in console/cmd/terminal, do "adb shell",
                                    // and then run sqlite3 on folder "data/data/<myPackage>/databases"
                NotesHelper.showToast(getBaseContext(),"Database reset");
        }
    }

}

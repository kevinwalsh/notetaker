package com.k2.notetaker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.k2.notetaker.RoomDB.AppDatabase;
import com.k2.notetaker.RoomDB.NoteTagModel;
import com.k2.notetaker.RoomDB.TagModel;

import java.util.ArrayList;

/**
 * Created by K2 on 23/03/2019.
 */

public class EditTags extends ListActivity implements View.OnClickListener {

    AppDatabase db;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayList<Integer> listItems2=new ArrayList<Integer>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    ListView myList;
    Button bFinish;
    Button bAddNewTag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelperSingleton.createAppDatabase(getApplicationContext());

        setContentView(R.layout.edit_tags);
        bFinish = (Button) findViewById(R.id.bFinish);
            bFinish.setOnClickListener(this);
        bAddNewTag= (Button) findViewById(R.id.bAddNewTag);
            bAddNewTag.setOnClickListener(this);

        myList = getListView();
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("test long click delete for item " + position + " id " + id);
                buildPopup(position, listItems2.get(position).intValue(),listItems.get(position));
                return true;
            }
        });
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
       getTagsFromDB();
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                listItems);

        Intent intent = getIntent();
        ArrayList<Integer> selected = new ArrayList<Integer>();
        selected = intent.getIntegerArrayListExtra("tagIDs");

        setListAdapter(adapter);
        if(selected != null){
            for(int i= 0; i < listItems2.size(); i++) {
                if (selected.contains(listItems2.get(i))) {
                    getListView().setItemChecked(i,true);
                }
            }
        }
    }

    private void getTagsFromDB() {
        ArrayList<TagModel> tags= db.getAllTags();
        ArrayList<Pair<Integer,String>> taglist = new ArrayList<Pair<Integer,String>>();
        for(TagModel tm : tags){
            taglist.add(new Pair<Integer, String> (tm.getId(), tm.getTagName()));
        }

        for (Pair x: taglist) {
//            taglist.add(x.second.toString());
            listItems2.add(Integer.parseInt(x.first.toString()));
            listItems.add(x.second.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bAddNewTag:
                openNewTagPopup();
                break;
            case R.id.bFinish:
                ArrayList<Integer> tagIDs = getCheckedTagIDs();
                Intent a = new Intent();
                Bundle b = new Bundle();
                b.putIntegerArrayList("tagIDs",tagIDs);
                a.putExtras(b);
                setResult(RESULT_OK, a);
                finish();
        }
    }
    protected ArrayList<Integer> getCheckedTagIDs(){
        ArrayList<Integer> tagIDs = new ArrayList<Integer>();
        SparseBooleanArray checked = getListView().getCheckedItemPositions();
        for (int i = 0; i < listItems2.size(); i++) {
            if (checked.valueAt(i)) {
                tagIDs.add(listItems2.get(checked.keyAt(i)));       // doublecheck, winging it
            }
        }
        return tagIDs;
    }



    protected void openNewTagPopup(){
        final EditText et = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(et)
                .setTitle("Add New Tag")
                .setPositiveButton("ADD",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        popupAddTagButton(et);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et.setText("");
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }
    protected void popupAddTagButton(EditText et) {
        String input = et.getText().toString();
        if(NotesHelper.validateAddTag(input, 1)) {
            TagModel tag = new TagModel();
            tag.setTagName(input);
            tag= db.saveTag(tag);
            listItems.add(tag.getTagName());
            listItems2.add(tag.getId());
            getListView().setItemChecked(listItems2.size()-1, true);      // auto-set as checked
        }
        else {
            NotesHelper.showToast(getBaseContext(),"ERROR: Tag text too short");
        }
    }

    protected void buildPopup(final int position, final int id, String title){
        String titleStart = title.length() < 11 ? title : title.substring(0,10)+ "...";
        ArrayList<NoteTagModel> notes = db.getNoteTagsUsingTag(id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(notes.size() + " note useages: Confirm Delete? (\"" + titleStart + "\") ")
                .setPositiveButton("DELETE",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int selectedId) {
                        deleteTagFromDB(position, id);
         }
                })
                .setNegativeButton("Cancel",null);
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void deleteTagFromDB(int position, int id){
        if(getListView().isItemChecked(position)) {
            getListView().setItemChecked(position, false);
        }
        db.deleteNoteTags(db.getNoteTagsUsingTag(id));

        int success = db.deleteTag(db.getTagById(id));
        if (success > 0) {
            shiftCheckedItems(position);
            listItems2.remove(position);
            listItems.remove(position);
            adapter.notifyDataSetChanged();
        }
        else {
            NotesHelper.showToast(getBaseContext(), "Error deleting tag, is in use by a note");
        }
    }

    private void shiftCheckedItems(int position){
        for(int i = position; i < listItems2.size() - 1; i++){
            getListView().setItemChecked(i, getListView().isItemChecked(i+1));
        }
    }

}

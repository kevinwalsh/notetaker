package com.k2.notetaker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.k2.notetaker.RoomDB.*;
import com.k2.notetaker.RoomDB.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class NotesList extends ListActivity{

	ArrayList<Pair<Integer,String>> notesList;
	ArrayList<String> notesTitles;
	ArrayAdapter adapter;
	AppDatabase db2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_list_layout);
		TextView header = (TextView) findViewById(R.id.ListActivityHeader);
		header.setText("Notes List");
		notesList = new ArrayList<Pair<Integer, String>>();
		notesTitles = new ArrayList<>();
		db2 = DatabaseHelperSingleton.createAppDatabase(getApplicationContext());
								// bad workaround, "allow this on main thread"

		ListView lv= getListView();
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("test long click delete for item " + position + " id " + id);
				buildPopup(position, notesList.get(position).first.intValue(),notesTitles.get(position));
				return true;
			}
		});
		adapter = new ArrayAdapter<String>(NotesList.this, android.R.layout.simple_list_item_1, notesTitles);
		setListAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadNotesAsync();
	}

	protected void buildPopup(int position, final int id, String title){
		String titleStart = title.length() < 11 ? title : title.substring(0,10)+ "...";
		final int selectedRow = position;
		final long selectedID = (long) id;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Confirm Delete? (\"" + titleStart + "\")")
						.setPositiveButton("DELETE",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int selectedId) {
						db2.deleteNote(db2.getNoteById(id));
						notesList.remove(selectedRow);
						notesTitles.remove(selectedRow);
						adapter.notifyDataSetChanged();
						System.out.println("test delete success");
					}
				})
				.setNegativeButton("Cancel",null);
		AlertDialog ad = builder.create();
		ad.show();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
			Intent myintent = new Intent(NotesList.this, EditNote.class);
			myintent.putExtra("noteID", notesList.get(position).first);
			startActivity(myintent);
	}


	/*
	@Override
	public boolean onCreateOptionsMenu(android.view.NotesList menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup= getMenuInflater();
		blowup.inflate(R.menu.dropdownmenu, menu);
		return true;
	}
*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.aboutUs :
			Intent i = new Intent("com.k2.notetaker.ABOUTUS");
			startActivity(i);
			break;
		case R.id.preferences :	
			Intent i2 = new Intent("com.k2.notetaker.PREFS");
			startActivity(i2);
			break;
		case R.id.exitapp :
			finish();
			break;
		}
		return false;
	}

	private void loadNotesAsync(){
		new AsyncTask<Void,Void,List<NoteModel>>() {
			@Override
			protected List<NoteModel> doInBackground(Void... params){
				return db2.getNoteDAO().getAllNotes();
			}

			@Override
			protected void onPostExecute(List<NoteModel> notes){
				notesList.clear();
				notesTitles.clear();	// need to clear for when page refreshes.

				for(NoteModel nm : notes){
					notesList.add(new Pair<Integer, String> (nm.getId(), nm.getTitle()));
				}

				notesList.add(0, new Pair<Integer, String>(0, "Add New Note"));
				for (Pair x: notesList) {
					notesTitles.add(x.second.toString());
				}
				adapter.notifyDataSetChanged();
			}
		}.execute();
	}

}

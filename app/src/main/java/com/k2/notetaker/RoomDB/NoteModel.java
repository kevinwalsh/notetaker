package com.k2.notetaker.RoomDB;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "notes")
public class NoteModel {

    @PrimaryKey(autoGenerate = true)
    public int noteID;

    @ColumnInfo(name = "note_title")
    public String noteTitle;

    @ColumnInfo (name = "note_description")
    public String noteDescription;

    @Ignore
    public ArrayList<Integer> tagIDs;

    //@Embedded
    //public ArrayList<NoteTagModel> tags;


    @ColumnInfo (name = "note_create_date")
    public String createDate;

    @ColumnInfo (name = "note_modified_date")
    public String modifiedDate;

    @ColumnInfo (name = "note_updatedby")
    public String updatedBy;

    @ColumnInfo (name = "note_createdby")       // must match altered column in migration version 2
    public String createdBy;

    public int getId(){ return noteID; }
    public void setId(int id){
        this.noteID = id;
    }

    public String getTitle(){
        return noteTitle;
    }
    public void setTitle(String title){
        this.noteTitle = title;
    }

    public String getDescription(){
        return noteDescription;
    }
    public void setDescription(String desc){
        this.noteDescription = desc;
    }

    public String getCreateDate(){
        return createDate;
    }
    public void setCreateDate(String date){
        this.noteDescription = date;
    }

    public String getModifiedDate(){
        return modifiedDate;
    }
    public void setModifiedDate(String date){
        this.modifiedDate= date;
    }

     public NoteModel() {
        this.noteID = 0;
        this.noteTitle = "title";
        this.noteDescription = "";
        tagIDs = new ArrayList<Integer>();
  //    this.tags = new ArrayList<NoteTagModel>();      //TODO remove above 2 again
        this.createDate = "n/a";
        this.modifiedDate = "n/a";
         this.updatedBy = "k";
         this.createdBy = "k";
    }

}

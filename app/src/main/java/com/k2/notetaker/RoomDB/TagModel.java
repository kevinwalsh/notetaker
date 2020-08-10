package com.k2.notetaker.RoomDB;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags")
public class TagModel {                     // not configured yet

    @PrimaryKey(autoGenerate = true)
    public int tagID;
    public String tagName;
  //  @Relation(parentColumn = tagID, entityColumn = tagID, entity = NoteTagModel.class)
  //  public ArrayList<NoteModel> notes;

    public int getId(){
        return tagID;
    }
    public void setId(int id){
        this.tagID= id;
    }

    public String getTagName(){
        return tagName;
    }
    public void setTagName(String name){
        this.tagName= name;
    }


}

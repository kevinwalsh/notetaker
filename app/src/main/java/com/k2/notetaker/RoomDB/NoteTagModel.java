package com.k2.notetaker.RoomDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "notestags",
    foreignKeys = {
        @ForeignKey(entity = NoteModel.class,
            parentColumns = "noteID",
            childColumns = "noteID",
            onDelete = CASCADE),
        @ForeignKey(entity = TagModel.class,
            parentColumns = "tagID",
            childColumns = "tagID",
            onDelete = CASCADE)
        })

public class NoteTagModel {         // not configured yet

    @PrimaryKey(autoGenerate = true)
    public int noteTagID;
    public final int noteID;
    public final int tagID;
    public int priority;

    public int getId(){return noteTagID;    }
    public void setId(int id){this.noteTagID = id;    }

    public int getNoteId(){ return noteID;    }
    // public void setNoteId(int id){this.noteID = id;    }

    public int getTagId(){        return tagID;    }
    // public void setTagId(int id){ this.tagID = id;    }

    public int getPriority(){ return priority; }
    public void setPriority(int num){ this.priority = num; }

    public NoteTagModel(final int noteID, final int tagID, int priority){
        this.noteID = noteID;
        this.tagID = tagID;
        this.priority = priority;
    }

}

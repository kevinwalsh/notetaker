package com.k2.notetaker.RoomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {NoteModel.class, NoteTagModel.class, TagModel.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDAO getNoteDAO();
    public static final String DB_NAME = "notesRoom.db";

    public void addnote(NoteModel nm) {
        getNoteDAO().addNote(nm);
    }

    public ArrayList<NoteModel> getAllNotes() {
        return (ArrayList) getNoteDAO().getAllNotes();
    }

    public ArrayList<Integer> getTagIDsByNoteID(int noteId) {
        return (ArrayList) getNoteDAO().getTagIDsByNoteId(noteId);
    }
    public ArrayList<NoteTagModel> getNoteTagsUsingTag(int tagid){
        return (ArrayList) getNoteDAO().getNoteTagsUsingTag(tagid);
    }

    public ArrayList<String> getTagNamesByTagIDs(ArrayList<Integer> tagids) {
        return (ArrayList) getNoteDAO().getTagNamesByTagIDs(tagids);
    }

    public TagModel getTagById(int tagid) {
        return getNoteDAO().getTagById(tagid);
    }

    public int deleteNote(NoteModel n){
        List<NoteTagModel> nt = getNoteDAO().getNoteTagsByNoteId(n.noteID);
        getNoteDAO().deleteNoteTags(nt);
        return getNoteDAO().deleteNote(n);
    }

    public int deleteTag(TagModel tag) {
        return getNoteDAO().deleteTag(tag);
    }

    public int deleteNoteTags(List<NoteTagModel> notetags) {
        return getNoteDAO().deleteNoteTags(notetags);
//        return getNoteDAO().deleteNoteTag(getNoteDAO().getSingleNoteTag(notetag));
    }

    public NoteModel getNoteById(int noteId) {
        NoteModel n =  getNoteDAO().getNoteById(noteId);
        n.tagIDs = (ArrayList<Integer>) getNoteDAO().getTagIDsByNoteId(n.noteID);
        return n;
    }

    public void saveNote(NoteModel n) {             // need to test
        if (n.noteID != 0) {
            getNoteDAO().updateNote(n);
            List nt = getNoteDAO().getNoteTagsByNoteId(n.noteID);
            getNoteDAO().deleteNoteTags(nt);
        } else {
            n.noteID= (int) getNoteDAO().addNote(n);
        }
        for(Integer i : n.tagIDs){
            // would rather just auto insert-or-update.... might be possible. will need to when priorities are implemented
            // https://stackoverflow.com/questions/3634984/insert-if-not-exists-else-update
               // int t =  getNoteDAO().addNoteTag(n.noteID, i,1);          //apparently cant pass "primitive" types to Room
               NoteTagModel nm = new NoteTagModel(n.noteID,i,1);
                long t = getNoteDAO().addNoteTag(nm);
                if(t!=1) {
                   // getNoteDAO().updateNoteTag(n.noteID, i, 1);
                    System.out.println("problem detected, somethingsomething vicky somethingsomething");
               }
        }
    }

    public ArrayList<TagModel> getAllTags() {
        return (ArrayList) getNoteDAO().getAllTags();
    }


    public TagModel saveTag(TagModel t) {
        if (t.tagID != 0) {
            getNoteDAO().updateTag(t);
        } else {
            t.setId((int) getNoteDAO().addTag(t));
        }
        return t;
    }
/*
    public static void destroyInstance() {
        INSTANCE = null;
    } */
}
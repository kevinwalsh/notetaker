package com.k2.notetaker.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {

    // stackoverflow, linking/nesting classes in ROOM
    // https://stackoverflow.com/questions/48295595/how-to-query-nested-embeded-objects-using-room-persistance-library-in-android
    // User -> Addr -> Location
    // ugh cant just easily link together entities. BUT could create some sort of a "view" data object,
    // using @Embedded & @Relation, and can combine entities.
    // but whatever, I dont actually need joined data objects, just a list of tagIDs

    @Query("SELECT * FROM NOTES")
    List<NoteModel> getAllNotes();

    // @Query("SELECT * FROM NOTES INNER JOIN NOTESTAGS ON NOTES.NOTEID = NOTESTAGS.NOTEID  WHERE NOTES.NOTEID =:noteId ")
    @Query("SELECT * FROM NOTES WHERE NOTEID =:noteId")
    NoteModel getNoteById(final int noteId);

//    @Query("SELECT noteID, note_title FROM NOTES")
//    List<Pair<Integer,String>>  getAllNoteTitlesAndIDs();         // "not sure how to convert cursor to this return type"

    @Query("SELECT * FROM NOTESTAGS WHERE NOTEID=:noteId")          // full Notetag
    List<NoteTagModel> getNoteTagsByNoteId(final int noteId);

    @Query("SELECT * FROM NOTESTAGS WHERE NOTETAGID=:noteTagId")          // full Notetag
    NoteTagModel getSingleNoteTag(int noteTagId);


    @Query("SELECT * FROM NOTESTAGS WHERE TAGID=:tagId")
    List<NoteTagModel> getNoteTagsUsingTag(final int tagId);

    @Query("SELECT TAGID FROM NOTESTAGS WHERE NOTEID=:noteId")      // just TagID
    List<Integer> getTagIDsByNoteId(final int noteId);

    @Query("SELECT TAGNAME FROM TAGS WHERE TAGID IN (:tagids)")
    List<String> getTagNamesByTagIDs(List<Integer> tagids);

    @Query("SELECT * FROM TAGS")
    List<TagModel> getAllTags();

    @Query("SELECT * FROM TAGS WHERE TAGID = (:tagid)")
    TagModel getTagById(final int tagid);

    @Insert
    public long addNote(NoteModel note);

    @Update
    public void updateNote(NoteModel note);

    @Insert
    public long addTag(TagModel tag);

    @Update
    public void updateTag(TagModel tag);

    @Insert(onConflict = OnConflictStrategy.IGNORE)             // returns -1 if error? possibly 3 ("default") if success?
   // public int addNoteTag(int noteId, int tagId, int priority);
    public long addNoteTag(NoteTagModel nt);

    @Update
    public void updateNoteTag(NoteTagModel nt);

    @Delete
    public int deleteTag(TagModel tag);


    @Delete
    public int deleteNote(NoteModel note);

    @Delete
    public int deleteNoteTag(NoteTagModel notetag);

    @Delete
    public int deleteNoteTags(List<NoteTagModel> notetags);

}


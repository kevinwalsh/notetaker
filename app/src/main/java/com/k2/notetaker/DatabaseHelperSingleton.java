package com.k2.notetaker;

import android.content.Context;

/*
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
*/;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Room;
import androidx.room.migration.Migration;

import com.k2.notetaker.RoomDB.AppDatabase;

/**
 * Created by K2 on 05/05/2019.
 */

 final class DatabaseHelperSingleton {

    public static AppDatabase createAppDatabase(Context context){
       return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
               .addMigrations(FROM_1_TO_2)
               .allowMainThreadQueries().build();
    }
    static final Migration FROM_1_TO_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Notes ADD COLUMN note_createdby TEXT");
        }
    };

}

package com.k2.notetaker;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by K2 on 05/05/2019.
 */

public class NotesHelper {

    public static boolean validateAddTag(String input, int minlength){
        if (input.length() > minlength){
            // TODO add more validation here, e.g. check DB for duplicate note entries
            return true;
        }
        else {return false;}
    }

    public static String removeComma(String s){

        if(s.length() > 0) {
            return s.substring(0, s.length() -2);      //remove last comma
        }
        else return s;
    }

    public static void showToast(Context c, String s){
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }
}

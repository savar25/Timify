package com.example.timify;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "days2.db";
    public static final String MONDAY = "mon";
    public static final String  TUESDAY = "tue";
    public static final String WEDNESDAY = "wed";
    public static final String THURSDAY = "thu";
    public static final String FRIDAY = "fri";
    public static final String SATURDAY = "sat";
    public static final String SUN = "sun";
    public static final String TABLE_NAME = "User_Table0";
    public static final String TIME="time";
    public static final String ID="id";
    public static final Integer DatabaseVersion = 1;
    private static final String TAG = "UserDets";
    static int id=1;

    public database(@Nullable Context context) {
        super(context, DATABASE_NAME,null,DatabaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+ "("+TIME+" varchar,"+SUN+" integer,"+ MONDAY+" integer,"+TUESDAY+" integer,"+WEDNESDAY+" integer,"+THURSDAY+" integer,"+FRIDAY+" integer,"+SATURDAY+" integer)");  }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE "+ TABLE_NAME);
    }

    public boolean add(alarmer a){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(TIME,a.getTime());
        contentValues.put(SUN,a.getDayList()[6]?1:0);
        contentValues.put(MONDAY,a.getDayList()[0]?1:0);
        contentValues.put(TUESDAY,a.getDayList()[1]?1:0);
        contentValues.put(WEDNESDAY,a.getDayList()[2]?1:0);
        contentValues.put(THURSDAY,a.getDayList()[3]?1:0);
        contentValues.put(FRIDAY,a.getDayList()[4]?1:0);
        contentValues.put(SATURDAY,a.getDayList()[5]?1:0);

        id++;
        db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return true;
    }

    public ArrayList<alarmer> getVals(){
        SQLiteDatabase database=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME;
        Cursor c=database.rawQuery(query,null);
        ArrayList<alarmer> alarmerArrayList=new ArrayList<>();
        while(c.moveToNext()){
            alarmerArrayList.add(new alarmer(c.getString(c.getColumnIndex(TIME)),new Boolean[]{c.getInt(c.getColumnIndex(MONDAY))==1,c.getInt(c.getColumnIndex(TUESDAY))==1,c.getInt(c.getColumnIndex(WEDNESDAY))==1,c.getInt(c.getColumnIndex(THURSDAY))==1,c.getInt(c.getColumnIndex(FRIDAY))==1,c.getInt(c.getColumnIndex(SATURDAY))==1,c.getInt(c.getColumnIndex(SUN))==1}));
        }
        return alarmerArrayList;
    }

    public boolean del(String time){
        if(checker(time)) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, TIME + " = '" + time + "'", null);
            db.close();
        }
            return true;


    }

    public boolean checker(String time){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME;
        Cursor c=db.rawQuery(query,null);
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex(TIME)).equals(time) && c.getInt(c.getColumnIndex(MONDAY))==0&&c.getInt(c.getColumnIndex(TUESDAY))==0&c.getInt(c.getColumnIndex(WEDNESDAY))==0&&c.getInt(c.getColumnIndex(THURSDAY))==0&&c.getInt(c.getColumnIndex(FRIDAY))==0&&c.getInt(c.getColumnIndex(SATURDAY))==0 && c.getInt(c.getColumnIndex(SUN))==0){
                return true;
            }
        }
        return false;
    }
}

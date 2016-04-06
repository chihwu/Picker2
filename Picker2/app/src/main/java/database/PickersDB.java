package database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.Cursor;

import dataObjects.User;

/**
 * Created by ChihWu on 4/5/16.
 */
public class PickersDB {

    public static final String DB_NAME = "pickers2.db";
    public static final int DB_VERSION = 2;

    public static final String USER_TABLE = "user";

    public static final String USER_ID = "_id";
    public static final int USER_ID_COL = 0;

    public static final String USER_NAME = "user_name";
    public static final int USER_NAME_COL = 1;

    public static final String USER_FIRSTNAME = "user_firstname";
    public static final int USER_FIRSTNAME_COL = 2;

    public static final String USER_LASTNAME = "user_lastname";
    public static final int USER_LASTNAME_COL = 3;

    public static final String USER_PASSWORD = "user_password";
    public static final int USER_PASSWORD_COL = 4;

    public static final String USER_DOB = "user_dob";
    public static final int USER_DOB_COL = 5;

    public static final String USER_EMAIL = "user_email";
    public static final int USER_EMAIL_COL = 6;

    public static final String USER_INTRO = "user_intro";
    public static final int USER_INTRO_COL = 7;


    public static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " ("+
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_NAME + " TEXT NOT NULL, "+
            USER_FIRSTNAME + " TEXT, "+
            USER_LASTNAME + " TEXT,  "+
            USER_PASSWORD + " TEXT NOT NULL, "+
            USER_EMAIL + " TEXT NOT NULL UNIQUE, "+
            USER_DOB + " TEXT, "+
            USER_INTRO + " TEXT);";

    public static final String DROP_USER_TABLE =
            "DROP TABLE IF EXISTS "+USER_TABLE;


    private static class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_USER_TABLE);

            db.execSQL("INSERT INTO user VALUES(1,'Raymond','Ray','Wu','1234', 'chihwu@bu.edu','11/23/1990','Hi, this is Ray.')");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(DROP_USER_TABLE);
            onCreate(db);
        }

    }


    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public PickersDB(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    private void openReadableDB(){
        db = dbHelper.getReadableDatabase();
    }

    private void openWritableDB()
    {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB()
    {
        if(db != null)
        {
            db.close();
        }
    }


    public User getUser(int id){
        String where = USER_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(USER_TABLE, null, where, whereArgs, null, null, null);

        cursor.moveToFirst();
        User user = getUserFromCursor(cursor);
        if(cursor != null)
        {
            cursor.close();
        }

        this.closeDB();

        return user;
    }

    public User getUserByEmail(String email){
        String where = USER_EMAIL + "= ?";
        String[] whereArgs = {email};

        this.openReadableDB();
        Cursor cursor = db.query(USER_TABLE, null, where, whereArgs, null, null, null);

        cursor.moveToFirst();
        User user = getUserFromCursor(cursor);
        if(cursor != null)
        {
            cursor.close();
        }

        this.closeDB();

        return user;
    }


    private static User getUserFromCursor(Cursor cursor){

        if(cursor == null || cursor.getCount() == 0)
        {
            return null;
        }
        else
        {
            try{
                return new User(
                        cursor.getInt(USER_ID_COL),
                        cursor.getString(USER_NAME_COL),
                        cursor.getString(USER_FIRSTNAME_COL),
                        cursor.getString(USER_LASTNAME_COL),
                        cursor.getString(USER_PASSWORD_COL),
                        cursor.getString(USER_EMAIL_COL),
                        cursor.getString(USER_DOB_COL),
                        cursor.getString(USER_INTRO_COL)
                );




            }
            catch(Exception e)
            {
                return null;
            }
        }


    }


    public long insertUser(User user){

        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, user.getUserName());
        cv.put(USER_FIRSTNAME, user.getFirstName());
        cv.put(USER_LASTNAME, user.getLastName());
        cv.put(USER_PASSWORD, user.getPassword());
        cv.put(USER_EMAIL, user.getEmail());
        cv.put(USER_DOB, user.getDateOfBirth());
        cv.put(USER_INTRO, user.getIntroduction());

        this.openWritableDB();

        long rowID = db.insert(USER_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }


    public int updateUser(User user){

        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, user.getUserName());
        cv.put(USER_FIRSTNAME, user.getFirstName());
        cv.put(USER_LASTNAME, user.getLastName());
        cv.put(USER_PASSWORD, user.getPassword());
        cv.put(USER_EMAIL, user.getEmail());
        cv.put(USER_DOB, user.getDateOfBirth());
        cv.put(USER_INTRO, user.getIntroduction());



        String where = USER_ID + "= ?";
        String[] whereArgs = {String.valueOf(user.getId())};


        this.openWritableDB();
        int rowCount = db.update(USER_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;

    }


    public int deleteUser(long id)
    {
        String where = USER_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openReadableDB();
        int rowCount = db.delete(USER_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


}

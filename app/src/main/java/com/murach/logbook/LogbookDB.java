package com.murach.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zacdenning on 4/20/16.
 */
public class LogbookDB {

    public static final String  DB_NAME = "logbook.db";
    public static final int     DB_VERSION = 1;

    public static final String  ENTRY_TABLE = "logbook";

    public static final String  ENTRY_ID = "_id";
    public static final int     ENTRY_ID_COL = 0;

    public static final String  ENTRY_GLUCOSE = "glucose";
    public static final int     ENTRY_GLUCOSE_COL = 1;

    public static final String  ENTRY_CARBS = "carbs";
    public static final int     ENTRY_CARBS_COL = 2;

    public static final String  ENTRY_TIME = "datetime";
    public static final int     ENTRY_TIME_COL = 3;


    public static final String CREATE_LOG_TABLE =
            "CREATE TABLE " + ENTRY_TABLE + " (" +
                    ENTRY_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ENTRY_GLUCOSE   + " TEXT, " +
                    ENTRY_CARBS     + " TEXT, " +
                    ENTRY_TIME  + " TEXT) ";

    public static final String DROP_LOG_TABLE =
            "DROP TABLE IF EXISTS " + ENTRY_TABLE;

    /**
     * DB Helper class
     */
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * Creates new table
         * @param db The SQLite database created
         */
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_LOG_TABLE);
        }

        /**
         * When upgrading DB to new version, drop old version and run onCreate()
         * @param db New database
         * @param oldVersion Old database
         * @param newVersion New database
         */
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                db.execSQL(DROP_LOG_TABLE);
                onCreate(db);
            }
        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public LogbookDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    /**
     * Returns an ArrayList containing the entry objects in the database.
     * @return Entries in DB
     */
    public ArrayList<Entry> getEntries() {
        this.openReadableDB();
        Cursor cursor = db.query(ENTRY_TABLE, null,null, null, null, null, "date, time", null);
        ArrayList<Entry> entries = new ArrayList<Entry>();
        while (cursor.moveToNext()) {
            entries.add(getEntryFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return entries;
    }

    /**
     * Returns single entry with the specified ID
     * @param id The ID of the entry you want
     * @return The entry associated with the given ID
     */
    public Entry getEntry(long id) {
        String where = ENTRY_ID + "= ?";
        String[] whereArgs = { Long.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(ENTRY_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Entry entry = getEntryFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return entry;
    }

    /**
     * Gets entry based on user input
     * @param cursor Where the user touches the screen
     * @return The entry in the position touched
     */
    public static Entry getEntryFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        else {
            try {
                Entry entry = new Entry(
                        cursor.getString(ENTRY_ID_COL),
                        cursor.getString(ENTRY_GLUCOSE_COL),
                        cursor.getString(ENTRY_CARBS_COL),
                        cursor.getString((ENTRY_TIME_COL)));
                return entry;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    /**
     * Insert new entry into database
     * @param entry Entry object to insert
     * @return The row of the inserted Entry
     */
    public long insertEntry(Entry entry) {
        ContentValues cv = new ContentValues();
        cv.put(ENTRY_ID, entry.getEntryID());
        cv.put(ENTRY_GLUCOSE, entry.getGlucose());
        cv.put(ENTRY_CARBS, entry.getCarbs());
        cv.put(ENTRY_TIME, entry.getTime());

        this.openWriteableDB();
        long rowID = db.insert(ENTRY_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    /**
     * Used for altering data in an existing entry
     * @param entry Entry to edit
     * @return Row count
     */
    public int updateEntry(Entry entry) {
        ContentValues cv = new ContentValues();
        cv.put(ENTRY_ID, entry.getEntryID());
        cv.put(ENTRY_GLUCOSE, entry.getGlucose());
        cv.put(ENTRY_CARBS, entry.getCarbs());
        cv.put(ENTRY_TIME, entry.getTime().toString());


        String where = ENTRY_ID + "= ?";
        String[] whereArgs = { String.valueOf(entry.getEntryID()) };

        this.openWriteableDB();
        int rowCount = db.update(ENTRY_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    /**
     * Delete entry with specified ID
     * @param id The ID of the entry to be deleted
     * @return Row count
     */
    public int deleteEntry(Entry id) {
        String where = ENTRY_ID + "= ?";
        String[] whereArgs = { String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(ENTRY_TABLE, where, whereArgs);

        return rowCount;

    }
}

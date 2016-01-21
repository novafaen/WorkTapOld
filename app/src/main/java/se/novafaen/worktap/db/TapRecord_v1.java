package se.novafaen.worktap.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import se.novafaen.worktap.db.TapRecordContract.TapEntry;

/**
 * WorkTap
 *
 * @since 2016-01-20
 * @author Kristoffer Nilsson
 */
public class TapRecord_v1 {

    public static final int DATABASE_VERSION = 1;

    /**
     * Create database, version 1.
     * @param db database
     */
    public void create(SQLiteDatabase db) {
        Log.d("TapRecord", "creating database");

        db.execSQL(
                "CREATE TABLE " + TapRecordContract.TapEntry.TABLE_NAME + " " +
                        "(" +
                        TapEntry._ID + " INTEGER PRIMARY KEY, " +
                        TapEntry.COLUMN_NAME_START + " INTEGER," +
                        TapEntry.COLUMN_NAME_STOP + " INTEGER," +
                        TapEntry.COLUMN_NAME_TIMEZONE + " TEXT," +
                        TapEntry.COLUMN_NAME_DELETED + " INTEGER" +
                        ")"
        );
    }

    /**
     * Insert record to database.
     * @param db database
     * @param startTime timestamp in milliseconds
     * @param stopTime timestamp in milliseconds
     * @param timeZone time zone name
     * @return true if store was successful
     */
    public boolean storeRecord(SQLiteDatabase db, Long startTime, Long stopTime, String timeZone) {
        ContentValues values = new ContentValues();
        values.put(TapEntry.COLUMN_NAME_START, startTime);
        values.put(TapEntry.COLUMN_NAME_STOP, stopTime);
        values.put(TapEntry.COLUMN_NAME_TIMEZONE, timeZone);
        values.put(TapEntry.COLUMN_NAME_DELETED, 0);

        long rowId = db.insert(
                TapEntry.TABLE_NAME,
                null,
                values
        );

        Log.d("TapRecord", "stored record, id=" + rowId);

        return rowId > 0;
    };

    /**
     * Load total time since a timestamp
     * @param db database
     * @param timestamp timestamp in milliseconds
     * @return total time
     */
    public Long totalTimeSinceTimestamp(SQLiteDatabase db, Long timestamp) {
        Log.d("TapRecord_v1", "loading total time since time=" + timestamp);

        String[] projection = {
                TapEntry._ID,
                TapEntry.COLUMN_NAME_START,
                TapEntry.COLUMN_NAME_STOP
        };

        String selection = TapEntry.COLUMN_NAME_START + " >= " + timestamp;

        Cursor cursor = db.query(
                TapEntry.TABLE_NAME,
                projection,           // columns
                selection,            // where
                null,                 // values for where
                null,                 // group
                null,                 // filter groups
                null                  // order
        );

        long totalTime = 0L;
        long startTime, stopTime;

        while (cursor.moveToNext()) {
            startTime = cursor.getLong(cursor.getColumnIndex(TapEntry.COLUMN_NAME_START));
            stopTime = cursor.getLong(cursor.getColumnIndex(TapEntry.COLUMN_NAME_STOP));

            totalTime += stopTime - startTime;
        }

        cursor.close();

        return totalTime;
    }
}

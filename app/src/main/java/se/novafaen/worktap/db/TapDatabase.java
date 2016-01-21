package se.novafaen.worktap.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import se.novafaen.worktap.db.TapRecordContract.TapEntry;

/**
 * WorkTap
 *
 * @since 2016-01-20
 * @author Kristoffer Nilsson
 */
public class TapDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "taprecord.db";

    private TapRecord_v1 tapRecord;

    public TapDatabase(Context context) {
        super(context, DATABASE_NAME, null, TapRecord_v1.DATABASE_VERSION);
        tapRecord = new TapRecord_v1();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TapDatabase", "creating database");
        tapRecord.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("TapDatabase", "upgrading database from " + oldVersion + " to " + newVersion);
        // no implementation, first version
    }

    public boolean saveToDatabase(long startTime, long stopTime, String timeZone) {
        return tapRecord.storeRecord(getWritableDatabase(),
                startTime, stopTime, timeZone);
    }

    public long loadTappedSinceTimestamp(long timestamp) {
        return tapRecord.totalTimeSinceTimestamp(getReadableDatabase(),
                timestamp);
    }
}

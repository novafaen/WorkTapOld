package se.novafaen.worktap.db;

import android.provider.BaseColumns;

/**
 * WorkTap
 *
 * @since 2016-01-20
 * @author Kristoffer Nilsson
 */
public final class TapRecordContract {

    public TapRecordContract() {
        // nothing, should not be instantiated
    }

    public static abstract class TapEntry implements BaseColumns {
        public static final String TABLE_NAME = "TABLE_TAP_RECORD";
        public static final String COLUMN_NAME_START = "COLUMN_STARTTIME";
        public static final String COLUMN_NAME_STOP = "COLUMN_ENDTIME";
        public static final String COLUMN_NAME_STATUS = "COLUMN_STATUS";
        public static final String COLUMN_NAME_TIMEZONE = "COLUMN_TIMEZONE";
        public static final String COLUMN_NAME_DELETED = "COLUMN_DELETED";
    }
}

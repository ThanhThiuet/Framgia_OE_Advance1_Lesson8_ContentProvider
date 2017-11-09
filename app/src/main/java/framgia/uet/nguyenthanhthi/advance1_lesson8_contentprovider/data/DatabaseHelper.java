package framgia.uet.nguyenthanhthi.advance1_lesson8_contentprovider.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thanhthi on 09/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "college.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + StudentContract.StudentEntry.TABLE_NAME + " ("
            + StudentContract.StudentEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + StudentContract.StudentEntry.COLUMN_NAME + " TEXT NOT NULL, "
            + StudentContract.StudentEntry.COLUMN_GRADE + " TEXT NOT NULL"
            + ")";

    private static final String DELETE_TABLE_QUERY =
            "DROP TABLE IF EXISTS " + StudentContract.StudentEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_QUERY);
        onCreate(db);
    }
}

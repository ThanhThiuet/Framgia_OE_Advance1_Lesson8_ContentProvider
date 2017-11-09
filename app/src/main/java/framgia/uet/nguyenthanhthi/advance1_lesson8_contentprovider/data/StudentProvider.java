package framgia.uet.nguyenthanhthi.advance1_lesson8_contentprovider.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by thanhthi on 09/11/2017.
 */

public class StudentProvider extends ContentProvider {

    private SQLiteDatabase mDb;
    private static final int STUDENTS = 1;
    private static final int STUDENT_ID = 2;

    public static final String PROVIDER_NAME = "StudentProvider";
    private static final String URL = "content://" + PROVIDER_NAME + "/students";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PROVIDER_NAME, "students", STUDENTS);
        URI_MATCHER.addURI(PROVIDER_NAME, "students/#", STUDENT_ID);
    }

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        mDb = dbHelper.getWritableDatabase();
        return mDb != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(StudentContract.StudentEntry.TABLE_NAME);

        switch (URI_MATCHER.match(uri)) {
            case STUDENTS:
                builder.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;
            case STUDENT_ID:
                builder.appendWhere(StudentContract.StudentEntry.COLUMN_ID + " = "
                        + uri.getPathSegments().get(1));
                break;
            default:
                break;
        }

        //default sort on student name
        if (sortOrder == null || sortOrder == "") sortOrder = StudentContract.StudentEntry.COLUMN_NAME;

        Cursor cursor = builder.query(mDb, projection, selection, selectionArgs,
                null, null, sortOrder);

        //register to see a content URI for changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case STUDENTS:
                //get all student records
                return "vnd.android.cursor.dir/vnd.example.students";
            case STUDENT_ID:
                //get a particular students
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //add a new student record
        long rowId = mDb.insert(StudentContract.StudentEntry.TABLE_NAME, "", values);

        if (rowId > 0) {
            //when new record was add successfully
            Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (URI_MATCHER.match(uri)) {
            case STUDENTS:
                count = mDb.delete(StudentContract.StudentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENT_ID:
                String id = uri.getPathSegments().get(1);
                count = mDb.delete(StudentContract.StudentEntry.TABLE_NAME,
                        StudentContract.StudentEntry.COLUMN_ID + " = " + id
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (URI_MATCHER.match(uri)) {
            case STUDENTS:
                count = mDb.update(StudentContract.StudentEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case STUDENT_ID:
                count = mDb.update(StudentContract.StudentEntry.TABLE_NAME, values,
                        StudentContract.StudentEntry.COLUMN_ID + " = " + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}

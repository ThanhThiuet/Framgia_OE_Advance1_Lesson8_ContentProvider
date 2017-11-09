package framgia.uet.nguyenthanhthi.advance1_lesson8_contentprovider.sreen;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import framgia.uet.nguyenthanhthi.advance1_lesson8_contentprovider.R;
import framgia.uet.nguyenthanhthi.advance1_lesson8_contentprovider.data.StudentContract;
import framgia.uet.nguyenthanhthi.advance1_lesson8_contentprovider.data.StudentProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtName, mEdtGrade;
    private Button mButtonAdd, mButtonRetrieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Content Provider Demo");

        initView();
    }

    private void initView() {
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtGrade = (EditText) findViewById(R.id.edt_grade);
        mButtonAdd = (Button) findViewById(R.id.button_add_student);
        mButtonRetrieve = (Button) findViewById(R.id.button_retrieve_student);

        mButtonAdd.setOnClickListener(this);
        mButtonRetrieve.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_student:
                addNewStudent();
                break;
            case R.id.button_retrieve_student:
                retrieveStudent();
                break;
            default:
                break;
        }
    }

    private void addNewStudent() {
        String name = mEdtName.getText().toString();
        String grade = mEdtGrade.getText().toString();

        ContentValues values = new ContentValues();
        values.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        values.put(StudentContract.StudentEntry.COLUMN_GRADE, grade);

        Uri uri = getContentResolver().insert(StudentProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    }

    private void retrieveStudent() {
        String Url = "content://" + StudentProvider.PROVIDER_NAME;
        Uri uri = Uri.parse(Url);
        Cursor cursor = managedQuery(uri, null, null, null, "name");

        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(this,
                        cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_ID)) + ", "
                                + cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NAME)) + ", "
                                + cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_GRADE)),
                        Toast.LENGTH_SHORT)
                        .show();
            } while (cursor.moveToNext());
        }
    }
}

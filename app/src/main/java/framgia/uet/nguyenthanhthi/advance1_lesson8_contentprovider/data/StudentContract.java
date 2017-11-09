package framgia.uet.nguyenthanhthi.advance1_lesson8_contentprovider.data;

import android.provider.BaseColumns;

/**
 * Created by thanhthi on 09/11/2017.
 */

public class StudentContract {

    public StudentContract() {

    }

    public static class StudentEntry implements BaseColumns {

        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GRADE = "grade";

    }

}

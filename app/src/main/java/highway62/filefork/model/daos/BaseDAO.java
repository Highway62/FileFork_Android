package highway62.filefork.model.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import highway62.filefork.model.DBContract;

public abstract class BaseDAO {

    private static final String TAG = "BASE_DAO";
    protected Context mContext;
    protected static DatabaseHelper mDbHelper;

    public BaseDAO(Context context) {
        super();

        if (context == null) {
            throw new AssertionError();
        } else {
            mContext = context;
        }
    }

    public SQLiteDatabase openDb() {
        if (mDbHelper == null) {
            mDbHelper = DatabaseHelper.getInstance(mContext);
        }
        return mDbHelper.getWritableDatabase();
    }

    public void closeDb() {
        mDbHelper.close();
    }

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        private static DatabaseHelper singleton = null;

        private DatabaseHelper(Context context) {
            super(context, DBContract.DB_NAME, DBContract.DB_CURSOR_FACTORY, DBContract.DB_VERSION);
        }

        public static DatabaseHelper getInstance(Context context){
            if(singleton == null) {
                singleton = new DatabaseHelper(context);
            }
            return singleton;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DBContract.CREATE_FORKS_TABLE);
            db.execSQL(DBContract.CREATE_MESSAGES_TABLE);
            db.execSQL(DBContract.CREATE_DEVICES_TABLE);
            db.execSQL(DBContract.CREATE_FORK_RECIPIENTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Handle DB Upgrade
            /*
            if(newVersion > oldVersion) {
                Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
                db.execSQL("DROP TABLE IF EXISTS " + DBContract.ForkTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + DBContract.MessagesTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + DBContract.ContactDevicesTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + DBContract.ForkRecipientsTable.TABLE_NAME);
                onCreate(db);
            }*/
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.ForkTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.MessagesTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.ContactDevicesTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.ForkRecipientsTable.TABLE_NAME);
            onCreate(db);

        }
    }
}


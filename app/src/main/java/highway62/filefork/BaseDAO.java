package highway62.filefork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDAO extends SQLiteOpenHelper{


    public BaseDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FORKS_TABLE =
                "CREATE TABLE " + DBContract.ForkTable.TABLE_NAME
                + "("
                + DBContract.ForkTable._ID + " INTEGER PRIMARY KEY NOT NULL,"
                + DBContract.ForkTable.COLUMN_FORK_NAME + " TEXT NOT NULL,"
                + DBContract.ForkTable.COLUMN_FORK_TYPE + " TEXT NOT NULL,"
                + DBContract.ForkTable.COLUMN_LAST_MSG + " TEXT,"
                + DBContract.ForkTable.COLUMN_LAST_MSG_TIME + " INTEGER,"
                + DBContract.ForkTable.COLUMN_LAST_MSG_TYPE + " TEXT"
                + ")";

        String CREATE_MESSAGES_TABLE =
                "CREATE TABLE " + DBContract.MessagesTable.TABLE_NAME
                + "("
                + DBContract.MessagesTable._ID + " INTEGER PRIMARY KEY NOT NULL,"
                + DBContract.MessagesTable.COLUMN_FORK_ID + " INTEGER REFERENCES " + DBContract.ForkTable.TABLE_NAME + "(" + DBContract.ForkTable._ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                + DBContract.MessagesTable.COLUMN_TEXT + " TEXT,"
                + DBContract.MessagesTable.COLUMN_FILE + " TEXT,"
                + DBContract.MessagesTable.COLUMN_TYPE + " TEXT NOT NULL,"
                + DBContract.MessagesTable.COLUMN_SENDER_ID + " INTEGER NOT NULL,"
                + DBContract.MessagesTable.COLUMN_SENT + " INTEGER DEFAULT 1,"
                + DBContract.MessagesTable.COLUMN_TIMESTAMP + " INTEGER NOT NULL"
                + ")";

        String CREATE_DEVICES_TABLE =
                "CREATE TABLE " + DBContract.ContactDevicesTable.TABLE_NAME
                + "("
                + DBContract.ContactDevicesTable._ID + " INTEGER PRIMARY KEY NOT NULL,"
                + DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME + " TEXT NOT NULL,"
                + DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE + " TEXT NOT NULL,"
                + DBContract.ContactDevicesTable.COLUMN_ACCEPTED + " INTEGER DEFAULT 1"
                + ")";

        String CREATE_FORK_RECIPIENTS_TABLE =
                "CREATE TABLE " + DBContract.ForkRecipientsTable.TABLE_NAME
                + "("
                + DBContract.ForkRecipientsTable.COLUMN_FORK_ID + " INTEGER REFERENCES " + DBContract.ForkTable.TABLE_NAME + "(" + DBContract.ForkTable._ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                + DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID + " INTEGER REFERENCES " + DBContract.ContactDevicesTable.TABLE_NAME + "(" + DBContract.ContactDevicesTable._ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                + " PRIMARY KEY" + "(" + DBContract.ForkRecipientsTable.COLUMN_FORK_ID + ", " + DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID + ")"
                + ")";

        db.execSQL(CREATE_FORKS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
        db.execSQL(CREATE_DEVICES_TABLE);
        db.execSQL(CREATE_FORK_RECIPIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ForkTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.MessagesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ContactDevicesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ForkRecipientsTable.TABLE_NAME);
        onCreate(db);
    }
}


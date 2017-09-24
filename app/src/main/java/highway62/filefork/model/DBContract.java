package highway62.filefork.model;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 *
 * Defines constants for each database table and enumerates their columns using inner classes
 *
 */
public final class DBContract {

    public static final int DB_VERSION = 21;
    public static final String DB_NAME = "FileForkDB.db";
    public static final SQLiteDatabase.CursorFactory DB_CURSOR_FACTORY  = null;

    // Tables
    // BaseColumns interface defines _ID and _COUNT column names

    public static abstract class ForkTable implements BaseColumns{
        public static final String TABLE_NAME = "forks";
        public static final String COLUMN_FORK_NAME = "name";
        public static final String COLUMN_FORK_TYPE = "type";
        public static final String COLUMN_LAST_MSG = "last_message";
        public static final String COLUMN_LAST_MSG_TIME = "last_message_time";
        public static final String COLUMN_LAST_MSG_TYPE = "last_message_type";
    }

    public static abstract class ForkTypes{
        public static final String IPHONE = "IPHONE";
        public static final String ANDROID = "ANDROID";
        public static final String WINDOWS = "WINDOWS";
        public static final String MAC = "MAC";
        public static final String MULTICAST_DEVICES = "MULTICAST_DEVICES";
        public static final String MUTLICAST_MIXED = "MULTICAST_MIXED";
        public static final String CONTACT = "CONTACT";
    }

    public static abstract class MessagesTable implements BaseColumns{
        public static final String TABLE_NAME = "messages";
        public static final String COLUMN_FORK_ID = "fork_id";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_FILE = "file";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SENDER_ID = "sender_id";
        public static final String COLUMN_SENT = "sent";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static abstract class MessageTypes{
        public static final String IMAGE = "IMAGE";
        public static final String VIDEO = "VIDEO";
        public static final String TEXT = "TEXT";
        public static final String DOCUMENT = "DOCUMENT";
        public static final String URL = "URL";
    }

    /*public static abstract class ContactsTable implements BaseColumns{
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_CONTACT_NAME = "contact_name";
    }*/

    public static abstract class ContactDevicesTable implements BaseColumns{
        public static final String TABLE_NAME = "contact_devices";
        public static final String COLUMN_DEVICE_NAME = "name";
        public static final String COLUMN_DEVICE_TYPE = "type";
        public static final String COLUMN_ACCEPTED = "accepted";
    }

    public static abstract class DeviceTypes{
        public static final String IPHONE = "IPHONE";
        public static final String ANDROID = "ANDROID";
        public static final String WINDOWS = "WINDOWS";
        public static final String MAC = "MAC";
        public static final String CONTACT = "CONTACT";
    }

    public static abstract class ForkRecipientsTable implements BaseColumns{
        public static final String TABLE_NAME = "fork_recipients";
        public static final String COLUMN_FORK_ID = "fork_id";
        public static final String COLUMN_RECIPIENT_ID = "recipient_id";
    }

    // CREATE TABLE STRINGS

    public static String CREATE_FORKS_TABLE =
            "CREATE TABLE " + DBContract.ForkTable.TABLE_NAME
                    + "("
                    + DBContract.ForkTable._ID + " INTEGER PRIMARY KEY NOT NULL,"
                    + DBContract.ForkTable.COLUMN_FORK_NAME + " TEXT NOT NULL,"
                    + DBContract.ForkTable.COLUMN_FORK_TYPE + " TEXT NOT NULL,"
                    + DBContract.ForkTable.COLUMN_LAST_MSG + " TEXT,"
                    + DBContract.ForkTable.COLUMN_LAST_MSG_TIME + " INTEGER,"
                    + DBContract.ForkTable.COLUMN_LAST_MSG_TYPE + " TEXT"
                    + ")";

    public static String CREATE_MESSAGES_TABLE =
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

    public static String CREATE_DEVICES_TABLE =
            "CREATE TABLE " + DBContract.ContactDevicesTable.TABLE_NAME
                    + "("
                    + DBContract.ContactDevicesTable._ID + " INTEGER PRIMARY KEY NOT NULL,"
                    + DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME + " TEXT NOT NULL,"
                    + DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE + " TEXT NOT NULL,"
                    + DBContract.ContactDevicesTable.COLUMN_ACCEPTED + " INTEGER DEFAULT 1"
                    + ")";

    public static String CREATE_FORK_RECIPIENTS_TABLE =
            "CREATE TABLE " + DBContract.ForkRecipientsTable.TABLE_NAME
                    + "("
                    + DBContract.ForkRecipientsTable.COLUMN_FORK_ID + " INTEGER REFERENCES " + DBContract.ForkTable.TABLE_NAME + "(" + DBContract.ForkTable._ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                    + DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID + " INTEGER REFERENCES " + DBContract.ContactDevicesTable.TABLE_NAME + "(" + DBContract.ContactDevicesTable._ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                    + " PRIMARY KEY" + "(" + DBContract.ForkRecipientsTable.COLUMN_FORK_ID + ", " + DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID + ")"
                    + ")";

}

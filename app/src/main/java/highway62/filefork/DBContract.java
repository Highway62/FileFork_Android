package highway62.filefork;

import android.provider.BaseColumns;

/**
 *
 * Defines constants for each database table and enumerates their columns using inner classes
 *
 */
public final class DBContract {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "FileForkDB.db";

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
}

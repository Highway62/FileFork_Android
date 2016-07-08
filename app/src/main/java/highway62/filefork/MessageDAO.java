package highway62.filefork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Steven on 30/09/2015.
 */
public class MessageDAO extends BaseDAO{

    public MessageDAO(Context context) {
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
    }

    public long addMessage(Message msg) throws Exception {

        ContentValues values = new ContentValues();
        values.put(DBContract.MessagesTable.COLUMN_FORK_ID, msg.getForkId());
        values.put(DBContract.MessagesTable.COLUMN_TEXT, msg.getText());
        values.put(DBContract.MessagesTable.COLUMN_FILE, msg.getFile());
        values.put(DBContract.MessagesTable.COLUMN_TYPE, msg.getType());
        values.put(DBContract.MessagesTable.COLUMN_SENDER_ID, msg.getSender_id());
        int msgSent = msg.isSent() ? 1 : 0;
        values.put(DBContract.MessagesTable.COLUMN_SENT, msgSent);
        values.put(DBContract.MessagesTable.COLUMN_TIMESTAMP, msg.getTimeStamp());

        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = db.insert(DBContract.MessagesTable.TABLE_NAME, null, values);
        db.close();

        if (!(rowId > 0)) {
            throw new Exception("Error inserting fork into Android DB");
        }else{
            return rowId;
        }
    }

    public Message getMessage(long msg_id){

        // Columns to return
        String[] projection = {
                DBContract.MessagesTable.COLUMN_FORK_ID,
                DBContract.MessagesTable.COLUMN_TEXT,
                DBContract.MessagesTable.COLUMN_FILE,
                DBContract.MessagesTable.COLUMN_TYPE,
                DBContract.MessagesTable.COLUMN_SENDER_ID,
                DBContract.MessagesTable.COLUMN_SENT,
                DBContract.MessagesTable.COLUMN_TIMESTAMP,
        };

        // Columns for the where clause
        String selection = DBContract.MessagesTable._ID + "=?";

        // Values for the where clause
        String[] selectionArgs = {
                String.valueOf(msg_id)
        };

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DBContract.MessagesTable.TABLE_NAME,
                projection,     // columns to return
                selection,      // columns for where clause
                selectionArgs,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if(cursor != null){
            cursor.moveToFirst();
            long fork_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_FORK_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_TEXT));
            String file = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_FILE));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_TYPE));
            long sender_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_SENDER_ID));
            boolean sent = (cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_SENT)) == 1);
            Long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_TIMESTAMP));
            cursor.close();
            db.close();
            return new Message(msg_id,fork_id,text,file,type,sender_id,sent,timestamp);
        } else {
            db.close();
            return null;
        }
    }

    //TODO make getAllMessagesByPage() method to only return so many rows at once (paginated)
    public ArrayList<Message> getAllMessages(long fork_id){

        ArrayList<Message> messages = new ArrayList<>();

        // Columns to return
        String[] projection = {
                DBContract.MessagesTable._ID,
                DBContract.MessagesTable.COLUMN_TEXT,
                DBContract.MessagesTable.COLUMN_FILE,
                DBContract.MessagesTable.COLUMN_TYPE,
                DBContract.MessagesTable.COLUMN_SENDER_ID,
                DBContract.MessagesTable.COLUMN_SENT,
                DBContract.MessagesTable.COLUMN_TIMESTAMP,
        };

        // Columns for the where clause
        String selection = DBContract.MessagesTable.COLUMN_FORK_ID + "=?";

        // Values for the where clause
        String[] selectionArgs = {
                String.valueOf(fork_id)
        };

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DBContract.MessagesTable.TABLE_NAME,
                projection,     // columns to return
                selection,      // columns for where clause
                selectionArgs,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if(cursor.moveToFirst()){
            do {

                long msg_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.MessagesTable._ID));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_TEXT));
                String file = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_FILE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_TYPE));
                long sender_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_SENDER_ID));
                boolean sent = (cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_SENT)) == 1);
                Long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.MessagesTable.COLUMN_TIMESTAMP));
                messages.add(new Message(msg_id,fork_id,text,file,type,sender_id,sent,timestamp));

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
            return messages;
        } else {
            db.close();
            return null;
        }
    }

    public int deleteMessage(long msg_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = DBContract.MessagesTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(msg_id) };
        int rowsAffected = db.delete(DBContract.MessagesTable.TABLE_NAME, selection, selectionArgs);
        db.close();
        return rowsAffected;
    }

    public int deleteAllMessages(long fork_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = DBContract.MessagesTable.COLUMN_FORK_ID + "=?";
        String[] selectionArgs = { String.valueOf(fork_id) };
        int rowsAffected = db.delete(DBContract.MessagesTable.TABLE_NAME, selection, selectionArgs);
        db.close();
        return rowsAffected;
    }
}

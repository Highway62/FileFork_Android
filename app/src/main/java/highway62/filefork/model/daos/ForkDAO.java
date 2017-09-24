package highway62.filefork.model.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import highway62.filefork.objects.Fork;
import highway62.filefork.objects.Message;
import highway62.filefork.model.DBContract;

public class ForkDAO extends BaseDAO {

    public ForkDAO(Context context) {
        super(context);
    }

    public long addFork(Fork fork) throws Exception {

        ContentValues values = new ContentValues();
        values.put(DBContract.ForkTable.COLUMN_FORK_NAME, fork.getForkName());
        values.put(DBContract.ForkTable.COLUMN_FORK_TYPE, fork.getForkType());
        values.putNull(DBContract.ForkTable.COLUMN_LAST_MSG);
        values.putNull(DBContract.ForkTable.COLUMN_LAST_MSG_TYPE);
        values.putNull(DBContract.ForkTable.COLUMN_LAST_MSG_TIME);

        SQLiteDatabase db = openDb();
        long forkId = db.insert(DBContract.ForkTable.TABLE_NAME, null, values);

        if (forkId < 0) {
            closeDb();
            throw new Exception("Error inserting fork into Android DB");
        }else{
            // Add fork recipient to ForkRecipients table
            ContentValues recipValues = new ContentValues();
            recipValues.put(DBContract.ForkRecipientsTable.COLUMN_FORK_ID, forkId);
            recipValues.put(DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID, fork.getRecipient());
            long recipId = db.insert(DBContract.ForkRecipientsTable.TABLE_NAME, null, recipValues);
            closeDb();
            if(recipId < 0){
                throw new Exception("Error adding recipient to fork");
            }
            return forkId;
        }
    }

    public Fork getFork(long id){

        // Columns to return
        String[] projection = {
                DBContract.ForkTable._ID,
                DBContract.ForkTable.COLUMN_FORK_NAME,
                DBContract.ForkTable.COLUMN_FORK_TYPE,
                DBContract.ForkTable.COLUMN_LAST_MSG,
                DBContract.ForkTable.COLUMN_LAST_MSG_TIME,
                DBContract.ForkTable.COLUMN_LAST_MSG_TYPE
        };

        // Columns for the where clause
        String selection = DBContract.ForkTable._ID + "=?";

        // Values for the where clause
        String[] selectionArgs = {
                String.valueOf(id)
        };

        SQLiteDatabase db = openDb();

        Cursor cursor = db.query(
                DBContract.ForkTable.TABLE_NAME,
                projection,     // columns to return
                selection,      // columns for where clause
                selectionArgs,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if (cursor != null) {
            cursor.moveToFirst();
            long fork_id = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.ForkTable._ID));
            String fork_name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_FORK_NAME));
            String fork_type = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_FORK_TYPE));
            String lastMsg = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_LAST_MSG));
            Long lastMsgTime = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_LAST_MSG_TIME));
            String lastMsgtype = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_LAST_MSG_TYPE));
            cursor.close();

            // Get fork recipient/s details

            // Columns to return
            String[] recip_projection = {
                    DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID
            };

            // Columns for the where clause
            String recip_selection = DBContract.ForkRecipientsTable.COLUMN_FORK_ID + "=?";

            // Values for the where clause
            String[] recip_selectionArgs = {
                    String.valueOf(id)
            };

            Cursor recip_cursor = db.query(
                    DBContract.ForkRecipientsTable.TABLE_NAME,
                    recip_projection,     // columns to return
                    recip_selection,      // columns for where clause
                    recip_selectionArgs,  // values for where clause
                    null,           // group by
                    null,           // having
                    null);          // order by

            if(recip_cursor != null){
                recip_cursor.moveToFirst();
                Long fork_recipient_id = recip_cursor.getLong(recip_cursor.getColumnIndexOrThrow(DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID));
                recip_cursor.close();
                closeDb();
                return Fork.newForkFromDatabase(fork_id,fork_name,fork_type,fork_recipient_id,lastMsg,lastMsgTime, lastMsgtype);
            } else {
                closeDb();
                return null;
            }
        } else {
            closeDb();
            return null;
        }
    }

    public ArrayList<Fork> getAllForks(){

        ArrayList<Fork> forks = new ArrayList<>();

        // Columns to return
        String[] projection = {
                DBContract.ForkTable._ID,
                DBContract.ForkTable.COLUMN_FORK_NAME,
                DBContract.ForkTable.COLUMN_FORK_TYPE,
                DBContract.ForkTable.COLUMN_LAST_MSG,
                DBContract.ForkTable.COLUMN_LAST_MSG_TIME,
                DBContract.ForkTable.COLUMN_LAST_MSG_TYPE
        };

        SQLiteDatabase db = openDb();

        Cursor cursor = db.query(
                DBContract.ForkTable.TABLE_NAME,
                projection,     // columns to return
                null,      // columns for where clause
                null,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if(cursor.moveToFirst()){
            do{
                long fork_id = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.ForkTable._ID));
                String fork_name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_FORK_NAME));
                String fork_type = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_FORK_TYPE));
                String lastMsg = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_LAST_MSG));
                Long lastMsgTime = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_LAST_MSG_TIME));
                String lastMsgType = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ForkTable.COLUMN_LAST_MSG_TYPE));

                // get recipient
                // Columns to return
                String[] recip_projection = {
                        DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID
                };

                // Columns for the where clause
                String recip_selection = DBContract.ForkRecipientsTable.COLUMN_FORK_ID + "=?";

                // Values for the where clause
                String[] recip_selectionArgs = {
                        String.valueOf(fork_id)
                };

                Cursor recip_cursor = db.query(
                        DBContract.ForkRecipientsTable.TABLE_NAME,
                        recip_projection,     // columns to return
                        recip_selection,      // columns for where clause
                        recip_selectionArgs,  // values for where clause
                        null,           // group by
                        null,           // having
                        null);          // order by

                if(recip_cursor != null){
                    if(recip_cursor.moveToFirst()) {
                        int fork_recipient = recip_cursor.getInt(recip_cursor.getColumnIndexOrThrow(DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID));
                        recip_cursor.close();
                        // If fork has recipient, then add to array
                        forks.add(Fork.newForkFromDatabase(fork_id, fork_name, fork_type, fork_recipient, lastMsg, lastMsgTime, lastMsgType));
                    }
                } else {
                    // If fork has no recipient, delete fork
                    deleteFork(fork_id);
                }
            }while(cursor.moveToNext());
            cursor.close();
            closeDb();
        } else {
            closeDb();
            return null;
        }
        return forks;
    }

    public int deleteFork(long fork_id){
        SQLiteDatabase db = openDb();
        String selection = DBContract.ForkTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(fork_id) };

        // delete row
        int noOfRows = db.delete(DBContract.ForkTable.TABLE_NAME, selection, selectionArgs);
        closeDb();
        return noOfRows;
    }

    public int updateLastMessage(long fork_id, Message msg){
        SQLiteDatabase db = openDb();
        String selection = DBContract.ForkTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(fork_id) };


        ContentValues values = new ContentValues();
        values.put(DBContract.ForkTable.COLUMN_LAST_MSG, msg.getText());
        values.put(DBContract.ForkTable.COLUMN_LAST_MSG_TYPE, msg.getType());
        values.put(DBContract.ForkTable.COLUMN_LAST_MSG_TIME, msg.getTimeStamp());

        // update row
        int noOfRows = db.update(DBContract.ForkTable.TABLE_NAME,values,selection,selectionArgs);
        closeDb();
        return noOfRows;
    }

    /* Checks whether a recipient (for a new fork) already exists in the DB */
    public boolean recipientExists(long recip_id){
        SQLiteDatabase db = openDb();
        String query =
                "SELECT 1 FROM " +
                        DBContract.ForkRecipientsTable.TABLE_NAME +
                        "WHERE" +
                        DBContract.ForkRecipientsTable.COLUMN_RECIPIENT_ID + "=? LIMIT 1";
        String[] selectionArgs = { String.valueOf(recip_id) };
        Cursor cursor = db.rawQuery(query,selectionArgs);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        closeDb();
        return exists;
    }
}

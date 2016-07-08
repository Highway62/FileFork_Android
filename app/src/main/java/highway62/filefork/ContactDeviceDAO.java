package highway62.filefork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class ContactDeviceDAO extends BaseDAO{

    public ContactDeviceDAO(Context context) {
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
    }

    public long addDevice(Device device) throws Exception {

        ContentValues values = new ContentValues();
        values.put(DBContract.ContactDevicesTable._ID, device.getId());
        values.put(DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME, device.getDeviceName());
        values.put(DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE, device.getDeviceType());
        values.put(DBContract.ContactDevicesTable.COLUMN_ACCEPTED, 1);

        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = db.insert(DBContract.ContactDevicesTable.TABLE_NAME, null, values);
        db.close();

        if (rowId <= 0) {
            throw new Exception("Error inserting device into Android DB");
        } else {
            return rowId;
        }
    }

    public long addContact(Contact contact) throws Exception {
        ContentValues values = new ContentValues();
        values.put(DBContract.ContactDevicesTable._ID, contact.getId());
        values.put(DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME, contact.getContactName());
        values.put(DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE, contact.getContactType());
        int isAccepted = contact.isAccepted() ? 1 : 0;
        values.put(DBContract.ContactDevicesTable.COLUMN_ACCEPTED, isAccepted);

        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = db.insert(DBContract.ContactDevicesTable.TABLE_NAME, null, values);
        db.close();

        if (rowId <= 0) {
            throw new Exception("Error inserting contact into Android DB");
        } else {
            return rowId;
        }
    }

    public void updateContactAccepted(long contactId) throws Exception {

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = DBContract.ContactDevicesTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(contactId) };


        ContentValues values = new ContentValues();
        values.put(DBContract.ContactDevicesTable.COLUMN_ACCEPTED,1);

        // update row
        int updatedRows = db.update(DBContract.ContactDevicesTable.TABLE_NAME,values,selection,selectionArgs);

        if(updatedRows == 0){
            throw new Exception("DB Error: Cannot Update Contact Accepted");
        }
    }

    public Device getDeviceById(long id) throws Exception {

        // Columns to return
        String[] projection = {
                DBContract.ContactDevicesTable._ID,
                DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME,
                DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE,
        };

        // Columns for the where clause
        String selection = DBContract.ContactDevicesTable._ID + "=?";

        // Values for the where clause
        String[] selectionArgs = {
                String.valueOf(id)
        };


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DBContract.ContactDevicesTable.TABLE_NAME,
                projection,     // columns to return
                selection,      // columns for where clause
                selectionArgs,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if (cursor != null) {
            cursor.moveToFirst();
            long dev_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable._ID));
            String dev_name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME));
            String dev_type = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE));
            cursor.close();
            db.close();

            if(!dev_type.equals(DBContract.DeviceTypes.CONTACT)){
                return new Device(dev_id,dev_name,dev_type);
            } else {
                throw new Exception("Supplied ID is of CONTACT, not DEVICE");
            }
        } else {
            return null;
        }
    }

    public ArrayList<Device> getAllDevices(){

        ArrayList<Device> devices = new ArrayList<Device>();

        // Columns to return
        String[] projection = {
                DBContract.ContactDevicesTable._ID,
                DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME,
                DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE
        };

        // Columns for the where clause
        String selection = DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE + "!=?";

        // Values for the where clause
        String[] selectionArgs = {
                DBContract.DeviceTypes.CONTACT
        };


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DBContract.ContactDevicesTable.TABLE_NAME,
                projection,     // columns to return
                selection,      // columns for where clause
                selectionArgs,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if (cursor.moveToFirst()) {
            do{
                long dev_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable._ID));
                String dev_name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME));
                String dev_type = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE));
                devices.add(new Device(dev_id,dev_name,dev_type));
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
            return devices;
        } else {
            return null;
        }
    }

    public Contact getContactById(long id) throws Exception {

        // Columns to return
        String[] projection = {
                DBContract.ContactDevicesTable._ID,
                DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME,
                DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE,
                DBContract.ContactDevicesTable.COLUMN_ACCEPTED
        };

        // Columns for the where clause
        String selection = DBContract.ContactDevicesTable._ID + "=?";

        // Values for the where clause
        String[] selectionArgs = {
                String.valueOf(id)
        };


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DBContract.ContactDevicesTable.TABLE_NAME,
                projection,     // columns to return
                selection,      // columns for where clause
                selectionArgs,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if (cursor != null) {
            cursor.moveToFirst();
            long con_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable._ID));
            String con_name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME));
            String con_type = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE));
            int con_accepted_int = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_ACCEPTED));
            boolean con_accepted = (con_accepted_int != 0);
            cursor.close();
            db.close();

            if(con_type.equals(DBContract.DeviceTypes.CONTACT)){
                return new Contact(con_id,con_name,con_accepted);
            } else {
                throw new Exception("Supplied ID not a CONTACT");
            }
        } else {
            return null;
        }
    }

    public ArrayList<Contact> getAllContacts(){

        ArrayList<Contact> contacts = new ArrayList<Contact>();

        // Columns to return
        String[] projection = {
                DBContract.ContactDevicesTable._ID,
                DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME,
                DBContract.ContactDevicesTable.COLUMN_ACCEPTED
        };

        // Columns for the where clause
        String selection = DBContract.ContactDevicesTable.COLUMN_DEVICE_TYPE + "=?";

        // Values for the where clause
        String[] selectionArgs = {
                DBContract.DeviceTypes.CONTACT
        };


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DBContract.ContactDevicesTable.TABLE_NAME,
                projection,     // columns to return
                selection,      // columns for where clause
                selectionArgs,  // values for where clause
                null,           // group by
                null,           // having
                null);          // order by

        if (cursor.moveToFirst()){
            do{
                long con_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable._ID));
                String con_name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_DEVICE_NAME));
                int con_accepted_int = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.ContactDevicesTable.COLUMN_ACCEPTED));
                boolean con_accepted = (con_accepted_int != 0);
                contacts.add(new Contact(con_id,con_name,con_accepted));
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
            return contacts;
        } else {
            return null;
        }
    }

}

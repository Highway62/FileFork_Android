package highway62.filefork;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Highway62 on 07/07/2016.
 */
public class DeviceDialog extends Dialog {

    Context context;
    DialogObserver mDialogObserver;
    ContactDeviceDAO conDevDao;
    ListView deviceList;
    ListView contactList;
    ArrayList<Device> devices;
    ArrayList<Contact> contacts;

    public DeviceDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_device_dialog);
        conDevDao = new ContactDeviceDAO(context);
        deviceList = (ListView) findViewById(R.id.device_dialog_device_list);
        contactList = (ListView) findViewById(R.id.device_dialog_contact_list);

        devices = conDevDao.getAllDevices();
        contacts = conDevDao.getAllContacts();

        if(devices != null) {
            DialogDeviceRowAdapter deviceAdapter = new DialogDeviceRowAdapter(context, devices);
            deviceList.setAdapter(deviceAdapter);
            deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Return the device ID to the fork activity
                    mDialogObserver.notifyResult(DeviceDialog.this.devices.get(position).getId());
                }
            });
        }

        if(contacts != null){
            DialogContactRowAdapter contactAdapter = new DialogContactRowAdapter(context,contacts);
            contactList.setAdapter(contactAdapter);
            contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mDialogObserver.notifyResult(DeviceDialog.this.contacts.get(position).getId());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        freeReferences();
        super.onBackPressed();
    }

    public void setDialogObserver(DialogObserver dObserver){
        mDialogObserver = dObserver;
    }

    public interface DialogObserver{
        void notifyResult(long device_id);
    }

    private void freeReferences(){
        mDialogObserver = null;
        deviceList = null;
        contactList = null;

        if(devices != null){
            devices.clear();
            devices = null;
        }

        if(contacts != null){
            contacts.clear();
            contacts = null;
        }
    }
}

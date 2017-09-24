package highway62.filefork.presenters;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import highway62.filefork.model.daos.ContactDeviceDAO;
import highway62.filefork.objects.Contact;
import highway62.filefork.objects.Device;
import highway62.filefork.views.ContactDeviceNotifier;

/**
 * Created by Highway62 on 14/04/2017.
 */
public class ContactDeviceHandler implements ContactDevicePresenter{

    ContactDeviceNotifier view;
    ContactDeviceDAO cdDao;

    public ContactDeviceHandler(Activity context){
        cdDao = new ContactDeviceDAO(context);
    }

    @Override
    public void subscribe(ContactDeviceNotifier view) {
        this.view = view;
    }

    @Override
    public void addContact(final Contact contact) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdDao.addContact(contact);
                    notifyContactAdded(true,null);
                }catch (Exception e){
                    Log.d("CONTACTHANDLER",e.getMessage());
                    notifyContactAdded(false,e);
                }
            }
        }).start();
    }

    @Override
    public void addDevice(final Device device) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdDao.addDevice(device);
                    notifyDeviceAdded(true,null);
                }catch (Exception e){
                    Log.d("DEVICEHANDLER",e.getMessage());
                    notifyDeviceAdded(false,e);
                }
            }
        }).start();
    }

    @Override
    public void requestDevice(final long deviceId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                 passDeviceToView(cdDao.getDeviceById(deviceId));
            }
        }).start();
    }

    @Override
    public void requestContact(final long contactId) {
        //TODO clean this up - handle requesting a contact with a device id
        new Thread(new Runnable() {
            @Override
            public void run() {
                Contact contact;
                Exception ex;
                try {
                    contact = cdDao.getContactById(contactId);
                    ex = null;
                }catch (Exception e){
                    contact = null;
                    ex = e;
                }
                passContactToView(contact,ex);
            }
        }).start();
    }

    @Override
    public void requestAllDevices() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                passDeviceListToView(cdDao.getAllDevices());
            }
        }).start();
    }

    @Override
    public void requestAllContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                passContactListToView(cdDao.getAllContacts());
            }
        }).start();
    }

    @Override
    public void updateContactAccepted(final long contactId) {
        //TODO
        // Find contact in DB
        // If exists - then update contact accepted
        // If contact accepted = successful, notify view
        // If not, deal with error
        // If contact doesn't exist, handle error
    }

    private void notifyContactAdded(boolean success, Exception e){
        if(view != null){
            view.notifyContactAdded(success,e);
        }
    }

    private void notifyDeviceAdded(boolean success, Exception e){
        if(view != null){
            view.notifyDeviceAdded(success,e);
        }
    }

    private void passDeviceToView(Device device){
        if(view != null){
            view.passDeviceToView(device);
        }
    }

    private void passContactToView(Contact contact, Exception e){
        if(view != null){
            view.passContactToView(contact,e);
        }
    }

    private void passDeviceListToView(ArrayList<Device> deviceList){
        if(view != null){
            view.passDeviceListToView(deviceList);
        }
    }

    private void passContactListToView(ArrayList<Contact> contactList){
        if(view != null){
            view.passContactListToView(contactList);
        }
    }

    private void notifyContactAccepted(Contact contact){
        if(view != null){
            view.notifyContactAccepted(contact);
        }
    }
}

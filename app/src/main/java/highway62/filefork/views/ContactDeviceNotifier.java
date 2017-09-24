package highway62.filefork.views;

import java.util.ArrayList;

import highway62.filefork.objects.Contact;
import highway62.filefork.objects.Device;

/**
 * Created by Highway62 on 14/04/2017.
 */
public interface ContactDeviceNotifier {

    void notifyContactAdded(boolean success, Exception e);

    void notifyDeviceAdded(boolean success, Exception e);

    void passContactToView(Contact contact, Exception e);

    void passDeviceToView(Device device);

    void passContactListToView(ArrayList<Contact> contactList);

    void passDeviceListToView(ArrayList<Device> deviceList);

    void notifyContactAccepted(Contact contact);

}

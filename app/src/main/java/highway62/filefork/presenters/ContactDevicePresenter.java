package highway62.filefork.presenters;

import highway62.filefork.objects.Contact;
import highway62.filefork.objects.Device;
import highway62.filefork.views.ContactDeviceNotifier;

/**
 * Created by Highway62 on 14/04/2017.
 */
public interface ContactDevicePresenter {

    void subscribe(ContactDeviceNotifier view);

    void addContact(Contact contact);

    void addDevice(Device device);

    void requestDevice(long deviceId);

    void requestContact(long contactId);

    void requestAllDevices();

    void requestAllContacts();

    void updateContactAccepted(long contactId);
}

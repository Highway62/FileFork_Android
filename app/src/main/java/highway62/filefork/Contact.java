package highway62.filefork;

/**
 * Created by Steven on 16/09/2015.
 */
public class Contact {

    private long id;
    private String contactName;
    private final String contactType = "CONTACT";
    private boolean accepted;

    public Contact(long id, String contactName, boolean accepted) {
        this.id = id;
        this.contactName = contactName;
        this.accepted = accepted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getContactType() {
        return contactType;
    }
}

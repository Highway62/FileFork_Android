package highway62.filefork;

/**
 * Created by Steven on 12/09/2015.
 *
 * Class to hold information about each fork
 */
public class Fork {

    private long id;
    private String forkName;
    private String forkType;
    private long recipient;
    String lastMsg;
    Long lastMsgTime;
    String lastMsgType;

    public Fork(){
        this.id = -1;
    }

    /**
     * Constructor for object to hold data from newly created fork
     *
     * @param forkName
     * @param forkType
     * @param recipient
     */
    public Fork(String forkName, String forkType, long recipient) {
        this.forkName = forkName;
        this.forkType = forkType;
        this.recipient = recipient;
    }

    /**
     * Constructor for object to hold data from the database (where id is known)
     */
    public Fork(long id, String forkName, String forkType, long recipient, String lastMsg, Long lastMsgTime, String lastMsgType) {
        this.id = id;
        this.forkName = forkName;
        this.forkType = forkType;
        this.recipient = recipient;
        this.lastMsg = lastMsg;
        this.lastMsgTime = lastMsgTime;
        this.lastMsgType = lastMsgType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getForkName() {
        return forkName;
    }

    public void setForkName(String forkName) {
        this.forkName = forkName;
    }

    public String getForkType() {
        return forkType;
    }

    public void setForkType(String forkType) {
        this.forkType = forkType;
    }

    public long getRecipient() {
        return recipient;
    }

    public void setRecipient(long recipient) {
        this.recipient = recipient;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public Long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(Long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public String getLastMsgType() {
        return lastMsgType;
    }

    public void setLastMsgType(String lastMsgType) {
        this.lastMsgType = lastMsgType;
    }
}

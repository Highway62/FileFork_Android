package highway62.filefork.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steven on 16/09/2015.
 */
public class Message implements Parcelable {
    private long id;
    private long forkId;
    private String text;
    private String file;
    private String type;
    private long timeStamp;
    private long sender_id;
    private boolean sent;

    /**
     * Constructor for entry  to the db
     */
    private Message(long forkId, String text, String file, String type, long sender_id, boolean sent, long timeStamp) {
        this.forkId = forkId;
        this.text = text;
        this.file = file;
        this.type = type;
        this.sender_id = sender_id;
        this.sent = sent;
        this.timeStamp = timeStamp;
    }

    /**
     * Static factory method for messages intended for the entry into the DB
     */
    public static Message newEntryMessage(long forkId, String text, String file, String type, long sender_id, boolean sent, long timeStamp){
        return new Message(forkId,text,file,type,sender_id,sent,timeStamp);
    }

    /**
     * Constructor for retrieval from the db
     */
    private Message(long id, long forkId, String text, String file, String type, long sender_id, boolean sent, long timeStamp) {
        this.id = id;
        this.forkId = forkId;
        this.text = text;
        this.file = file;
        this.type = type;
        this.sender_id = sender_id;
        this.sent = sent;
        this.timeStamp = timeStamp;
    }

    /**
     * Static factory method for messages being retrieved by the DB
     */
    public static Message newRetrievalMessage(long id, long forkId, String text, String file, String type, long sender_id, boolean sent, long timeStamp){
        return new Message(id,forkId,text,file,type,sender_id,sent,timeStamp);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getForkId() {
        return forkId;
    }

    public void setForkId(long forkId) {
        this.forkId = forkId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getSender_id() {
        return sender_id;
    }

    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.forkId);
        dest.writeString(this.text);
        dest.writeString(this.file);
        dest.writeString(this.type);
        dest.writeLong(this.timeStamp);
        dest.writeLong(this.sender_id);
        dest.writeByte(this.sent ? (byte) 1 : (byte) 0);
    }

    protected Message(Parcel in) {
        this.id = in.readLong();
        this.forkId = in.readLong();
        this.text = in.readString();
        this.file = in.readString();
        this.type = in.readString();
        this.timeStamp = in.readLong();
        this.sender_id = in.readLong();
        this.sent = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}

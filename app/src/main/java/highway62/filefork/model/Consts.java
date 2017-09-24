package highway62.filefork.model;

/**
 * Created by Steven on 08/09/2015.
 *
 * Collected general utility constants
 *
 * All members of this class are immutable
 */
public final class Consts {

    public static final String APP_NAME = "File Fork";
    public static final String FONT_ACTION_BAR = "Orbitron.ttf";
    public static final String FONT_FORK_ROW_TITLE = "Roboto.ttf";
    public static final String FORK_DATE_FORMAT = "dd/MM/yy HH:mm";
    public static final long USER_ID = 1L; //TODO set this when user logs in and store permanently.

    /**
     * Name of JSON Fork sent to ForkListAdaptor
     *
     * Used as title in Fork ListView rows
     */
    public static final String FORK_NAME = "ForkName";

    /**
     * Text of last message sent in Fork conversation
     *
     * Used as summary in Fork ListView Rows
     */
    public static final String FORK_LAST_MESSAGE = "ForkLastMessage";

    /**
     * TimeStamp of the last message sent in Fork conversation
     *
     * Used as msg timestamp in Fork ListView Row
     */
    public static final String FORK_TIMESTAMP = "ForkTimeStamp";

    /**
     * Used as intent label in ForksActivity and MessageScreenActivity for fork ID
     */
    public static final String FORK_ID = "fork_id";

    /**
     * Used as intent label in ForksActivity and MessageScreenActivity for messages list
     */
    public static final String FORK_MSGS = "fork_msgs";

    /**
     * Title displayed in the action bar of the fork message screen
     */
    public static final String FORK_MSG_SCREEN_TITLE = "Send File";

    /**
     * File types
     */
    public static final String FILETYPE_TEXT = "filetype text";
    public static final String FILETYPE_IMG = "filetype img";
    public static final String FILETYPE_DOC = "filetype doc";
    public static final String FILETYPE_VIDEO = "filetype video";
    public static final String FILETYPE_AUDIO = "filetype audio";

    /*
    public enum FILETYPE{
        TEXT, IMG, DOC, VIDEO, AUDIO
    }*/



    private Consts(){
        throw new AssertionError();
    }
}

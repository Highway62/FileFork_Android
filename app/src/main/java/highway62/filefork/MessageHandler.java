package highway62.filefork;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Highway62 on 06/07/2016.
 */
public class MessageHandler {

    MessageDAO mDao;
    ForkDAO fDao;

    public MessageHandler(Activity context){
        mDao = new MessageDAO(context);
        fDao = new ForkDAO(context);
    }

    public ArrayList<Message> loadMessagesForFork(long forkId){
        return mDao.getAllMessages(forkId);
    }

    public void addMessageToDb(Message msg){
        try{
            mDao.addMessage(msg);
        }catch(Exception e){
            Log.d("MSGHANDLR", "Error adding message to the Database");
        }
    }

    public Message downloadMessage(){
        Message m = null;
        // Contact API
        // Download message
        // Check forks for sender id
        // If fork exists with sender id
        //   Place message in message object and add to database
        //   If message activity is open
        //      update message listview
        //      update fork last message
        //   else
        //      update fork last message
        // else
        //   create new fork and add fork to db
        //   update new fork last message
        //   update fork list view
        return m;
    }

    public boolean sendMessage(Message m){
        // Place message in database with send = false
        // Update fork last message
        // Update message screen listview to show new message
        // Contact API
        // Attempt to send the message
        // If message sent
        //   update message in database with sent = true
        //   update message screen listview to show message has been sent successfully (optional)
        // else
        //   update message screen listview with option to re-send message
        return false;
    }
}

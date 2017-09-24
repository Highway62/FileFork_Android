package highway62.filefork.model.connectors;

import highway62.filefork.objects.Message;
import highway62.filefork.presenters.MessagePresenter;

/**
 * Connects to the server and handles all FTP functionality
 * Receives messages from the server and notifies the message handler
 * Receives messages from the message handler and notifies server
 * Receives confirmation that messages have been stored in the DB after being received -
 * Sends Acks back to the server so the server knows they're safe to delete.
 *
 * Created by Highway62 on 14/04/2017.
 */
public class Connector implements FTPConnector{

    MessagePresenter presenter;

    //TODO Implement this
    public Message downloadMessageFromServer(){
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

    //TODO Implement this
    public boolean sendMessageToServer(Message m){
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

    //TODO Implement this - send some message to server, return true if sent
    public boolean sendAckToServer(){
        return false;
    }

    /**
     * Notifies the presenter that a message has arrived.
     */
    private void notifyPresenter(){
        if(presenter != null){
            //TODO deal with passing message to the presenter
            presenter.notifyPresenter(null);
        }
    }

    @Override
    public void subscribe(MessagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sendMessage(Message msg) {

    }

    @Override
    public void sendAck() {

    }
}

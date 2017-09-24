package highway62.filefork.presenters;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import highway62.filefork.model.daos.MessageDAO;
import highway62.filefork.objects.Message;
import highway62.filefork.views.ForkNotifier;

/**
 * The Message 'Presenter'.
 * Views subscribe and unsubscribe to this object.
 * On message received: stores message in DB (Model), passes message to the View, tells connector to send Ack
 * On message send: stores messgage in DB (Model), tells connector to send message to server
 *
 * Created by Highway62 on 06/07/2016.
 */
public class MessageHandler implements MessagePresenter{

    ForkNotifier view;
    MessageDAO mDao;

    public MessageHandler(Activity context){
        mDao = new MessageDAO(context);
    }

    public ArrayList<Message> loadMessagesForFork(long forkId){
        return mDao.getAllMessages(forkId);
    }

    public void addMessageToDb(Message msg){
        try{
            mDao.addMessage(msg);
        }catch(Exception e){
            Log.d("MSGHANDLER", "Error adding message to the Database");
        }
    }

    private void passMessageToConnector(Message msg){

    }

    private void notifyView(Message msg){
        if(view != null){
            //TODO pass the message to the view
            //view.notifyView();
        }
    }

    @Override
    public void subscribe(ForkNotifier view) {
        this.view = view;
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void notifyPresenter(Message msg) {
        //TODO deal with receiving a message from the connector
        //Eg. put it in DB, then pass it to the view
    }

    @Override
    public Message getMessage() {
        return null;
    }


}

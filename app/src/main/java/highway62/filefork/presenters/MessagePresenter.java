package highway62.filefork.presenters;

import highway62.filefork.objects.Message;
import highway62.filefork.views.ForkNotifier;

/**
 * Uses:
 * Susbscribe to the MessageHandler class
 * Send and retrieve messages
 * Created by Highway62 on 28/03/2017.
 */
public interface MessagePresenter {

    /* Subscribe a view to the MessageHandler class to receive DB and network notifications */
    void subscribe(ForkNotifier view);

    /* Send a message to another device/contact */
    void sendMessage(Message message);

    /* Retrieve a messgage that was received from another device/contact */
    Message getMessage();

    /* Notification from the object/service the presenter is subscribed to */
    void notifyPresenter(Message msg);

}

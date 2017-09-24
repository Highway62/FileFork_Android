package highway62.filefork.presenters;

import highway62.filefork.objects.Fork;
import highway62.filefork.objects.Message;
import highway62.filefork.views.ForkNotifier;

/**
 * Created by Highway62 on 14/04/2017.
 */
public interface ForkPresenter {

    /* Subscribe a view to the ForkHandler class */
    void subscribe(ForkNotifier view);

    void unsubscribe();

    void addFork(Fork fork);

    void requestFork(long forkId);

    void requestAllForks();

    boolean forkRecipientExists(long recipientId);

    void updateLastMessage(long forkId, Message msg);
}

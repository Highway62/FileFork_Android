package highway62.filefork.views;

import java.util.ArrayList;

import highway62.filefork.objects.Fork;
import highway62.filefork.objects.Message;

/**
 * Handles notifications from presenters
 * Created by Highway62 on 28/03/2017.
 */
public interface ForkNotifier {

    /* Presenter passes fork to the view */
    void passFork(Fork fork);

    /* Presenter passes list of forks to the view */
    void passForkList(ArrayList<Fork> forkList);

    /* Notifies the view that a fork has been added successfully to the DB */
    void notifyForkAdded(boolean successful, long forkId, Exception e);

    void notifyLastMesageUpdated(long forkId, Message msg);

}

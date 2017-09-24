package highway62.filefork.presenters;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import highway62.filefork.model.daos.ForkDAO;
import highway62.filefork.objects.Fork;
import highway62.filefork.objects.Message;
import highway62.filefork.views.ForkNotifier;

/**
 * Created by Highway62 on 14/04/2017.
 */
public class ForkHandler implements ForkPresenter{

    ForkNotifier view;
    ForkDAO fDao;

    public ForkHandler(Activity context){
        fDao = new ForkDAO(context);
    }

    @Override
    public void subscribe(ForkNotifier view) {
        this.view = view;
    }

    @Override
    public void unsubscribe() {
        this.view = null;
    }

    /**
     * Receives request to add a fork to the DB.
     * Runs request in a background thread
     * Notifies the view if successful or not
     */
    @Override
    public void addFork(final Fork fork) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    notifyForkAdded(true,fDao.addFork(fork),null);
                }catch(Exception e){
                    Log.d("FORKHANDLER",e.getMessage());
                    notifyForkAdded(false,-1L,e);
                }
            }
        }).start();
    }

    /**
     * Receives request to get a fork.
     * Runs request in a background thread
     * Passes the result to the view
     */
    @Override
    public void requestFork(final long forkId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                passForkToView(fDao.getFork(forkId));
            }
        }).start();
    }

    /**
     * Receives request to get a list of all forks.
     * Runs request in a background thread
     * Passes the result to the view
     */
    @Override
    public void requestAllForks() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000); //TODO remove this (used for testing retrieving the list straight after devices added)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passForkListToView(fDao.getAllForks());
            }
        }).start();
    }

    @Override
    public void updateLastMessage(final long forkId, final Message msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(fDao.updateLastMessage(forkId, msg) > 0){
                    notifyLastMessageUpdated(forkId,msg);
                }else{
                    //TODO deal with error updating
                }
            }
        }).start();
    }

    /**
     * Checks whether a new recipient already exists in the DB as a fork
     */
    @Override
    public boolean forkRecipientExists(long recipientId) {
        return fDao.recipientExists(recipientId);
    }

    private void passForkToView(Fork fork){
        if(view != null){
            view.passFork(fork);
        }
    }

    private void passForkListToView(ArrayList<Fork> forkList){
        if(view != null){
            view.passForkList(forkList);
        }
    }

    private void notifyForkAdded(boolean successful, long forkId, Exception e){
        if(view != null){
            view.notifyForkAdded(successful,forkId, e);
        }
    }

    private void notifyLastMessageUpdated(long forkId, Message msg){
        if(view != null){
            view.notifyLastMesageUpdated(forkId, msg);
        }
    }
}

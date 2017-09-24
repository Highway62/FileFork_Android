package highway62.filefork.model.connectors;

import highway62.filefork.objects.Message;
import highway62.filefork.presenters.MessagePresenter;

/**
 * Created by Highway62 on 14/04/2017.
 */
public interface FTPConnector {

    void subscribe(MessagePresenter presenter);
    void sendMessage(Message msg);
    void sendAck();
}

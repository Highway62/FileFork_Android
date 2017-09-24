package highway62.filefork.views;

import java.util.ArrayList;

import highway62.filefork.objects.Message;

/**
 * Created by Highway62 on 14/04/2017.
 */
public interface MessageNotifier {

    void passMessage(Message msg);

    void passMessageList(ArrayList<Message> msgList);
}

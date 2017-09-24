package highway62.filefork.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import highway62.filefork.R;
import highway62.filefork.model.Consts;
import highway62.filefork.model.daos.ForkDAO;
import highway62.filefork.objects.Fork;
import highway62.filefork.objects.Message;
import highway62.filefork.presenters.MessagePresenter;

public class MessageScreenActivity extends AppCompatActivity implements MessageNotifier {

    private MessagePresenter presenter;
    private InputMethodManager imm;
    private Intent mInt;
    private ForkDAO forkDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mInt = getIntent();
        forkDao = new ForkDAO(this);

        setupKeyboardListeners();
        populateList();
    }

    /* Listens for touches to the screen which hide the keyboard */
    private void setupKeyboardListeners(){

        EditText editText = (EditText) findViewById(R.id.message_scr_editText);
        ListView chatWindow = (ListView) findViewById(R.id.message_scr_chatWindow);
        if(chatWindow != null) {
            chatWindow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return true;
                }
            });
        }

        // Listen for change to focus of EditText
        if(editText != null) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });
        }
    }

    /* Hides the keyboard if focus is lost */
    private void hideKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void populateList(){

        long fork_id;

        if(mInt.hasExtra(Consts.FORK_MSGS)){
            ArrayList<Message> msgs = mInt.getParcelableArrayListExtra(Consts.FORK_MSGS);
            MessageScreenAdapter adapter = new MessageScreenAdapter(this,msgs,Consts.USER_ID);
            ListView msgList = (ListView) findViewById(R.id.message_scr_chatWindow);
            if(msgList != null){
                msgList.setAdapter(adapter);
            }
        }

        if(mInt.hasExtra(Consts.FORK_ID)){
            // Set fork name in action bar
            fork_id = mInt.getLongExtra(Consts.FORK_ID, -1L);
            if(fork_id != -1){
                Fork f = forkDao.getFork(fork_id);
                if(f != null){
                    setupActionBar(f.getForkName());
                }
            }
        }

        /*
        // Set up dummy messages for testing
        ArrayList<Message> msgs = new ArrayList<>();
        Message m1 = new Message(1, "Hi look at this pic", "pic.jpg", Consts.FILETYPE_IMG, 1L, true, System.currentTimeMillis());
        msgs.add(m1);
        Message m2 = new Message(2, "Yeehoo", "pic.jpg", Consts.FILETYPE_TEXT, 1L, true, System.currentTimeMillis());
        msgs.add(m2);
        Message m3 = new Message(3, "Check this Doc", "pic.jpg", Consts.FILETYPE_DOC, 1L, true, System.currentTimeMillis());
        msgs.add(m3);*/
    }

    /* Set up ActionBar with fork name */
    private void setupActionBar(String name){
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(name);
            } else {
                throw new Exception("Error Setting Support Action Bar");
            }
        } catch (Exception e){
            Log.d("MSGSCREEN", e.getMessage());
        }
    }

    /* Receives message from the presenter */
    @Override
    public void passMessage(Message msg) {

    }

    /* Receives list of messages from the presenter */
    @Override
    public void passMessageList(ArrayList<Message> msgList) {

    }
}
package highway62.filefork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MessageScreenActivity extends AppCompatActivity {

    private InputMethodManager imm;
    private long forkId;
    private Intent mInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mInt = getIntent();
        forkId = mInt.getLongExtra(Consts.FORK_ID, -1);

        setupActionBar();
        setupKeyboardListeners();
        populateList();
    }

    private void setupActionBar(){
        // Set up Action Bar
        SpannableString s = new SpannableString(Consts.FORK_MSG_SCREEN_TITLE);
        s.setSpan(new TypefaceSpan(this, Consts.FONT_ACTION_BAR), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Green)), 0, 1, 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Green)), 5, 6, 0);


        //Update Action Bar Title with TypefaceSpan instance
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(s);
            } else {
                throw new Exception("Error Setting Support Action Bar");
            }
        } catch (Exception e){
            System.err.print(e.getMessage());
        }
    }

    private void setupKeyboardListeners(){

        EditText editText = (EditText) findViewById(R.id.message_scr_editText);
        ListView chatWindow = (ListView) findViewById(R.id.message_scr_chatWindow);
        chatWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return true;
            }
        });

        // Listen for change to focus of EditText
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void populateList(){

        if(mInt.hasExtra(Consts.FORK_MSGS)){
            ArrayList<Message> msgs = mInt.getParcelableArrayListExtra(Consts.FORK_MSGS);
            MessageScreenAdapter adapter = new MessageScreenAdapter(this,msgs,Consts.USER_ID);
            ListView msgList = (ListView) findViewById(R.id.message_scr_chatWindow);
            msgList.setAdapter(adapter);
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

}

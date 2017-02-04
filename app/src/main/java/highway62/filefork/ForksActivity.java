package highway62.filefork;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ForksActivity extends AppCompatActivity{

    //ListView forkList;
    CustomSwipeRefreshLayout forkSwipeContainer;
    ArrayList<Fork> forks;
    ForkDAO forkDAO;
    ContactDeviceDAO contactDeviceDAO; // For testing fork retrieval (each fork needs a recipient device)
    MessageDAO messageDAO; // For testing opening forks
    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forks);
        forkDAO = new ForkDAO(this);
        contactDeviceDAO = new ContactDeviceDAO(this);
        messageDAO = new MessageDAO(this);
        mContainerView = (ViewGroup) findViewById(R.id.container);


        // Set the font and colour of the Action Bar title
        SpannableString s = new SpannableString(Consts.APP_NAME);
        s.setSpan(new TypefaceSpan(this, Consts.FONT_ACTION_BAR), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Green)), 0, 9, 0);


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

        //Test notification icon /////////////////////////////////////////////////////////
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_fork)
                .setContentTitle("New File Received")
                .setContentText("Click here to view file");

        int notId = 001;
        NotificationManager notMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notMgr.notify(notId, mBuilder.build()); //////////////////////////////////////////





        // Testing Add Forks to DB and retrieve them to display///////////////////////////

        // Add some devices to act as recipients
        Device dev1 = new Device(1, "iPhone", DBContract.DeviceTypes.IPHONE);
        Device dev2 = new Device(2, "Android", DBContract.DeviceTypes.ANDROID);
        Device dev3 = new Device(3, "Windows", DBContract.DeviceTypes.WINDOWS);

        long dev1Id = 0;
        long dev2Id = 0;
        long dev3Id = 0;

        try {
            dev1Id = contactDeviceDAO.addDevice(dev1);
            dev2Id = contactDeviceDAO.addDevice(dev2);
            dev3Id = contactDeviceDAO.addDevice(dev3);
        } catch (Exception e){
            Log.v("ForksActivityDevices", e.getMessage());
        }

        // Add some forks
        Fork f1 = new Fork(dev1.getDeviceName(), DBContract.ForkTypes.IPHONE, dev1Id);
        Fork f2 = new Fork(dev2.getDeviceName(), DBContract.ForkTypes.ANDROID, dev2Id);
        //Fork f3 = new Fork(dev3.getDeviceName(), DBContract.ForkTypes.WINDOWS, dev3Id);

        long forkId1 = 0;
        long forkId2 = 0;
        //long forkId3 = 0;

        try {
            forkId1 = forkDAO.addFork(f1);
            forkId2 = forkDAO.addFork(f2);
            //forkId3 = forkDAO.addFork(f3);
        } catch (Exception e) {
            Log.v("ForksActivityForks", e.getMessage());
        }

        // Update the last message and timestamp of the forks
        Message msg1 = new Message(forkId1,"testing message 1",null, DBContract.MessageTypes.TEXT, Consts.USER_ID,true,1333125342L);
        Message msg2 = new Message(forkId2,"testing message 2",null, DBContract.MessageTypes.TEXT, Consts.USER_ID,true,1334125342L);
        //Message msg3 = new Message(forkId3,"testing message 3",null, DBContract.MessageTypes.TEXT, dev1Id,true,1335125342L);

        MessageHandler mHandler = new MessageHandler(this);
        mHandler.addMessageToDb(msg1);
        mHandler.addMessageToDb(msg2);
        //mHandler.addMessageToDb(msg3);

        forkDAO.updateLastMessage(forkId1, msg1);
        forkDAO.updateLastMessage(forkId2, msg2);
        //forkDAO.updateLastMessage(forkId3, msg3);

        // Add all the forks to the layout
        refreshForks();

        // Set Custom List View Rows
        forkSwipeContainer = (CustomSwipeRefreshLayout) findViewById(R.id.swipelayout);
        //ListView forkList = (ListView) forkSwipeContainer.findViewById(R.id.forks_list);
        //final ForkListAdapter forkListAdapter = new ForkListAdapter(this, forks);
        //forkList.setAdapter(forkListAdapter);

        forkSwipeContainer.setCustomHeadview(new CustomSwipeHeadView(this));
        forkSwipeContainer.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        //Tint the fork button in the action bar
        MenuItem forkBtn = menu.findItem(R.id.action_forks_btn);
        Drawable tinted_forkBtn = forkBtn.getIcon();
        tinted_forkBtn.mutate().setColorFilter(getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_IN);
        forkBtn.setIcon(tinted_forkBtn);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings_btn) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSwipeRefresh(){
        Toast t = new Toast(getApplicationContext());
        t.makeText(this, "New Fork Created", Toast.LENGTH_LONG).show();
        addNewFork();
        forkSwipeContainer.refreshComplete();
    }

    private void refreshForks(){
        // Retrieve Fork List as an Array of forks
        forks = forkDAO.getAllForks();

        // Remove previous fork rows, if any
        if(mContainerView.getChildCount() > 0){
            mContainerView.removeAllViews();
        }
        // Add forks to the list layout
        for(int i = 0; i < forks.size(); i++){
            addItem(i);
        }
    }

    /**
     * Adds rows to the Linear Layout read from the DB file
     * @param position
     */
    private void addItem(int position) {
        // Instantiate a new "row" view.
        final ViewGroup row = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.forks_row, mContainerView, false);

        // Get the text views of the row
        TextView rowTitle = (TextView) row.findViewById(R.id.forks_row_title);
        TextView rowSummary = (TextView) row.findViewById(R.id.forks_row_summary);
        TextView rowTimeStamp = (TextView) row.findViewById(R.id.fork_row_timestamp);

        //Fork fork = forks.get(position);
        Fork fork = forkDAO.getFork(forks.get(position).getId()); // for testing (need to get fork from db after updating its last message)

        // Get the title and summary from the fork
        String title = fork.getForkName();
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, Consts.FONT_FORK_ROW_TITLE), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String summary = fork.getLastMsg();
        String timeStamp = null;
        if(summary != null){
            timeStamp = getDateTime(fork.getLastMsgTime());
        }

        // Set the title of the row view as the title from JSON fork
        rowTitle.setText(s);
        rowSummary.setText(summary);
        rowTimeStamp.setText(timeStamp);

        mContainerView.addView(row);
        //mContainerView.removeView(newView);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowSelected(mContainerView.indexOfChild(row));
            }
        });
    }

    /**
     * Adds rows to the Linear Layout after user creates a new fork by swiping
     */
    private void addNewFork() {
        // Instantiate a new "row" view.
        final ViewGroup row = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.forks_row, mContainerView, false);

        // Get the text views of the row
        TextView rowTitle = (TextView) row.findViewById(R.id.forks_row_title);
        TextView rowLastMsg = (TextView) row.findViewById(R.id.forks_row_summary);

        Fork f = new Fork();
        rowTitle.setText("New Fork");
        rowLastMsg.setText("Click to Add New Recipient");
        forks.add(0, f);

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(row, 0);
        //mContainerView.removeView(newView);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowSelected(mContainerView.indexOfChild(row));
            }
        });
    }

    private void onRowSelected(final int position){
        long fork_id = forks.get(position).getId();
        // Check if fork is newly created (id of -1), if so ask for recipient
        if(fork_id == -1){
            //launch 'set recipient' dialog, once it has recipient, add it to the database + launch msg screen
            DeviceDialog dialog = new DeviceDialog(this);
            dialog.setDialogObserver(new DeviceDialog.DialogObserver() {
                @Override
                public void notifyResult(long device_id) {
                    // Check if device is already open in fork. If so, switch to that
                    boolean exists = false;
                    long existingForkId = -1l;
                    for(int i = 0; i < forks.size(); i++){
                        Fork f = forks.get(i);
                        if(f.getRecipient() == device_id){
                            // Fork already created
                            exists = true;
                            existingForkId = f.getId();
                            break;
                        }
                    }

                    if(exists && existingForkId != -1){
                        launchMessageScreen(existingForkId);
                    }else{
                        // New Fork
                        long newforkId = addNewForkToDb(device_id, position);
                        if(newforkId != -1L){
                            launchMessageScreen(newforkId);
                        }else{
                            Log.d("FORKSCREEN", "Error adding fork to DB from message screen");
                        }
                    }
                }
            });
            dialog.show();
        }else{
            //launch message srceen for fork
            launchMessageScreen(fork_id);
        }
    }

    private long addNewForkToDb(long device_id, int position){
        long newForkId = -1;
        Fork f = forks.get(position);
        Device d = contactDeviceDAO.getDeviceById(device_id);
        if(f != null && d != null){
            // Set fork name to device name
            f.setForkName(d.getDeviceName());
            f.setForkType(d.getDeviceType());
            // Set blank last message for summary
            f.setLastMsg("");
            try {
                newForkId = forkDAO.addFork(f);
            } catch (Exception e) {
                Log.d("FORKSCREEN", e.getMessage());
            }
        }
        return newForkId;
    }

    private void launchMessageScreen(long forkId){
        Intent intent = new Intent(this, MessageScreenActivity.class);
        intent.putExtra(Consts.FORK_ID, forkId);
        // Get fork messages
        MessageHandler mHandler = new MessageHandler(this);
        ArrayList<Message> forkMsgs = mHandler.loadMessagesForFork(forkId);
        if(forkMsgs != null){
            intent.putParcelableArrayListExtra(Consts.FORK_MSGS,forkMsgs);
        }
        startActivity(intent);
    }

    private String getDateTime(long time) {

        String timestamp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        Date messageDate = new Date(time);
        if (sdf.format(today).equals(sdf.format(messageDate))) {
            // If date of message is today then display the time
            SimpleDateFormat tf = new SimpleDateFormat("kk:mm");
            timestamp = tf.format(messageDate);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
            timestamp = df.format(messageDate);
        }

        return timestamp;
    }

    // TODO change onPause(?) or onResume(?) so that when back is pressed from the message screen, or the screen is reloaded...
    // Then the fork list is refreshed from the database.
}

package highway62.filefork.views;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import highway62.filefork.R;
import highway62.filefork.model.Consts;
import highway62.filefork.model.DBContract;
import highway62.filefork.objects.Contact;
import highway62.filefork.objects.Device;
import highway62.filefork.objects.Fork;
import highway62.filefork.objects.Message;
import highway62.filefork.presenters.ContactDeviceHandler;
import highway62.filefork.presenters.ContactDevicePresenter;
import highway62.filefork.presenters.ForkHandler;
import highway62.filefork.presenters.ForkPresenter;
import highway62.filefork.presenters.MessageHandler;
import highway62.filefork.presenters.MessagePresenter;
import highway62.filefork.views.dialogs.DeviceDialog;
import highway62.filefork.views.typefaces.TypefaceSpan;

public class ForksActivity extends AppCompatActivity implements ForkNotifier, ContactDeviceNotifier{

    //ListView forkList;
    private CustomSwipeRefreshLayout forkSwipeContainer;
    private ViewGroup mContainerView;
    private ForkPresenter presenter;
    private ContactDevicePresenter contactDevicePresenter;
    private MessagePresenter msgPresenter;
    private ArrayList<Fork> forks;
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forks);
        mContainerView = (ViewGroup) findViewById(R.id.container);
        setupActionBar();
        presenter = new ForkHandler(this);
        contactDevicePresenter = new ContactDeviceHandler(this);
        msgPresenter = new MessageHandler(this);
        presenter.subscribe(this);
        contactDevicePresenter.subscribe(this);
        msgPresenter.subscribe(this);
        forks = new ArrayList<>();

        //Test notification icon /////////////////////////////////////////////////////////
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_fork)
                .setContentTitle("New File Received")
                .setContentText("Click here to view file");

        int notId = 001;
        NotificationManager notMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notMgr.notify(notId, mBuilder.build()); //////////////////////////////////////////

        // Show loading spinner before retrieving forks
        loadingSpinner = (ProgressBar)findViewById(R.id.forkLoadingSpinner);

        // Add some devices to act as recipients
        Device dev1 = Device.newDevice(19, "iPhone", DBContract.DeviceTypes.IPHONE);
        Device dev2 = Device.newDevice(20, "Android", DBContract.DeviceTypes.ANDROID);
        contactDevicePresenter.addDevice(dev1);
        contactDevicePresenter.addDevice(dev2);

        // Set Custom List View Rows
        forkSwipeContainer = (CustomSwipeRefreshLayout) findViewById(R.id.swipelayout);
        forkSwipeContainer.setCustomHeadview(new CustomSwipeHeadView(this));
        forkSwipeContainer.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });

        requestForksFromPresenter();

    }

    private void setupActionBar(){
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
            Log.d("FORKS_ACTIVITY",e.getMessage());
        }
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

        // Check if new empty fork already created, if not, then add a new empty fork
        if(forks != null){
            if(forks.isEmpty() || forks.get(0) == null || forks.get(0).getRecipient() > 0){
                Toast t = new Toast(getApplicationContext());
                t.makeText(this, "New Fork Created", Toast.LENGTH_LONG).show();
                addNewFork();
            }
        }
        forkSwipeContainer.refreshComplete();
    }

    private void requestForksFromPresenter(){
        presenter.requestAllForks();
    }

    private void initialiseForks(ArrayList<Fork> forkList){

        loadingSpinner.setVisibility(View.GONE);
        // Remove previous fork rows, if any
        if(mContainerView.getChildCount() > 0){
            mContainerView.removeAllViews();
        }

        if(forkList != null) {
            forks = forkList;
            // Add forks to the list layout
            for (int i = 0; i < forkList.size(); i++) {
                addForkToView(forkList.get(i));
            }
        }

    }

    /**
     * Adds rows to the Linear Layout read from the DB file
     */
    private void addForkToView(Fork fork) {

        // Instantiate a new "row" view.
        final ViewGroup row = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.forks_row, mContainerView, false);

        // Get the text views of the row
        TextView rowTitle = (TextView) row.findViewById(R.id.forks_row_title);
        TextView rowSummary = (TextView) row.findViewById(R.id.forks_row_summary);
        TextView rowTimeStamp = (TextView) row.findViewById(R.id.fork_row_timestamp);

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

        rowTitle.setText("New Fork");
        rowLastMsg.setText("Select to Add New Recipient");

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

        // Add blank fork to local fork list
        Fork f = Fork.newEmptyFork();
        forks.add(0,f);
    }

    //TODO - FIX this, when you select a new empty fork and select a device, the fork name should change when you go back from the message screen. It's not working.
    private void onRowSelected(final int position){
        final Fork forkSelected = forks.get(position);
        // Check if fork is newly created (id of -1), if so ask for recipient
        if(forkSelected.getId() == -1){
            //launch 'set recipient' dialog, once it has recipient, add it to the database + launch msg screen
            final DeviceDialog dialog = new DeviceDialog(this);
            dialog.setDialogObserver(new DeviceDialog.DialogObserver() {

                @Override
                public void notifyResultDevice(Device device) {
                    // Check if device is already open in fork. If so, switch to that
                    // and delete newly created fork
                    boolean exists = false;
                    long existingForkId = -1L;
                    for(int i = 0; i < forks.size(); i++){
                        Fork f = forks.get(i);
                        if(f.getRecipient() == device.getId()){
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
                        forkSelected.setForkName(device.getDeviceName());
                        forkSelected.setForkType(device.getDeviceType());
                        forkSelected.setRecipient(device.getId());
                        forkSelected.setLastMsg("");
                        presenter.addFork(forkSelected);
                    }

                    dialog.dismiss();
                }

                @Override
                public void notifyResultContact(Contact contact) {
                    // Check if contact is already open in fork. If so, switch to that
                    // and delete newly created fork
                    boolean exists = false;
                    long existingForkId = -1L;
                    for(int i = 0; i < forks.size(); i++){
                        Fork f = forks.get(i);
                        if(f.getRecipient() == contact.getId()){
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
                        forkSelected.setForkName(contact.getContactName());
                        forkSelected.setForkType(contact.getContactType());
                        forkSelected.setRecipient(contact.getId());
                        forkSelected.setLastMsg("");
                        presenter.addFork(forkSelected);
                    }

                    dialog.dismiss();
                }
            });
            dialog.show();
        }else{
            //launch message screen for fork
            launchMessageScreen(forkSelected.getId());
        }
    }

    private void updateForkInView(Fork forkToUpdate, int position){
        // Instantiate a new "row" view.
        final ViewGroup row = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.forks_row, mContainerView, false);

        //TODO sort this - get the row from the viewgroup (mContainerView), and update the title etc
        //TODO of the row in the viewGroup. It's not enough to only update the fork in the local forklist (forks)

        // Get the text views of the row
        TextView rowTitle = (TextView) row.findViewById(R.id.forks_row_title);
        TextView rowSummary = (TextView) row.findViewById(R.id.forks_row_summary);
        TextView rowTimeStamp = (TextView) row.findViewById(R.id.fork_row_timestamp);

        // Get the title and summary from the fork
        String title = forkToUpdate.getForkName();
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, Consts.FONT_FORK_ROW_TITLE), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String summary = forkToUpdate.getLastMsg();
        String timeStamp = null;
        if(summary != null){
            timeStamp = getDateTime(forkToUpdate.getLastMsgTime());
        }

        // Set the title of the row view as the title from JSON fork
        rowTitle.setText(s);
        rowSummary.setText(summary);
        rowTimeStamp.setText(timeStamp);

        mContainerView.addView(row);
        //mContainerView.removeView(newView);
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

    @Override
    public void passFork(Fork fork) {

    }

    @Override
    public void passForkList(final ArrayList<Fork> forkList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initialiseForks(forkList);
            }
        });
    }

    @Override
    public void notifyForkAdded(boolean successful, long forkId, Exception e) {
        // Switch to the newly added fork
        if(successful){
            launchMessageScreen(forkId);
        }else{
            ForksActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast t = new Toast(getApplicationContext());
                    t.makeText(ForksActivity.this, "Error Creating New Fork, Please Try Again", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void notifyLastMesageUpdated(long forkId, Message msg) {

    }

    @Override
    public void notifyContactAdded(boolean success, Exception e) {

    }

    @Override
    public void notifyDeviceAdded(boolean success, Exception e) {

    }

    @Override
    public void passContactToView(Contact contact, Exception e) {

    }

    @Override
    public void passDeviceToView(Device device) {

    }

    @Override
    public void passContactListToView(ArrayList<Contact> contactList) {

    }

    @Override
    public void passDeviceListToView(ArrayList<Device> deviceList) {

    }

    @Override
    public void notifyContactAccepted(Contact contact) {

    }

    /**
     * Called after the back button is pressed from a different activity
     * E.g The message Screen activity.
     * Refresh the list of forks after returning from a different activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if(presenter != null){
            presenter.requestAllForks();
        }
    }
}

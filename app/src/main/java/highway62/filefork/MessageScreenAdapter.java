package highway62.filefork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Highway62 on 30/06/2016.
 */
public class MessageScreenAdapter extends BaseAdapter {

    Context context;
    ArrayList<Message> messages;
    private static LayoutInflater inflater = null;
    long userID;

    public MessageScreenAdapter(Context c, ArrayList<Message> m, long uid) {
        this.context = c;
        this.messages = m;
        this.userID = uid;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message msg = messages.get(position);
        ImageView msgImg = null;
        TextView msgFileName = null;
        TextView msgText = null;
        TextView msgTime = null;
        RelativeLayout imgContainer = null;
        RelativeLayout fNameContainer = null;
        RelativeLayout textContainer = null;
        LinearLayout msgBodyContainer = null;

        String mType = msg.getType();
        String mText = msg.getText();
        long mTimeStamp = msg.getTimeStamp();
        String mTime = getDateTime(mTimeStamp);
        String mFname = msg.getFile();

        View view = convertView;
        if (view == null) {
            // Inflate the correct layout based on whether the user sent the message
            if (msg.getSender_id() == userID) {
                // User sent the message
                view = inflater.inflate(R.layout.message_row_sender, null);
                // Get relevant views from the layout
                msgBodyContainer = (LinearLayout) view.findViewById(R.id.msg_scr_sender_msg_body_container);
                imgContainer = (RelativeLayout) view.findViewById(R.id.message_scr_sender_file_img_container);
                fNameContainer = (RelativeLayout) view.findViewById(R.id.message_scr_sender_fileName_container);
                textContainer = (RelativeLayout) view.findViewById(R.id.message_scr_sender_msg_container);

                msgImg = (ImageView) view.findViewById(R.id.message_scr_sender_file_img);
                msgFileName = (TextView) view.findViewById(R.id.message_scr_sender_fileName);
                msgText = (TextView) view.findViewById(R.id.message_scr_sender_msg);
                msgTime = (TextView) view.findViewById(R.id.message_scr_sender_time);
            } else {
                // User received the message
                view = inflater.inflate(R.layout.message_row_receiver, parent, false);
                // Get relevant views from the layout
                msgImg = (ImageView) view.findViewById(R.id.message_scr_receiver_file_img);
                msgFileName = (TextView) view.findViewById(R.id.message_scr_receiver_fileName);
                msgText = (TextView) view.findViewById(R.id.message_scr_receiver_msg);
                msgTime = (TextView) view.findViewById(R.id.message_scr_receiver_time);
            }
        }

        if (msgBodyContainer != null && msgImg != null && msgFileName != null && msgText != null && msgTime != null) {

            switch (mType) {
                case Consts.FILETYPE_TEXT:
                    msgBodyContainer.removeView(imgContainer);
                    msgBodyContainer.removeView(fNameContainer);
                    break;
                case Consts.FILETYPE_IMG:
                    //TODO set msgImg to image icon
                    msgImg.setImageResource(R.drawable.ic_forks_row_arrow);
                    msgFileName.setText(mFname);
                    break;
                case Consts.FILETYPE_DOC:
                    //TODO set msgImg to doc icon
                    msgFileName.setText(mFname);
                    break;
                case Consts.FILETYPE_VIDEO:
                    //TODO set msgImg to video icon
                    msgFileName.setText(mFname);
                    break;
                case Consts.FILETYPE_AUDIO:
                    //TODO set msgImg to audio icon
                    msgFileName.setText(mFname);
                    break;
            }

            if (mText == null || mText.isEmpty()) {
                msgBodyContainer.removeView(textContainer);
            } else {
                msgText.setText(mText);
            }

            msgTime.setText(mTime);
        }

        return view;
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
}

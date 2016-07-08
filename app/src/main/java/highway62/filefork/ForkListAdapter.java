package highway62.filefork;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Steven on 08/09/2015.
 */
public class ForkListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Fork> forks;
    private static LayoutInflater inflater = null;

    public ForkListAdapter(Context context, ArrayList<Fork> forks) {
        this.context = context;
        this.forks = forks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return forks.size();
    }

    @Override
    public Object getItem(int position) {
        return forks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.forks_row, null);
        }

        // Get the text views of the row
        TextView rowTitle = (TextView) view.findViewById(R.id.forks_row_title);
        TextView rowSummary = (TextView) view.findViewById(R.id.forks_row_summary);
        TextView rowTimeStamp = (TextView) view.findViewById(R.id.fork_row_timestamp);

        // Get the JSON fork from the array
        Fork fork = forks.get(position);

        // Get the title and summary from the fork
        String title = fork.getForkName();
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this.context, Consts.FONT_FORK_ROW_TITLE), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String summary = fork.getLastMsg();
        String timeStamp = null;
        if(summary != null){
           timeStamp = new SimpleDateFormat(Consts.FORK_DATE_FORMAT).format(new Date(fork.getLastMsgTime() * 1000L));
        }

        // Set the title of the row view as the title from JSON fork
        rowTitle.setText(s);
        rowSummary.setText(summary);
        rowTimeStamp.setText(timeStamp);


        return view;
    }

}

package highway62.filefork.views.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import highway62.filefork.R;
import highway62.filefork.objects.Contact;

/**
 * Created by Highway62 on 07/07/2016.
 */
public class DialogContactRowAdapter extends BaseAdapter{

    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Contact> contacts;

    public DialogContactRowAdapter(Context c, ArrayList<Contact> con){
        this.context = c;
        this.contacts = con;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.dialog_contact_row, parent, false);
        }

        // Get the views of the row
        TextView contactText = (TextView) view.findViewById(R.id.dialog_contact_row_text);

        // Get the contact
        Contact c = contacts.get(position);

        // Get the contact name
        String contactName = c.getContactName();

        // Set row text to contact name
        contactText.setText(contactName);

        return view;
    }
}

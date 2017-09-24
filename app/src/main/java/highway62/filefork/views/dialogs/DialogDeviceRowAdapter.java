package highway62.filefork.views.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import highway62.filefork.R;
import highway62.filefork.objects.Device;

/**
 * Created by Highway62 on 07/07/2016.
 */
public class DialogDeviceRowAdapter extends BaseAdapter{

    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Device> devices;

    public DialogDeviceRowAdapter(Context c, ArrayList<Device> d){
        this.context = c;
        this.devices = d;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.dialog_device_row, parent, false);
        }

        // Get the views of the row
        ImageView deviceImg = (ImageView) view.findViewById(R.id.dialog_device_row_img);
        TextView deviceText = (TextView) view.findViewById(R.id.dialog_device_row_text);

        // Get the device
        Device d = devices.get(position);

        // Get the device details
        String deviceType = d.getDeviceType();
        String deviceName = d.getDeviceName();

        //TODO switch device type and set correct imae for device dialog row
        deviceText.setText(deviceName);

        return view;
    }
}

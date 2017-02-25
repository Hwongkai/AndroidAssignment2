package a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.R;
import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.data.Message;

/**
 * Created by huangkai on 2017/2/6.
 */

public class MsgAdapter extends ArrayAdapter<Message> {
    private int resourceId;

    public MsgAdapter(Context context, int textViewResourceId, List<Message> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        class ViewHolder{
            LinearLayout leftLayout;
            LinearLayout rightLayout;
            TextView leftMsg;
            TextView rightMsg;
            TextView timeofright;
            TextView timeofleft;
            TextView R_Username;
            TextView S_Username;
        }
        Message msg = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView)view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView)view.findViewById(R.id.right_msg);
            viewHolder.timeofright = (TextView) view.findViewById(R.id.time_right);
            viewHolder.timeofleft = (TextView) view.findViewById(R.id.time_left);
            viewHolder.R_Username = (TextView) view.findViewById(R.id.left_name);
            viewHolder.S_Username = (TextView) view.findViewById(R.id.right_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if(msg.getType() == Message.Received) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
            viewHolder.timeofleft.setText(msg.getTime());
            viewHolder.R_Username.setText(msg.getName());

        } else if(msg.getType() == Message.send) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.getContent());
            viewHolder.timeofright.setText(msg.getTime());
            viewHolder.S_Username.setText(msg.getName());

        }
        return view;

    }


}

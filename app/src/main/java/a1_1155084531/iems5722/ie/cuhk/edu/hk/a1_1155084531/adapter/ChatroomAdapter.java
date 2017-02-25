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
import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.data.Chatroom;
import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.data.Message;

/**
 * Created by huangkai on 2017/2/16.
 */

public class ChatroomAdapter extends ArrayAdapter<Chatroom>{

    private int resourceId;

    public ChatroomAdapter(Context context, int textViewResourceId, List<Chatroom> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        class ViewHolder {
            TextView chatroom_name;
        }
        Chatroom chatroom = getItem(position);
        View view;
        ViewHolder viewHolder;

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.chatroom_name = (TextView) view.findViewById(R.id.chatroom_name);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.chatroom_name.setText(chatroom.getContent());


        return view;

    }
}

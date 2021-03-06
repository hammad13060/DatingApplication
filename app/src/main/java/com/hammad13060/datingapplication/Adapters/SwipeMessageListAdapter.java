package com.hammad13060.datingapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.provider.BaseColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.hammad13060.datingapplication.Activities.ChatActivity;
import com.hammad13060.datingapplication.R;
import com.hammad13060.datingapplication.helper.MessageClientHelper;
import com.parse.ParseObject;

import java.lang.reflect.AccessibleObject;
import java.util.List;

/**
 * Created by Hammad on 13-11-2015.
 */
public class SwipeMessageListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ParseObject> messages;

    public SwipeMessageListAdapter(Activity activity, List<ParseObject> messages){
        this.activity = activity;
        this.messages = messages;
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
    public long getItemId(int i) {
        return 0;
    }

    public List<ParseObject> getMessages() {
        return messages;
    }

    public void setMessages(List<ParseObject> messages) {
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.message_row, null);

        ParseObject messageObject = messages.get(position);
        String textMessage = messageObject.getString(MessageClientHelper.MESSAGE_TEXT_MESSAGE);
        String user_id = messageObject.getString(MessageClientHelper.MESSAGE_SENDER_ID);

        TextView messageTextView = null;

        if (user_id.equals(AccessToken.getCurrentAccessToken().getUserId())) {
            messageTextView = (TextView)convertView.findViewById(R.id.my_message_text_view);
        } else {
            messageTextView = (TextView)convertView.findViewById(R.id.sender_message_text_view);
        }

        messageTextView.setText(textMessage);

        return convertView;


    }
}

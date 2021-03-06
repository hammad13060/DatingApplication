package com.hammad13060.datingapplication.Activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.hammad13060.datingapplication.Adapters.SwipeMessageListAdapter;
import com.hammad13060.datingapplication.BroadcastRecievers.NewMessageBroadcastReceiver;
import com.hammad13060.datingapplication.Fragments.DisplayMatchFragment;
import com.hammad13060.datingapplication.Interfaces.UpdateLayoutInterface;
import com.hammad13060.datingapplication.R;
import com.hammad13060.datingapplication.helper.MessageClientHelper;
import com.parse.ParseObject;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.messaging.MessageClientListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends MainActivity implements SwipeRefreshLayout.OnRefreshListener, UpdateLayoutInterface {

    private String recipient_user_id = null;
    private String recipient_user_name = null;
    private String chat_id = null;

    private ListView messageListView = null;
    //SwipeRefreshLayout swipeRefreshLayout = null;
    //ScrollView scrollView = null;


    private List<ParseObject> messages = null;

    MessageClientHelper messageClientHelper = null;

    private SwipeMessageListAdapter adapter = null;

    BroadcastReceiver newMessageBroadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        recipient_user_id = intent.getStringExtra(DisplayMatchFragment.EXTRA_RECIPIENT_USER_ID);
        recipient_user_name = intent.getStringExtra(DisplayMatchFragment.EXTRA_RECIPIENT_USER_NAME);
        chat_id = intent.getStringExtra(DisplayMatchFragment.EXTRA_CHAT_ID);

        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        //scrollView = (ScrollView)findViewById(R.id.scrollView);

        messageListView = (ListView)findViewById(R.id.listView);

        messageClientHelper = MessageClientHelper.getInstance(this);

        //messages = messageClientHelper.fetchMessages(chat_id);

        messages = new ArrayList<>(0);
        adapter = new SwipeMessageListAdapter(this, messages);

        messageListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMessages();
        registerNewMessageReceiver();

        //scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterNewMessageReceiver();
    }

    private void fetchMessages() {
        messages = messageClientHelper.fetchMessages(chat_id);
        adapter.setMessages(messages);
        adapter.notifyDataSetChanged();
        messageListView.deferNotifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        fetchMessages();
    }

    public void onSendButtonClicked(View view) {
        EditText message_box = (EditText)findViewById(R.id.message_text_box);
        String message = message_box.getText().toString();

        messageClientHelper.sendTextMessage(recipient_user_id, message, chat_id);

        message_box.setText("");
        message_box.setHint("enter message here");
    }

    @Override
    public void updateLayoutOnEvent() {

        fetchMessages();
        Toast.makeText(this, "new message received", Toast.LENGTH_SHORT).show();
    }
    public void updateLayoutOnEvent(String message) {
        ParseObject object = new ParseObject(MessageClientHelper.CLASS_MESSAGE);
        object.put(MessageClientHelper.MESSAGE_TEXT_MESSAGE, message);
        object.put(MessageClientHelper.MESSAGE_SENDER_ID, AccessToken.getCurrentAccessToken().getUserId());
        messages.add(object);
        Toast.makeText(this, "your message delivered", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    private void registerNewMessageReceiver() {
        IntentFilter filter = new IntentFilter(NewMessageBroadcastReceiver.EVENT_NEW_MESSAGE);
        newMessageBroadcastReceiver = new NewMessageBroadcastReceiver(this, chat_id);
        registerReceiver(newMessageBroadcastReceiver, filter);
    }

    private void unRegisterNewMessageReceiver() {
        unregisterReceiver(newMessageBroadcastReceiver);
    }
}

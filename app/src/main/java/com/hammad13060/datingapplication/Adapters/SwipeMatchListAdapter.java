package com.hammad13060.datingapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hammad13060.datingapplication.Activities.ChatActivity;
import com.hammad13060.datingapplication.DBEntity.Person;
import com.hammad13060.datingapplication.Fragments.DisplayMatchFragment;
import com.hammad13060.datingapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hammad on 08-11-2015.
 */
public class SwipeMatchListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Person> matchList;
    private String[] bgColors;

    private Person match;


    public SwipeMatchListAdapter(Activity activity, List<Person> matchList) {
        this.activity = activity;
        this.matchList = matchList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.matches_serial_bg);
    }


    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int location) {
        return matchList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.list_row, null);

        ImageView match_image = (ImageView) convertView.findViewById(R.id.match_image);
        TextView match_name = (TextView) convertView.findViewById(R.id.match_name);

        Person match = matchList.get(position);

        setPicture(match.get_url(), match_image);
        match_name.setText(match.get_name());

        onMatchClicked(convertView, match);

        String color = bgColors[position % bgColors.length];
        match_name.setBackgroundColor(Color.parseColor(color));

        return convertView;

    }

    private void setPicture(String url, ImageView imageView) {
        Picasso.with(activity).load(url).into(imageView);
    }

    private void onMatchClicked(View view, Person match_given) {
        final Person match = match_given;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChatActivity.class);
                intent.putExtra(DisplayMatchFragment.EXTRA_RECIPIENT_USER_ID, match.get_user_id());
                intent.putExtra(DisplayMatchFragment.EXTRA_RECIPIENT_USER_NAME, match.get_name());
                intent.putExtra(DisplayMatchFragment.EXTRA_CHAT_ID, match.get_chat_id());
                activity.startActivity(intent);
            }
        });
    }
}
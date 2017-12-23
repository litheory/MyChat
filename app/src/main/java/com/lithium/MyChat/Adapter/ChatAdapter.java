package com.lithium.MyChat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lithium.MyChat.R;
import com.lithium.MyChat.Class.Chat;


import java.util.List;

/**
 * Created by lithium on 2017/12/2.
 */

public class ChatAdapter extends ArrayAdapter<Chat> {

    private int resourceId;

    public ChatAdapter(Context contex , int textViewResourceId , List<Chat> objects){
        super(contex , textViewResourceId , objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position , View converView, ViewGroup parent){
        final Chat friendListView = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(converView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent , false);
            viewHolder = new ViewHolder();
            viewHolder.FImage = (ImageView) view.findViewById(R.id.friend_image);
            viewHolder.FName = (TextView) view.findViewById(R.id.friend_name);
            view.setTag(viewHolder);
        }else{
            view = converView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.FImage.setImageResource(friendListView.getImageId());
        viewHolder.FName.setText(friendListView.getName());
        return view;
    }
    class ViewHolder{
        ImageView FImage;
        TextView FName;
    }
}

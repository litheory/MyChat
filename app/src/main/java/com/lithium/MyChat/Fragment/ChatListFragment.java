package com.lithium.MyChat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.lithium.MyChat.R;
import com.lithium.MyChat.Adapter.ChatAdapter;
import com.lithium.MyChat.Class.Chat;
import com.lithium.MyChat.Activity.MsgActivity;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lithium on 2017/12/23.
 */

public class ChatListFragment extends Fragment {

    private ExpandableListView mExpandableListView;
    private List<Chat> chatList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initChats();
        View view =  inflater.inflate(R.layout.fragment_chatlist,container,false);
        ListView ChatListView = (ListView) view.findViewById(R.id.list_view);
        ChatAdapter adapter = new ChatAdapter(getActivity(), R.layout.list_chatlist_item, chatList);
        ChatListView.setAdapter(adapter);

        ChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Chat chat = chatList.get(position);
                Intent intent = new Intent(getActivity(), MsgActivity.class);
                intent.putExtra("username", chat.getName());
                Log.d("ChatList", chat.getName());
                startActivity(intent);
            }
        });
        return view;
    }


    private void initChats(){
        Chat f1 = new Chat("Hiukong", R.drawable.f1 );
        chatList.add(f1);
        Chat f2 = new Chat("Majiang", R.drawable.f2 );
        chatList.add(f2);
        Chat f3 = new Chat("Yanbin", R.drawable.f3 );
        chatList.add(f3);
        Chat f4 = new Chat("Android", R.drawable.f4 );
        chatList.add(f4);
        Chat f5 = new Chat("Doramon", R.drawable.f5 );
        chatList.add(f5);
        Chat f6 = new Chat("Firefox", R.drawable.f6 );
        chatList.add(f6);
    }
}


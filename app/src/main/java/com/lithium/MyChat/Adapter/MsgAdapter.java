package com.lithium.MyChat.Adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lithium.MyChat.R;
import com.lithium.MyChat.Class.Msg;


public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgList;

    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout right_photo_layout;
        LinearLayout left_photo_layout;
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView leftPhoto;
        ImageView rightPhoto;

        public ViewHolder(View v){
            super(v);
            leftLayout = (LinearLayout) v.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) v.findViewById(R.id.right_layout);
            left_photo_layout = (LinearLayout) v.findViewById(R.id.left_photo_layout);
            right_photo_layout = (LinearLayout) v.findViewById(R.id.right_photo_layout);
            leftMsg = (TextView) v.findViewById(R.id.left_msg);
            rightMsg = (TextView) v.findViewById(R.id.right_msg);
            leftPhoto = (ImageView) v.findViewById(R.id.left_photo);
            rightPhoto = (ImageView) v.findViewById(R.id.right_photo);
        }
    }

    public MsgAdapter(List<Msg> msgLis)
    {
        mMsgList = msgLis;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (mContext == null) mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_msg_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position){
        Msg msg = mMsgList.get(position);
        if(msg.getType()== Msg.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.right_photo_layout.setVisibility(View.GONE);
            holder.left_photo_layout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getText());
        }
        else if(msg.getType()== Msg.TYPE_SENT){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.right_photo_layout.setVisibility(View.GONE);
            holder.left_photo_layout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getText());
        } else if(msg.getType() == Msg.TYPE_PHOTO_SNET ){
            holder.right_photo_layout.setVisibility(View.VISIBLE);
            holder.left_photo_layout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            Glide.with(mContext).load(msg.getText()).into(holder.rightPhoto);
        }
        else if (msg.getType() == Msg.TYPE_PHOTO_RECEIVED){
            holder.left_photo_layout.setVisibility(View.VISIBLE);
            holder.right_photo_layout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            Glide.with(mContext).load(msg.getText()).into(holder.leftPhoto);
        }
    }
    @Override
    public int getItemCount(){
        return mMsgList.size();
    }
}
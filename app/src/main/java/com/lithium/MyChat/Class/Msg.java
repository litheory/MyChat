package com.lithium.MyChat.Class;

import org.litepal.crud.DataSupport;

/**
 * Created by lithium on 2017/12/21.
 */


public class Msg extends DataSupport{

    public String username;
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    public static final int TYPE_PHOTO_SNET = 2;
    public static final int TYPE_PHOTO_RECEIVED = 3;
    public String text;
    public int type;

    public Msg(){}

    public Msg(String text, int type , String username)
    {
        this.text = text;
        this.type=type;
        this.username = username;
    }
    public String getText(){
        return text;
    }
    public void setText(String text) {this.text = text; }

    public int getType(){
        return type;
    }
    public void setType(int type) { this.type = type; }

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

}

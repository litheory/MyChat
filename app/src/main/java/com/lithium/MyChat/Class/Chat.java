package com.lithium.MyChat.Class;

/**
 * Created by lithium on 2017/12/22.
 */

public class Chat {
    private String Name;
    private int imageId;


    public Chat(String name, int imageid ){
        this.Name = name;
        this.imageId = imageid;
    }

    public String getName(){
        return Name;
    }

    public int getImageId(){
        return imageId;
    }
}

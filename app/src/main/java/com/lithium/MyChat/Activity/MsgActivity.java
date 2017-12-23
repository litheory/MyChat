package com.lithium.MyChat.Activity;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.annotation.TargetApi;

import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lithium.MyChat.R;
import com.lithium.MyChat.Adapter.MsgAdapter;
import com.lithium.MyChat.Class.Msg;


import org.litepal.crud.DataSupport;


public class MsgActivity extends Activity {

    private static final String TAG = "MsgActivity";
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private ImageButton send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private String username;
    private ImageButton send_photo;

    public static final int TAKE_PHOTO = 1 ;
    public static final int CHOOSE_PHOTO = 2 ;

    public String PhotoPath;

    private Handler handler;
    private String text;
    private DatagramSocket datagramSocket;
    private SendThread sendThread = null;
    private Object sendLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        Intent intent =  getIntent();
        username = intent.getStringExtra("username");
        Log.d("Main",username);

        initMsg();//恢复聊天历史

        inputText = (EditText) findViewById(R.id.input_text);
        send = (ImageButton) findViewById(R.id.send);
        send_photo = (ImageButton) findViewById(R.id.btn_photo);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //创建对象后设置到RecyclerView中
        msgRecyclerView.setLayoutManager(layoutManager);

        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SocketAddress socketAddress = new InetSocketAddress("127.0.0.1",10201);
                    datagramSocket = new DatagramSocket(socketAddress);
                    ReceiveThread receiveThread = new ReceiveThread(datagramSocket);
                    new Thread(receiveThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Msg chatMes = new Msg();
                chatMes.setType(Msg.TYPE_RECEIVED);
                chatMes.setText(msg.obj.toString());
                chatMes.setUsername(username);
                chatMes.save();
                Msg msg1 = new Msg(msg.obj.toString(), Msg.TYPE_RECEIVED, username);
                msgList.add(msg1);
                adapter.notifyItemInserted(msgList.size() - 1);
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
            }
        };
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = inputText.getText().toString();
                if (!"".equals(text)) {
                    Msg msg = new Msg(text, Msg.TYPE_SENT, username);
                    msgList.add(msg);
                    msg.save();
                    inputText.setText("");
                    if(sendThread == null)
                    {
                        sendThread = new SendThread(datagramSocket);
                        new Thread(sendThread).start();
                    }
                    else {
                        synchronized (sendLock){
                            sendLock.notify();
                        }
                    }
                    adapter.notifyItemInserted(msgList.size() - 1);//新消息刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//将ListView定位到最后一行
                } else {
                    Toast.makeText(MsgActivity.this, "You can't send empty msg.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        send_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MsgActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MsgActivity.this , new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else {
                    openAlbum();
                }
            }
        });
    }


    public class ReceiveThread implements Runnable {

        private DatagramSocket socket;

        ReceiveThread(DatagramSocket socket){ this.socket = socket; }
        @Override
        public void run(){
            byte buf[]=new byte[1024];
            try {
                DatagramPacket p = new DatagramPacket(buf, 1024);
                while(true){
                    socket.receive(p);
                    Message msg = new Message();
                    msg.obj = new String(p.getData());
                    handler.sendMessage(msg);
                    for(int i = 0; i<1024 ; i++)
                        buf[i]=0;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public class SendThread implements Runnable {

        private DatagramSocket socket;

        SendThread(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {
                SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 10201);
                DatagramPacket message = new DatagramPacket(text.getBytes(), text.getBytes().length, socketAddress);
                try {
                    socket.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (sendLock) {
                    try {
                        sendLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data){
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if(Build.VERSION.SDK_INT>=19){
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4及以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri =data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id =docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Msg msg = new Msg();
            msg.setText(imagePath);
            msg.setType(Msg.TYPE_PHOTO_SNET);
            msg.setUsername(username);
            msg.save();
            msgList.add(msg);
            adapter.notifyItemInserted(msgList.size()-1);
            msgRecyclerView.scrollToPosition(msgList.size()-1);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private void initMsg() {
        List<Msg> msgs = DataSupport.where("username = ?", username).find(Msg.class);
        if(!msgs.isEmpty())
        for (Msg msg : msgs) {
                Msg txt = new Msg(msg.getText(), msg.getType(), username);
                msgList.add(txt);
        /*Msg msg1 = new Msg("Hello guy.",Msg.TYPE_RECEIVED );
        msgList.add(msg1);
        Msg msg2 = new Msg("Hi.",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is TOP.",Msg.TYPE_RECEIVED);
        msgList.add(msg3);*/
        }
    }
}

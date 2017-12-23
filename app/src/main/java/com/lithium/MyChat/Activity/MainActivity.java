package com.lithium.MyChat.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lithium.MyChat.R;
import com.lithium.MyChat.Adapter.ViewPagerFragmentAdapter;
import com.lithium.MyChat.Class.Chat;
import com.lithium.MyChat.Fragment.ContactsFragment;
import com.lithium.MyChat.Fragment.ExploreFragment;
import com.lithium.MyChat.Fragment.ChatListFragment;
import com.lithium.MyChat.Fragment.MyinfoFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
    //主要要继承自FragmentActivity,这样才能在初始适配器类是使用getSupportFragmentManager方法获取FragmentManager对象
    private ViewPager mViewPager;
    private List<Fragment> datas;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private LinearLayout mLLChat,mLLFrd,mLLFind,mLLMe;
    private ImageView mImageViewChat,mImageViewFrd,mImageViewFind,mImageViewMe;

    //Chat
    private ExpandableListView mExpandableListView;
    private List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //toolbar控件
        Toolbar menu_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(menu_toolbar);

       //Fragment控件操作
        initDatas();//初始化数据集
        initView();// 初始化控件
        initEvent();// 注册单击监听
        viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(getSupportFragmentManager(),datas);//初始化适配器类
        mViewPager.setAdapter(viewPagerFragmentAdapter);

    }
//
    private void initDatas() {
        datas=new ArrayList<Fragment>();
        datas.add(new ChatListFragment());
        datas.add(new ContactsFragment());
        datas.add(new ExploreFragment());
        datas.add(new MyinfoFragment());

    }
    private void initEvent() {
        mLLChat.setOnClickListener(this);
        mLLFrd.setOnClickListener(this);
        mLLFind.setOnClickListener(this);
        mLLMe.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {//ViewPager滑动切换监听
            @Override
            public void onPageSelected(int arg0) {
                int currentItem=mViewPager.getCurrentItem();
                resetImag();
                switch (currentItem) {
                    case 0:
                        mImageViewChat.setImageResource(R.drawable.ic_tabbar_chat_active);
                        break;
                    case 1:
                        mImageViewFrd.setImageResource(R.drawable.ic_tabbar_contacts_active);
                        break;
                    case 2:
                        mImageViewFind.setImageResource(R.drawable.ic_tabbar_explore_active);
                        break;
                    case 3:
                        mImageViewMe.setImageResource(R.drawable.ic_tabbar_myinfo_active);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLLChat = (LinearLayout) findViewById(R.id.ll_chat);
        mLLFrd = (LinearLayout) findViewById(R.id.ll_frd);
        mLLFind = (LinearLayout) findViewById(R.id.ll_find);
        mLLMe = (LinearLayout) findViewById(R.id.ll_me);
        mImageViewChat = (ImageView) findViewById(R.id.img_chat);
        mImageViewFrd = (ImageView) findViewById(R.id.img_frd);
        mImageViewFind = (ImageView) findViewById(R.id.img_find);
        mImageViewMe = (ImageView) findViewById(R.id.img_me);
    }
    @Override
    public void onClick(View v) {
        resetImag();
        switch (v.getId()) {
            case R.id.ll_chat:
                mViewPager.setCurrentItem(0);
                mImageViewChat.setImageResource(R.drawable.ic_tabbar_chat_active);
                break;
            case R.id.ll_frd:
                mViewPager.setCurrentItem(1);
                mImageViewFrd.setImageResource(R.drawable.ic_tabbar_contacts_active);
                break;
            case R.id.ll_find:
                mViewPager.setCurrentItem(2);
                mImageViewFind.setImageResource(R.drawable.ic_tabbar_explore_active);
                break;
            case R.id.ll_me:
                mViewPager.setCurrentItem(3);
                mImageViewMe.setImageResource(R.drawable.ic_tabbar_myinfo_active);
                break;
            default:
                break;
        }
    }
    private void resetImag() {//重置图片
        mImageViewChat.setImageResource(R.drawable.ic_tabbar_chat);
        mImageViewFrd.setImageResource(R.drawable.ic_tabbar_contacts);
        mImageViewFind.setImageResource(R.drawable.ic_tabbar_explore);
        mImageViewMe.setImageResource(R.drawable.ic_tabbar_myinfo);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more:
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

}

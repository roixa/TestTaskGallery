package com.tomleto.roix.testtaskgallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.AbsListView;
import android.widget.EditText;

import com.tomleto.roix.testtaskgallery.transformers.DepthPageTransformer;
import com.tomleto.roix.testtaskgallery.transformers.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;


public class GalleryActivity extends AppCompatActivity {


    private ViewPager mPager;

    //need to automatic change of page
    private Timer animationTimer;
    private Handler handler;
    private Runnable update;
    private boolean animationIsRightToLeft;


    private ScreenSidePagerAdapter mPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //init a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // init a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSidePagerAdapter(getSupportFragmentManager(),getApplicationContext());
        mPager.setAdapter(mPagerAdapter);
        //on click a fab shows add to favorite and comment dialog
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentAlertDialog();
            }
        });

        //need to correctly save current item id
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                DataManager.currItemId=mPagerAdapter.getPageIdFromPosition(position);
                Log.d("aaaa",DataManager.currItemId+" in currItemId addOnPageChangeListener");
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set different PageTransformer from saved value in shared preferences
        boolean pageTransformerNeedToChange=DataManager.getBooleanFromPrefs(getApplicationContext(),DataManager.ANIMATION_CHANGE,false);
        if(pageTransformerNeedToChange) mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        else mPager.setPageTransformer(true,new DepthPageTransformer());

        //and refresh view pager with new data
        refreshViewPager();
        startAnimationTread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animationTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }
    //on click settings menu item starts    SettingsActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //works when fab clicked
    private void showCommentAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add to favorites and comment");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String commentText = input.getText().toString();
                Log.d("aaaa",DataManager.currItemId+" in save favor item");

                DataManager.setStringInPrefs(getApplicationContext(),DataManager.COMMENT_PREFIX+DataManager.currItemId,commentText);
                DataManager.setBooleanInPrefs(getApplicationContext(),DataManager.FAVORITES_PREFIX+DataManager.currItemId,true);
                refreshViewPager();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //load new data and refresh pager
    private void refreshViewPager(){
        int pos=mPager.getCurrentItem();
        mPagerAdapter.refreshData();
        mPagerAdapter = new ScreenSidePagerAdapter(getSupportFragmentManager(),getApplicationContext());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(pos );

    }

    //init page change animation
    private void startAnimationTread(){
        handler = new Handler();
        update = new Runnable() {
            public void run() {
                int pos= mPager.getCurrentItem();
                int size=mPagerAdapter.getCount();
                if(pos==0){
                    animationIsRightToLeft=false;
                }
                if(pos==(size-1)){
                    animationIsRightToLeft=true;
                }
                int newPos=animationIsRightToLeft?(pos-1):(pos+1);

                mPager.setCurrentItem(newPos);
                mPager.setCurrentItem(newPos, true);
            }
        };


        animationTimer=new Timer();
        if(!DataManager.getBooleanFromPrefs(getApplicationContext(),DataManager.ANIMATION_KEY,false))return;
        int animationTime=DataManager.getIntFromPrefs(getApplicationContext(),DataManager.ANIMATION_TIME_KEY,1);
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 0, animationTime*1000);
    }


    //realize our view pager adapter
    private class ScreenSidePagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<ImageItem> items;
        private ArrayList<ImageItem> favoritesItems;
        private Context appContext;
        public ScreenSidePagerAdapter(FragmentManager fm, Context appContext) {
            super(fm);
            this.appContext=appContext;
            this.items=DataManager.loadImageItems(appContext);
            genFavoritesItems();

            //needs for sort
            Comparator comparator=new Comparator<ImageItem>() {
                @Override
                public int compare(ImageItem lhs, ImageItem rhs) {
                    return lhs.getCount() > rhs.getCount() ? 1 : lhs.getCount() == rhs.getCount() ? 0 : -1;
                }
            };
            //sort or shuffle our item arrays by saved shared prefs
            boolean isRandom=DataManager.getBooleanFromPrefs(appContext,DataManager.RANDOM_KEY,false);
            if(isRandom){
                Collections.shuffle(items);
                Collections.shuffle(favoritesItems);
            }
            else {
                Collections.sort(items, comparator);
                Collections.sort(favoritesItems, comparator);
            }
        }

        @Override
        public Fragment getItem(int position) {
            //prepare page when it needs
            ArrayList<ImageItem> localItems=getCurrItemsArray();
            ScreenSidePage page=new ScreenSidePage();
            page.setInfo(localItems.get(position));
            return page;
        }



        public int getPageIdFromPosition(int pos){
            ArrayList<ImageItem> localItems=getCurrItemsArray();
            return localItems.get(pos).getId();
        }

        @Override
        public int getCount() {
            return getCurrItemsArray().size();
        }

        //return current, actual items array
        public ArrayList<ImageItem> getCurrItemsArray(){
            boolean isFavorites=DataManager.getBooleanFromPrefs(getApplicationContext(),DataManager.FAVORITES_KEY,false);
            return isFavorites?favoritesItems:items;
        }

        //refresh item arrays
        public void refreshData(){
            DataManager.loadImageItems(appContext);
            genFavoritesItems();
        }

        //select items when favored
        private void genFavoritesItems(){
            favoritesItems=new ArrayList<>();
            for (ImageItem item:items){
                if(item.isFavored())favoritesItems.add(item);
            }
        }

    }


}

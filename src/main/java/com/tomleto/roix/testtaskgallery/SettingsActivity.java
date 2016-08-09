package com.tomleto.roix.testtaskgallery;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


//activity with many switches for app function manage by users
public class SettingsActivity extends AppCompatActivity {
    private Switch animationSwitch;
    private Switch favoritesSwitch;
    private Switch randomSwitch;
    private EditText animationTimeEditText;
    private Switch animationChangeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        animationSwitch=(Switch)findViewById(R.id.switch_animation);
        favoritesSwitch=(Switch)findViewById(R.id.switch_favorites);
        randomSwitch=(Switch)findViewById(R.id.switch_randon);
        animationTimeEditText=(EditText)findViewById(R.id.edit_text_animation_time);
        animationChangeSwitch=(Switch) findViewById(R.id.switch_change_anim);

        //set actual element states
        int num=DataManager.getIntFromPrefs(getApplicationContext(),DataManager.ANIMATION_TIME_KEY,1);
        animationTimeEditText.setText(num+"");
        animationSwitch.setChecked(DataManager.getBooleanFromPrefs(getApplicationContext(),DataManager.ANIMATION_KEY,false));
        favoritesSwitch.setChecked(DataManager.getBooleanFromPrefs(getApplicationContext(),DataManager.FAVORITES_KEY,false));
        randomSwitch.setChecked(DataManager.getBooleanFromPrefs(getApplicationContext(),DataManager.RANDOM_KEY,false));
        animationChangeSwitch.setChecked(DataManager.getBooleanFromPrefs(getApplicationContext(),DataManager.ANIMATION_CHANGE,false));

        listenersUI();
    }

    //manage clicking to elements
    private void listenersUI(){
        animationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataManager.setBooleanInPrefs(getApplicationContext(),DataManager.ANIMATION_KEY,isChecked);
            }
        });
        favoritesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataManager.setBooleanInPrefs(getApplicationContext(),DataManager.FAVORITES_KEY,isChecked);
            }
        });
        randomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataManager.setBooleanInPrefs(getApplicationContext(),DataManager.RANDOM_KEY,isChecked);
            }
        });

        animationTimeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    int num=Integer.parseInt(v.getText().toString());
                    if(num>0&&num<10)
                        DataManager.setIntInPrefs(getApplicationContext(),DataManager.ANIMATION_TIME_KEY,num);
                }
                return false;
            }
        });
        animationChangeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataManager.setBooleanInPrefs(getApplicationContext(),DataManager.ANIMATION_CHANGE,isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

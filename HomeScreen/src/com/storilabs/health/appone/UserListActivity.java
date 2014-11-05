package com.storilabs.health.appone;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.storilabs.health.appone.database.DatabaseFacade;

import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;


public class UserListActivity extends ListActivity {
	
	
	ProgressBar bar;
	AtomicBoolean isRunning=new AtomicBoolean(false);
	DatabaseFacade dbFacade;// = new DatabaseFacade(get);
	Button addScheduleBtn, viewAllBtn;
	ButtonAction buttonAction;
	
	
	Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
		bar.incrementProgressBy(5);
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_screen); //this should happen here only then shall 'findViewById(R.id.scheduleAddButton)' will return valid objects
        dbFacade = new DatabaseFacade(this,getString(R.string.db_name), Integer.valueOf(getString(R.string.db_version)) );
        dbFacade.openDatabase();
        buttonAction = new ButtonAction(this,dbFacade);
        addScheduleBtn = (Button)findViewById(R.id.scheduleAddButton);
        viewAllBtn = (Button)findViewById(R.id.viewAllButton);
        
        //addScheduleBtn.setOnClickListener(buttonAction);
        viewAllBtn.setOnClickListener(buttonAction);
        
        try {
	        ArrayList<String> values = dbFacade.getAllUsers();
	        /*for(int i=0;i<3;i++) {
	        	//Button scheduleButtons = new Button(this);
	        	values.add("Med. " + i + ": scheduled at " + i + "'O clock");
	        	//views.add(scheduleButtons);
	        }*/
	        //findViewById(R.layout.activity_home_screen).addTouchables(views);
	        setListAdapter(new ArrayAdapter<String>(this,
	        		android.R.layout.simple_list_item_1,
	        		values.toArray(new String[values.size()])));
        } catch (Exception ex) {
        	showMessage("Error", ex.getLocalizedMessage());
        }
        
        bar=(ProgressBar)findViewById(R.id.progress);
        
    }
    
    @Override
    public void onDestroy() {
      super.onDestroy();

      dbFacade.close();
    }
    
    public void addUser(View view) {

    	Intent intent = new Intent(this, AddUserActivity.class);
    	
    	startActivity(intent);
    }
    
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
    	String selectedValue = "";
    	if (parent != null) {
    		if (parent.getSelectedItem()!= null) {
    			selectedValue = parent.getSelectedItem().toString() + "==";
    		} else {
    			if (parent.getItemAtPosition(position) != null)
    				selectedValue = parent.getItemAtPosition(position).toString() + " -- ";
    		}
    	} else {
    		selectedValue = "parent is null";
    	}
    	showMessage("Med Details", selectedValue);
    }
    
    public void onStart() {
		super.onStart();
		/**/bar.setProgress(0);
		Thread background=new Thread(new Runnable() {
			public void run() {
				try {
					for (int i=0;i<20 && isRunning.get();i++) {
						Thread.sleep(1000);
						handler.sendMessage(handler.obtainMessage());
					}
				}
				catch (Throwable t) {
				// just end the background thread
				}
			}
		});
		isRunning.set(true);
		background.start();//*/
	}
    
    public void onStop() {
    	super.onStop();
    	isRunning.set(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void showMessage(String title,String message) {
    	Builder builder=new Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.show();
	}
}

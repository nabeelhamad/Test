package com.storilabs.health.appone;

import java.util.ArrayList;

import com.storilabs.health.appone.database.DatabaseFacade;
import com.storilabs.health.appone.database.User;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog.Builder;


public class HomeScreen extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    
    static DatabaseFacade dbFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        dbFacade = new DatabaseFacade(this,getString(R.string.db_name), Integer.valueOf(getString(R.string.db_version)) );
        dbFacade.openDatabase();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_home);
                break;
            case 2:
                mTitle = getString(R.string.title_personal_info);
                break;
            case 3:
                mTitle = getString(R.string.title_pill_history);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home_screen, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    	private static int mSectionNum = 0;
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
        	mSectionNum = sectionNumber;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	View rootView = null;
        	switch (mSectionNum) {
            case 1:
                //mTitle = getString(R.string.title_home);
            	rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
                break;
            case 2:
                //mTitle = getString(R.string.title_personal_info);
            	rootView = inflater.inflate(R.layout.user_list_screen, container, false);
            	ListView listView = (ListView) rootView.findViewById(R.id.userList);
            	((ListView)listView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         //selectItem(position);
                    	 showMessage(view.getContext(), "Med Details",  parent.getItemAtPosition(position).toString() );
                     }
                 });
                try {
        	        ArrayList<String> values = dbFacade.getAllUsers();
        	        /*for(int i=0;i<3;i++) {
        	        	//Button scheduleButtons = new Button(this);
        	        	values.add("Med. " + i + ": scheduled at " + i + "'O clock");
        	        	//views.add(scheduleButtons);
        	        }*/
        	        //findViewById(R.layout.activity_home_screen).addTouchables(views);
        	        ((ListView)listView).setAdapter(new ArrayAdapter<String>(container.getContext(),
        	        		android.R.layout.simple_list_item_1,
        	        		values.toArray(new String[values.size()])));
                } catch (Exception ex) {
                	showMessage(container.getContext(),"Error", ex.getLocalizedMessage());
                }
                break;
            case 3:
                //mTitle = getString(R.string.title_pill_history);
            	rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
                break;
            case 100:
            	rootView = inflater.inflate(R.layout.add_user, container, false);
                break;
            default:
            	rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        	}
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeScreen) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    
    public void addUser(View view) {

    	FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(100))
                .commit();
    }
    
    public void saveUser(View view) {
    	User newUser = new User();
		newUser.setName( ((EditText)findViewById(R.id.nameField)).getText().toString());
		newUser.setSex( ((EditText)findViewById(R.id.sexField)).getText().toString());
		//newUser.setDob((EditText)activity.findViewById(R.id.dobField)).getText().toString());
		newUser.setPhone1( ((EditText)findViewById(R.id.phone1Field)).getText().toString());
		newUser.setPhone2( ((EditText)findViewById(R.id.phone2Field)).getText().toString());
		newUser.setEmail1( ((EditText)findViewById(R.id.email1Field)).getText().toString());
		newUser.setEmail2( ((EditText)findViewById(R.id.email2Field)).getText().toString());
		
		dbFacade.addModifyUser(newUser);
		
		FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(2))
                .commit();
    	
    }
    
    public static void showMessage(Context context, String title,String message) {
    	Builder builder=new Builder(context);
    	builder.setCancelable(true);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.show();
	}

}

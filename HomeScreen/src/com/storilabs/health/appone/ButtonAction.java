package com.storilabs.health.appone;

import com.storilabs.health.appone.database.DatabaseFacade;
import com.storilabs.health.appone.database.User;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;


public class ButtonAction implements OnClickListener {
	DatabaseFacade db = null;
	Activity activity = null;
	
	public ButtonAction(Activity activity, DatabaseFacade db) {
		this.db = db;
		this.activity = activity;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == activity.findViewById(R.id.scheduleAddButton)) {
			
		} else if (view == activity.findViewById(R.id.viewAllButton)) {
			
		} else if (view == activity.findViewById(R.id.saveUserButton)) {
			User newUser = new User();
			newUser.setName( ((EditText)activity.findViewById(R.id.nameField)).getText().toString());
			newUser.setSex( ((EditText)activity.findViewById(R.id.sexField)).getText().toString());
			//newUser.setDob((EditText)activity.findViewById(R.id.dobField)).getText().toString());
			newUser.setPhone1( ((EditText)activity.findViewById(R.id.phone1Field)).getText().toString());
			newUser.setPhone2( ((EditText)activity.findViewById(R.id.phone2Field)).getText().toString());
			newUser.setEmail1( ((EditText)activity.findViewById(R.id.email1Field)).getText().toString());
			newUser.setEmail2( ((EditText)activity.findViewById(R.id.email2Field)).getText().toString());
			
			db.addModifyUser(newUser);
			
			Intent intent = new Intent(activity, UserListActivity.class);
	    	
			activity.startActivity(intent);
		}
		

	}

}

package com.storilabs.health.appone.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseFacade extends SQLiteOpenHelper
{
	//private static final String DATABASE_NAME="pillalertdb";
	SQLiteDatabase db;
	Context context;
	String dbName;
	
	
	public DatabaseFacade(Context context,String dbName, int version) {
		//String dbName = context.getResources().getString(R.string.db_name);
		//String 
		super(context, dbName, null, version);
		this.dbName = dbName;
		this.context = context;
		//db = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
	}
	
	public void openDatabase() {
		db = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS user (user_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, sex TEXT, dob TEXT, phone1 TEXT, phone2 TEXT," +
				 " email1 TEXT, email2 TEXT, logon_id TEXT, password TEXT);");
				
		db.execSQL("CREATE TABLE IF NOT EXISTS user_med (user_med_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, med_name TEXT, chemi_name TEXT," + 
				" doc_id INTEGER, pharmacy_id INTEGER, image TEXT, notes TEXT, med_type_id INTEGER, dosage TEXT, dosage_unit INTEGER);");
	}

	@Override
	public void onCreate(SQLiteDatabase dbArg) {
		// This method will be called when the database is created for the first time.
		/*   db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
		 * CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `sex` varchar(1) NOT NULL,
  `dob` varchar(45) NOT NULL,
  `phone1` varchar(45) NOT NULL,
  `phone2` varchar(45) DEFAULT NULL,
  `email1` varchar(45) DEFAULT NULL,
  `email2` varchar(45) DEFAULT NULL,
  `logon_id` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`);
  
  CREATE TABLE `user_med` (
  `user_med_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `med_name` varchar(45) NOT NULL,
  `chemi_name` varchar(45) NOT NULL,
  `doc_id` int(11) NOT NULL,
  `pharmacy_id` varchar(45) NOT NULL,
  `image` varchar(45) DEFAULT NULL,
  `notes` varchar(45) DEFAULT NULL,
  `med_type_id` int(11) DEFAULT NULL,
  `dosage` varchar(5) DEFAULT NULL,
  `dosage_unit` int(11) DEFAULT NULL,
  `prescription_doc_id` int(11) DEFAULT NULL,
  `prescription_pharm_id` int(11) DEFAULT NULL);
  
		 */
		
		
		dbArg.execSQL("CREATE TABLE user (user_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, sex TEXT, dob TEXT, phone1 TEXT, phone2 TEXT," +
		 " email1 TEXT, email2 TEXT, logon_id TEXT, password TEXT);");
		
		dbArg.execSQL("CREATE TABLE user_med (user_med_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, med_name TEXT, chemi_name TEXT," + 
		" doc_id INTEGER, pharmacy_id INTEGER, image TEXT, notes TEXT, med_type_id INTEGER, dosage TEXT, dosage_unit INTEGER);");
		
		this.db = dbArg;

	}

	@Override
	public void onUpgrade(SQLiteDatabase dbArg, int arg1, int arg2) {
		//This method will be called whenever there is an upgrade for the database.
		android.util.Log.w("Constants", "Upgrading database, which will destroy all old data; old version: " + arg1 +
				"   new version: " + arg2);
		dbArg.execSQL("DROP TABLE IF EXISTS user");
		dbArg.execSQL("DROP TABLE IF EXISTS user_med");
	    onCreate(dbArg);

	}
	
	public void addModifyUser(User user) {
		ContentValues cv=new ContentValues();
		
		cv.put("name", user.getName());
		cv.put("sex", user.getSex());
		//cv.put("dob", user.getDob().toString());
		//cv.put("logon_id", user.getLogon_id());
		cv.put("phone1", user.getPhone1());
		cv.put("phone2", user.getPhone2());
		cv.put("email1", user.getEmail1());
		cv.put("email2", user.getEmail2());
		
		db.insert("user", null, cv);
	}
	
	public ArrayList<String> getAllUsers() {
		ArrayList<String> userList = new ArrayList<String>();
		Cursor c=db.rawQuery("SELECT * FROM user", null);
		while(c.moveToNext()) {
			userList.add(c.getString(0) + "   " + c.getString(1));
		}
		return userList;
	}
	
	/*public void addModifyMedicine(UserMeds med) {
		
		ContentValues cv=new ContentValues();
		
		cv.put("user_id", med.getUserId());
		cv.put("med_name", med.getMedName());
		cv.put("chemi_name", med.getChemiName());
		cv.put("doc_id", med.getDocId());
		cv.put("dosage", med.getDosage());
		cv.put("dosage_unit",med.getDosageUnit());
		cv.put("notes", med.getNotes());
		
		db.insert("user_med", null, cv);
		
	}*/
	
	

}
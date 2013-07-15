package com.barcamppenang2013;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {
	public static final String DB_NAME = "BarCamp2013";
	public static final int DB_VERSION = 1;
	public static final String TABLE_1 = "USERPROFILE";		
	public static final String COLUMN_MYKEYID = "MYKEYID";
	public static final String COLUMN_ISPFOFILECREATED = "ISPFOFILECREATED";
	public static final String COLUMN_MYNAME = "MYNAME";
	public static final String COLUMN_MYEMAIL = "MYEMAIL";
	public static final String COLUMN_MYPHONE = "MYPHONE";
	public static final String COLUMN_MYPROF = "MYPROFESSION";
	public static final String COLUMN_MYFBID = "MYFBID";
	
	
	public static final String TABLE_2 = "MYFRIENDS";
	public static final String COLUMN_FRIENDKEYID = "FRIENDKEYID";
	public static final String COLUMN_FRIENDNAME = "FRIENDNAME";
	public static final String COLUMN_FRIENDEMAIL = "FRIENDEMAIL";
	public static final String COLUMN_FRIENDPHONE = "FRIENDPHONE";
	public static final String COLUMN_FRIENDPROF = "FRIENDPROF";
	public static final String COLUMN_FRIENDFB = "FRIENDFB";
	

	public MyDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		Log.d("yc", "database oncreate(), start creating two tables");
		//Execute all the queries to create table here
		String sqlQueryToCreateUserProfileTable = "create table if not exists "
				+ TABLE_1 + " ( " + COLUMN_MYKEYID
				+ " integer primary key autoincrement, " + COLUMN_ISPFOFILECREATED				
				+ " VARCHAR(255) NOT NULL, " + COLUMN_MYNAME 
				+ " VARCHAR(255) NOT NULL, " + COLUMN_MYEMAIL 
				+ " VARCHAR(255) NOT NULL, " + COLUMN_MYPHONE 
				+ " VARCHAR(255) NOT NULL," + COLUMN_MYPROF
				+ " VARCHAR(255) NOT NULL," + COLUMN_MYFBID
				+ " VARCHAR(255) NOT NULL);";
		
		String sqlQueryToCreateMyFriendsTable = "create table if not exists "
				+ TABLE_2 + " ( " + COLUMN_FRIENDKEYID
				+ " integer primary key autoincrement, " + COLUMN_FRIENDNAME
				+ " VARCHAR(255) NOT NULL, " + COLUMN_FRIENDEMAIL
				+ " VARCHAR(255) NOT NULL, " + COLUMN_FRIENDPHONE
				+ " VARCHAR(255) NOT NULL," + COLUMN_FRIENDPROF
				+ " VARCHAR(255) NOT NULL," + COLUMN_FRIENDFB
				+ " VARCHAR(255) NOT NULL); ";

		db.execSQL(sqlQueryToCreateUserProfileTable);
		db.execSQL(sqlQueryToCreateMyFriendsTable);
		Log.d("onCreate(SQLiteDatabase db)", "is executed");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}

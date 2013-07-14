/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.barcamppenang2013.tabfragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.barcamppenang2013.MainActivity;
import com.barcamppenang2013.MyDatabase;
import com.barcamppenang2013.R;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ProfileFragment extends SherlockFragment implements TabInterface {
	private Button bt_save;
	private ImageButton bt_connect;
	private EditText et_name;
	private EditText et_email;
	private EditText et_phone;
	private EditText et_profession;
	private EditText et_fbId;
	private String isProfileCreated;
	public static final String TITLE = "Profile";
	// Google Analytics
	private Tracker GaTracker;
	private GoogleAnalytics GaInstance;

	// Connecting to facebook
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GaInstance = GoogleAnalytics.getInstance(getActivity());
		GaTracker = GaInstance.getTracker("UA-35359053-9");
		GaTracker.sendView("TITLE");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.update_profile_page, container,
				false);

		bt_save = (Button) v.findViewById(R.id.btnSave);
		bt_connect = (ImageButton) v.findViewById(R.id.btnfbconnect);
		et_name = (EditText) v.findViewById(R.id.editTextName);
		et_email = (EditText) v.findViewById(R.id.editTextEmail);
		et_phone = (EditText) v.findViewById(R.id.editTextPhone);
		et_profession = (EditText) v.findViewById(R.id.editTextProfession);
		et_fbId = (EditText) v.findViewById(R.id.editTextFbId);

		// Retrive the data from sqlite if profile is created.
		isProfileCreated = check();

		if (isProfileCreated.equalsIgnoreCase("true")) {

			String[] myInfo = fillTextField();
			et_name.setText(myInfo[0]);
			et_email.setText(myInfo[1]);
			et_phone.setText(myInfo[2]);
			et_profession.setText(myInfo[3]);
			if (myInfo[4].equals("none")) {
				et_fbId.setText("");
			} else {
				et_fbId.setText(myInfo[4]);
			}
		}

		bt_connect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				fbLogin();
			}

		});

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null) {

			if (savedInstanceState != null) {

				session = Session.restoreSession(getActivity(), null,
						statusCallback, savedInstanceState);
			}
			if (session == null) {
				Activity temp = getActivity();
				session = new Session(temp);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}
		}

		bt_save.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// Insert data into sqlite. Table: USERPROFILE
				String name = et_name.getText().toString();
				String email = et_email.getText().toString();
				String phone = et_phone.getText().toString();
				String profession = et_profession.getText().toString();
				String fbId = et_fbId.getText().toString();

				if (fbId.equals("")) {
					fbId = "none";
				}

				if (name.equals("") || email.equals("") || phone.equals("")
						|| profession.equals("") || fbId.equals("")) {
					Toast.makeText(getActivity(),
							"Please fill in all the details.",
							Toast.LENGTH_LONG).show();
				}

				else {

					MyDatabase database = new MyDatabase(getActivity());
					SQLiteDatabase sqliteDatabase = database
							.getWritableDatabase();
					String sql = "INSERT OR REPLACE INTO USERPROFILE (ISPFOFILECREATED,MYKEYID, MYNAME, MYEMAIL, MYPHONE,MYPROFESSION, MYFBID )"
							+ " VALUES ('true','"
							+ "1','"
							+ name
							+ "', '"
							+ email
							+ "', '"
							+ phone
							+ "', '"
							+ profession
							+ "', '" + fbId + "'); ";

					sqliteDatabase.execSQL(sql);

					database.close();
					sqliteDatabase.close();

					MainActivity main = (MainActivity) getActivity();
//					main.switchContentWithinTab(new ProfileQrFragment(), TITLE);
					main.switchContent(new ProfileQrFragment());

				}

			}

		});

		return v;

	}

	@Override
	public void onStart() {
		super.onStart();
		//Log.d("fb", "onStart");
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		//Log.d("fb", "onStop");
		Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Log.d("fb", "onActivityResult");
		Session.getActiveSession().onActivityResult(getActivity(), requestCode,
				resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Log.d("fb", "onSaveInstanceState at profileFragment is called");
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private void updateView() {
		Session session = Session.getActiveSession();
		Log.d("fb", "Is session open? " + Boolean.toString(session.isOpened())
				+ "session state: " + session.getState().toString());
		if (session.isOpened()) {
			//Log.d("fb", "at updateView, session is open");
			Request.executeMeRequestAsync(session,
					new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							//Log.d("fb", "onCompleted");
							if (user != null) {

								et_fbId.setText(user.getId());

							}
						}
					});
			/*
			 * bt_connect.setOnClickListener(new View.OnClickListener() { public
			 * void onClick(View view) { onClickLogout(); } });
			 */
		} /*
		 * else {
		 * 
		 * // bt_connect.setText("login"); bt_connect.setOnClickListener(new
		 * View.OnClickListener() { public void onClick(View view) {
		 * onClickLogin(); } }); }
		 */
	}

	private void fbLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			//Log.d("fb", "there is session");
			session.openForRead(new Session.OpenRequest(this)
					.setCallback(statusCallback));
		} else {
			//Log.d("fb", "there is NO session");
			Session.openActiveSession(getActivity(), this, true, statusCallback);
		}
	}

	/*
	 * private void onClickLogout() { Session session =
	 * Session.getActiveSession(); if (!session.isClosed()) {
	 * session.closeAndClearTokenInformation(); } }
	 */
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			//Log.d("at SessionStatusCallback", "calling updateView");
			updateView();
		}
	}

	private String check() {
		//Log.d("yc", "at check()");
		String isCreated = "false";

		MyDatabase database = new MyDatabase(getActivity());
		SQLiteDatabase sqliteDatabase = database.getReadableDatabase();
		String sql = "SELECT ISPFOFILECREATED FROM USERPROFILE;";
		Cursor retrieved = sqliteDatabase.rawQuery(sql, null);

		if (retrieved.moveToFirst()) {
			isCreated = retrieved.getString(retrieved
					.getColumnIndex("ISPFOFILECREATED"));
		}

		//Log.d("debug", "checking isProfileCreated " + isCreated);
		retrieved.close();
		database.close();
		sqliteDatabase.close();
		return isCreated;
	}

	// User data is already stored in sqlite.
	private String[] fillTextField() {
		String[] myInfo = new String[5];

		MyDatabase database = new MyDatabase(getActivity());
		SQLiteDatabase sqliteDatabase = database.getReadableDatabase();
		String sql = "SELECT * FROM USERPROFILE;";
		Cursor retrieved = sqliteDatabase.rawQuery(sql, null);

		//Log.d("yc","row of cursor in database is "+ Integer.toString(retrieved.getCount()));

		// If cursor is not null
		while (retrieved.moveToNext()) {

			myInfo[0] = retrieved.getString(retrieved.getColumnIndex("MYNAME"));
			myInfo[1] = retrieved
					.getString(retrieved.getColumnIndex("MYEMAIL"));
			myInfo[2] = retrieved
					.getString(retrieved.getColumnIndex("MYPHONE"));
			myInfo[3] = retrieved.getString(retrieved
					.getColumnIndex("MYPROFESSION"));
			myInfo[4] = retrieved.getString(retrieved.getColumnIndex("MYFBID"));

		}

		retrieved.close();
		database.close();
		sqliteDatabase.close();

		return myInfo;

	}

	@Override
	public String printTitle() {

		return ProfileFragment.TITLE;
	}
}

package com.barcamppenang2013.tabfragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.barcamppenang2013.CustomAdapter;
import com.barcamppenang2013.FriendObject;
import com.barcamppenang2013.MainActivity;
import com.barcamppenang2013.MyDatabase;
import com.barcamppenang2013.R;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class FriendListFragment extends SherlockFragment implements
		TabInterface {
	public static final String TITLE = "  Friends";
	private ListView friendlist;	
	private Button bt_scan;
	private EditText search;
	private ImageView img;
	private CustomAdapter adapter;
	// Google Analytics
	private Tracker GaTracker;
	private GoogleAnalytics GaInstance;

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

		View v = inflater.inflate(R.layout.friend_listview, container, false);
		//To disable touch listener on other alive fragments. 
		v.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		friendlist = (ListView) v.findViewById(R.id.list);
		search = (EditText) v.findViewById(R.id.etSearch);
		img = (ImageView) v.findViewById(R.id.imgNoFriend);

		updateListView();
		bt_scan = (Button) v.findViewById(R.id.btnScan);
		bt_scan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // com.google.zxing.client.android.SCAN.SCAN_MODE
				startActivityForResult(intent, 0);

			}

		});
		return v;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == getActivity().RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Log.i("xZing", "contents: " + contents + " format: " +
				// format);

				storeInDatabase(contents);
				updateListView();

				// Handle successful scan
			} else if (resultCode == getActivity().RESULT_CANCELED) {
				// Handle cancel
				// Log.i("xZing", "Cancelled");
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
		actionBar.setTitle(TITLE);
		actionBar.setDisplayHomeAsUpEnabled(false);
	}
	// Get the data from database and repopulate the listview.
	public void updateListView() {

		List<FriendObject> friendObjs = getFriendObjs();
		if (friendObjs.size() == 0) {
			img.setVisibility(View.VISIBLE);
//			search.setVisibility(View.INVISIBLE);

		} else {
			img.setVisibility(View.GONE);
//			search.setVisibility(View.VISIBLE);
		}
//		img.setVisibility(View.GONE);
//		search.setVisibility(View.VISIBLE);
		adapter = new CustomAdapter(friendObjs, getActivity());
		
		//friendlist.setSelector( R.drawable.selector_listview);
		friendlist.setAdapter(adapter);

		if (isNetworkAvailable()) {
			for (FriendObject obj : friendObjs) {
				obj.loadImage(adapter);
			}
		}

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

				String text = search.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}
		});

		// Remove friend
		friendlist.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> a, View v,
					int position, long id) {

				final FriendObject obj = (FriendObject) friendlist
						.getItemAtPosition(position);

				AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
				adb.setTitle("Delete Contact");
				adb.setMessage("Removing " + obj.getName()
						+ " from your contact list.");
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						removeFriendObj(obj);
						updateListView();
					}
				});
				adb.show();
				return true;
			}
		});

		friendlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				FriendObject objSelected = (FriendObject) friendlist
						.getItemAtPosition(position);

				MainActivity main = (MainActivity) getActivity();
				// Pass data to the next fragment
				main.setBundle(objSelected.getId(),
						objSelected.getName(), objSelected.getEmail(),
						objSelected.getPhone(), objSelected.getProfession(),
						objSelected.getFbId());				
				main.switchFragmentPassBundle(new FriendPageFragment(),TITLE);
				

			}
		});

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void removeFriendObj(FriendObject remove) {
		MyDatabase database = new MyDatabase(getActivity());
		SQLiteDatabase sqliteDatabase = database.getWritableDatabase();

		String sql = "DELETE FROM MYFRIENDS WHERE FRIENDKEYID= '"
				+ remove.getId() + "'";
		sqliteDatabase.execSQL(sql);
		database.close();
		sqliteDatabase.close();
	}

	private void storeInDatabase(String contents) {

		String delims = "[||]+";
		String[] tokens = contents.split(delims);

		try {
			MyDatabase database = new MyDatabase(getActivity());
			SQLiteDatabase sqliteDatabase = database.getWritableDatabase();

			ContentValues contentValues = new ContentValues();
			contentValues.put(MyDatabase.COLUMN_FRIENDNAME, tokens[0]);
			contentValues.put(MyDatabase.COLUMN_FRIENDEMAIL, tokens[1]);
			contentValues.put(MyDatabase.COLUMN_FRIENDPHONE, tokens[2]);
			contentValues.put(MyDatabase.COLUMN_FRIENDPROF, tokens[3]);
			contentValues.put(MyDatabase.COLUMN_FRIENDFB, tokens[4]);

			sqliteDatabase.insert(MyDatabase.TABLE_2, null, contentValues);
			database.close();
			sqliteDatabase.close();
		} catch (Exception e) {
			Toast.makeText(getActivity(),
					"Only accept QR code from Barcampers.", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}

	// Return all rows from MYFRIENDS
	private List<FriendObject> getFriendObjs() {
		List<FriendObject> friendObjs = new ArrayList<FriendObject>();

		MyDatabase database = new MyDatabase(getActivity());
		SQLiteDatabase sqliteDatabase = database.getReadableDatabase();
		String sql = "SELECT * FROM MYFRIENDS;";
		Cursor retrieved = sqliteDatabase.rawQuery(sql, null);
		//Log.d("row of cursor", Integer.toString(retrieved.getCount()));
		//Log.d("yc",	"row of friend cursor in database is "+ Integer.toString(retrieved.getCount()));

		// If cursor is not null
		while (retrieved.moveToNext()) {
			FriendObject obj = new FriendObject();
			String id = retrieved.getString(retrieved
					.getColumnIndex("FRIENDKEYID"));
			String name = retrieved.getString(retrieved
					.getColumnIndex("FRIENDNAME"));			
			String email = retrieved.getString(retrieved
					.getColumnIndex("FRIENDEMAIL"));
			String phone = retrieved.getString(retrieved
					.getColumnIndex("FRIENDPHONE"));
			String profession = retrieved.getString(retrieved
					.getColumnIndex("FRIENDPROF"));
			String fbId = retrieved.getString(retrieved
					.getColumnIndex("FRIENDFB"));

			obj.setId(id);
			obj.setName(name);
			obj.setEmail(email);
			obj.setPhone(phone);
			obj.setProfession(profession);
			obj.setFbId(fbId);

			//Log.d("getFriendObjs", name + " " + email + " " + phone + " "+ profession + " " + fbId);
			friendObjs.add(obj);

		}

		retrieved.close();
		database.close();
		sqliteDatabase.close();

		return friendObjs;
	}

	@Override
	public String printTitle() {
		return FriendListFragment.TITLE;
	}
}

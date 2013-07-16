package com.barcamppenang2013.tabfragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.barcamppenang2013.MainActivity;
import com.barcamppenang2013.MyDatabase;
import com.barcamppenang2013.R;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class FriendEditFragment extends Fragment implements TabInterface {
	public static final String TITLE = "FriendEdit";
	private Button bt_save;
	private ImageView img;
	private EditText et_name;
	private EditText et_email;
	private EditText et_phone;
	private EditText et_profession;
	private EditText et_fbId;

	private String name;
	private String profession;
	private String phone;
	private String email;
	private String fbId;
	private String id;
	//Google Analytics
	private Tracker GaTracker;
	private GoogleAnalytics GaInstance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GaInstance = GoogleAnalytics.getInstance(getActivity());
		GaTracker  = GaInstance.getTracker("UA-35359053-9");
		GaTracker.sendView("TITLE");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.edit_friend, container, false);
		v.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		et_name = (EditText) v.findViewById(R.id.fName);
		et_email = (EditText) v.findViewById(R.id.fEmail);
		et_phone = (EditText) v.findViewById(R.id.fPhone);
		et_profession = (EditText) v.findViewById(R.id.fProfession);
		et_fbId = (EditText) v.findViewById(R.id.fFbId);
		bt_save = (Button) v.findViewById(R.id.btnSave);

		Bundle bundle = this.getArguments();
		if (bundle != null) {

			name = bundle.getString("name");
			profession = bundle.getString("profession");
			phone = bundle.getString("phone");
			email = bundle.getString("email");
			fbId = bundle.getString("fbId");
			id = bundle.getString("id");

		}
		et_name.setText(name);
		et_email.setText(email);
		et_phone.setText(phone);
		et_profession.setText(profession);
		et_fbId.setText(fbId);

		bt_save.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				name = et_name.getText().toString();
				email = et_email.getText().toString();
				phone = et_phone.getText().toString();
				profession = et_profession.getText().toString();
				fbId = et_fbId.getText().toString();

				MyDatabase database = new MyDatabase(getActivity());
				SQLiteDatabase sqliteDatabase = database.getWritableDatabase();

				ContentValues contentValues = new ContentValues();
				contentValues.put(MyDatabase.COLUMN_FRIENDNAME, name);
				contentValues.put(MyDatabase.COLUMN_FRIENDEMAIL, email);
				contentValues.put(MyDatabase.COLUMN_FRIENDPHONE, phone);
				contentValues.put(MyDatabase.COLUMN_FRIENDPROF, profession);
				contentValues.put(MyDatabase.COLUMN_FRIENDFB, fbId);

				sqliteDatabase.update(MyDatabase.TABLE_2, contentValues,
						"FRIENDKEYID = '" + id + "'", null);

				database.close();
				sqliteDatabase.close();

				// update bundle
				MainActivity main = (MainActivity) getActivity();
				main.setBundle(id, name, email, phone, profession, fbId);
				Toast.makeText(getActivity(), "The profile is updated!",
						Toast.LENGTH_LONG).show();

			}
		});

		return v;
	}

	@Override
	public String printTitle() {
		return FriendEditFragment.TITLE;
	}
}

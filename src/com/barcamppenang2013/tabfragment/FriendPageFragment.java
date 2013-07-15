package com.barcamppenang2013.tabfragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcamppenang2013.FriendObject;
import com.barcamppenang2013.MainActivity;
import com.barcamppenang2013.MyDatabase;
import com.barcamppenang2013.R;
import com.barcamppenang2013.RoundProfilePicture;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class FriendPageFragment extends Fragment implements TabInterface {
	public static final String TITLE = "FriendPage";
	private Button bt_email;
	private Button bt_call;
	private Button bt_sms;
	private ImageView bt_edit;
	private Button bt_fb;
	private ImageView img;
	private TextView tv_name;
	private TextView tv_profession;
	private TextView tv_email;
	private TextView tv_phone;
	private TextView tv_sms;
	private TextView tv_url;
	private String id;
	private String name;
	private String profession;
	private String phone;
	private String to_email;
	private String fbId;
	private Bitmap image;
	// Google Analytics
	private Tracker GaTracker;
	private GoogleAnalytics GaInstance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Log.d("Debug", "yc-at friend page ");
		GaInstance = GoogleAnalytics.getInstance(getActivity());
		GaTracker = GaInstance.getTracker("UA-35359053-9");
		GaTracker.sendView("TITLE");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Debug", "call oncreateview of  friend page ");
		View v = inflater.inflate(R.layout.my_friend_info, container, false);
		v.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		bt_email = (Button) v.findViewById(R.id.btEmail);
		bt_call = (Button) v.findViewById(R.id.btCall);
		bt_sms = (Button) v.findViewById(R.id.btSms);
		bt_edit = (ImageView) v.findViewById(R.id.btEdit);
		bt_fb = (Button) v.findViewById(R.id.btFb);
		tv_name = (TextView) v.findViewById(R.id.tvName);
		tv_profession = (TextView) v.findViewById(R.id.tvProfession);
		tv_email = (TextView) v.findViewById(R.id.tvEmail);
		tv_phone = (TextView) v.findViewById(R.id.tvCall);
		tv_sms = (TextView) v.findViewById(R.id.tvSms);
		tv_url = (TextView) v.findViewById(R.id.tvFb);
		img = (ImageView) v.findViewById(R.id.pic);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			  id = bundle.getString("id");			
			  name = bundle.getString("name");
			  profession =bundle.getString("profession"); 
			  phone = bundle.getString("phone"); 
			  to_email = bundle.getString("email");
			  fbId = bundle.getString("fbId");
			 
		}

		//getDetailFromDatabase();

		RoundProfilePicture pp = new RoundProfilePicture();
		Bitmap bitmDefault = pp.getRoundedShape(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.defaultpic));
		img.setImageBitmap(bitmDefault);
		// image= pp.getBitMap(getActivity(), fbId);

		new ImageLoadTask().execute("http://graph.facebook.com/" + fbId
				+ "/picture?type=large");

		/*
		 * new ImageLoadTask().execute("http://graph.facebook.com/" + fbId +
		 * "/picture?type=large"); if (image != null) {
		 * Log.d("bitmap","is not null"); img.setImageBitmap(image); } else {
		 * Log.d("bitmap","is null, use default"); RoundProfilePicture pp = new
		 * RoundProfilePicture(); Bitmap bitmDefault =
		 * pp.getRoundedShape(BitmapFactory
		 * .decodeResource(getActivity().getResources(),
		 * R.drawable.defaultpic)); img.setImageBitmap(bitmDefault); }
		 */

		tv_name.setText(name);
		tv_profession.setText(profession);
		tv_email.setText(to_email);
		tv_phone.setText(phone);
		tv_sms.setText(phone);
		tv_url.setText("facebook.com/" + fbId);

		bt_call.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + phone));
				startActivity(callIntent);

			}
		});

		bt_email.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[] { to_email });
				// need this to prompts email client only
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email,
						"Choose an Email client :"));

			}
		});

		bt_sms.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", phone);
				startActivity(smsIntent);
			}
		});

		bt_fb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("fb://profile/" + fbId));
					startActivity(intent);
				} catch (Exception e) {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("http://facebook.com/profile.php?id="
									+ fbId)));
				}

			}
		});

		bt_edit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				MainActivity main = (MainActivity) getActivity();
				main.switchFragmentPassBundle(new FriendEditFragment(), TITLE);

			}
		});
		return v;
	}

	/*private void getDetailFromDatabase() {
		MyDatabase database = new MyDatabase(getActivity());
		SQLiteDatabase sqliteDatabase = database.getReadableDatabase();

		String sql = "SELECT * FROM MYFRIENDS WHERE FRIENDKEYID=" + id ;
		Cursor retrieved = sqliteDatabase.rawQuery(sql, null);

		
		 * String id = retrieved.getString(retrieved
		 * .getColumnIndex("FRIENDKEYID"));
		 
		name = retrieved.getString(retrieved.getColumnIndex("FRIENDNAME"));
		to_email = retrieved.getString(retrieved.getColumnIndex("FRIENDEMAIL"));
		phone = retrieved.getString(retrieved.getColumnIndex("FRIENDPHONE"));
		profession = retrieved
				.getString(retrieved.getColumnIndex("FRIENDPROF"));
		fbId = retrieved.getString(retrieved.getColumnIndex("FRIENDFB"));

		retrieved.close();
		database.close();
		sqliteDatabase.close();
	}*/

	@Override
	public String printTitle() {
		return FriendPageFragment.TITLE;
	}

	// ASYNC TASK TO AVOID CHOKING UP UI THREAD
	private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

		@Override
		protected void onPreExecute() {
			Log.i("ImageLoadTask", "Loading image...");
		}

		// PARAM[0] IS IMG URL
		protected Bitmap doInBackground(String... param) {
			Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
			// Default bitmap
			RoundProfilePicture pp = new RoundProfilePicture();
			// Bitmap bitmDefault =
			// pp.getRoundedShape(BitmapFactory.decodeResource(this.getResources(),R.drawable.defaultpic));
			// FB Profile bitmap
			URL imgUrl = null;
			Bitmap bitm = null;

			try {
				imgUrl = new URL(param[0]);
			} catch (MalformedURLException e) {
				// bitm = bitmDefault;
			}

			try {
				Bitmap bitmProfile = BitmapFactory.decodeStream(imgUrl
						.openConnection().getInputStream());
				bitm = pp.getRoundedShape(bitmProfile);

			} catch (IOException e) {
				// bitm = bitmDefault;
			}

			return bitm;

		}

		protected void onProgressUpdate(String... progress) {
			// NO OP
		}

		protected void onPostExecute(Bitmap ret) {
			if (ret != null) {
				img.setImageBitmap(ret);
				Log.i("ImageLoadTask", "Successfully loaded " + name + " image");
				// image = ret;
			} else {
				Log.e("ImageLoadTask", "Failed to load " + name + " image");
			}
		}
	}
}

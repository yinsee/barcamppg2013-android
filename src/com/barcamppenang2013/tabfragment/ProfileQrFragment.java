package com.barcamppenang2013.tabfragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.barcamppenang2013.R;
import com.barcamppenang2013.MainActivity;
import com.barcamppenang2013.MyDatabase;
import com.barcamppenang2013.QRCodeEncoder;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;

public class ProfileQrFragment extends Fragment implements TabInterface {
	public static final String TITLE = "  ProfileQr";
	private ImageView img_myqr;
	private Button btn_edit;
	// Google Analytics
	private Tracker GaTracker;
	private GoogleAnalytics GaInstance;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GaInstance = GoogleAnalytics.getInstance(getActivity());
		GaTracker  = GaInstance.getTracker("UA-35359053-9");
		GaTracker.sendView("TITLE");
	}

	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.user_qr, container, false);

		img_myqr = (ImageView) v.findViewById(R.id.imgMyQr);
		btn_edit = (Button) v.findViewById(R.id.btnEdit);

		String encodeData = getMyInfo();

		int qrCodeDimention = 500;

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(encodeData, null,
				Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
				qrCodeDimention);

		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			img_myqr.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		btn_edit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				MainActivity main = (MainActivity) getActivity();
//				main.switchContentWithinTab(new ProfileFragment(), TITLE);
				main.switchContent(new ProfileFragment());
			}
		});

		return v;
	}

	private String getMyInfo() {

		MyDatabase database = new MyDatabase(getActivity());
		SQLiteDatabase sqliteDatabase = database.getReadableDatabase();
		String sql = "SELECT * FROM USERPROFILE;";
		String completeStr = null;
		Cursor retrieved = sqliteDatabase.rawQuery(sql, null);

		while (retrieved.moveToNext()) {
			String name = retrieved.getString(retrieved
					.getColumnIndex("MYNAME"));
			String email = retrieved.getString(retrieved
					.getColumnIndex("MYEMAIL"));
			String phone = retrieved.getString(retrieved
					.getColumnIndex("MYPHONE"));
			String profession = retrieved.getString(retrieved
					.getColumnIndex("MYPROFESSION"));
			String fbId = retrieved.getString(retrieved
					.getColumnIndex("MYFBID"));

			completeStr = name + "||" + email + "||" + phone + "||"
					+ profession + "||" + fbId;
			Log.d("completeStr", completeStr);
		}
		retrieved.close();
		database.close();
		sqliteDatabase.close();

		return completeStr;

	}

	@Override
	public String printTitle() {
		return ProfileQrFragment.TITLE;
	}
}

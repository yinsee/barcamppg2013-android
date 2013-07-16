package com.barcamppenang2013;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class FriendObject {
	String name;
	String email;
	String phone;
	String profession;
	String id;
	String fbId;
	Bitmap image;
	CustomAdapter adapter;

	/*
	 * public FriendObject() { this.name = ""; this.email = ""; this.phone = "";
	 * }
	 */
	public FriendObject() {
		this.name = "";
		this.email = "";
		this.phone = "";
		this.profession = "";
		this.id = "";
		this.fbId = "";
		this.image = null;
	}

	public CustomAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(CustomAdapter adapter) {
		this.adapter = adapter;
	}

	public Bitmap getImage() {
		return image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
		// this.fbId = "http://graph.facebook.com/111/picture?type=large" ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void loadImage(CustomAdapter adapter) {
		
		this.adapter = adapter;

		if (fbId != null && !fbId.equals("")) {
			new ImageLoadTask().execute("http://graph.facebook.com/" + fbId
					+ "/picture?type=large");
		}
	}

	
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
			
		}

		protected void onPostExecute(Bitmap ret) {
			if (ret != null) {
				//Log.i("ImageLoadTask", "Successfully loaded " + name + " image");
				image = ret;
				if (adapter != null) {
					
					adapter.notifyDataSetChanged();
				}
			} else {
				//Log.e("ImageLoadTask", "Failed to load " + name + " image");
			}
		}
	}
}

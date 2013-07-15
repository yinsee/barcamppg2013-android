package com.barcamppenang2013;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	List<FriendObject> friendObjs = new ArrayList<FriendObject>();
	private ArrayList<FriendObject> arraylist;
	Context context;

	public CustomAdapter(List<FriendObject> data, Context c) {
		friendObjs = data;
		context = c;
		this.arraylist = new ArrayList<FriendObject>();
		this.arraylist.addAll(friendObjs);
	}

	public int getCount() {
		return friendObjs.size();
	}

	public Object getItem(int position) {
		return friendObjs.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.friend_rowview, null);
		}

		TextView tv_name = (TextView) v.findViewById(R.id.tvName);
		TextView tv_profession = (TextView) v.findViewById(R.id.tvProfession);
		ImageView img = (ImageView) v.findViewById(R.id.pic);

		tv_name.setText(friendObjs.get(position).getName());
		tv_profession.setText(friendObjs.get(position).getProfession());

		// Set default photo if there's no internet access or fb id is not
		// working.
		// String fbid = friendObjs.get(position).getFbId();

		if (friendObjs.get(position).getImage() != null) {
			img.setImageBitmap(friendObjs.get(position).getImage());
		} else {

			RoundProfilePicture pp = new RoundProfilePicture();
			Bitmap bitmDefault = pp.getRoundedShape(BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.defaultpic));
			img.setImageBitmap(bitmDefault);
		}

	
		return v;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		friendObjs.clear();
		if (charText.length() == 0) {
			friendObjs.addAll(arraylist);
		} else {
			for (FriendObject wp : arraylist) {
				if (wp.getName().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					friendObjs.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}

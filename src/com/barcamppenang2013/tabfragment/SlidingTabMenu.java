package com.barcamppenang2013.tabfragment;

import com.barcamppenang2013.MainActivity;
import com.barcamppenang2013.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SlidingTabMenu extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

//		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
//				android.R.layout.simple_list_item_1, android.R.id.text1, tabNames);
//		setListAdapter(colorAdapter);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		adapter.add(new SampleItem(HomeFragment.TITLE, R.drawable.barcamp));
		adapter.add(new SampleItem(MapFragment.TITLE, android.R.drawable.ic_dialog_map));
		adapter.add(new SampleItem(AgendaFragment.TITLE, android.R.drawable.ic_dialog_info));
		adapter.add(new SampleItem(ProfileFragment.TITLE, android.R.drawable.ic_menu_myplaces));
		adapter.add(new SampleItem(BadgeFragment.TITLE, android.R.drawable.ic_menu_slideshow));
		
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new HomeFragment();
			break;
		case 1:
			newContent = new MapFragment();
			break;
		case 2:
			newContent = new AgendaFragment();
			break;
		case 3:
			newContent = new ProfileFragment();
			break;
		case 4:
			newContent = new BadgeFragment();
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
	}
	
	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}
	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
}

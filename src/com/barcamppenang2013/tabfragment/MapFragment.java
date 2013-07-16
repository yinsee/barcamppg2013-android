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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.barcamppenang2013.MainActivity;
import com.barcamppenang2013.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements TabInterface {
	public static final String TITLE = "  Map";
	private static View view;
	private GoogleMap mMap;
	public static final LatLng QUEENSBAY = new LatLng(5.33292, 100.3066);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		((MainActivity) getActivity()).getSupportActionBar()
//				.setHomeButtonEnabled(true);

	}
	
	@Override
	public void onResume(){
		super.onResume();
//		ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
//		actionBar.setTitle(TITLE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// View v = inflater.inflate(R.layout.info_page, container, false);
		// return v;
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.map_layout, container, false);
			mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			Marker marker = mMap.addMarker(new MarkerOptions()
					.position(QUEENSBAY)
					.title("Click Me!")
					.snippet("How To Go"));
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				public void onInfoWindowClick(Marker marker) {

					if (getActivity() == null)
						return;
					MainActivity fca = (MainActivity) getActivity();
						fca.switchContent(new IndoorMapFragment());
//					Intent intent = new Intent();                   
//					intent.setAction(Intent.ACTION_VIEW);
//					Uri hacked_uri = Uri.parse("file://" + Uri.parse("android.resource://com.barcamppenang2013/"+R.drawable.ic_launcher).getPath());
//					Log.d("ddw", "file://" + Uri.parse("android.resource://com.barcamppenang2013/"+R.drawable.ic_launcher).getPath());
//					intent.setDataAndType(hacked_uri,"image/png");
//					((MainActivity)getActivity()).startActivity(intent);
				}
			});
			marker.showInfoWindow();
			mMap.getUiSettings().setZoomControlsEnabled(false);
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(5.339027, 100.3066)) // Sets the center
															// of the map to
															// Mountain View
					.zoom(12) // Sets the zoom
					.build(); // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		} catch (InflateException e) {
			/* map is already there, just return view as it is */
		}
		return view;
	}

	@Override
	public String printTitle() {
		return MapFragment.TITLE;
	}
}

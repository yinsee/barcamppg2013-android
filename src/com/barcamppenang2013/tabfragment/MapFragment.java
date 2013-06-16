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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barcamppenang2013.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

    public class MapFragment extends Fragment implements TabInterface{
    	public static final String TITLE = "Map";
    	private static View view;
    	private GoogleMap mMap;
    	public static final LatLng QUEENSBAY = new LatLng(5.33292,100.3066);
    	
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.info_page, container, false);
//            return v;
        	if (view != null) {
		        ViewGroup parent = (ViewGroup) view.getParent();
		        if (parent != null)
		            parent.removeView(view);
		    }
		    try {
		        view = inflater.inflate(R.layout.map_layout, container, false);
		        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	            Marker marker = mMap.addMarker(new MarkerOptions()
			                    .position(QUEENSBAY)
			                    .title("InfoTrek, Queensbay Mall"));
	            marker.showInfoWindow();
	            CameraPosition cameraPosition = new CameraPosition.Builder()
	            .target(QUEENSBAY)      // Sets the center of the map to Mountain View
	            .zoom(14)               // Sets the zoom
	            .build();               // Creates a CameraPosition from the builder
	            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		    } catch (InflateException e) {
		        /* map is already there, just return view as it is */
		    }
		    return view;
        }
        
        @Override
        public String printTitle(){
        	return MapFragment.TITLE;
        }
    }

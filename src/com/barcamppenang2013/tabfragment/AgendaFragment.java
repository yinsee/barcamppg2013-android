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

import com.barcamppenang2013.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


    public class AgendaFragment extends ListFragment implements TabInterface {
    	public static final String TITLE = "Agenda";
    	
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.friend_page, container, false);
//            return v;
//        }
    	  public void onActivityCreated(Bundle savedInstanceState) {
    		    super.onActivityCreated(savedInstanceState);
    		    String[] values = new String[] {
    		    		"08:30 - slot1", 
    		    		"09:30 - slot2",
    		    		"10:30 - slot3",
    		    		"11:30 - slot4", 
    		    		"12:30 - lunch break", 
    		    		"14:30 - slot5",
    		    		"15:30 - slot6", 
    		    		"16:30 - slot7", 
    		    		"17:30 - slot8",
    		    	};
    		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
    		        android.R.layout.simple_list_item_1, values);
    		    setListAdapter(adapter);
    		  }

    		  @Override
    		  public void onListItemClick(ListView l, View v, int position, long id) {
    		    // Do something with the data
    			  this.getActivity().getSupportFragmentManager()
    				.beginTransaction()
    				.replace(R.id.content_frame, new AgendaDetailFragment())
    				.addToBackStack( "tag" )
    				.commit();
    		  }
        @Override
        public String printTitle(){
        	return AgendaFragment.TITLE;
        }
    }

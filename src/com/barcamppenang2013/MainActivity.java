package com.barcamppenang2013;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.barcamppenang2013.tabfragment.BadgeFragment;
import com.barcamppenang2013.tabfragment.HomeFragment;
import com.barcamppenang2013.tabfragment.MapFragment;
import com.barcamppenang2013.tabfragment.TabInterface;

public class MainActivity extends SherlockFragmentActivity {

	private Fragment fragment;
	private String mUrl = "https://docs.google.com/spreadsheet/pub?key=0AhLn4HpbOY9JdEJqVTBFNU5MaHdHMGRuMDFIcEVxX3c&output=html";
	private final static int REFRESH_MENU_ID = 0x1234;
    private FragmentTabHost mTabHost;
    private final static String HOME_TAB = "Home";
    private final static String MAP_TAB = "Map";
    private final static String AGENDA_TAB = "Agenda";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
//		if (savedInstanceState != null) {
//			fragment = getSupportFragmentManager().getFragment(
//					savedInstanceState, "fragment");
//			updateActionBarTitle(fragment);
//		}
		if (fragment == null) {
			fragment = new HomeFragment();
			updateActionBarTitle(fragment);
		}
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(HOME_TAB).setIndicator(HOME_TAB),
        		HomeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(MAP_TAB).setIndicator(MAP_TAB),
        		MapFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(AGENDA_TAB).setIndicator(AGENDA_TAB),
        		BadgeFragment.class, null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
        	
        @Override
        public void onTabChanged(String tabId) {
            ActionBar actionBar = getSupportActionBar();
    		actionBar.setTitle(tabId);
        }
       	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, REFRESH_MENU_ID, Menu.NONE, "Refresh")
				.setIcon(R.drawable.ic_refresh_inverse)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH_MENU_ID:
			(new Downloader(this)).execute(mUrl);
			switchContent(new BadgeFragment());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void switchContent(Fragment fragment) {
		updateActionBarTitle(fragment);
	}

	public void updateActionBarTitle(Fragment fragment) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(((TabInterface) fragment).printTitle());
//		actionBar.setIcon(R.drawable.ic_action_github);
	}

	@Override
	protected void onResume() {
		super.onResume();
		(new Downloader(this)).execute(mUrl);
	}

	static class Downloader extends AsyncTask<String, Void, String> {
		MainActivity activity = null;
		private Exception exception;

		Downloader(MainActivity pActivity) {
			activity = pActivity;
		}

		protected String doInBackground(String... urls) {
			try {
				return httpGet(urls[0]);
			} catch (Exception e) {
				Log.d("ddw", e.toString());
				return null;
			}
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String result) {
			// Log.d("ddw",result);
			if (result != null) {
				FileOutputStream fOut = null;
				try {
					fOut = activity.openFileOutput("agenda.html",
							Context.MODE_WORLD_WRITEABLE);
				} catch (FileNotFoundException e) {
					Log.d("ddw1", e.toString());
				}

				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				try {
					osw.write(result);
					osw.flush();
					osw.close();
				} catch (IOException e) {
					Log.d("ddw", e.toString());
				}
			}
		}

		public String httpGet(String url) throws URISyntaxException,
				ClientProtocolException, IOException {
			String htmlBody = null;
			try {
				HttpGet request = new HttpGet();
				HttpClient client = new DefaultHttpClient();

				request.setURI(new URI(url));
				HttpResponse response = client.execute(request);

				htmlBody = EntityUtils.toString(response.getEntity());
			} catch (Exception ex) {
				Log.d("ddw", ex.toString());
			}
			return htmlBody;
		}
	}

}

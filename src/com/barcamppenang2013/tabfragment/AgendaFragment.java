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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AgendaFragment extends Fragment implements TabInterface {
	// public static final String TITLE = "Badges";
	public static final String TITLE = "New Agenda";
	private WebView mWebView;
	private boolean mIsWebViewAvailable;
	// real one
	// private String mUrl =
	// "https://docs.google.com/spreadsheet/pub?key=0Ah8I5GkmGav8dEZlT0JxclpiMTJJMVpnUE9lSGoyUVE&output=html";

	// a duplicate for testing

	/**
	 * Called to instantiate the view. Creates and returns the WebView.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mWebView != null) {
			mWebView.destroy();
		}
		mWebView = new WebView(getActivity());
		mWebView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
					mWebView.goBack();
					return true;
				}
				return false;
			}

		});
		mWebView.setWebViewClient(new InnerWebViewClient()); // forces it to
																// open in app

		// mUrl = "file:///android_asset/agenda/about.html";
		// mUrl = this.getActivity().getFilesDir()+"/agenda.html";
		// mUrl = "file:///data/about.html";
		// mWebView.loadUrl(mUrl);

		mWebView.loadData(readFromFile("agenda.html"), "text/html", "utf-8");

		// Log.d("ddw", "outside!!");
		mIsWebViewAvailable = true;
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		return mWebView;
	}

	/**
	 * Convenience method for loading a url. Will fail if {@link View} is not
	 * initialised (but won't throw an {@link Exception})
	 * 
	 * @param url
	 */
//	public void loadUrl(String url) {
//		if (mIsWebViewAvailable)
//			getWebView().loadUrl(mUrl = url);
//		else
//			Log.w("ImprovedWebViewFragment",
//					"WebView cannot be found. Check the view and fragment have been loaded.");
//	}

	/**
	 * Called when the fragment is visible to the user and actively running.
	 * Resumes the WebView.
	 */
	@Override
	public void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	/**
	 * Called when the fragment is no longer resumed. Pauses the WebView.
	 */
	@Override
	public void onResume() {
		mWebView.onResume();
		super.onResume();
	}

	/**
	 * Called when the WebView has been detached from the fragment. The WebView
	 * is no longer available after this time.
	 */
	@Override
	public void onDestroyView() {
		mIsWebViewAvailable = false;
		super.onDestroyView();
	}

	/**
	 * Called when the fragment is no longer in use. Destroys the internal state
	 * of the WebView.
	 */
	@Override
	public void onDestroy() {
		if (mWebView != null) {
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	/**
	 * Gets the WebView.
	 */
	public WebView getWebView() {
		return mIsWebViewAvailable ? mWebView : null;
	}

	/* To ensure links open within the application */
	private class InnerWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public String printTitle() {
		return AgendaFragment.TITLE;
	}



	private String readFromFile(String fileName) {

		String ret = "";

		try {
			InputStream inputStream = this.getActivity()
					.openFileInput(fileName);

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}
		return ret;
	}
}

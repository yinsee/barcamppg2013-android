package com.barcamppenang2013;

import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class HelpActivity extends SherlockFragmentActivity {

	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 10; // 1 seconds
	private WebView mWebView;
	private boolean mIsWebViewAvailable;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 ActionBar bar = this.getSupportActionBar();
		 bar.setTitle("  How To Exchange Contacts");
		 BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(
				R.drawable.bg_striped);
		 bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		 getSupportActionBar().setBackgroundDrawable(bg);

		 BitmapDrawable bgSplit = (BitmapDrawable) getResources().getDrawable(
				R.drawable.bg_striped_split_img);
		 bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		 getSupportActionBar().setSplitBackgroundDrawable(bgSplit);
		 mWebView = new WebView(this);
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
			mWebView.setWebViewClient(new InnerWebViewClient());
			String imageUrl =  "file:///android_asset/help_page.png";

//	        mWebView.getSettings().setBuiltInZoomControls(true);
	        mWebView.loadUrl(imageUrl);

			mIsWebViewAvailable = true;
//			WebSettings settings = mWebView.getSettings();
//			settings.setJavaScriptEnabled(true);
			setContentView(mWebView);
	}
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
	public void onBackPressed() {
		mIsBackButtonPressed = true;
		super.onBackPressed();
	}
}

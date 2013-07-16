package com.barcamppenang2013;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 10; // 1 seconds

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
				if (!mIsBackButtonPressed) {
					Intent intent = new Intent(SplashScreenActivity.this,
							MainActivity.class);
					SplashScreenActivity.this.startActivity(intent);
				}
			}
		}, SPLASH_DURATION);
	}

	@Override
	public void onBackPressed() {
		mIsBackButtonPressed = true;
		super.onBackPressed();
	}
}

package com.barcamppenang2013;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SplashScreenActivity extends SherlockFragmentActivity {

	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 10; // 1 seconds

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		 BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(
				R.drawable.bg_striped);
		 bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		 getSupportActionBar().setBackgroundDrawable(bg);

		 BitmapDrawable bgSplit = (BitmapDrawable) getResources().getDrawable(
				R.drawable.bg_striped_split_img);
		 bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		 getSupportActionBar().setSplitBackgroundDrawable(bgSplit);
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

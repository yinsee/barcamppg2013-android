package com.barcamppenang2013;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.barcamppenang2013.tabfragment.HomeFragment;
import com.barcamppenang2013.tabfragment.SlidingTabMenu;
import com.barcamppenang2013.tabfragment.TabInterface;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends BaseActivity {
	
	private Fragment fragment;
	private View mMenuFrame;
	
	public MainActivity() {
		super(R.string.changing_fragments);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null)
			{fragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
			updateActionBarTitle(fragment);}
		if (fragment == null)
			{fragment = new HomeFragment();
			updateActionBarTitle(fragment);}
		
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		
		// set the Behind View
		setBehindContentView(R.layout.sliding_menu_frame);
		mMenuFrame = findViewById(R.id.menu_frame);
		mMenuFrame.setBackgroundColor(Color.BLACK);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new SlidingTabMenu())
		.commit();
		
		// customize the SlidingMenu
		SlidingMenu mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setBehindWidth(400);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//getSupportFragmentManager().putFragment(outState, "fragment", fragment);
	}
	
	public void switchContent(Fragment fragment) {
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
		updateActionBarTitle(fragment);
	}
	
	public void updateActionBarTitle(Fragment fragment){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(((TabInterface)fragment).printTitle()); 
	}

}
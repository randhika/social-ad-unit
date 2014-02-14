package com.example.socialaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

import com.facebook.Session;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{
	
	ViewPager mViewPager;
	PagerAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // for each of the sections in the app, add a tab to the action bar.
	    actionBar.addTab(actionBar.newTab().setText("Contacts")
	        .setTabListener(this));
	    actionBar.addTab(actionBar.newTab().setText("Facebook")
	        .setTabListener(this));
	    
	    mAdapter = new PagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	public class PagerAdapter extends FragmentStatePagerAdapter {
	    public PagerAdapter(FragmentManager fm) {
	        super(fm);
	    }

	    @Override
	    public Fragment getItem(int i) {
	    	Fragment fragment = null;
	    	if( i == 0 ) {
	    		fragment = new ContactsFragment();
	    	} else if ( i ==1 ) {
	    		fragment = new FbFragment();
	    	}
	        return fragment;
	    }

	    @Override
	    public int getCount() {
	        return 2;
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	        return "OBJECT " + (position + 1);
	    }
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}

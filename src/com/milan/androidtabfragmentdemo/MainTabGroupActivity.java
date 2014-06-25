package com.milan.androidtabfragmentdemo;


import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MainTabGroupActivity extends Activity implements TabHost.OnTabChangeListener{

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tabgroup);
		initialiseTabHost(savedInstanceState);
		
	}
	
	private View getTabHeader(String tabTitle, int drawable) {

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab,
				null);
		TextView textView = (TextView) tabIndicator.findViewById(R.id.tabText);
		textView.setText(tabTitle);
		if(tabTitle.length()==0)
			textView.setVisibility(View.GONE);
		((ImageView) tabIndicator.findViewById(R.id.tabIcon))
				.setImageResource(drawable);

		return tabIndicator;
	}
	
	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		// tabWidget.set
		mTabHost.setup();
		TabInfo tabInfo = null;
		addTab(this.mTabHost.newTabSpec("Tab1").setIndicator(
						getTabHeader("",
								R.drawable.profile_tab)), (tabInfo = new TabInfo(
						"Tab1", MyProfileFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		addTab(
				this.mTabHost.newTabSpec("Tab2").setIndicator(
						getTabHeader(getString(R.string.tab_organize),
								R.drawable.organize_tab)),
				(tabInfo = new TabInfo("Tab2", MyProfileFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		addTab(
				this.mTabHost.newTabSpec("Tab3").setIndicator(
						getTabHeader(getString(R.string.tab_home),
								R.drawable.home_tan)),
				(tabInfo = new TabInfo("Tab3", MyProfileFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		addTab(
						this.mTabHost.newTabSpec("Tab4").setIndicator(
								getTabHeader(getString(R.string.tab_my_plans),
										R.drawable.my_plans_tab)),
						(tabInfo = new TabInfo("Tab4",
								MyProfileFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
			addTab(
					this.mTabHost.newTabSpec("Tab5").setIndicator(
							getTabHeader(getString(R.string.tab_chats),
									R.drawable.chats_tab)),
					(tabInfo = new TabInfo("Tab5", MyProfileFragment.class,
							args)));
		
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to third tab

			try {
				mTabHost.setCurrentTab(0);
				this.onTabChanged("Tab1");
			} catch (Exception e) {
				e.printStackTrace();
			}
		mTabHost.setOnTabChangedListener(this);

	}
	
	private void addTab(TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new TabFactory(MainTabGroupActivity.this));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = getFragmentManager()
				.findFragmentByTag(tag);

		mTabHost.addTab(tabSpec);

	}

	@Override
	public void onTabChanged(String tabId) {
		

		// adding tab
		TabInfo newTab = this.mapTabInfo.get(tabId);
		if (mLastTab != newTab) {
			FragmentTransaction ft = getFragmentManager()
					.beginTransaction();

			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this,
							newTab.clss.getName(), newTab.args);
					
					
					ft.add(android.R.id.tabcontent, newTab.fragment, newTab.tag);
					if(mLastTab!=null)
						ft.hide(mLastTab.fragment);

				}else{
					ft.show(newTab.fragment);
					ft.hide(mLastTab.fragment);
				}
			}

			mLastTab = newTab;

			ft.commit();
			getFragmentManager().executePendingTransactions();

		}
		
	}
	
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;

		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}
}

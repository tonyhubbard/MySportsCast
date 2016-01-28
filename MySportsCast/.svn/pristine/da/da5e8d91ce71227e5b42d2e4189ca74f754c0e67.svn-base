package com.mysportsshare.mysportscast.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mysportsshare.mysportscast.R;
import com.mysportsshare.mysportscast.fragments.SearchEventFragment;
import com.mysportsshare.mysportscast.fragments.SearchPageFragment;
import com.mysportsshare.mysportscast.fragments.SearchPeopleFragment;
import com.mysportsshare.mysportscast.utils.Constants;
import com.mysportsshare.mysportscast.utils.CustomLog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;

public class SearchPageAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "EVENTS", "PEOPLE" };
	static HashMap<String, Fragment> fragmentPages;
	private String searchStr;
	Context context;

	public SearchPageAdapter(FragmentManager fm) {
		super(fm);
		if (fragmentPages == null) {
			fragmentPages = new HashMap();
		}
	}

	public String getSearchStr() {
		return searchStr;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		// Open FragmentTab1.java
		case 0:
			SearchEventFragment fragmenttab1;
			if (fragmentPages.get("0") == null) {
				fragmenttab1 = new SearchEventFragment();
				fragmentPages.put("0", fragmenttab1);
			} else {
				fragmenttab1 = (SearchEventFragment) fragmentPages.get(position
						+ "");
			}
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			SearchPeopleFragment fragmenttab2;
			if (fragmentPages.get("1") == null) {
				fragmenttab2 = new SearchPeopleFragment();
				fragmentPages.put("1", fragmenttab2);
			} else {
				fragmenttab2 = (SearchPeopleFragment) fragmentPages
						.get(position + "");
			}
			return fragmenttab2;
		default:
			break;
		}
		Log.v("testnull", "null");
		return null;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public float getPageWidth(int position) {

		return super.getPageWidth(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}

	/*@Override
	public Object instantiateItem(ViewGroup container, int position) {
		switch (position) {
		case 0:
			if (fragmentPages.get("0") != null) {
				SearchEventFragment g = (SearchEventFragment) fragmentPages
						.get("0");
				SearchPageFragment parentFragment = (SearchPageFragment) g
						.getParentFragment();
				if (parentFragment != null) {
					String searchStr = parentFragment.searchData;
					if (searchStr != null) {
						if (searchStr.trim().length() != 0) {
							g.getSearchResult(searchStr);
						}
					}else{
						g.serviceToGetDefaultResults();
					}

				}
				return g;
			}
			return super.instantiateItem(container, position);
		case 1:
			if (fragmentPages.get("1") != null) {
				SearchPeopleFragment g = (SearchPeopleFragment) fragmentPages
						.get("1");
				SearchPageFragment parentFragment = (SearchPageFragment) g
						.getParentFragment();
				if (parentFragment != null) {
					String searchStr = parentFragment.searchData;
					if (searchStr != null) {
						if (searchStr.trim().length() != 0) {
							g.getSearchResult(searchStr);
						}
					}else{
						g.serviceToGetDefaultResults();
					}
				}
				return g;
			}
			return super.instantiateItem(container, position);
		default:
			// Fragment g = fragmentPages.get(position);
			// GridView gridView = (GridView) g.getView();
			Log.v("default called", "true");
			return super.instantiateItem(container, position);
		}

	}*/

	public void updateSearchData() {
		CustomLog.i("SearchPageAdapter:", "search: " + searchStr);
		// Updating events data
		SearchEventFragment searchFragment = (SearchEventFragment) fragmentPages
				.get("0");
		if (searchFragment == null) {
			return;
		}
		SearchPageFragment parentFragment = (SearchPageFragment) searchFragment
				.getParentFragment();
		if (parentFragment != null) {
			String searchStr = parentFragment.searchData;
			CustomLog.v("SearchPageAdapter:", "search: " + searchStr);
			if (searchStr != null) {
				if (searchStr.trim().length() != 0) {
					searchFragment.getSearchResult(searchStr);
				} else {
					searchFragment.serviceToGetDefaultResults();
				}
			} else {
				searchFragment.serviceToGetDefaultResults();
			}
		}

		// Updating pages data
		SearchPeopleFragment mSearchPageFragment = (SearchPeopleFragment) fragmentPages.get("1");
		if (mSearchPageFragment == null) {
			return;
		}
		SearchPageFragment parentFragment1 = (SearchPageFragment) mSearchPageFragment
				.getParentFragment();
		if (parentFragment1 != null) {
			String searchStr = parentFragment1.searchData;
			CustomLog.v("SearchPageAdapter:", "search: " + searchStr);
			if (searchStr != null) {
				if (searchStr.trim().length() != 0) {
					mSearchPageFragment.getSearchResult(searchStr);
				} else {
					mSearchPageFragment.serviceToGetDefaultResults();
				}
			} else {
				mSearchPageFragment.serviceToGetDefaultResults();
			}
		}
	}
}

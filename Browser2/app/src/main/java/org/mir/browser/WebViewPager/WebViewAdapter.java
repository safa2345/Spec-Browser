package org.mir.browser.WebViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class WebViewAdapter extends FragmentStateAdapter {

    private ArrayList<WebViewFragment> fragmentList;

    public WebViewAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<WebViewFragment> fragmentList) {
        super(fragmentManager, lifecycle);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }



    @Override
    public int getItemCount() {
        return fragmentList.size();
    }



    public void addFragment(WebViewFragment fragment) {
        fragmentList.add(fragment);
        notifyItemInserted(fragmentList.size());
    }

    public void removeFragment(int position) {
        fragmentList.remove(position);
        notifyItemRangeChanged(position, fragmentList.size());
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return fragmentList.get(position).hashCode();
    }

    public WebViewFragment focusedWebView(int position) {
        return fragmentList.get(position);
    }

}

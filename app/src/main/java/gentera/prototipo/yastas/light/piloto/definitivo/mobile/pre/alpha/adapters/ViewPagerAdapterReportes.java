package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapterReportes extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    ArrayList<Drawable> icons = new ArrayList<>();

    public void addFragments(Fragment fragments, String tabTitles, Drawable icons) {
        this.fragments.add(fragments);
        this.tabTitles.add(tabTitles);
        this.icons.add(icons);
    }
    public ViewPagerAdapterReportes(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}

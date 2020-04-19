package example.com.furnitureproject.view.navigation;

import android.support.v4.app.Fragment;

public class FragmentItem {
    public int id;
    public Fragment fragment;
    public Class clz;

    public FragmentItem(int id,Class<?> clz){
        this.id = id;
        this.clz = clz;
    }
}

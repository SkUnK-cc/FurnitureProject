package example.com.furnitureproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.util.ArrayList;

import example.com.furnitureproject.R;
import example.com.furnitureproject.fragment.FragmentAdd;
import example.com.furnitureproject.fragment.FragmentBill;
import example.com.furnitureproject.fragment.FragmentCard;
import example.com.furnitureproject.fragment.FragmentMe;
import example.com.furnitureproject.fragment.chart.FragmentChart;
import example.com.furnitureproject.view.navigation.BottomAdapter;
import example.com.furnitureproject.view.navigation.FragmentItem;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView navigation;

    private BottomAdapter bottomAdapter;

    private ArrayList<FragmentItem> fragmentList;

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId()==R.id.navigation_add){
                Intent intent = new Intent(MainActivity.this,AccountAddActivity.class);
                startActivity(intent);
                return false;
            }
            for(int i=0;i<fragmentList.size();i++){
                if(fragmentList.get(i).id == item.getItemId()){
                    viewPager.setCurrentItem(i);
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);

        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentItem(R.id.navigation_bill, FragmentBill.class));
        fragmentList.add(new FragmentItem(R.id.navigation_chart, FragmentChart.class));
        fragmentList.add(new FragmentItem(R.id.navigation_add, FragmentAdd.class));
        fragmentList.add(new FragmentItem(R.id.navigation_card, FragmentCard.class));
        fragmentList.add(new FragmentItem(R.id.navigation_me, FragmentMe.class));

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        bottomAdapter = new BottomAdapter(this,getSupportFragmentManager());
        bottomAdapter.setData(fragmentList);
        viewPager.setAdapter(bottomAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView navigationView) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

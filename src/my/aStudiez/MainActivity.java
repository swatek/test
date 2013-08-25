package my.aStudiez;

import my.aStudiez.adapters.PagerAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

	Button btn_today;
	Button btn_assign;
	Button btn_planner;
	ViewPager mViewPager;
	AStudiezFragmentToday fragmentToday;
	AStudiezFragmentAssignments fragmentAss;
	AStudiezFragmentPlanner fragmentPlanner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
       
		setContentView(R.layout.main);
		
		//initialize paging
		initializePaging();
		
		//initialize buttons
		btn_assign =(Button)findViewById(R.id.button_assignments);
		btn_today = (Button)findViewById(R.id.button_today);
		btn_planner = (Button)findViewById(R.id.button_planner);
	
		//initialize button listeners
		btn_today.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	mViewPager.setCurrentItem(0);
	        }
	    });
		btn_assign.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	mViewPager.setCurrentItem(1);
	        }
	    });
		
		btn_planner.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	mViewPager.setCurrentItem(2);
	        }
	    });
	}
	
	

	private void initializePaging() {
	
		fragmentToday = new AStudiezFragmentToday();
		fragmentAss = new AStudiezFragmentAssignments();
		fragmentPlanner = new AStudiezFragmentPlanner();
	
		PagerAdapter mPagerAdapter = new PagerAdapter(
				getSupportFragmentManager());
		mPagerAdapter.addFragment(fragmentToday);
		mPagerAdapter.addFragment(fragmentAss);
		mPagerAdapter.addFragment(fragmentPlanner);
	
		mViewPager = (ViewPager) super
				.findViewById(R.id.awesomepager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setCurrentItem(0);
		fragmentToday.main = this;
		fragmentAss.main = this;
		fragmentPlanner.main = this;
	}
	
}
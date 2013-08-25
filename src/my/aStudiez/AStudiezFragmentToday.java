package my.aStudiez;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import my.aStudiez.adapters.LazyAdapterToday;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AStudiezFragmentToday extends Fragment {
	
	//HashMap ids
	public static final String CLASS_ID = "class_id";
	public static final String COURSE_ID = "course_id";
	public static final String COURSE_NAME = "course_name";
	public static final String CLASS_TYPE = "class_type";
	public static final String TEACHER_SECOND_NAME = "teacher_second_name";
	public static final String CLASS_LOCATION = "class_location";
	public static final String TIME = "time";
	
	public static final int SAVED_COURSE = 1;
	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;
	
	ListView list;
	LazyAdapterToday adapterToday;
	ArrayList<HashMap<String, String>> dataListToday;
	Button btn_add_class_today;
	Button btn_today_up;
	Button btn_day_back;
	Button btn_day_forward;
	Calendar now;
	DbHelper dbHelper;
	View view;
	int year;
	int month;
	int day;
	int semester_id_today;
	MainActivity main;
	
	public AStudiezFragmentToday() {
		super();
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					//Initialize HashMap
					HashMap<String, String> dataMap = new HashMap<String, String>();
					//Read the right item
					dataMap = dataListToday.get(position);
					Intent i = new Intent(getActivity(), AStudiezActivityCourseName.class);
					i.putExtra(COURSE_ID, dataMap.get(COURSE_ID));
					i.putExtra(COURSE_NAME, dataMap.get(COURSE_NAME));
					i.putExtra("semester_id",semester_id_today);
					startActivityForResult(i,0);
			}
		});
		
		btn_add_class_today.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(getActivity(), AStudiezActivityAddClass.class);
	        	startActivityForResult(intent,0);
	        }
	    });
		
			btn_day_back.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				now.add(Calendar.DAY_OF_MONTH, -1);
				year = now.get(Calendar.YEAR);
				month = now.get(Calendar.MONTH)+1; // Note: zero based!
				day = now.get(Calendar.DAY_OF_MONTH);
				
				//Button temp = (Button)view.findViewById(R.id.button_today_up);
				if(isToday(day, month, year)) 
					btn_today_up.setText("Today");
				else
					btn_today_up.setText(day + "/" + month + "/" + year);
				
				semester_id_today=dbHelper.getSemesterByDate(Integer.toString(year), Integer.toString(month), Integer.toString(day));
				
				//call method to fill array list
				dataListToday.clear();
				fillArrayListToday(Integer.toString(year), Integer.toString(month), Integer.toString(day));
				
				// Getting adapter by passing data ArrayList
				adapterToday = new LazyAdapterToday(getActivity(), dataListToday);
				list.setAdapter(adapterToday);
				adapterToday.notifyDataSetChanged();	
			}
		});
		
		btn_day_forward.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				now.add(Calendar.DAY_OF_MONTH, 1);
				year = now.get(Calendar.YEAR);
				month = now.get(Calendar.MONTH)+1; // Note: zero based!
				day = now.get(Calendar.DAY_OF_MONTH);
				
				//TextView temp = (TextView)view.findViewById(R.id.button_today_up);
				if(isToday(day, month, year)) 
					btn_today_up.setText("Today");
				else
					btn_today_up.setText(day + "/" + month + "/" + year);

				
				semester_id_today=dbHelper.getSemesterByDate(Integer.toString(year), Integer.toString(month), Integer.toString(day));
				
				//call method to fill array list
				dataListToday.clear();
				fillArrayListToday(Integer.toString(year), Integer.toString(month), Integer.toString(day));
				
				// Getting adapter by passing data ArrayList
				adapterToday = new LazyAdapterToday(getActivity(), dataListToday);
				list.setAdapter(adapterToday);
				adapterToday.notifyDataSetChanged();	
			}
		});
		
	
		btn_today_up.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				now = Calendar.getInstance();
				year = now.get(Calendar.YEAR);
				month = now.get(Calendar.MONTH)+1; // Note: zero based!
				day = now.get(Calendar.DAY_OF_MONTH);
				
				//TextView temp = (TextView)view.findViewById(R.id.button_today_up);
				btn_today_up.setText("Today ");
	
				
				semester_id_today=dbHelper.getSemesterByDate(Integer.toString(year), Integer.toString(month), Integer.toString(day));
				
				//call method to fill array list
				dataListToday.clear();
				fillArrayListToday(Integer.toString(year), Integer.toString(month), Integer.toString(day));
				
				// Getting adapter by passing data ArrayList
				adapterToday = new LazyAdapterToday(getActivity(), dataListToday);
				list.setAdapter(adapterToday);
				adapterToday.notifyDataSetChanged();	
			}
		});
	}	
	
	protected void start(Intent intent) {
    	this.startActivityForResult(intent, 0);
    }

	public void onActivityResult(int requestCode, int resultCode, Intent data){
	    switch(requestCode) {
	    	case 0: 
	          if (resultCode == SAVED_COURSE) {
	        	  Intent intent = new Intent(getActivity(), MainActivity.class);
	        	  startActivity(intent); 
	        	  getActivity().finish();
	              break;
	          }
	          if (resultCode == TODAY) {
	        	  main.mViewPager.setCurrentItem(0);
	              break;
	          }
	          if (resultCode == ASSIGNMENTS) {
	        	  main.mViewPager.setCurrentItem(1);
	              break;
	          }
	          if (resultCode == PLANNER) {
	        	  main.mViewPager.setCurrentItem(2);
	              break;
	          }
	          
	    }
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	now = Calendar.getInstance();
    	view = inflater.inflate(R.layout.today, container, false);

	    dbHelper  = new DbHelper(getActivity());
		dataListToday = new ArrayList<HashMap<String, String>>();
		list = (ListView) view.findViewById(R.id.list_courses);
		btn_add_class_today= (Button) view.findViewById(R.id.button_add_class_today);
		btn_day_back = (Button) view.findViewById(R.id.button_day_back);
		btn_day_forward = (Button) view.findViewById(R.id.button_day_forward);
		btn_today_up = (Button) view.findViewById(R.id.button_today_up);
		
		//get today values
		//Calculate date for NOW
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH)+1; // Note: zero based!
		day = now.get(Calendar.DAY_OF_MONTH);
		
		semester_id_today=dbHelper.getSemesterByDate(Integer.toString(year), Integer.toString(month), Integer.toString(day));
		
		//call method to fill array list
		fillArrayListToday(Integer.toString(year), Integer.toString(month), Integer.toString(day));
		
		// Getting adapter by passing data ArrayList
		adapterToday = new LazyAdapterToday(getActivity(), dataListToday);
		list.setAdapter(adapterToday);
		return view;
    }
	
	/**
	 * Fills the ArrayList with HashMaps. Every HashMap is one
	 * row in listView. Methods gets cursor from dbHelper class methods
	 * and fills the HashMap with one row. HashMap is then added to 
	 * global ArrayList.
	 * */
	public void fillArrayListToday(String year, String month, String day){
		
		Cursor cursor = null;
		
		cursor = dbHelper.getDayClass(year, month, day);
		//cursor.moveToFirst();
		System.out.println(year+"-"+month+"-"+day);
		
		while(cursor.moveToNext()){
			
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(CLASS_ID, cursor.getString(0));
			map.put(COURSE_ID, cursor.getString(1));
			map.put(COURSE_NAME, cursor.getString(2));
			map.put(CLASS_TYPE, cursor.getString(3));
			//time is made of FROM and TO time and then concetanated
			String from = cursor.getString(4);
			String to = cursor.getString(5);
			map.put(TIME, from +" - " + to);
			
			// adding HashList to ArrayList
			dataListToday.add(map);
		}
	}
	
	boolean isToday(int day, int month, int year)
	{
		Calendar temp = Calendar.getInstance();
		if(day != temp.get(Calendar.DAY_OF_MONTH) || month - 1 != temp.get(Calendar.MONTH) || year != temp.get(Calendar.YEAR))
			return false;
		return true;	
	}


}
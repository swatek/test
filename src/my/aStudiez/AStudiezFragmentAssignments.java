package my.aStudiez;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import my.aStudiez.adapters.LazyAdapterAssignments;
import my.aStudiez.adapters.LazyAdapterCourses;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AStudiezFragmentAssignments extends Fragment {

	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;
	public static final int SAVED_ASS = 5;

	public static final String ASS_ID = "ass_id";
	public static final String ASS_NAME = "ass_name";
	public static final String ASS_DUE_DATE = "ass_due_date";
	public static final String ASS_PRIORITY = "ass_priority";
	public static final String ASS_COMPLETED = "ass_completed";
	public static final String ASS_DESCRIPTION = "ass_description";

	LazyAdapterAssignments adapterAss;
	LazyAdapterCourses adapterAssCourses;
	Button btn_by_course, btn_by_date, btn_by_priority, btn_add_ass;

	ListView listAss;
	ArrayList<HashMap<String, String>> dataListAss;
	Button btn_add;
	DbHelper dbHelper;
	View view;
	MainActivity main;

	int yearAss;
	int monthAss;
	int dayAss;
	int semester_id_ass;
	Boolean launchCourse;

	public AStudiezFragmentAssignments() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Click event for single list row
		launchCourse = false;
		listAss.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// Initialize HashMap
				HashMap<String, String> dataMap = new HashMap<String, String>();
				// Read the right item
				dataMap = dataListAss.get(position);
				Intent i;
				if(launchCourse){
					i = new Intent(getActivity(),
							AStudiezActivityCourseName.class);
					i.putExtra("semester_id", semester_id_ass);
					i.putExtra("course_id", dataMap.get("course_id"));
					i.putExtra("course_name", dataMap.get("course_name"));
				}
				else{
					i = new Intent(getActivity(),
							AStudiezActivityAssignmentDetails.class);
					i.putExtra("semester_id", semester_id_ass);
					//i.putExtra("course_id", dataMap.get("course_id"));
					i.putExtra("course_name", dataMap.get("course_name"));
					i.putExtra("ass_id", dataMap.get("ass_id"));
					i.putExtra("ass_name", dataMap.get("ass_name"));
					i.putExtra("ass_due_date", dataMap.get("ass_due_date"));
					i.putExtra("ass_priority", dataMap.get("ass_priority"));
					i.putExtra("ass_completed", dataMap.get("ass_completed"));
					i.putExtra("ass_description", dataMap.get("ass_description"));
				}
				startActivityForResult(i, 0);
			}
		});

		btn_by_course.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				launchCourse = true;
				dataListAss.clear();
				fillArrayListAssByCourse();
				adapterAssCourses = new LazyAdapterCourses(getActivity(),
						dataListAss);
				listAss.setAdapter(adapterAssCourses);
			}
		});

		btn_by_date.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				launchCourse = false;
				dataListAss.clear();
				fillArrayListAssByDate();
				adapterAss = new LazyAdapterAssignments(getActivity(),
						dataListAss);
				listAss.setAdapter(adapterAss);
			}
		});

		btn_by_priority.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				launchCourse = false;
				dataListAss.clear();
				fillArrayListAssByPriority();
				adapterAss = new LazyAdapterAssignments(getActivity(),
						dataListAss);
				listAss.setAdapter(adapterAss);
			}
		});

		btn_add_ass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intentReturn = new Intent(getActivity(),
						AStudiezActivityAddAssignment.class);
				intentReturn.putExtra("from", "AStudiezFragmentAssignments");
				intentReturn.putExtra("semester_id", semester_id_ass);
				startActivityForResult(intentReturn, 0);
			}
		});
	}

	protected void start(Intent intent) {
		this.startActivityForResult(intent, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
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
			if (resultCode == SAVED_ASS) {
				//Intent intent = new Intent(getActivity(), MainActivity.class);
				//startActivity(intent);
				//getActivity().finish();
				break;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.assignments_by, container, false);
		btn_by_course = (Button) view.findViewById(R.id.button_by_course);
		btn_by_date = (Button) view.findViewById(R.id.button_by_date);
		btn_by_priority = (Button) view.findViewById(R.id.button_by_priority);
		btn_add_ass = (Button) view.findViewById(R.id.button_add_ass);
		listAss = (ListView) view.findViewById(R.id.list_ass);

		dbHelper = new DbHelper(getActivity());
		dataListAss = new ArrayList<HashMap<String, String>>();

		// get today values
		// Calculate date for NOW
		Calendar nowAss = Calendar.getInstance();
		yearAss = nowAss.get(Calendar.YEAR);
		monthAss = nowAss.get(Calendar.MONTH) + 1; // Note: zero based!
		dayAss = nowAss.get(Calendar.DAY_OF_MONTH);
		
		semester_id_ass=dbHelper.getSemesterByDate(Integer.toString(yearAss), Integer.toString(monthAss), Integer.toString(dayAss));

		fillArrayListAssByPriority();
		// Getting adapter by passing data ArrayList
		adapterAss = new LazyAdapterAssignments(getActivity(), dataListAss);
		listAss.setAdapter(adapterAss);

		return view;
	}

	public void fillArrayListAssByPriority() {
		// priority can be high, medium, low
		Cursor cursor = null; 
		DbHelper dbHelper = new DbHelper(getActivity());
		cursor = dbHelper.getAssByPriority();
		System.out.println("Cursor im toliko vrstic: " + cursor.getCount());
		while (cursor.moveToNext()) {

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(ASS_ID, cursor.getString(0));
			map.put(ASS_NAME, cursor.getString(2));
			map.put(ASS_DUE_DATE, cursor.getString(3));
			map.put(ASS_PRIORITY, cursor.getString(4));
			map.put(ASS_COMPLETED, cursor.getString(5));
			map.put(ASS_DESCRIPTION, cursor.getString(6));
			System.out.println("Priority of " + cursor.getString(2) + " is " + cursor.getString(4));

			// adding HashList to ArrayList
			dataListAss.add(map);
		}
	}

	public void fillArrayListAssByDate() {
		// priority can be high, medium, low
		Cursor cursor = null;
		DbHelper dbHelper = new DbHelper(getActivity());
		cursor = dbHelper.getAssByDate();
		System.out.println("Cursor im toliko vrstic: " + cursor.getCount());
		while (cursor.moveToNext()) {

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(ASS_ID, cursor.getString(0));
			map.put(ASS_NAME, cursor.getString(2));
			System.out.println("imena: " + cursor.getString(2));
			map.put(ASS_DUE_DATE, cursor.getString(3));
			System.out.println("due_date: " + cursor.getString(3));
			map.put(ASS_PRIORITY, cursor.getString(4));
			System.out.println("Priority of " + cursor.getString(2) + " is " + cursor.getString(4));
			map.put(ASS_COMPLETED, cursor.getString(5));
			map.put(ASS_DESCRIPTION, cursor.getString(6));
			System.out.println("String 0 " + cursor.getString(0));
			System.out.println("String 1 " + cursor.getString(1));
			System.out.println("String 2 " + cursor.getString(2));
			System.out.println("String 3 " + cursor.getString(3));
			System.out.println("String 4 " + cursor.getString(4));
			System.out.println("String 5 " + cursor.getString(5));
			System.out.println("String 6 " + cursor.getString(6));

			// adding HashList to ArrayList
			dataListAss.add(map);
		}
	}

	public void fillArrayListAssByCourse(){
		Cursor cursor = null;
		cursor = dbHelper.getCoursesBySemesterId(semester_id_ass);

		while (cursor.moveToNext()) {

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put("course_id", cursor.getString(0));
			map.put("course_name", cursor.getString(1));

			dataListAss.add(map);
		}
	}
}
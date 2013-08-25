package my.aStudiez;

import java.util.ArrayList;
import java.util.HashMap;

import my.aStudiez.adapters.LazyAdapterCourseName;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AStudiezActivityCourseName extends Activity {
	
	//HashMap ids
	public static final String ASS_ID = "ass_id";
	public static final String ASS_NAME = "ass_name";
	public static final String ASS_DUE_DATE = "ass_due_date";
	public static final String ASS_PRIORITY = "ass_priority";
	public static final String ASS_COMPLETED = "ass_completed";
	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;
	public static final int ADD_ASS = 5;
	ListView list;
	TextView textCourseName;
	LazyAdapterCourseName adapter;
	ArrayList<HashMap<String, String>> dataList;
	
	
	Button btn_today;
	Button btn_assign;
	Button btn_planner;
	Button btn_back;
	Button btt_add_assign;
	
	//intent variables
	int course_id;
	String course_name;
	int semester_id;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.course_assignments);
		
		//get values from AStudiezTodayActivity
		Intent intent = getIntent();
		course_id = Integer.parseInt(intent.getStringExtra("course_id"));
		course_name = intent.getStringExtra("course_name");
		semester_id = intent.getIntExtra("semester_id", 1);

		dataList = new ArrayList<HashMap<String, String>>();
		list = (ListView) findViewById(R.id.list_ass);
		textCourseName = (TextView) findViewById(R.id.textView_course_name);
		textCourseName.setText(course_name);
		
		btn_assign = (Button) findViewById(R.id.button_assignments);
		btn_back = (Button) findViewById(R.id.button_back);
		btn_today = (Button) findViewById(R.id.button_today);
		btn_planner = (Button) findViewById(R.id.button_planner);
		btt_add_assign = (Button) findViewById(R.id.button_add_ass);
		
		//call method to fill array list
		fillArrayList(course_id);
		
		// Getting adapter by passing data ArrayList
		adapter = new LazyAdapterCourseName(this, dataList);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					
				HashMap<String, String> dataMap = new HashMap<String, String>();
				dataMap = dataList.get(position);
				Intent returnIntent = new Intent(AStudiezActivityCourseName.this, AStudiezActivityAssignmentDetails.class);
				
				returnIntent.putExtra(ASS_ID, Integer.parseInt(dataMap.get(ASS_ID)));
				returnIntent.putExtra(ASS_NAME, dataMap.get(ASS_NAME));
				returnIntent.putExtra(ASS_DUE_DATE, dataMap.get(ASS_DUE_DATE));
				returnIntent.putExtra(ASS_PRIORITY, dataMap.get(ASS_PRIORITY));
				returnIntent.putExtra(ASS_COMPLETED, dataMap.get(ASS_COMPLETED));
				
				returnIntent.putExtra("course_name", course_name);
				returnIntent.putExtra("semester_id", semester_id);
				
				startActivityForResult(returnIntent,0);
			}
		});
		
		btn_assign.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
			    setResult(ASSIGNMENTS, returnIntent);        
			    finish();
			}
		});
		btn_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		btn_today.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
			    setResult(TODAY, returnIntent);
				finish();
			}
		});
		btn_planner.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
			    setResult(PLANNER, returnIntent);
				finish();
			}
		});
		btt_add_assign.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent(AStudiezActivityCourseName.this, AStudiezActivityAddAssignment.class);
				returnIntent.putExtra("from", "AStudiezActivityCourseName");
				returnIntent.putExtra("semester_id", semester_id);
				returnIntent.putExtra("course_name", course_name);
				startActivityForResult(returnIntent,0);
			}
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		 super.onActivityResult(requestCode, resultCode, data);
		 switch(requestCode) {
	    	case 0: 
	          if (resultCode == TODAY) {
	        	  Intent returnIntent = new Intent();
				    setResult(TODAY, returnIntent);
					finish();
	              break;
	          }
	          if (resultCode == ASSIGNMENTS) {
	        	  Intent returnIntent = new Intent();
				    setResult(ASSIGNMENTS, returnIntent);
					finish();
	              break;
	          }
	          if (resultCode == PLANNER) {
	        	  Intent returnIntent = new Intent();
				    setResult(PLANNER, returnIntent);
					finish();
	              break;
	          }
	          if (resultCode == ADD_ASS) {
					Intent returnIntent = new Intent(AStudiezActivityCourseName.this, AStudiezActivityCourseName.class);
					returnIntent.putExtra("semester_id", semester_id);
					returnIntent.putExtra("course_name", course_name);
					returnIntent.putExtra("course_id", Integer.toString(course_id));
					startActivity(returnIntent);
					this.finish();
					break;
				}
		 	}
	}
	
	/**
	 * Gets course_id and returns
	 * */
	public void fillArrayList(int c_id){
		
		Cursor cursor = null;
		DbHelper dbHelper = new DbHelper(this);
		cursor = dbHelper.getAssByCourseId(c_id);
		
		while(cursor.moveToNext()){
			
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(ASS_ID, cursor.getString(0));
			map.put(ASS_NAME, cursor.getString(2));
			map.put(ASS_DUE_DATE, cursor.getString(3));
			map.put(ASS_PRIORITY, cursor.getString(4));
			map.put(ASS_COMPLETED, cursor.getString(5));
			
			// adding HashList to ArrayList
			dataList.add(map);
		}
	}
}
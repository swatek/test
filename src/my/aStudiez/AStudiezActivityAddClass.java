package my.aStudiez;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AStudiezActivityAddClass extends Activity {
	
	DbHelper dbHelper;
	final Context context = this;
	int semester_id;
	static final int SAVED = 1;
	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;
	
	//initialize Strings needed for insertion
	int from_day; //done
	int from_month; //done
	int from_year; //done
	int to_day; //done
	int to_month; //done
	int to_year; //done
	
	//Read: from_hour[x] = HH ->on day of week x class starts at HH hours
	int[] from_hour = {0,0,0,0,0};
	int[] from_minute = {0,0,0,0,0};
	int[] to_hour = {0,0,0,0,0};
	int[] to_minute = {0,0,0,0,0};
	
	String type; //done
	String course; //done
	String location;
	String instructor;
	
	//initialize buttons
	Button btn_cancel, btn_save, btn_today, btn_assign, btn_planner;
	Button btn_from_date; //done
	Button btn_to_date; 
	
	//initialize spinners
	Spinner spinner_type; //done
	Spinner spinner_course; //done
	
	//initialize chechBoxes
	CheckBox checkBox_MO;
	CheckBox checkBox_TU;
	CheckBox checkBox_WE;
	CheckBox checkBox_TH;
	CheckBox checkBox_FR;
	
	//initialize EditTexts
	EditText editText_location, editText_instructor;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.add_class);
		
		//Buttons
		btn_cancel = (Button) findViewById(R.id.button_cancel);
		btn_save = (Button) findViewById(R.id.button_save);
		btn_assign = (Button) findViewById(R.id.button_assignments);
		btn_today = (Button) findViewById(R.id.button_today);
		btn_planner = (Button) findViewById(R.id.button_planner);
		btn_from_date = (Button) findViewById(R.id.btn_from_date);
		btn_to_date = (Button) findViewById(R.id.btn_to_date);
		
		//EditTexts
		editText_instructor = (EditText)findViewById(R.id.editText_instructor);
		editText_location = (EditText)findViewById(R.id.editText_location);
		
		//Spinners
		spinner_type = (Spinner) findViewById(R.id.spinner_type);
		spinner_course = (Spinner) findViewById(R.id.spinner_course);
		
		//CheckBoxes
		checkBox_MO = (CheckBox) findViewById(R.id.checkBox_MO);
		checkBox_TU = (CheckBox) findViewById(R.id.checkBox_TU);
		checkBox_WE = (CheckBox) findViewById(R.id.checkBox_WE);
		checkBox_TH = (CheckBox) findViewById(R.id.checkBox_TH);
		checkBox_FR = (CheckBox) findViewById(R.id.checkBox_FR);

		//set listener
		spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    		type = parent.getItemAtPosition(pos).toString();
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		
		//set items for spinner_course
		addItemsOnSpinnerCourse();
		
		//set listener for spinner_course
		spinner_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    		course = parent.getItemAtPosition(pos).toString();
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				// TODO
				finish();
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					calculateDates();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent returnIntent = new Intent();
			    setResult(SAVED, returnIntent);        
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
		
		btn_assign.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(ASSIGNMENTS, returnIntent);
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
		
		// add button listener
		btn_from_date.setOnClickListener(new View.OnClickListener() {
 
		  public void onClick(View arg0) {
 
			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog_date);
			dialog.setTitle("Set date");
 
			// set the custom dialog components
			
			Button dialog_confirm = (Button) dialog.findViewById(R.id.button_confirm_date);
			Button dialog_cancel = (Button) dialog.findViewById(R.id.button_cancel_date);
			final DatePicker date_picker = (DatePicker) dialog.findViewById(R.id.datePicker1);
			
			//get today values
			//Calculate date for NOW
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH)+1; // Note: zero based!
			int day = now.get(Calendar.DAY_OF_MONTH);
			
			int semID = dbHelper.getSemesterByDate(Integer.toString(year), Integer.toString(month), Integer.toString(day));
			Cursor cursor = dbHelper.getSemesters();
			for(int i = 0; i < semID; i++) cursor.moveToNext();
			String fromDateString = cursor.getString(2);
			String[] fromDateArray = fromDateString.split("-");
			date_picker.updateDate(new Integer(fromDateArray[0]), new Integer(fromDateArray[1]) - 1, new Integer(fromDateArray[2]));
			
			
			// if button is clicked, close the custom dialog
			dialog_confirm.setOnClickListener(new View.OnClickListener() {
		
				public void onClick(View v) {
					from_month = date_picker.getMonth()+1;
					from_day = date_picker.getDayOfMonth();
					from_year = date_picker.getYear();
					btn_from_date.setBackgroundColor(0xff666666);
					dialog.dismiss();
				}
			});
			
			dialog_cancel.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
 
			dialog.show();
		  }
		});
		
		// add button listener
		btn_to_date.setOnClickListener(new View.OnClickListener() {
 
		  public void onClick(View arg0) {
 
			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog_date);
			dialog.setTitle("Set date");
 
			// set the custom dialog components
			
			Button dialog_confirm = (Button) dialog.findViewById(R.id.button_confirm_date);
			Button dialog_cancel = (Button) dialog.findViewById(R.id.button_cancel_date);
			final DatePicker date_picker = (DatePicker) dialog.findViewById(R.id.datePicker1);
			
			//get today values
			//Calculate date for NOW
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH)+1; // Note: zero based!
			int day = now.get(Calendar.DAY_OF_MONTH);
			
			int semID = dbHelper.getSemesterByDate(Integer.toString(year), Integer.toString(month), Integer.toString(day));
			Cursor cursor = dbHelper.getSemesters();
			for(int i = 0; i < semID; i++) cursor.moveToNext();
			String toDateString = cursor.getString(3);
			String[] toDateArray = toDateString.split("-");
			date_picker.updateDate(new Integer(toDateArray[0]), new Integer(toDateArray[1]) - 1, new Integer(toDateArray[2]));
			
			// if button is clicked, close the custom dialog
			dialog_confirm.setOnClickListener(new View.OnClickListener() {
		
				public void onClick(View v) {
					to_month = date_picker.getMonth()+1;
					to_day = date_picker.getDayOfMonth();
					to_year = date_picker.getYear();
					btn_to_date.setBackgroundColor(0xff666666);
					dialog.dismiss();
				}
			});
			
			dialog_cancel.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
 
			dialog.show();
		  }
		});
		
		checkBox_MO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	// custom dialog
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_time);
					dialog.setTitle("Set time");
		 
					// set the custom dialog components
					
					Button dialog_confirm = (Button) dialog.findViewById(R.id.button_confirm_time);
					Button dialog_cancel = (Button) dialog.findViewById(R.id.button_cancel_time);
					final TimePicker time_picker_from = (TimePicker) dialog.findViewById(R.id.timePicker1);
					final TimePicker time_picker_to = (TimePicker) dialog.findViewById(R.id.timePicker2);
					
					// if button is clicked, close the custom dialog
					dialog_confirm.setOnClickListener(new View.OnClickListener() {
				
						public void onClick(View v) {
							
							from_hour[0] = time_picker_from.getCurrentHour()+1;
							from_minute[0] = time_picker_from.getCurrentMinute()+1;
							to_hour[0] = time_picker_to.getCurrentHour()+1;
							to_minute[0] = time_picker_to.getCurrentMinute()+1;
							
							dialog.dismiss();
						}
					});
					
					dialog_cancel.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							onUncheckBox(checkBox_MO, 0);
							dialog.dismiss();
						}
					});
		 
					dialog.show();
		        }
		        
		        else onUncheckBox(checkBox_MO, 0);

		    }
		});
		
		checkBox_TU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	// custom dialog
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_time);
					dialog.setTitle("Set time");
		 
					// set the custom dialog components
					
					Button dialog_confirm = (Button) dialog.findViewById(R.id.button_confirm_time);
					Button dialog_cancel = (Button) dialog.findViewById(R.id.button_cancel_time);
					final TimePicker time_picker_from = (TimePicker) dialog.findViewById(R.id.timePicker1);
					final TimePicker time_picker_to = (TimePicker) dialog.findViewById(R.id.timePicker2);

					// if button is clicked, close the custom dialog
					dialog_confirm.setOnClickListener(new View.OnClickListener() {
				
						public void onClick(View v) {
							
							from_hour[1] = time_picker_from.getCurrentHour()+1;
							from_minute[1] = time_picker_from.getCurrentMinute()+1;
							to_hour[1] = time_picker_to.getCurrentHour()+1;
							to_minute[1] = time_picker_to.getCurrentMinute()+1;
							
							dialog.dismiss();
						}
					});
					
					dialog_cancel.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							onUncheckBox(checkBox_TU, 1);
							dialog.dismiss();
						}
					});
					
					dialog.show();
		        }
		        else onUncheckBox(checkBox_TU, 1);
		    }
		});
		
		checkBox_WE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	// custom dialog
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_time);
					dialog.setTitle("Set time");
		 
					// set the custom dialog components
					
					Button dialog_confirm = (Button) dialog.findViewById(R.id.button_confirm_time);
					Button dialog_cancel = (Button) dialog.findViewById(R.id.button_cancel_time);
					final TimePicker time_picker_from = (TimePicker) dialog.findViewById(R.id.timePicker1);
					final TimePicker time_picker_to = (TimePicker) dialog.findViewById(R.id.timePicker2);
					// if button is clicked, close the custom dialog
					dialog_confirm.setOnClickListener(new View.OnClickListener() {
				
						public void onClick(View v) {
							
							from_hour[2] = time_picker_from.getCurrentHour()+1;
							from_minute[2] = time_picker_from.getCurrentMinute()+1;
							to_hour[2] = time_picker_to.getCurrentHour()+1;
							to_minute[2] = time_picker_to.getCurrentMinute()+1;
							
							dialog.dismiss();
						}
					});
					
					dialog_cancel.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							onUncheckBox(checkBox_WE, 2);
							dialog.dismiss();
						}
					});
		 
					dialog.show();
		        }
		        else onUncheckBox(checkBox_WE, 2);
		    }
		});
		
		checkBox_TH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	// custom dialog
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_time);
					dialog.setTitle("Set time");
		 
					// set the custom dialog components
					
					Button dialog_confirm = (Button) dialog.findViewById(R.id.button_confirm_time);
					Button dialog_cancel = (Button) dialog.findViewById(R.id.button_cancel_time);
					final TimePicker time_picker_from = (TimePicker) dialog.findViewById(R.id.timePicker1);
					final TimePicker time_picker_to = (TimePicker) dialog.findViewById(R.id.timePicker2);
					
					
					
					// if button is clicked, close the custom dialog
					dialog_confirm.setOnClickListener(new View.OnClickListener() {
				
						public void onClick(View v) {
							
							from_hour[3] = time_picker_from.getCurrentHour()+1;
							from_minute[3] = time_picker_from.getCurrentMinute()+1;
							to_hour[3] = time_picker_to.getCurrentHour()+1;
							to_minute[3] = time_picker_to.getCurrentMinute()+1;
							
							dialog.dismiss();
						}
					});
					
					dialog_cancel.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							onUncheckBox(checkBox_TH, 3);
							dialog.dismiss();
						}
					});
		 
					dialog.show();
		        }
		        else onUncheckBox(checkBox_TH, 3);
		    }
		});
		
		checkBox_FR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	// custom dialog
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_time);
					dialog.setTitle("Set time");
		 
					// set the custom dialog components
					
					Button dialog_confirm = (Button) dialog.findViewById(R.id.button_confirm_time);
					Button dialog_cancel = (Button) dialog.findViewById(R.id.button_cancel_time);
					final TimePicker time_picker_from = (TimePicker) dialog.findViewById(R.id.timePicker1);
					final TimePicker time_picker_to = (TimePicker) dialog.findViewById(R.id.timePicker2);

					// if button is clicked, close the custom dialog
					dialog_confirm.setOnClickListener(new View.OnClickListener() {
				
						public void onClick(View v) {
							
							from_hour[4] = time_picker_from.getCurrentHour()+1;
							from_minute[4] = time_picker_from.getCurrentMinute()+1;
							to_hour[4] = time_picker_to.getCurrentHour()+1;
							to_minute[4] = time_picker_to.getCurrentMinute()+1;
							
							dialog.dismiss();
						}
					});
					
					dialog_cancel.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							onUncheckBox(checkBox_FR, 4);
							dialog.dismiss();
						}
					});
		 
					dialog.show();
		        }
		        else onUncheckBox(checkBox_FR, 4);
		    }
		});
	}
	
	public void onUncheckBox(CheckBox c, int i){
		c.setChecked(false);
		from_hour[i] = 0;
		from_minute[i] = 0;
		to_hour[i] = 0;
		to_minute[i] = 0;
	}
	
	
	/**
	 * Method executes the algorithm which calculates parameters for insertion
	 * @throws ParseException 
	 * */
	public void calculateDates() throws ParseException{

		Calendar from_c = Calendar.getInstance();
		from_c.set(from_year, from_month-1, from_day);
		System.out.println("Dan: "+from_day+"Month: "+from_month+"Year: "+from_year);
		
		Calendar to_c = Calendar.getInstance();
		to_c.set(to_year, to_month-1, to_day);
		
		Date from_dt = from_c.getTime();
		Date to_dt = to_c.getTime();
		
		//monday is 2
		int day_of_week = from_c.get(Calendar.DAY_OF_WEEK);
		int day_of_week_2 = 0;
		int diff = 0;

		String from_time;
		String to_time;
		
		int course_id = dbHelper.getCoursesId(semester_id, course);
		
		for(int i = 0; i<from_hour.length; i++){

			if(from_hour[i]!=0){
				System.out.println("From_hour: "+from_hour[i]);
				
				day_of_week_2 = reSet(day_of_week);
				
				if(day_of_week_2>=0){
					diff = i - day_of_week_2;
				}
				else{
					if(day_of_week_2 ==-1)
						diff=i+1;
					else
						diff=i+2; 
				}
				from_c.add(Calendar.DATE, diff);  // number of days to add
				System.out.println("diff: "+diff);
				
				System.out.println(from_dt.compareTo(to_dt)<0);
				
				while(from_dt.compareTo(to_dt)<0)
				{
				  from_time=from_hour[i]+":"+from_minute[i];
				  to_time=to_hour[i]+":"+to_minute[i];
				  dbHelper.insertClass(3, course_id , type, Integer.toString(from_c.get(Calendar.YEAR)), Integer.toString(from_c.get(Calendar.MONTH)+1), Integer.toString(from_c.get(Calendar.DAY_OF_MONTH)), Integer.toString(from_year),Integer.toString(from_month), Integer.toString(from_day), Integer.toString(to_year), Integer.toString(to_month), Integer.toString(to_day), from_time, to_time, editText_location.getText().toString(), 0);
				  from_c.add(Calendar.DATE, 7);
				  from_dt = from_c.getTime();
				}
				from_c.set(from_year, from_month-1, from_day);
				from_dt = from_c.getTime();
			}
		}
	}
	
	/**
	 * Methods looks into database and finds every course, then fills the spinner with
	 * names of courses
	 * */
	public void addItemsOnSpinnerCourse(){
		
		dbHelper = new DbHelper(this);
		Cursor cursor = null;
		
		ArrayList<String> list = new ArrayList<String>();
		//get today values
		//Calculate date for NOW
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH)+1; // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);
		
		semester_id = dbHelper.getSemesterByDate(Integer.toString(year), Integer.toString(month), Integer.toString(day));
		cursor = dbHelper.getCoursesBySemesterId(semester_id);
		System.out.println("semester: "+semester_id);
		
		while(cursor.moveToNext()){
			list.add(cursor.getString(1));
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_course.setAdapter(dataAdapter);
		
	}
	
	public int reSet(int d){
		//MONDAY = 2
		if(d==2){
			d = 0;
		}
		else if(d==3){
			d = 1;
		}
		else if(d==4){
			d = 2;
		}//thursday
		else if(d==5){
			d = 3;
		}
		else if(d==6){
			d = 4;
		}
		else if(d==7){
			d = -2;
		}
		else if(d==1){
			d = -1;
		}
		return d;
	}
	
}
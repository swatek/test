package my.aStudiez;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AStudiezActivityAddAssignment extends Activity {
	Button btn_cancel;
	Button btn_save;
	Button btn_today;
	Button btn_assign;
	Button btn_planner;
	Button btn_due_date;
	

	// variables for insertion
	EditText editText_title;
	EditText editText_description;
	Spinner spinner_course, spinner_priority;
	TextView textview_description;
	int day;
	int month;
	int year;

	DbHelper dbHelper;
	final Context context = this;

	// intent variables
	int semester_id;
	String course_name;
	boolean control_edit;
	String title;
	String due_date;
	String priority;
	String completed;
	int ass_id;
	// /
	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;
	public static final int ADD_ASS = 5;
	public static final int EDIT_ASS = 6;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.add_assignment);

		// get intents
		Intent intent = getIntent();

		// if the previous activity was AstudiezActivityDetails then we set
		// control_edit on true. Otherwise false
		// depends where it comes from :)
		semester_id = intent.getIntExtra("semester_id", 0);

		if (intent.getStringExtra("from").equals("AStudiezActivityCourseName")) {
			course_name = intent.getStringExtra("course_name");
			control_edit = false;
		} else if (intent.getStringExtra("from").equals(
				"AStudiezFragmentAssignments")) {
			course_name = null;
			control_edit = false;
		} else {
			ass_id = intent.getIntExtra("ass_id", 0);
			course_name = intent.getStringExtra("course_name");
			due_date = intent.getStringExtra("ass_due_date");
			// parse date
			year = Integer.parseInt(due_date.substring(0, 4));
			month = Integer.parseInt(due_date.substring(5, 7));
			day = Integer.parseInt(due_date.substring(8));

			priority = intent.getStringExtra("ass_priority");
			completed = intent.getStringExtra("ass_completed");
			title = intent.getStringExtra("ass_name");
			control_edit = true;
		}

		btn_cancel = (Button) findViewById(R.id.button_cancel);
		btn_save = (Button) findViewById(R.id.button_save);
		btn_assign = (Button) findViewById(R.id.button_assignments);
		btn_today = (Button) findViewById(R.id.button_today);
		btn_planner = (Button) findViewById(R.id.button_planner);
		btn_due_date = (Button) findViewById(R.id.button_due_date);

		dbHelper = new DbHelper(this);

		editText_title = (EditText) findViewById(R.id.editText_title);
		editText_description = (EditText) findViewById(R.id.editText_description);

		spinner_course = (Spinner) findViewById(R.id.spinner_course);
		spinner_priority = (Spinner) findViewById(R.id.spinner_priority);

		if (control_edit) {
			editText_title.setText(title);
		}

		addItemsOnSpinnerCourse();

		btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				// TODO
				finish();
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				if (control_edit) {
					dbHelper.editAssignment(ass_id, dbHelper.getCoursesId(
							semester_id, spinner_course.getSelectedItem()
									.toString()), editText_title.getText()
							.toString(), Integer.toString(year), Integer
							.toString(month), Integer.toString(day),
							spinner_priority.getSelectedItem().toString(), "false",editText_description.getText()
							.toString());
					Intent returnIntent = new Intent();
					returnIntent.putExtra("ass_id", ass_id);
					returnIntent.putExtra("ass_name", editText_title.getText()
							.toString());
					returnIntent.putExtra("ass_due_date",year+"-"+month+"-"+day);
					returnIntent.putExtra("ass_priority", spinner_priority.getSelectedItem().toString());
					returnIntent.putExtra("ass_completed", "ni");
					returnIntent.putExtra("course_name", course_name);
					setResult(EDIT_ASS, returnIntent);
					finish();
				} else {
					dbHelper.insertAssignment(dbHelper.getCoursesId(
							semester_id, spinner_course.getSelectedItem()
									.toString()), editText_title.getText()
							.toString(), Integer.toString(year), Integer
							.toString(month), Integer.toString(day),
							spinner_priority.getSelectedItem().toString(), "false", 
							editText_description.getText().toString());
					Intent returnIntent = new Intent();
					setResult(ADD_ASS, returnIntent);
					finish();
				}
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
		btn_due_date.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				// custom dialog
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.dialog_date);
				dialog.setTitle("Set date");

				// set the custom dialog components

				Button dialog_confirm = (Button) dialog
						.findViewById(R.id.button_confirm_date);
				Button dialog_cancel = (Button) dialog
						.findViewById(R.id.button_cancel_date);
				final DatePicker date_picker = (DatePicker) dialog
						.findViewById(R.id.datePicker1);

				// we check if activity before that was assignment details
				// activity, then we set date picker
				if (control_edit) {
					date_picker.init(
							Integer.parseInt(due_date.substring(0, 4)),
							Integer.parseInt(due_date.substring(5, 7)),
							Integer.parseInt(due_date.substring(8)), null);
				}

				// if button is clicked, close the custom dialog
				dialog_confirm.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						month = date_picker.getMonth() + 1;
						day = date_picker.getDayOfMonth();
						year = date_picker.getYear();
						btn_due_date.setBackgroundColor(0xff666666);
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

	}

	/**
	 * Methods looks into database and finds every course, then fills the
	 * spinner with names of courses
	 * */
	public void addItemsOnSpinnerCourse() {

		DbHelper dbHelper = new DbHelper(this);
		Cursor cursor = null;

		ArrayList<String> list = new ArrayList<String>();
		cursor = dbHelper.getCoursesBySemesterId(semester_id);

		while (cursor.moveToNext()) {
			list.add(cursor.getString(1));
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_course.setAdapter(dataAdapter);

		// sets the value selected on course that was on previous activity
		/*
		 * if(course_name!= null){ int spinnerPosition =
		 * dataAdapter.getPosition(course_name);
		 * 
		 * //set the default according to value
		 * spinner_course.setSelection(spinnerPosition); }
		 */

	}
}
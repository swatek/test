package my.aStudiez;

import java.util.ArrayList;
import java.util.HashMap;

import my.aStudiez.adapters.LazyAdapterCourses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AStudiezActivityCourses extends Activity {

	public final static String COURSE_ID = "course_id";
	public final static String COURSE_NAME = "course_name";
	public static final int SAVED = 1;
	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;
	ArrayList<HashMap<String, String>> dataList;

	ListView list;
	LazyAdapterCourses adapter;
	Button btn_today;
	Button btn_add;
	Button btn_ass;
	Button btn_planner;
	TextView semester_name;
	DbHelper dbHelper;

	final Context context = this;

	// intent variables
	int semester_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.courses_add);

		dataList = new ArrayList<HashMap<String, String>>();
		semester_name = (TextView) findViewById(R.id.textView_semester_name);
		list = (ListView) findViewById(R.id.list_course_add);
		btn_add = (Button) findViewById(R.id.button_add_course);
		btn_ass = (Button) findViewById(R.id.button_assignments);
		btn_today = (Button) findViewById(R.id.button_today);
		btn_planner = (Button)findViewById(R.id.button_planner);
		dbHelper = new DbHelper(this);

		Intent intent = getIntent();
		semester_id = intent.getIntExtra("semester_id", 0);
		semester_name.setText(intent.getStringExtra("semester_name"));

		fillArrayList();

		// Getting adapter by passing data ArrayList
		adapter = new LazyAdapterCourses(this, dataList);
		list.setAdapter(adapter);

		btn_today.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(TODAY, returnIntent);
				finish();
			}
		});
		btn_ass.setOnClickListener(new View.OnClickListener() {
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

		btn_add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				// custom dialog
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.dialog_add_course);

				// set the custom dialog components

				Button dialog_confirm = (Button) dialog
						.findViewById(R.id.button_confirm_name);
				Button dialog_cancel = (Button) dialog
						.findViewById(R.id.button_cancel_name);
				final EditText name = (EditText) dialog
						.findViewById(R.id.editText_course_name);

				// if button is clicked, close the custom dialog
				dialog_confirm.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						dbHelper.insertCourse(name.getText().toString(),
								semester_id);
						dialog.dismiss();
						dataList.clear();
						fillArrayList();
						adapter.notifyDataSetChanged();
						//startActivity(getIntent());
						//finish();
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
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

		}
	}

	public void fillArrayList() {

		Cursor cursor = null;
		cursor = dbHelper.getCoursesBySemesterId(semester_id);

		while (cursor.moveToNext()) {

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(COURSE_ID, cursor.getString(0));
			map.put(COURSE_NAME, cursor.getString(1));

			dataList.add(map);
		}
	}
}
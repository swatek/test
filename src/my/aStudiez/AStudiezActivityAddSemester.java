package my.aStudiez;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AStudiezActivityAddSemester extends Activity {

	// variables for insertion
	EditText editText_name;
	Button btn_save;
	Button btn_cancel, btn_today, btn_assign, btn_planner;
	public static final int SAVED = 1;
	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;

	int from_day;
	int from_month;
	int from_year;
	int to_day;
	int to_month;
	int to_year;

	DbHelper dbHelper;
	final Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.add_semester);

		dbHelper = new DbHelper(this);

		editText_name = (EditText) findViewById(R.id.editText_name);
		final DatePicker date_picker_from_date = (DatePicker) findViewById(R.id.datePicker_from_date);
		final DatePicker date_picker_to_date = (DatePicker) findViewById(R.id.datePicker_to_date);
		btn_save = (Button) findViewById(R.id.button_save);
		btn_cancel = (Button) findViewById(R.id.button_cancel);
		btn_assign = (Button) findViewById(R.id.button_assignments);
		btn_today = (Button) findViewById(R.id.button_today);
		btn_planner = (Button) findViewById(R.id.button_planner);


		btn_save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				from_month = date_picker_from_date.getMonth() + 1;
				from_day = date_picker_from_date.getDayOfMonth();
				from_year = date_picker_from_date.getYear();

				to_month = date_picker_to_date.getMonth() + 1;
				to_day = date_picker_to_date.getDayOfMonth();
				to_year = date_picker_to_date.getYear();

				dbHelper.insertSemester(editText_name.getText().toString(),
						Integer.toString(from_year),
						Integer.toString(from_month),
						Integer.toString(from_day), Integer.toString(to_year),
						Integer.toString(to_month), Integer.toString(to_day));
				Intent returnIntent = new Intent();
				setResult(SAVED, returnIntent);
				finish();
			}
		});
		
		btn_cancel.setOnClickListener(new View.OnClickListener() {
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
	}
}
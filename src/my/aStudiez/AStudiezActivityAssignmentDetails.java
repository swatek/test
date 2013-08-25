package my.aStudiez;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AStudiezActivityAssignmentDetails extends Activity {
	/** Called when the activity is first created. */
	
	public static final int EDIT_ASS = 6;
	
	Button btn_back;
	Button btn_edit;
	
	TextView textViewDueDate;
	TextView textViewCourseName;
	TextView textViewDescription;
	TextView textViewAssDescription;
	TextView textViewAssName;
	
	String course_name;
	String due_date;
	String description;
	String name;
	String priority;
	String completed;
	ImageView imagePriority;
	int ass_id;
	int semester_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		//get intent values
		Intent intent = getIntent();
		
		ass_id = intent.getIntExtra(AStudiezActivityCourseName.ASS_ID,0);
		name = intent.getStringExtra(AStudiezActivityCourseName.ASS_NAME);
		due_date = intent.getStringExtra(AStudiezActivityCourseName.ASS_DUE_DATE);
		course_name = intent.getStringExtra("course_name");
		priority = intent.getStringExtra(AStudiezActivityCourseName.ASS_PRIORITY);
		completed = intent.getStringExtra(AStudiezActivityCourseName.ASS_COMPLETED);
		semester_id = intent.getIntExtra("semester_id", 0);
		description = intent.getStringExtra("ass_desctiption");
		
		setContentView(R.layout.assignment_details);
		
		imagePriority = (ImageView) findViewById(R.id.image_priority_ass);

			if(priority.equals("3"))
				imagePriority.setImageResource(R.drawable.greenpoint);
			else if(priority.equals("2"))
				imagePriority.setImageResource(R.drawable.yellowpoint);
			else
				imagePriority.setImageResource(R.drawable.redpoint);
		
		
		
		
		textViewDueDate = (TextView) findViewById(R.id.text_due_date);
		textViewCourseName = (TextView) findViewById(R.id.text_course_name);
		textViewDescription = (TextView) findViewById(R.id.text_ass_description);
		textViewAssName = (TextView) findViewById(R.id.textView_name_of_ass);
		
		btn_back = (Button) findViewById(R.id.button_back);
		btn_edit = (Button) findViewById(R.id.button_edit);	
		
		textViewDueDate.setText(due_date);
		textViewCourseName.setText(course_name);
		textViewDescription.setText(description);
		textViewAssName.setText(name);
		
		
		btn_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				finish();
			}
		});
		
		btn_edit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent(AStudiezActivityAssignmentDetails.this, AStudiezActivityAddAssignment.class);
				returnIntent.putExtra("from", "AStudiezActivityAssignmentDetails");
				returnIntent.putExtra("semester_id", semester_id);
				returnIntent.putExtra("ass_id", ass_id);
				returnIntent.putExtra("ass_name", name);
				returnIntent.putExtra("ass_due_date", due_date);
				returnIntent.putExtra("ass_priority", priority);
				returnIntent.putExtra("ass_completed", completed);
				returnIntent.putExtra("course_name", course_name);
				startActivityForResult(returnIntent,0);
			}
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		 super.onActivityResult(requestCode, resultCode, data);
		 switch(requestCode) {
	    	case 0: 
	          if (resultCode == EDIT_ASS) {
					Intent returnIntent = new Intent(AStudiezActivityAssignmentDetails.this, AStudiezActivityAssignmentDetails.class);
					
					//override values because they might be new ones
					ass_id = data.getIntExtra(AStudiezActivityCourseName.ASS_ID,0);
					name = data.getStringExtra(AStudiezActivityCourseName.ASS_NAME);
					due_date = data.getStringExtra(AStudiezActivityCourseName.ASS_DUE_DATE);
					course_name = data.getStringExtra("course_name");
					priority = data.getStringExtra(AStudiezActivityCourseName.ASS_PRIORITY);
					completed = data.getStringExtra(AStudiezActivityCourseName.ASS_COMPLETED);
					
					returnIntent.putExtra("semester_id", semester_id);
					returnIntent.putExtra("ass_id", ass_id);
					returnIntent.putExtra("ass_name", name);
					returnIntent.putExtra("ass_due_date", due_date);
					returnIntent.putExtra("ass_priority", priority);
					returnIntent.putExtra("ass_completed", completed);
					returnIntent.putExtra("course_name", course_name);
					startActivity(returnIntent);
					this.finish();
					break;
				}
		 	}
	}
	
	public void finishActivity(View v) {
	    finish();
	  }
}
package my.aStudiez.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import my.aStudiez.AStudiezFragmentAssignments;
import my.aStudiez.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapterAssignments extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public LazyAdapterAssignments(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row_course_name, null);

		TextView textNameAss = (TextView) vi.findViewById(R.id.text_name_ass);
		TextView textDueDate = (TextView) vi.findViewById(R.id.text_due_date);
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.imageView2);

		
		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap = data.get(position);

		// Setting all values in listview
		textNameAss.setText(dataMap.get(AStudiezFragmentAssignments.ASS_NAME));
		//textDueDate.setText(dataMap.get(AStudiezFragmentAssignments.ASS_DUE_DATE));
		textDueDate.setText(dataMap.get(AStudiezFragmentAssignments.ASS_DUE_DATE));
		
		if(dataMap.get(AStudiezFragmentAssignments.ASS_PRIORITY).contains("1"))
			thumb_image.setImageResource(R.drawable.redpoint);
		else if(dataMap.get(AStudiezFragmentAssignments.ASS_PRIORITY).contains("2"))
			thumb_image.setImageResource(R.drawable.yellowpoint);
		else if(dataMap.get(AStudiezFragmentAssignments.ASS_PRIORITY).contains("3"))
			thumb_image.setImageResource(R.drawable.greenpoint);
		
		//thumb_image.setImageResource(R.drawable.redpoint);
		return vi;
	}
}
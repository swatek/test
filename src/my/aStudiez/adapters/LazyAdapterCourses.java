package my.aStudiez.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import my.aStudiez.AStudiezActivityCourses;
import my.aStudiez.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LazyAdapterCourses extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public LazyAdapterCourses(Activity a, ArrayList<HashMap<String, String>> d) {
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
			vi = inflater.inflate(R.layout.list_row_course_add, null);

		TextView textCourseName = (TextView) vi.findViewById(R.id.text_course_name_add);
		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap = data.get(position);

		// Setting all values in listview
		textCourseName.setText(dataMap.get(AStudiezActivityCourses.COURSE_NAME));
		return vi;
	}
}
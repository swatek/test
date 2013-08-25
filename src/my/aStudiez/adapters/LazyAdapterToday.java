package my.aStudiez.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import my.aStudiez.*;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapterToday extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	
	public LazyAdapterToday(Activity a, ArrayList<HashMap<String, String>> d) {
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
			vi = inflater.inflate(R.layout.list_row_today, null);

		TextView textCourse = (TextView) vi.findViewById(R.id.textCourse);
		TextView textType = (TextView) vi.findViewById(R.id.textType);
		TextView textInstructor = (TextView) vi
				.findViewById(R.id.textInstructor);
		TextView textPlace = (TextView) vi.findViewById(R.id.textPlace);
		TextView textTime = (TextView) vi.findViewById(R.id.textTime);
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image);

		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap = data.get(position);

		// Setting all values in listview
		textCourse.setText(dataMap.get(AStudiezFragmentToday.COURSE_NAME));
		textType.setText(dataMap.get(AStudiezFragmentToday.CLASS_TYPE));
		textTime.setText(dataMap.get(AStudiezFragmentToday.TIME));
		thumb_image.setImageResource(R.drawable.books);
		return vi;
	}
}
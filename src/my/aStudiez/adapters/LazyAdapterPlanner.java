package my.aStudiez.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import my.aStudiez.AStudiezFragmentPlanner;
import my.aStudiez.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LazyAdapterPlanner extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public LazyAdapterPlanner(Activity a, ArrayList<HashMap<String, String>> d) {
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
			vi = inflater.inflate(R.layout.list_row_semestr, null);
		
		TextView textSemestrName = (TextView) vi.findViewById(R.id.text_semestr_name);
		TextView textStartDate = (TextView) vi.findViewById(R.id.text_start_date);
		TextView textStopDate = (TextView) vi.findViewById(R.id.text_stop_date);
		
		//ImageView thumb_image = (ImageView) vi.findViewById(R.id.imageView2);

		
		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap = data.get(position);

		// Setting all values in listview
		textSemestrName.setText(dataMap.get(AStudiezFragmentPlanner.NAME));
		textStartDate.setText(dataMap.get(AStudiezFragmentPlanner.FROM_DATE));
		textStopDate.setText(dataMap.get(AStudiezFragmentPlanner.TO_DATE));
		
		//thumb_image.setImageResource(R.drawable.redpoint);
		return vi;
	}
	
	
}
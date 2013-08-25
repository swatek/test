package my.aStudiez;

import java.util.ArrayList;
import java.util.HashMap;

import my.aStudiez.adapters.LazyAdapterPlanner;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AStudiezFragmentPlanner extends Fragment {
	public static final int SAVED = 1;
	public static final int TODAY = 2;
	public static final int ASSIGNMENTS = 3;
	public static final int PLANNER = 4;
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String FROM_DATE = "from_date";
	public static final String TO_DATE = "to_date";

	Button btn_add_semester;
	LazyAdapterPlanner adapter;
	ArrayList<HashMap<String, String>> dataListPlanner;
	ListView list;
	DbHelper dbHelper;
	MainActivity main;
	
	int semester_id_clicked;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(getActivity(),
						AStudiezActivityCourses.class);
				// Initialize HashMap
				HashMap<String, String> dataMap = new HashMap<String, String>();
				// Read the right item
				dataMap = dataListPlanner.get(position);
				semester_id_clicked = Integer.parseInt(dataMap.get(ID));
				System.out.println("regular click triggered for semester id: " + semester_id_clicked);
				i.putExtra("semester_id", Integer.parseInt(dataMap.get(ID)));
				i.putExtra("semester_name", dataMap.get(NAME));
				startActivityForResult(i, 0);
			}
		});

		btn_add_semester.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						AStudiezActivityAddSemester.class);
				startActivityForResult(intent, 0);
			}
		});

	}

	protected void start(Intent intent) {
		this.startActivityForResult(intent, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == SAVED) {
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
				break;
			}
			if (resultCode == TODAY) {
				main.mViewPager.setCurrentItem(0);
				break;
			}
			if (resultCode == ASSIGNMENTS) {
				main.mViewPager.setCurrentItem(1);
				break;
			}
			if (resultCode == PLANNER) {
				main.mViewPager.setCurrentItem(2);
				break;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.planner, container, false);
		btn_add_semester = (Button) view.findViewById(R.id.button_add_semestr);
		list = (ListView) view.findViewById(R.id.list_semestrs);
		dataListPlanner = new ArrayList<HashMap<String, String>>();
		dbHelper = new DbHelper(getActivity());

		fillArrayListPlanner();

		// Getting adapter by passing data ArrayList
		adapter = new LazyAdapterPlanner(getActivity(), dataListPlanner);
		list.setAdapter(adapter);
		registerForContextMenu(list);
		return view;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo);  
	    menu.setHeaderTitle("Context Menu");  
	    menu.add(0, v.getId(), 0, "Delete");  
	    
	    // Get the info on which item was selected
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

	    // Retrieve the item that was clicked on
	    Object item = adapter.getItem(info.position);
		}  
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {  
		if(item.getTitle()=="Delete"){
			//delete(item.getItemId());
			
		    // Here's how you can get the correct item in onContextItemSelected()
		    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		    //adapter.getItem(info.position);
			delete(Integer.parseInt(dataListPlanner.get(info.position).get(ID)));
			dataListPlanner.remove(info.position);
			//dataListPlanner.clear();
			//fillArrayListPlanner();
			adapter.notifyDataSetChanged();
		}  
	    else {return false;}  
		return true;  
	}  
	   
	public void delete(int id){  
			System.out.println("semester_id_clicked: "+semester_id_clicked);
	        //dbHelper.deleteSemesterById(semester_id_clicked);  
			dbHelper.deleteSemesterById(id); 
	}
	   
	public void fillArrayListPlanner() {

		Cursor cursor = null;

		cursor = dbHelper.getSemesters();
		while (cursor.moveToNext()) {

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(ID, cursor.getString(0));
			map.put(NAME, cursor.getString(1));
			map.put(FROM_DATE, cursor.getString(2));
			map.put(TO_DATE, cursor.getString(3));

			// adding HashList to ArrayList
			dataListPlanner.add(map);
		}
	}
}

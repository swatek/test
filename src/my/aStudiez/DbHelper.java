package my.aStudiez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbHelper extends SQLiteOpenHelper {
	//Database model
	
	//database properties
	static final String DB_NAME = "database_aStudiez";
	static final int DB_VERSION = 1;
	
	static final String TAG = "DbHelper";
	Context context;
	
	//Constructor only makes DbHelper object.
	//Here, database is still not created.
	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}
	
	// Called only once(!), first time the DB is created
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql;
		sql = "create table Teacher( _id integer primary key, first_name text, second_name text)";
		db.execSQL(sql);
		Log.i(TAG, "onCreated Teacher; sql: " + sql);
		
		sql = "create table Semester(_id integer primary key, name text, from_date timestamp, to_date timestamp)";
		db.execSQL(sql); //
		Log.i(TAG, "onCreated Semester; sql: " + sql);
		
		sql = "create table Course (_id integer primary key, FK_semester_id int, name text ,FOREIGN KEY(FK_semester_id) REFERENCES Semester(_id) ON DELETE CASCADE )";
		db.execSQL(sql); //
		Log.i(TAG, "onCreated Course; sql: " + sql);
		
		sql = "create table Class (_id integer primary key, FK_teacher_id int, FK_course_id int, type text, date timestamp, from_date timestamp, to_date timestamp, from_time timestamp, to_time timestamp, location text, repeat int, " + 
		"FOREIGN KEY(FK_teacher_id) REFERENCES Teacher(_id) ON DELETE CASCADE, FOREIGN KEY(FK_course_id) REFERENCES Course(_id) ON DELETE CASCADE)";
		db.execSQL(sql); //
		Log.i(TAG, "onCreated Class; sql: " + sql);
		
		sql = "create table Assignment(_id integer primary key, FK_course_id int, name text, due_date timestamp, priority text, completed text, description text, FOREIGN KEY(FK_course_id) REFERENCES Course(_id) ON DELETE CASCADE)";
		db.execSQL(sql); //
		Log.i(TAG, "onCreated Assignment; sql: " + sql);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		// Typically do ALTER TABLE statements, but...we're just in development,
		// so:
	}
	
	/**
	 * * Returns cursor. Rows of classes at defined date
	 * @param year - year you are searching for
	 * @param month - month you are searching for
	 * @param day - day you are searching for
	 * @return
	 */
	public Cursor getDayClass(String year, String month, String day){
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		//day has to be 09 not 9 for example. same for month
		if(day.length()==1)
			day = "0"+day;
		if(month.length()==1)
			month="0"+month;
			
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT  Class._id, Class.FK_course_id, Course.name, Class.type, Class.from_time, Class.to_time "+ 
							"FROM Class, Course "+ 
							"WHERE Class.FK_course_id=Course._id AND Class.date='"+year+"-"+month+"-"+day+"';";
			cursor = db.rawQuery(sqlQuery, null);
			System.out.println("fillArrayList ROWS: "+cursor.getCount());
			Log.i("getDayClass", "sql Executed: " + sqlQuery);
			db.close();
			return cursor;
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	
	public Cursor getAssByCourseId(int course_id){
		SQLiteDatabase db = null;
		Cursor cursor = null;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT * "+
							"FROM Assignment "+
							"WHERE FK_course_id="+course_id;
			cursor = db.rawQuery(sqlQuery, null);
			Log.i("getAssByCourseId", "sql Executed: " + sqlQuery);
			System.out.println("ASSSES: "+cursor.getCount());
			db.close();
			return cursor;
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	
	public Cursor getAssByPriority(){
		SQLiteDatabase db = null;
		Cursor cursor = null;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT * "+
							"FROM Assignment "+
							"ORDER BY priority ASC ";
			cursor = db.rawQuery(sqlQuery, null);
			Log.i("getAssByPriority", "sql Executed: " + sqlQuery);
			System.out.println("ASSSES: "+cursor.getCount());
			db.close();
			return cursor;
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	
	public Cursor getAssByDate(){
		SQLiteDatabase db = null;
		Cursor cursor = null;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT * "+
							"FROM Assignment "+
							"ORDER BY due_date ASC ";
			cursor = db.rawQuery(sqlQuery, null);
			Log.i("getAssByDate", "sql Executed: " + sqlQuery);
			System.out.println("ASSSES: "+cursor.getCount());
			db.close();
			return cursor;
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	
	public Cursor getAssByCourse(){
		SQLiteDatabase db = null;
		Cursor cursor = null;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT * "+
							"FROM Assignment ";
			cursor = db.rawQuery(sqlQuery, null);
			Log.i("getAssByCourse", "sql Executed: " + sqlQuery);
			System.out.println("ASSSES: "+cursor.getCount());
			db.close();
			return cursor;
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	/**
	 * Return Cursor with Courses
	 * */
	public Cursor getCoursesBySemesterId(int semester_id){
		SQLiteDatabase db = null;
		Cursor cursor = null;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT _id, name "+
							"FROM Course "+
							"WHERE FK_semester_id="+semester_id+";";
			cursor = db.rawQuery(sqlQuery, null);
			Log.i("getCourses", "sql Executed: " + sqlQuery);
			System.out.println("ASSSES: "+cursor.getCount());
			db.close();
			return cursor;
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	
	/**
	 * Return Course id
	 * */
	public int getCoursesId(int semester_id, String course_name){
		SQLiteDatabase db = null;
		Cursor cursor = null;
		int course_id = -1;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT _id "+
							"FROM Course "+
							"WHERE FK_semester_id="+semester_id+" AND name='"+course_name+"';";
			cursor = db.rawQuery(sqlQuery, null);
			Log.i("getCourses", "sql Executed: " + sqlQuery);
			cursor.moveToNext();
			course_id = Integer.parseInt(cursor.getString(0));
			db.close();
			return course_id;
		}catch(SQLException e){
			e.printStackTrace();
			return course_id;
		}
	}
	
	/**
	 * Return Cursor with Semester id for specified date
	 * */
	public int getSemesterByDate(String year, String month, String day){
		SQLiteDatabase db = null;
		Cursor cursor = null;
		
		///WWWWWWWWTFFF IS WRONG
		//day has to be 09 not 9 for example. same for month
		if(day.length()==1)
			day = "0"+day;
		if(month.length()==1)
			month="0"+month;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT _id "+ 
							"FROM Semester "+ 
							"WHERE from_date<'"+year+"-"+month+"-"+day+"' AND to_date>'"+year+"-"+month+"-"+day+"';";
			cursor = db.rawQuery(sqlQuery, null);
			Log.i("getSemesterByDate", "sql Executed: " + sqlQuery);
			db.close();
			if(cursor.moveToNext())
				return Integer.parseInt(cursor.getString(0));
			else{
				return 1; //to return the first semester in the list
			}
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return 0;
		}
	}
	
	/*****************************
	 * Return Cursor all Semesters
	 *****************************/
	public Cursor getSemesters(){
		SQLiteDatabase db = null;
		Cursor cursor = null;
	
		try{
			db = this.getReadableDatabase();
			String sqlQuery="SELECT * "+ 
							"FROM Semester; ";
			cursor = db.rawQuery(sqlQuery, null);
			System.out.println("Cursor ima vrstic: "+cursor.getCount());
			Log.i("getSemesters", "sql Executed: " + sqlQuery);
			db.close();
			return cursor;
		}catch(SQLException e){
			e.printStackTrace();
			db.close();
			return null;
		}
	}
	
	/*******************************
	 *Inserts Course Into Database.
	 *Method finds right semester 
	 *foreign key by looking at semester   
	 *table.					   
	 *******************************/
	public void insertCourse(String name, int semester_id){
		//Course (_id PK, FK_semester_id, name text)
		SQLiteDatabase db = null;
		ContentValues values = null;
        try {
        	db = this.getWritableDatabase();
	    	values = new ContentValues();
	    	values.put("name",name);
	    	values.put("FK_semester_id",semester_id);
    	
        	db.insertOrThrow("Course", null, values);
        	Log.i("insertCourse", "Successful inserting in database");
    	} catch (SQLException e) { //
    		// Ignore exception
    	} finally{
    		db.close();
    	}
	}
	
	/*******************************				   
	 *******************************/
	public void insertAssignment(int course_id, String name, String due_year, String due_month, String due_day, String priority, String completed, String description){
		//Assignment(_id PK, FK_course_id, name text, due_date int, priority text, completed text)
		
		SQLiteDatabase db = null;
		ContentValues values = null;
		
		//day has to be 09 not 9 for example. same for month
		if(due_day.length()==1)
			due_day = "0"+due_day;
		if(due_month.length()==1)
			due_month="0"+due_month;
		
        try {
        	db = this.getWritableDatabase();
	    	values = new ContentValues();
	    	values.put("FK_course_id",course_id);
	    	values.put("name",name);
	    	values.put("due_date",due_year+"-"+due_month+"-"+due_day);
	    	
	    	if(priority.equals("High"))
	    		values.put("priority","1");
	    	else if(priority.equals("Medium"))
	    		values.put("priority","2");
	    	else
	    		values.put("priority","3");
	    	
	    	values.put("completed",completed);
	    	values.put("description", description);
    	
        	db.insertOrThrow("Assignment", null, values);
        	Log.i("insertAssignment", "Successful inserting in database");
    	} catch (SQLException e) { //
    		//ignore exception
    	} finally{
    		db.close();
    	}
	}
	
	/**
	 * Edit row in Assignment
	 * */
	public void editAssignment(int id, int course_id, String name, String due_year, String due_month, String due_day, String priority, String completed, String description){
		
		//day has to be 09 not 9 for example. same for month
		if(due_day.length()==1)
			due_day = "0"+due_day;
		if(due_month.length()==1)
			due_month="0"+due_month;
		
		SQLiteDatabase db = null;
		ContentValues values = null;
        try {
        	db = this.getWritableDatabase();
        	values = new ContentValues();
        	String strFilter = "_id=" + id;
        	values.put("_id",id);
	    	values.put("FK_course_id",course_id);
	    	values.put("name",name);
	    	values.put("due_date",due_year+"-"+due_month+"-"+due_day);
	    	
	    	if(priority.equals("High"))
	    		values.put("priority","1");
	    	else if(priority.equals("Medium"))
	    		values.put("priority","2");
	    	else
	    		values.put("priority","3");
	    	
	    	values.put("completed",completed);
	    	values.put("description", description);
	    	
        	db.update("Assignment", values, strFilter, null);
    	} catch (SQLException e) { //
    		// Ignore exception
    	} finally{
    		db.close();
    	}
	}
	
	/*******************************				   
	 *******************************/
	public void insertTeacher(String first_name, String second_name){
		//Teacher( _id PK, first_name text, second_name text)
		
		SQLiteDatabase db = null;
		ContentValues values = null;
        try {
        	db = this.getWritableDatabase();
	    	values = new ContentValues();
	    	values.put("first_name",first_name);
	    	values.put("second_name", second_name);
	    	
        	db.insertOrThrow("Teacher", null, values);
        	Log.i("insertTeacher", "Successful inserting in database");
    	} catch (SQLException e) { //
    		// Ignore exception
    	} finally{
    		db.close();
    	}
	}
	
	/*******************************				   
	 *******************************/
	public void insertClass(int teacher_id, int course_id, String type, String year, String month, String day, String from_year, String from_month, String from_day, String to_year, String to_month, String to_day, String from_time, String to_time, String location, int repeat){
		//Class(_id PK, FK_teacher_id, FK_course_id, type text, date, from_date, to_date, from_time, to_time, location text)
		
		SQLiteDatabase db = null;
		ContentValues values = null;
		
		System.out.println("we will insert: ");
		
		//day has to be 09 not 9 for example. same for month
		if(day.length()==1)
			day = "0"+day;
		if(month.length()==1)
			month="0"+month;
		//day has to be 09 not 9 for example. same for month
		if(from_day.length()==1)
			from_day = "0"+from_day;
		if(to_month.length()==1)
			from_month="0"+from_month;
		//day has to be 09 not 9 for example. same for month
		if(to_day.length()==1)
			to_day = "0"+to_day;
		if(to_month.length()==1)
			to_month="0"+to_month;
		
        try {
        	db = this.getWritableDatabase();
        	values =  new ContentValues();
	    	values.put("FK_teacher_id",teacher_id);
	    	values.put("FK_course_id", course_id);
	    	values.put("type", type);
	    	values.put("date", year+"-"+month+"-"+day);
	    	values.put("from_date", from_year+"-"+from_month+"-"+from_day);
	    	values.put("to_date", to_year+"-"+to_month+"-"+to_day);
	    	values.put("from_time", from_time);
	    	values.put("to_time", to_time);
	    	values.put("location", location);
	    	//0 - just once, 1 every week, 2 every 2 weeks
	    	values.put("repeat", repeat);
    	
        	db.insertOrThrow("Class", null, values);
        	Log.i("insertClass", "Successful inserting in database");
    	} catch (SQLException e) { //
    		// Ignore exception
    	} finally{
    		db.close();
    	}
	}
	
	public void insertSemester(String name, String from_year, String from_month, String from_day, String to_year, String to_month, String to_day){
		//Semester(_id PK, name text, from_date int, to_date int)
		
		SQLiteDatabase db = null;
		ContentValues values = null;
		
		//day has to be 09 not 9 for example. same for month
		if(from_day.length()==1)
			from_day = "0"+from_day;
		if(from_month.length()==1)
				from_month="0"+from_month;

		//day has to be 09 not 9 for example. same for month
		if(to_day.length()==1)
			to_day = "0"+to_day;
		if(to_month.length()==1)
			to_month="0"+to_month;
		
        try {
        	db = this.getWritableDatabase();
	    	values = new ContentValues();
	    	values.put("name",name);
	    	values.put("from_date", from_year+"-"+from_month+"-"+from_day);
	    	values.put("to_date", to_year+"-"+to_month+"-"+to_day);
    	
        	db.insertOrThrow("Semester", null, values);
        	Log.i("insertSemester", "Successful inserting into database");
    	} catch (SQLException e) { //
    		// Ignore exception
    	} finally{
    		db.close();
    	}
	}
	
	
	/**
	 * Deleting semester row from table "Semester" where _id=id
	 * */
	public void deleteSemesterById(int id){
		SQLiteDatabase db = null;
        try {
        	db = this.getWritableDatabase();
        	db.execSQL("PRAGMA foreign_keys=ON;");        	
        	System.out.println(db.delete("Semester", "_id="+id, null));
    	} catch (SQLException e) { //
    		Log.d("DBHelper_deleteSemesterByID", "Caught Exception", e);
    	} finally{
    		db.close();
    	}
	}
}
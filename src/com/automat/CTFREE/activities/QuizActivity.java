package com.automat.CTFREE.activities;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.automat.CTFREE.Functions;
import com.automat.CTFREE.Functions.Note;
import com.automat.CTFREE.Functions.QuizType;
import com.automat.CTFREE.PianoApplication;
import com.automat.CTFREE.R;
import com.automat.CTFREE.views.HistoryView;
import com.automat.CTFREE.views.HistoryView.HistoryRowTuple;
import com.automat.CTFREE.views.QueryView;
import com.automat.CTFREE.views.SelectorView;
import com.automat.CTFREE.views.StatsView;

public class QuizActivity extends Activity {
	
	PianoApplication appState;

    @Override
    public void onCreate(Bundle savedInstanceState) {  
    	
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.quiz);
        
        appState = (PianoApplication)this.getApplication();
        appState.quizActivity = this;
        
        appState.db = new DBHelper(appState).getWritableDatabase();
        
        //Send views to appState
        QueryView qv = (QueryView)findViewById(R.id.qv);
        SelectorView s0 = (SelectorView)findViewById(R.id.s0);
        SelectorView s1 = (SelectorView)findViewById(R.id.s1);
        SelectorView s2 = (SelectorView)findViewById(R.id.s2);
        SelectorView s3 = (SelectorView)findViewById(R.id.s3);
        StatsView sv = (StatsView)findViewById(R.id.sv);       
        appState.queryView = qv;
        appState.selectorViews[0] = s0;
        appState.selectorViews[1] = s1;
        appState.selectorViews[2] = s2;
        appState.selectorViews[3] = s3;
        appState.statsView = sv;     
        
        initHistory();
        initAvailKeys();
        initAvailChords();
        resumeRound();
                
        Functions.reset_quiz();
        
    }    
    
    @Override
    public void onStop(){
    	super.onStop();
    	
    	appState.db.close();
    	
    	SharedPreferences settings = getSharedPreferences(appState.quizType.tableName(), 0);
    	SharedPreferences.Editor editor = settings.edit();         
    	editor.putInt("correctCount", appState.correctCount);
    	editor.putInt("attempts", appState.attempts);
    	editor.putLong("lastResponseTime", appState.lastResponseTime);
    	editor.putLong("totalResponseTimeForRound", appState.totalResponseTimeForRound);
    	editor.commit();
    }
    
    private void initAvailChords(){
    	appState.availChords = new ArrayList<Functions.ChordType>();
    	
    	SharedPreferences avails = getSharedPreferences("availChords", 0);
    	for(Functions.ChordType t : Functions.ChordType.values()){
    		boolean setting = avails.getBoolean(t.toString(), true);
    		if(setting)	appState.availChords.add(t);
    	}
    }
    
    private void initAvailKeys(){
    	appState.availKeys = new ArrayList<Functions.Note>();
    	String[] keys = {"A", "As", "B", "C", "Cs", "D" , "Ds", "E", "F", "Fs", "G", "Gs"};
    	
    	SharedPreferences avails = getSharedPreferences("availKeys", 0);
    	for(String k : keys){
    		if(k.equals("A")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting)	appState.availKeys.add(new Note(Functions.Tone.A, 0));	
    		}
    		else if(k.equals("As")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting){
        			appState.availKeys.add(new Note(Functions.Tone.A, 1));	
        			appState.availKeys.add(new Note(Functions.Tone.B, -1));
        		}
    		}
    		else if(k.equals("B")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting)	appState.availKeys.add(new Note(Functions.Tone.B, 0));	
    		}
    		else if(k.equals("C")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting)	appState.availKeys.add(new Note(Functions.Tone.C, 0));	
    		}
    		else if(k.equals("Cs")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting){
        			appState.availKeys.add(new Note(Functions.Tone.C, 1));	
        			appState.availKeys.add(new Note(Functions.Tone.D, -1));
        		}
    		}
    		else if(k.equals("D")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting)	appState.availKeys.add(new Note(Functions.Tone.D, 0));	
    		}
    		else if(k.equals("Ds")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting){
        			appState.availKeys.add(new Note(Functions.Tone.D, 1));	
        			appState.availKeys.add(new Note(Functions.Tone.E, -1));
        		}
    		}
    		else if(k.equals("E")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting)	appState.availKeys.add(new Note(Functions.Tone.E, 0));	
    		}
    		else if(k.equals("F")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting)	appState.availKeys.add(new Note(Functions.Tone.F, 0));	
    		}
    		else if(k.equals("Fs")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting){
        			appState.availKeys.add(new Note(Functions.Tone.F, 1));
        			appState.availKeys.add(new Note(Functions.Tone.G, -1));
        		}
    		}
    		else if(k.equals("G")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting)	appState.availKeys.add(new Note(Functions.Tone.G, 0));	
    		}
    		else if (k.equals("Gs")){
    			boolean setting = avails.getBoolean(k, true);
        		if(setting){
        			appState.availKeys.add(new Note(Functions.Tone.G, 1));
        			appState.availKeys.add(new Note(Functions.Tone.A, -1));
        		}
    		}
    		System.out.println(Arrays.toString(appState.availKeys.toArray()));
    		
    	}
    
    }
    
    private void resumeRound(){
    	//Initalize round data
        SharedPreferences settings = getSharedPreferences(appState.quizType.tableName(), 0);
        appState.correctCount = settings.getInt("correctCount", 0);
        appState.attempts = settings.getInt("attempts", 0);
        appState.lastResponseTime = settings.getLong("lastResponseTime", 0);
        appState.totalResponseTimeForRound = settings.getLong("totalResponseTimeForRound", 0);
        appState.quizStartTime = System.currentTimeMillis();
        
//        System.out.println( "On quizactivity create: " +
//        					appState.correctCount + " " +
//        					appState.attempts + " " + 
//        					appState.lastResponseTime + " " + 
//        					appState.totalResponseTimeForRound);

    }
    
    private void initHistory(){
    	appState.history = new ArrayList<HistoryView.HistoryRowTuple>();
        
        String query = "SELECT date, percent_correct, avg_response_time FROM " + 
						appState.quizType.tableName() + " " +
						"ORDER BY _id ASC " +
						"LIMIT 100";

        Cursor cursor = appState.db.rawQuery(query, null);

        if(cursor.getCount() > 0){        	
        	cursor.moveToFirst();	
        	while (!cursor.isAfterLast()) {
        		appState.history.add(        				
        				new HistoryRowTuple(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))), 
        							        cursor.getFloat(cursor.getColumnIndex("percent_correct")),
        							        cursor.getFloat(cursor.getColumnIndex("avg_response_time"))));
        		cursor.moveToNext();
        	}		
        	cursor.close();
        }    	
    }
    
    class DBHelper extends SQLiteOpenHelper {

	    private static final int DATABASE_VERSION = 3;
	    private static final String DATABASE_NAME = "chordtrainer_stats.db";

	    DBHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	System.out.println("on create db");
	    	for(QuizType type : Functions.available_quizTypes){
	    		String cmd = 	"CREATE TABLE " + 
	    						type.tableName() + 
	    						" (_id integer primary key autoincrement, " +
	    						"date text, " +
	    						"percent_correct real, " +
	    						"avg_response_time real);";   	
	    		db.execSQL(cmd);	
	    	}	        
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			System.out.println("on upgrade db");
//	    	for(QuizType type : Functions.available_quizTypes){
//	    		String cmd = 	"CREATE TABLE " + 
//	    						type.tableName() + 
//	    						" (_id integer primary key autoincrement, " +
//	    						"round integer, " +
//	    						"target_chord text, " +
//	    						"selected_chord text, " + 
//	    						"correct integer, " +
//	    						"response_time integer, " + 
//	    						"date integer);";	    	
//	    		db.execSQL(cmd);	
//	    	}			
		}
	}

}

package com.automat.CTFREE;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.media.SoundPool;

import com.automat.CTFREE.activities.QuizActivity;
import com.automat.CTFREE.views.HistoryView;
import com.automat.CTFREE.views.QueryView;
import com.automat.CTFREE.views.SelectorView;
import com.automat.CTFREE.views.StatsView;

public class PianoApplication extends Application{
	public Functions.QuizType quizType;
	
	public Functions.Chord 			target_chord;
	public HashMap<SelectorView, Functions.Chord> viewToChord = new HashMap<SelectorView, Functions.Chord>();
	
	public QueryView 		queryView;
	public SelectorView[] 	selectorViews = new SelectorView[4];
	public StatsView		statsView;
	
	public QuizActivity quizActivity;
	
	public SoundPool soundPool;
	public int[] sound_array;
	
	public SQLiteDatabase 	db;
	
	public long quizStartTime;
	public long totalResponseTimeForRound;
	public long lastResponseTime;	
	public int correctCount;
	public int attempts;
	
	
	
	public ArrayList<Functions.ChordType> availChords;
	public ArrayList<Functions.Note> availKeys;
	
	
	public int maxTrials = 10;
	
	
	public ArrayList<HistoryView.HistoryRowTuple> history;
}

package com.automat.CTFREE.activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.automat.CTFREE.Functions;
import com.automat.CTFREE.Functions.QuizType;
import com.automat.CTFREE.PianoApplication;
import com.automat.CTFREE.R;

public class MenuActivity extends Activity {
	
	private PianoApplication app;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.menu);

        
        /*
         * Set up app state
         */
        app = (PianoApplication)this.getApplication();
        Functions.set_app(app);
        
        /*
         * Set up sounds
         */        
        new SoundLoadingThread(this,app).run();
        
        
        /*
         * Set up quiztype selector
         */        
        Spinner quizTypeSpinner = (Spinner)findViewById(R.id.quizTypeSpinner);
        ArrayAdapter<QuizType> adapter = new ArrayAdapter<QuizType>(this,
        															//R.layout.spinner_item,
        															android.R.layout.simple_spinner_item,
        															Functions.available_quizTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quizTypeSpinner.setAdapter(adapter);        
        quizTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> p, View arg1, int pos, long arg3) {
				Functions.set_quiz_type((QuizType)p.getItemAtPosition(pos));								
			}

			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});                
	}
	
	
	public void onStartButtonClick(View v){
		Functions.onStartButtonClick(this, v);		
	}
	
	public void onOptionsButtonClick(View v){
		
		Functions.onOptionsButtonClick(this, v);
	}
	
	
	
    class SoundLoadingThread implements Runnable{
		private Context c;
		private PianoApplication app;
		
		public SoundLoadingThread(Context c, PianoApplication app){
			this.c = c;
			this.app = app;
		}
		
		public void run(){
			
			app.soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
			
			int[] sound_refs = {R.raw.a,
								R.raw.asharp,
								R.raw.b,    								
								R.raw.c,
								R.raw.csharp,
								R.raw.d,
								R.raw.dsharp,
								R.raw.e,    								
								R.raw.f,
								R.raw.fsharp,
								R.raw.g,
								R.raw.gsharp};	
			
			app.sound_array = new int[sound_refs.length];

			
			for (int i = 0; i < sound_refs.length; i++) {
				app.sound_array[i] = app.soundPool.load(c, sound_refs[i], 1);
			}
		}
    }	
}

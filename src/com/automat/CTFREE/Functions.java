package com.automat.CTFREE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.automat.CTFREE.views.HistoryView;
import com.automat.CTFREE.views.HistoryView.HistoryRowTuple;
import com.automat.CTFREE.views.QueryView;
import com.automat.CTFREE.views.SelectorView;
import com.automat.CTFREE.views.StatsView;


/*
* TODO
* -problem with f# drawing as f (draw double flat/sharps! in staves)
* -make keyboard keys match staves (i.e. from g to f)
* -draw clef symbols
*/
public class Functions {
	
	private static Random rand = new Random();
	private static PianoApplication appState;
	private static final String flat_str  = "\u266D";
	private static final String sharp_str = "\u266F";

	
	
	/*Data types*/	
	public static enum Tone{
		A,B,C,D,E,F,G;
	}
	
	public static class ScaleInterval{
		public int interval;
		public int accidental;
		public ScaleInterval(int interval, int accidental) {this.interval = interval;this.accidental = accidental;}		
	}
	
	public static enum ChordType{
		maj,min,aug,dim;
	}	
	
	
	private static enum DisplayType{
		KEYBOARD, SYMBOL, STAVE;
	}
	
	public static final class QuizType{
		DisplayType query;
		DisplayType selector;
		public QuizType(DisplayType query, DisplayType selector) {
			super();
			this.query = query;
			this.selector = selector;
		}
		@Override
		public String toString(){
			return "Match " + query.toString().toLowerCase() + " to " + selector.toString().toLowerCase();
		}
		
		public String tableName(){
			return query + "_" + selector;
		}
	}
	
	
	
	
	public static final QuizType[] available_quizTypes = {		
		new QuizType(DisplayType.KEYBOARD, DisplayType.SYMBOL),
		new QuizType(DisplayType.SYMBOL, DisplayType.KEYBOARD),
		new QuizType(DisplayType.STAVE, DisplayType.KEYBOARD),
		new QuizType(DisplayType.STAVE, DisplayType.SYMBOL),
	};

    	

	private static enum ScaleType{
		maj, min;
	}
	
	public static class Note{
		public Tone root;
		public int accidental;
		public Note(Tone root, int a){this.root = root; this.accidental = a;}
		@Override 
		public boolean equals(Object o){
			Note n = (Note)o;
			if(n.root == this.root && n.accidental == this.accidental){
				return true;
			}
			else return false;
		}
		@Override
		public String toString(){
			return this.root + " " + this.accidental;
		}
	}	
	
	public static class DiatonicIntervalNode{
		public Note note;
		public int toNextNote;
		public DiatonicIntervalNode(Note n, int toNextNote) {this.note = n;this.toNextNote = toNextNote;}
		@Override
		public String toString(){
			return note.toString() + " " + this.toNextNote;
		}
	}
	
	public static class Chord{
		public Note 		root;
		public ChordType 	type;
		public int 			inversion;
		public int 			octave;
		public Chord(Note root, ChordType type, int inversion, int octave) {this.root = root;this.type = type;this.inversion = inversion;this.octave = octave;}
		@Override
		public String toString(){
			return root.toString() + " " + this.type.toString() + " " + this.octave;
		}
		@Override
		public boolean equals(Object c){
			Chord ch = (Chord) c;
			return equivalent_note(this.root, ch.root) && this.type == ch.type;
		}
	}
	
	private static class PianoKey{
		Note note;
		RectF bounds;
		boolean isSharp;
		public PianoKey(Note n, RectF b, boolean s){this.note=n;this.bounds=b;this.isSharp=s;}
	}

	private static Map<ChordType,ScaleInterval[]> chord_definitions;
		static{
			HashMap<ChordType,ScaleInterval[]> temp = new HashMap<ChordType, ScaleInterval[]>();
			temp.put(ChordType.maj,  new ScaleInterval[] {	new ScaleInterval(1,0),
														  	new ScaleInterval(3,0),
														  	new ScaleInterval(5,0)});
			temp.put(ChordType.min,  new ScaleInterval[] {	new ScaleInterval(1,0),
					  										new ScaleInterval(3,-1),
					  										new ScaleInterval(5,0)});
//			temp.put(ChordType.sus2,  new ScaleInterval[] {	new ScaleInterval(1,0),
//															new ScaleInterval(2,0),
//															new ScaleInterval(5,0)});
//			temp.put(ChordType.sus4,  new ScaleInterval[] {	new ScaleInterval(1,0),
//															new ScaleInterval(4,0),
//															new ScaleInterval(5,0)});
//			temp.put(ChordType.maj7,  new ScaleInterval[] {	new ScaleInterval(1,0),
//															new ScaleInterval(3,0),
//															new ScaleInterval(5,0),
//															new ScaleInterval(7,0)});
//			temp.put(ChordType.maj6,  new ScaleInterval[] {	new ScaleInterval(1,0),
//															new ScaleInterval(3,0),
//															new ScaleInterval(5,0),
//															new ScaleInterval(6,0)});		
//			temp.put(ChordType.min7,  new ScaleInterval[] {	new ScaleInterval(1,0),
//															new ScaleInterval(3,-1),
//															new ScaleInterval(5,0),
//															new ScaleInterval(7,-1)});
//			temp.put(ChordType.dom7,  new ScaleInterval[] {	new ScaleInterval(1,0),
//															new ScaleInterval(3,0),
//															new ScaleInterval(5,0),
//															new ScaleInterval(7,-1)});
			temp.put(ChordType.aug,  new ScaleInterval[] {	new ScaleInterval(1,0),
															new ScaleInterval(3,0),
															new ScaleInterval(5,1)});
			temp.put(ChordType.dim,  new ScaleInterval[] {	new ScaleInterval(1,0),
															new ScaleInterval(3,-1),
															new ScaleInterval(5,-1)});
//			temp.put(ChordType.maj7b5,  new ScaleInterval[] {	new ScaleInterval(1,0),
//																new ScaleInterval(3,0),
//																new ScaleInterval(5,-1),
//																new ScaleInterval(7,0)});
			chord_definitions = Collections.unmodifiableMap(temp);		
		}

	private static Map<ScaleType,int[]> scale_definitions;
		static{
			HashMap<ScaleType,int[]> temp = new HashMap<ScaleType, int[]>();
			temp.put(ScaleType.maj,  new int[] {0, 2, 4, 5, 7, 9, 11});
			temp.put(ScaleType.min,  new int[] {0, 2, 3, 5, 7, 8, 11});
			scale_definitions = Collections.unmodifiableMap(temp);		
		}

	private static DiatonicIntervalNode[] diatonic_scale = {		
		new DiatonicIntervalNode(new Note(Tone.A, 0), 2),
		new DiatonicIntervalNode(new Note(Tone.B, 0), 1),
		new DiatonicIntervalNode(new Note(Tone.C, 0), 2),
		new DiatonicIntervalNode(new Note(Tone.D, 0), 2),
		new DiatonicIntervalNode(new Note(Tone.E, 0), 1),
		new DiatonicIntervalNode(new Note(Tone.F, 0), 2),
		new DiatonicIntervalNode(new Note(Tone.G, 0), 2)
	};
	
	private static Map<ChordType,String> chord_strings;
	static{
		HashMap<ChordType,String> temp = new HashMap<ChordType, String>();
		temp.put(ChordType.maj, "");
		temp.put(ChordType.min, "m");		
		temp.put(ChordType.aug, "+");
		temp.put(ChordType.dim, "\u00B0");
				
		chord_strings = Collections.unmodifiableMap(temp);		
	}
	
	
	
	/*
	 * Business logic
	 */
	private static Note random_note(){
		//Tone t = Tone.values()[rand.nextInt(Tone.values().length)];
		//int acc = rand.nextInt(3)-1;
		
		Note n = appState.availKeys.get(rand.nextInt(appState.availKeys.size()));
		return n;
//		if(t == Tone.B && acc==1){
//			return new Note(Tone.C, 0);
//		}
//		else if(t == Tone.C && acc==-1){
//			return new Note(Tone.B, 0);
//		}
//		else if(t == Tone.E && acc==1){
//			return new Note(Tone.F, 0);
//		}
//		else if(t == Tone.F && acc==-1){
//			return new Note(Tone.E, 0);
//		}
//		else return new Note(t, acc);
	}
	
	private static Chord random_chord(){
		Note root = random_note();
		//ChordType type = ChordType.values()[rand.nextInt(ChordType.values().length)];
		ChordType type = appState.availChords.get(rand.nextInt(appState.availChords.size()));
				
		while(	root.root == Tone.G && root.accidental == -1 && (type == ChordType.min) ||
				root.root == Tone.D && root.accidental == -1 && (type == ChordType.min) ||
				root.root == Tone.A && root.accidental == -1 && (type == ChordType.min) ||
				root.root == Tone.C && root.accidental == 1 && (type == ChordType.maj ||  type == ChordType.aug) ||
				root.root == Tone.G && root.accidental == 1 && (type == ChordType.maj ||  type == ChordType.aug) ||
				root.root == Tone.D && root.accidental == 1 && (type == ChordType.maj ||  type == ChordType.aug)){
			root = random_note();
			type = ChordType.values()[rand.nextInt(ChordType.values().length)];
		}		
		int octave = rand.nextInt(2);
		if(root.root == Tone.G || root.root == Tone.A || root.root == Tone.B){
			if(octave >= 1){
				octave=0;
			}
		}		
		return new Chord(	root,
							type,
							rand.nextInt(4),
							octave);		
	}
	
	private static DiatonicIntervalNode[] rotate_to(Note n, DiatonicIntervalNode[] ar){
		DiatonicIntervalNode[] rotated = new DiatonicIntervalNode[ar.length];		
		
		int seek = 0;
		while(ar[seek].note.root != n.root){
			seek++;
		}
		
		for (int i = 0; i < rotated.length; i++) {
			int ix = (seek + i) % ar.length;
			rotated[i] = ar[ix];
		}		
		
		return rotated;
	}
	
	private static Note[] major_scale_notes(Note root){
		int[] scale_def = scale_definitions.get(ScaleType.maj);
		
		DiatonicIntervalNode[] skeleton = rotate_to(root, diatonic_scale);
		
		Note[] notes = new Note[scale_def.length];
		
		int expected_interval = 0;
		int actual_interval = 0;
		int skeleton_ix = 0;
		for (int i = 0; i < scale_def.length; i++) {
			expected_interval=scale_def[i];
			notes[i] = new Note(skeleton[skeleton_ix].note.root,
								expected_interval - actual_interval);			
			
			if(skeleton_ix<skeleton.length){				
				skeleton_ix+=1;				
				actual_interval +=	skeleton[skeleton_ix-1].toNextNote;
			}
			
		}
		
		//If root note is nondiatonic adjust scale
		for (int i = 0; i < notes.length; i++) {
			notes[i].accidental+=root.accidental;
		}
		
		return notes;		
	}
	
	private static Note[] chord_notes(Chord c){
		ScaleInterval[] chord_def = chord_definitions.get(c.type);
		Note[] major_scale = major_scale_notes(c.root);
		Note[] chord_notes = new Note[chord_def.length];
		
		for (int i = 0; i < chord_notes.length; i++) {
			int index = chord_def[i].interval-1;
			int acc = chord_def[i].accidental;
						
			Note chord_note = major_scale[index];
			chord_note.accidental += acc;
			chord_notes[i] = chord_note;
		}
		
		return chord_notes;
	}
	
	private static String chord_symbol(Chord c){
		StringBuilder sb = new StringBuilder();
		sb.append(c.root.root);
		if(c.root.accidental == 1)sb.append(sharp_str);
		if(c.root.accidental == -1)sb.append(flat_str);
		sb.append(chord_strings.get(c.type));
		return sb.toString();
	}
	
	private static int natural_offset(Note lower, Note higher){
		DiatonicIntervalNode[] rotated = rotate_to(lower, diatonic_scale);
		Note rootnote = new Note(higher.root, 0);
		
		int j = 0;
		int offset = 0;
		while(!equivalent_note(rootnote, rotated[j].note)){
			offset+=rotated[j].toNextNote;
			j++;			
		}
		
		offset -= lower.accidental;
		offset += higher.accidental;
		
		return offset;
	}
	
	
	
	private static int diatonic_offset(Note lower, Note higher){
		DiatonicIntervalNode[] rotated = rotate_to(lower, diatonic_scale);
		Note rootnote = new Note(higher.root, 0);
		
		int j = 0;
		while(!equivalent_note(rootnote, rotated[j].note)){
			j++;
		}
		
		return j;
	}
	
	private static int note_index(Note n){
		int diatonic_index = 0;
		switch(n.root){
		case A: diatonic_index = 0; break;
		case B: diatonic_index = 2; break;
		case C: diatonic_index = 3; break;
		case D: diatonic_index = 5; break;
		case E: diatonic_index = 7; break;
		case F: diatonic_index = 8; break;
		case G: diatonic_index = 10; break;
		}
		
		diatonic_index += n.accidental;
		
		//loop left
		if(diatonic_index < 0){
			int j = (-1*diatonic_index) % 12;
			return 12 - j;
		}
		//loop right
		else return diatonic_index % 12;
	}
	
	private static boolean equivalent_note(Note a, Note b){
		return note_index(a)==note_index(b);
	}
	
	
	private static ArrayList<PianoKey> position_keys(ArrayList<PianoKey> keys, int num_white_keys, RectF bounds){
		//Get bounds
		float lft = bounds.left;
		float rgt = bounds.right;
		float top = bounds.top;
		float bot = bounds.bottom;
		
		//Scale widths and heights
		float w_w = (rgt - lft) / (float) num_white_keys;
		float w_h = (bot - top);
		float b_w = 0.666f * w_w;
		float b_h = 0.666f * w_h;

		//Note: first key is always off screen
		//TODO we assume here first key is white...
		keys.get(0).bounds.left 	= lft - w_w;
		keys.get(0).bounds.top 		= top;
		keys.get(0).bounds.bottom 	= top + w_h;
		keys.get(0).bounds.right	= keys.get(0).bounds.left + w_w;

		for (int i = 1; i < keys.size(); i++) {
			PianoKey curr_key = keys.get(i);
			if(curr_key.isSharp){
				curr_key.bounds.top 	= top;
				curr_key.bounds.bottom 	= top + b_h;
				switch(curr_key.note.root){
				case C:
					curr_key.bounds.left = keys.get(i-1).bounds.left + 0.550f * w_w;						
					break;
				case D: 
					curr_key.bounds.left = keys.get(i-1).bounds.left + 0.800f * w_w;						
					break;
				case F: 
					curr_key.bounds.left = keys.get(i-1).bounds.left + 0.550f * w_w;						
					break;
				case G: 
					curr_key.bounds.left = keys.get(i-1).bounds.left + 0.700f * w_w;						
					break;
				case A: 
					curr_key.bounds.left = keys.get(i-1).bounds.left + 0.800f * w_w;						
					break;
				}
				curr_key.bounds.right = curr_key.bounds.left + b_w;
			}
			else{ //is a white key
				curr_key.bounds.top = top;
				curr_key.bounds.bottom = top + w_h;
				curr_key.bounds.left = (keys.get(i-1).note.accidental != 0) ? keys.get(i-2).bounds.right : keys.get(i-1).bounds.right;
				curr_key.bounds.right = curr_key.bounds.left + w_w;				
			}
		}		

		return keys;	

	}
	
	
	
	private static Canvas render_keyboard(	Canvas c, 
											RectF rect,
											Chord chord){
		int num_white_keys = 17;
		Note start_note = new Note(Tone.C,0);
		DiatonicIntervalNode[] keyboard_scale = rotate_to(start_note, diatonic_scale); 
		
		//Create list of visible keys
		ArrayList<PianoKey> keys = new ArrayList<PianoKey>();
		int counter = 0;
		while(counter < num_white_keys){
			DiatonicIntervalNode current_node = keyboard_scale[(counter % keyboard_scale.length)];
			for (int i = 0; i < current_node.toNextNote; i++) {
				keys.add(new PianoKey(new Note(current_node.note.root, i), 
						 			  new RectF(),
						 			  i>0?true:false));	
			}
			counter++;
		}
		
		//Position keys
		keys = position_keys(keys, num_white_keys, rect);
		
		//Render keys 
		//Fill whites first
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.WHITE);
		p.setStyle(Style.FILL);
		for(PianoKey k : keys){		
			if(!k.isSharp){
				c.drawRect(k.bounds, p);
			}
		}

		//Then black keys
		p.setColor(Color.BLACK);
		p.setStyle(Style.FILL);
		for(PianoKey k : keys){
			if(k.isSharp){
				c.drawRect(k.bounds, p);
			}
		}
		
		/*Then overlay grid*/
		p.setColor(Color.BLACK);
		p.setStyle(Style.STROKE);
		for(PianoKey k : keys){		
			if(!k.isSharp){
				c.drawRect(k.bounds, p);
			}
		}
		
		
		/*Now overlay chord notes*/		
		Note[] chord_notes = chord_notes(chord);
		Note last_note = start_note;
		int last_offset = chord.octave * 12;
		for (int i = 0; i < chord_notes.length; i++) {
			int offset = last_offset + natural_offset(last_note, chord_notes[i]);
			if(offset < 0) offset += 12;
			if(offset > keys.size()-1) offset -= 12;	
			PianoKey k = keys.get(offset);
			
			p.setColor(Color.BLUE);
			p.setStyle(Style.FILL);
			c.drawRect(k.bounds, p);
			
			/*Redraw occluded sharps*/			
			if(offset > 1 && 
					keys.get(offset-1).isSharp){ /*(left sharp key)*/
				PianoKey l = keys.get(offset-1);
				p.setColor(Color.BLACK);
				p.setStyle(Style.FILL);
				c.drawRect(l.bounds, p);
			}
			
			if(offset < keys.size()-2 &&
					keys.get(offset+1).isSharp){ /*(right sharp key)*/
				PianoKey r = keys.get(offset+1);
				p.setColor(Color.BLACK);
				p.setStyle(Style.FILL);
				c.drawRect(r.bounds, p);
			}
			
			last_note = chord_notes[i];
			last_offset = offset;
		}	
		return c;		
	}
	
	private static final Canvas render_staves(	Canvas c, 
												RectF bounds,
												Chord chord){
		float space = (bounds.bottom - bounds.top) / 10;

		/*Render staff lines*/ 
		Paint p = new Paint();
		p.setAntiAlias(true);
		c.drawRect(bounds, p);
		p.setColor(Color.WHITE);
		for (int i = 0; i < 11; i++) {
			if(i!=5){
				c.drawLine(	bounds.left, 
							bounds.top + (i * space), 
							bounds.right, 
							bounds.top + (i * space), p);
			}
		}
		
		/*Render chord notes*/
		Note[] chord_notes = chord_notes(chord);
		int curr_offset = diatonic_offset(new Note(Tone.G,0), chord_notes[0]);
		curr_offset+=chord.octave * 7;
		for (int i = 0; i < chord_notes.length; i++) {
			if(i>0){
				curr_offset += diatonic_offset(chord_notes[i-1], chord_notes[i]);
			}
			p.setColor(Color.WHITE);
			
			float circle_x = bounds.left + (bounds.right - bounds.left) / 2.0f;
			float circle_y = bounds.bottom - (curr_offset * (space / 2.0f));
			
			c.drawCircle(	circle_x, 
							circle_y, 
							space / 2.0f, 
							p);	
			
			/*Render accidentals*/
			if(chord_notes[i].accidental > 0){
				/*Scale text -- hacky*/
				float ts = 1;
				p.setTextSize(ts);	
				Rect tbounds = new Rect();			
				p.getTextBounds(sharp_str, 0, sharp_str.length(), tbounds);
				while(tbounds.height() < space && ts < 1000){
					ts = 1.1f * ts;
					p.setTextSize(ts);
					p.getTextBounds(sharp_str, 0, sharp_str.length(), tbounds);	
				}		
				c.drawText(sharp_str, circle_x * 0.85f, circle_y + (space/2.0f), p);
			}
			if(chord_notes[i].accidental < 0){
				/*Scale text -- hacky*/
				float ts = 1;
				p.setTextSize(ts);	
				Rect tbounds = new Rect();			
				p.getTextBounds(flat_str, 0, flat_str.length(), tbounds);
				while(tbounds.height() < space && ts < 1000){
					ts = 1.1f * ts;
					p.setTextSize(ts);
					p.getTextBounds(flat_str, 0, flat_str.length(), tbounds);	
				}		
				c.drawText(flat_str, circle_x * 0.85f, circle_y + (space/2.0f), p);
			}
			
			/*Render line*/
			
		}
		
		return c;		
	}


	@SuppressWarnings("unused")
	private static final Canvas render_symbol(	Canvas c, 
												RectF rect,
												Chord chord){
		
		return c;		
	}
	
	private static final void play_chord(Chord c, View v){
		AudioManager mgr = (AudioManager)v.getContext().getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);    
        float volume = streamVolumeCurrent / streamVolumeMax;		
		
		Note[] notes = chord_notes(c);
		SoundPool sp = appState.soundPool;
		int[] sound_array = appState.sound_array;
		
		for (int i = 0; i < notes.length; i++) {
			sp.play(sound_array[note_index(notes[i])], volume, volume, 1, 0, 1f);
		}
	}


//	private static final void punch_timestamp(Chord selected_chord){
//		ContentValues values = new ContentValues();
//		values.put("target_chord", appState.target_chord.toString());
//		values.put("round", appState.currentRound);
//		values.put("selected_chord", selected_chord.toString());
//		values.put("correct", (selected_chord.equals(appState.target_chord) ? 1 : 0));
//		values.put("response_time", System.currentTimeMillis() - appState.quizStartTime);
//		values.put("date", System.currentTimeMillis());
//		appState.db.insert(appState.quizType.tableName(), null, values);
//	}


	/*
	 * GUI user event callbacks
	 */
	public static void on_selector_down(SelectorView v){
		/*
		 * When a selector view is pressed, check if correct selection
		 */
	
		if(appState.target_chord == appState.viewToChord.get(v)){
			v.setBackgroundColor(Color.GREEN);			
		}		
		else{
			v.setBackgroundColor(Color.RED);
		}
	}

	public static void on_selector_up(SelectorView v){
		v.setBackgroundColor(Color.BLACK);
		//punch_timestamp(appState.viewToChord.get(v));
		appState.attempts++;
		
		if(appState.target_chord == appState.viewToChord.get(v)){
			play_chord(appState.target_chord, v);
			
			appState.correctCount++;
			reset_quiz();
		}
		else{		
			appState.statsView.invalidate();
		}
	}


	/*
	 * GUI rendering callbacks
	 */
	public static final Canvas on_render_queryView(QueryView v, Canvas c){
		if(appState.target_chord != null){
			if(appState.quizType.query == DisplayType.KEYBOARD){			
				Chord chord = appState.target_chord;
				float left = (0.1f * v.getWidth());
				float top  = (0.2f * v.getHeight());
				float rgt = (1.0f * v.getWidth());
				float bot = (0.8f * v.getHeight());
				c = render_keyboard(	c, 
										new RectF(	left, 
													top, 
													rgt, 
													bot), 
										chord);
			}
			if(appState.quizType.query == DisplayType.SYMBOL){
				Chord chord = appState.target_chord;
				String chord_str = chord_symbol(chord);
				Paint p = new Paint();
				p.setColor(Color.BLUE);
				p.setAntiAlias(true);
				p.setStrokeWidth(3);
				
				/*Scale text*/
				float ts = v.getHeight() * 0.7f;
				p.setTextSize(ts);	
				Rect bounds = new Rect();			
				p.getTextBounds(chord_str, 0, chord_str.length(), bounds);
				while(bounds.width() > v.getWidth() && ts > 1){
					Log.d("", ts+"");
					ts = 0.9f * ts;
					p.setTextSize(ts);
					p.getTextBounds(chord_str, 0, chord_str.length(), bounds);	
				}
				
							
				p.setTextAlign(Paint.Align.CENTER);
		
				c.drawText(chord_str, 0.5f * v.getWidth(), 0.7f * v.getHeight(), p);
			}
			if(appState.quizType.query == DisplayType.STAVE){
				Chord chord = appState.target_chord;
				float left = (0.05f * v.getWidth());
				float top  = (0.1f * v.getHeight());
				float rgt = (0.95f * v.getWidth());
				float bot = (0.9f * v.getHeight());
				c = render_staves(c, new RectF(left, top, rgt, bot), chord);
				
			}	
		}
		
		return c;
	}


	public static final Canvas on_render_selectorView(SelectorView v, Canvas c){
		if(appState.target_chord!=null){
			/*
			 * Draw the selector based on type. 
			 */		
			if(appState.quizType.selector == DisplayType.KEYBOARD){
				Paint p = new Paint();
				p.setAntiAlias(true);
				Chord chord = appState.viewToChord.get(v);
				float left = (0.1f * v.getWidth());
				float top  = (0.05f * v.getHeight());
				float rgt = (1.0f * v.getWidth());
				float bot = (0.95f * v.getHeight());
				c = render_keyboard(c,
									new RectF(	left, 
												top, 
												rgt, 
												bot),								 
									chord);
			}
			if(appState.quizType.selector == DisplayType.SYMBOL){
				Chord chord = appState.viewToChord.get(v);
				String chord_str = chord_symbol(chord);
				
				Paint p = new Paint();
				p.setColor(Color.BLUE);
				p.setAntiAlias(true);
				p.setStrokeWidth(3);
				
				/*Scale text*/
				float ts = v.getHeight() * 0.7f;
				p.setTextSize(ts);	
				Rect bounds = new Rect();			
				p.getTextBounds(chord_str, 0, chord_str.length(), bounds);
				while(bounds.width() > v.getWidth() && ts > 1){
					Log.d("", ts+"");
					ts = 0.9f * ts;
					p.setTextSize(ts);
					p.getTextBounds(chord_str, 0, chord_str.length(), bounds);	
				}		
				p.setTextAlign(Paint.Align.CENTER);				
		
				c.drawText(	chord_str, 
							0.5f * v.getWidth(), 
							0.7f * v.getHeight(), p);
			}
			if(appState.quizType.selector == DisplayType.STAVE){
				Chord chord = appState.viewToChord.get(v);
				float left = (0.05f * v.getWidth());
				float top  = (0.1f * v.getHeight());
				float rgt = (0.95f * v.getWidth());
				float bot = (0.9f * v.getHeight());
				c = render_staves(c, new RectF(left, top, rgt, bot), chord);	
			}		
		}
		return c;
	}
	
	
	public static final Canvas on_render_statsView(StatsView v, Canvas c){
		
		String resp = "Reaction time: " + appState.lastResponseTime / 1000.0 + "sec";
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.MAGENTA);
		
		/*Scale text -- hacky*/
		float ts = 1f;
		p.setTextSize(ts);	
		Rect tbounds = new Rect();			
		p.getTextBounds(resp, 0, resp.length(), tbounds);
		while(tbounds.height() < 0.55f * v.getHeight() && ts < 1000){
			ts = 1.05f * ts;
			p.setTextSize(ts);
			p.getTextBounds(resp, 0, resp.length(), tbounds);	
		}		
		c.drawText(resp, 10, tbounds.height(), p);
		
		
		String cors = appState.correctCount + " for " + appState.attempts;
				
		/*Scale text -- hacky*/
		Rect vbounds = new Rect();			
		p.getTextBounds(cors, 0, cors.length(), vbounds);
		c.drawText(cors, v.getRight() - vbounds.width() - 10, tbounds.height(), p);
		

		
		return c;
	}
	
		
//	public static Canvas on_render_historyView(View v, Canvas c){
//		
//		System.out.println("in redner hidstory");
//		
//		//TESTDATA (TODO  PING DB)
//		ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
//		for (int i = 0; i < 30; i++) {
//			HashMap<String,String> entry = new HashMap<String,String>();
//			entry.put("date", Long.toString(System.currentTimeMillis()));
//			entry.put("percent_correct", Float.toString(rand.nextFloat()));
//			entry.put("avg_reaction_time", Float.toString(rand.nextFloat() * 10.0f));
//			data.add(entry);
//		}
//		
//		float width = v.getWidth();
//		float left = v.getLeft();
//		float top = v.getTop();
//		float col_1_w = 0.2f * width;
//		float col_2_w = 0.4f * width;
//		float col_3_w = 0.4f * width;		
//		float row_h = 0.1f * width;
//		float title_row_h = 0.15f *width;
//		
//		Paint p = new Paint();
//		p.setAntiAlias(true);
//		p.setColor(Color.WHITE);
//		p.setTextSize(20);
//		
//		//DRAW TITLE ROW
//		c.drawText("Accuracy", left + col_1_w, top, p);
//		c.drawText("Response Time", left + col_1_w + col_2_w, top, p);
//		
//		for (int i = 0; i < data.size(); i++) {
//			HashMap<String,String> entry = data.get(i);
//			long date = Long.parseLong(entry.get("date"));
//			float percent_correct = Float.parseFloat(entry.get("percent_correct"));
//			float avg_reaction_time = Float.parseFloat(entry.get("avg_reaction_time"));
//			
//			float thisy = i*row_h + title_row_h;
//			
//			if(i%5==0){
//				c.drawText(new Date(date).toString(), 0, thisy, p);
//			}
//			
//			//Draw percent correct
//			c.drawLine(	left + col_1_w, 
//						thisy, 
//						left + col_1_w + col_2_w * percent_correct, 
//						thisy, 
//						p);
//			
//			//Draw response time
//			c.drawLine(	left + col_1_w + col_2_w, 
//						thisy, 
//						left + col_1_w + col_2_w + col_3_w * (avg_reaction_time / 10f), 
//						thisy, 
//						p);			
//		}
//		
//		
//		
//		return c;
//	}
	
	public static Canvas on_render_barGraph(View v, Canvas c, float value, float maxValue){
		
		Paint p = new Paint();
		p.setColor(Color.MAGENTA);
		p.setStyle(Style.FILL_AND_STROKE);
		p.setStrokeWidth(20);
		
		System.out.println(value + " " + maxValue);
		
		c.drawLine(	0, 
					v.getHeight() / 2, 
					(value/maxValue)*v.getWidth(), 
					v.getHeight() / 2, 
					p);
	
		return c;
	}
	
	
	
	/*
	 * Main menu
	 */
	public static void set_quiz_type(QuizType qt){
		appState.quizType = qt;
	}
	
	public static  void onStartButtonClick(Activity a, View v){
		a.startActivity(new Intent(a, com.automat.CTFREE.activities.QuizActivity.class));
	}
	
	public static void onOptionsButtonClick(Activity a, View v){
		a.startActivity(new Intent(a, com.automat.CTFREE.activities.OptionsActivity.class));
	}

	/*
	 * Initialize app
	 */
	public static final void set_app(PianoApplication app){
		Functions.appState = app;
	}
	
	public static void reset_chords(){
		/*
		 * Select a target answer and choices. Render all views.
		 */			
		//Reset target and choices
		appState.target_chord = random_chord();	
//		for (int i = 0; i < appState.selectorViews.length; i++) {			
//			appState.viewToChord.put(appState.selectorViews[i], random_chord());
//		}
		appState.viewToChord.clear();
		int i = 0;
		while(i < 4){
			Chord c = random_chord();
			if(!c.equals(appState.target_chord) && !appState.viewToChord.containsValue(c)){
				appState.viewToChord.put(appState.selectorViews[i], c);
				i++;
			}
		}		
		//Ensure at least one choice is correct
		appState.viewToChord.put(appState.selectorViews[rand.nextInt(appState.selectorViews.length)], appState.target_chord);
			
	
		//Redraw query and selector views
		appState.queryView.invalidate();
		for (i = 0; i < appState.selectorViews.length; i++) {
			appState.selectorViews[i].invalidate();
		}
		
		appState.statsView.invalidate();
		
		appState.quizStartTime = System.currentTimeMillis();
		
	}

	
	public static void reset_quiz(){
		
//		int lastResponseTime = 0;
//		int correctCount = 0;
//		int attempts = 0;
//		
//		String query = "SELECT correct, response_time FROM " + 
//						appState.quizType.tableName() + " " +
//						"WHERE round=" + appState.currentRound + " " +
//						"ORDER BY _id DESC";
//		
//		Cursor cursor = appState.db.rawQuery(query, null);
//		
//		if(cursor.getCount() > 0){
//			
//			cursor.moveToFirst();
//			
//			//Get latest response time
//			lastResponseTime = cursor.getInt(cursor.getColumnIndex("response_time"));
//			//Calculate correct / attempts
//			while (!cursor.isAfterLast()) {
//				correctCount += cursor.getInt(cursor.getColumnIndex("correct"));			
//				attempts++;
//				cursor.moveToNext();
//			}		
//			cursor.close();
			
		if(appState.correctCount >= appState.maxTrials){

			//1. Write new round entry into history and db
			long date = System.currentTimeMillis();
			float percent_correct = (float)appState.correctCount / (float)appState.attempts;
			float avg_response_time = appState.totalResponseTimeForRound / (float)appState.maxTrials;

			appState.history.add(new HistoryRowTuple(date, percent_correct, avg_response_time));				

			ContentValues values = new ContentValues();
			values.put("date", date);
			values.put("percent_correct", percent_correct);
			values.put("avg_response_time", avg_response_time);				
			appState.db.insert(appState.quizType.tableName(), null, values);
			

			//2. Show dialog with SimpleCursorAdapter
			final Dialog dialog = new Dialog(appState.quizActivity);

			dialog.setContentView(R.layout.history);
			dialog.setTitle("Round Complete!");

			HistoryView historyView = (HistoryView) dialog.findViewById(R.id.historyList);				

			historyView.setAdapter(new HistoryView.CustomAdapter(appState.quizActivity, appState.history));

			Button button = (Button) dialog.findViewById(R.id.dismissDialogButton);
			button.setOnClickListener(new OnClickListener() {					
				public void onClick(View v) {
					reset_chords();						
					dialog.cancel();						
				}
			});				

			dialog.setOnCancelListener(new OnCancelListener() {
				
				public void onCancel(DialogInterface dialog) {
					reset_chords();			
				}
			});
			dialog.show();


			//3. Reset appstate data
			appState.correctCount = 0;
			appState.lastResponseTime = 0;
			appState.totalResponseTimeForRound = 0;				
			appState.attempts = 0;
		}
		else{
			if(appState.attempts != 0){
				appState.lastResponseTime = System.currentTimeMillis() - appState.quizStartTime;
				appState.totalResponseTimeForRound += appState.lastResponseTime;
			}
			reset_chords();			
		}
//		}
//		else{
//			reset_chords();			
//		}		
	}
}

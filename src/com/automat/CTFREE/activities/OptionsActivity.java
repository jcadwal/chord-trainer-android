package com.automat.CTFREE.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.automat.CTFREE.PianoApplication;
import com.automat.CTFREE.R;

public class OptionsActivity extends Activity{
	
	//ArrayList<CheckBox> selectedKeyCBs = new ArrayList<CheckBox>();
	//ArrayList<CheckBox> selectedChordCBs = new ArrayList<CheckBox>();
	int selKeyCount = 0;
	int selChordCount = 4;
	
	@Override 
	public void onCreate(Bundle savedInstanceState){		
		
		super.onCreate(savedInstanceState);		
		this.setContentView(R.layout.options);
		
		PianoApplication appState = (PianoApplication)getApplication();
		
//		SharedPreferences availChords = getSharedPreferences("availChords", 0);
		SharedPreferences availKeys = getSharedPreferences("availKeys", 0);		
		LinearLayout layout = (LinearLayout) findViewById(R.id.optionslayout);
		
		
		for (int i = 0; i < layout.getChildCount(); i++) {
			View v = layout.getChildAt(i);
			CheckBox cb;
			switch(v.getId()){
//			case R.id.major:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("maj", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.minor:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("min", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.maj7:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("maj7", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.min7:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("min7", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.dom7:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("dom7", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.aug:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("aug", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.sus2:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("sus2", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.sus4:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("sus4", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.dim:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("dim", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
//			case R.id.maj6:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("maj6", true));
//				if(cb.isChecked()) selChordCount++;
//				break;				
//			case R.id.maj7b5:
//				cb = (CheckBox)v;
//				cb.setChecked(availChords.getBoolean("maj7b5", true));
//				if(cb.isChecked()) selChordCount++;
//				break;
			
			
			case R.id.checkBoxA:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("A", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxAs:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("As", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxB:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("B", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxC:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("C", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxCs:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("Cs", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxD:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("D", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxDs:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("Ds", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxE:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("E", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxF:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("F", true));
				if(cb.isChecked()) selKeyCount++;
				break;
				
			case R.id.checkBoxFs:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("Fs", true));
				if(cb.isChecked()) selKeyCount++;
				break;				
			
			case R.id.checkBoxG:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("G", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			
			case R.id.checkBoxGs:
				cb = (CheckBox)v;
				cb.setChecked(availKeys.getBoolean("Gs", true));
				if(cb.isChecked()) selKeyCount++;
				break;
			}			
		}
	}
	
	public void onKey(View v){
		

		SharedPreferences availKeys = getSharedPreferences("availKeys", 0);    	
    	SharedPreferences.Editor keysEditor = availKeys.edit();    	
    	CheckBox cb = (CheckBox)v;
    	
		switch(v.getId()){
		case R.id.checkBoxA:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("A", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("A", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxAs:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("As", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("As", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxB:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("B", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("B", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxC:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("C", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("C", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxCs:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("Cs", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("Cs", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxD:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("D", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("D", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxDs:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("Ds", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("Ds", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxE:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("E", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("E", cb.isChecked());
				selKeyCount++;
			}
			break;
		case R.id.checkBoxF:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("F", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("F", cb.isChecked());
				selKeyCount++;
			}
			break;
			
		case R.id.checkBoxFs:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("Fs", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("Fs", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxG:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("G", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("G", cb.isChecked());
				selKeyCount++;
			}
			break;
		
		case R.id.checkBoxGs:
			if(!cb.isChecked()){
				if(selChordCount * selKeyCount > 6 && selKeyCount > 1){
					keysEditor.putBoolean("Gs", cb.isChecked());
					selKeyCount--;
				}
				else cb.setChecked(true);
			}
			else{
				keysEditor.putBoolean("Gs", cb.isChecked());
				selKeyCount++;
			}
			break;
		}    	

		keysEditor.commit();

	}
	
	
//	public void onChord(View v){
//		
//		SharedPreferences availChords = getSharedPreferences("availChords", 0);		
//		SharedPreferences.Editor chordsEditor = availChords.edit();
//
//
//
//		CheckBox cb = (CheckBox)v;
//
//		switch(v.getId()){
//		case R.id.major:
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("maj", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("maj", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.minor:
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("min", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("min", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.sus2:			
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("sus2", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("sus2", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.sus4:			
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("sus4", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("sus4", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.dom7:			
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("dom7", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("dom7", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.maj6:
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("maj6", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("maj6", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.maj7:
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("maj7", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("maj7", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.min7:
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("min7", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("min7", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.aug:			
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("aug", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("aug", cb.isChecked());
//				selChordCount++;
//			}
//			break;			
//		case R.id.dim:			
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("dim", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("dim", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		case R.id.maj7b5:
//			if(!cb.isChecked()){
//				if(selChordCount * selKeyCount > 6 && selChordCount > 1){
//					chordsEditor.putBoolean("maj7b5", cb.isChecked());
//					selChordCount--;
//				}
//				else cb.setChecked(true);
//			}
//			else{
//				chordsEditor.putBoolean("maj7b5", cb.isChecked());
//				selChordCount++;
//			}
//			break;
//		}
//
//		chordsEditor.commit();
//		System.out.println(selChordCount + " " + selKeyCount);
//
//	}


}

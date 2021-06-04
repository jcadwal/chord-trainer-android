package com.automat.CTFREE.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.automat.CTFREE.R;

public class HistoryView extends ListView {

	public HistoryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public static class HistoryRowTuple{
		long date;
		float percentCorrect;
		float avgResponseTime;
		public HistoryRowTuple(long date, float percentCorrect,
				float avgResponseTime) {
			super();
			this.date = date;
			this.percentCorrect = percentCorrect;
			this.avgResponseTime = avgResponseTime;
		}
		
		
	}
	
	public static class CustomAdapter extends ArrayAdapter<HistoryRowTuple>{
		
		private final Context context;
		private final List<HistoryRowTuple> list;

		public CustomAdapter(Context context, List<HistoryRowTuple> objects) {
			super(context, R.layout.historyrow, objects);
			this.context = context;
			this.list = objects;
		}
		
		private static class ViewHolder{
			TextView dateView;
			BarGraph accuracyView;
			BarGraph reactionView;
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.historyrow, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.dateView = (TextView) rowView.findViewById(R.id.dateTextView);
				viewHolder.accuracyView = (BarGraph) rowView.findViewById(R.id.accuracyGraph);
				viewHolder.reactionView = (BarGraph) rowView.findViewById(R.id.responseTimeGraph);
				rowView.setTag(viewHolder);
			}

			ViewHolder holder = (ViewHolder) rowView.getTag();
			
			HistoryRowTuple row = list.get(list.size() - 1 - position);
			
			SimpleDateFormat df = new SimpleDateFormat("MMM dd");
						
			holder.dateView.setText(df.format(new Date(row.date)));
			holder.accuracyView.setMaxValue(1.0f);
			holder.accuracyView.setValue(row.percentCorrect);
			holder.reactionView.setMaxValue(7000.0f);
			holder.reactionView.setValue(row.avgResponseTime);

			return rowView;
		}
		
	}

}

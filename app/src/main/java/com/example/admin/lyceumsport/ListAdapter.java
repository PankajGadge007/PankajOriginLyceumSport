package com.example.admin.lyceumsport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ListAdapter(Context context,
					   ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView grade;
		TextView section;
		TextView timeto;
		TextView timefrom;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.row, parent, false);
		// Get the position
		resultp = data.get(position);

		// Locate the TextViews in listview_item.xml
		grade = (TextView) itemView.findViewById(R.id.grade);
		section = (TextView) itemView.findViewById(R.id.section);
		timeto = (TextView) itemView.findViewById(R.id.timeto);
		timefrom = (TextView) itemView.findViewById(R.id.timefrom);

		// Capture position and set results to the TextViews
		grade.setText(resultp.get("grade"));
		section.setText(resultp.get("section"));
		timeto.setText(resultp.get("dateto"));
		timefrom.setText(resultp.get("datefrom"));

		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get the position
				resultp = data.get(position);
				/*Intent intent = new Intent(context, SingleItemView.class);
				// Pass all data rank
				intent.putExtra("name", resultp.get(CurricullumFragment.NAME));
				// Pass all data country
				intent.putExtra("description", resultp.get(CurricullumFragment.DESCRIPTION));
				// Pass all data population

				// Pass all data flag
				intent.putExtra("img", resultp.get(CurricullumFragment.IMAGE));
				// Start SingleItemView Class
				context.startActivity(intent);*/

			}
		});
		return itemView;
	}
}

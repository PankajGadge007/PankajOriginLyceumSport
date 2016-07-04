package com.example.admin.lyceumsport;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    String mId;
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();
    ImageLoader imageLoaderNew = AppController.getInstance().getImageLoader();

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arraylist, String userId) {
        this.context = context;
        data = arraylist;
        mId = userId;
//		imageLoader = new ImageLoader(context);
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
        TextView name;
        TextView description;
        NetworkImageView image;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.cardlistview, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        name = (TextView) itemView.findViewById(R.id.textView);
        description = (TextView) itemView.findViewById(R.id.textView1);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        // Locate the ImageView in listview_item.xml
        image = (NetworkImageView) itemView.findViewById(R.id.imageView);

        // Capture position and set results to the TextViews
        name.setText(resultp.get(CurricullumFragment.NAME));
        description.setText(resultp.get(CurricullumFragment.DESCRIPTION));
//        Created by pankaj
        image.setImageUrl(resultp.get(CurricullumFragment.IMAGE), imageLoaderNew);
//		imageLoader.DisplayImage(resultp.get(CurricullumFragment.IMAGE), image);


        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data Id
                intent.putExtra("id", mId);
                // Pass all data rank
                intent.putExtra("name", resultp.get(CurricullumFragment.NAME));
                // Pass all data country
                intent.putExtra("description", resultp.get(CurricullumFragment.DESCRIPTION));
                // Pass all data population
                // Pass all data flag
                intent.putExtra("img", resultp.get(CurricullumFragment.IMAGE));
                // Start SingleItemView Class
                context.startActivity(intent);
            }
        });
        return itemView;
    }
}

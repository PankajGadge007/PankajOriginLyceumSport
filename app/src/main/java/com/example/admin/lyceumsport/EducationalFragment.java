package com.example.admin.lyceumsport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Ratan on 7/29/2015.
 */
public class EducationalFragment extends Activity {
    String id;

   String[] itemname ={
            "Progress Report",
         //   "Assesment Report",
          //  "Session Completion Report",
            "Equipment Report",
          //  "Talent Identification Report",

    };
    String[] des={
            "To submit progress report for sports education center",
          //  "To submit assesment report for sports education center",
          //  "To view session completion report for sports education center",
            "To submit equipment report for sports education center",
          //  "To submit talent identification report for sports education center"
    };

    Integer[] imgid={
            R.drawable.progress,
            //R.drawable.assesment,
            R.drawable.equipment,
           /* R.drawable.back,
            R.drawable.back,*/

    };
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.education);

       Bundle extras=getIntent().getExtras();
       id=extras.getString("id");


       CustomAdapter adapter=new CustomAdapter(EducationalFragment.this, itemname,des, imgid);
       ListView  list=(ListView)findViewById(R.id.list);
       list.setAdapter(adapter);


       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id1) {
                int itm = parent.getPositionForView(view);
                switch(itm) {

                    case 0:
                        Intent myIntent = new Intent(EducationalFragment.this, ProgressReport.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("id" ,id);
                        myIntent.putExtras(mBundle);
                        startActivity(myIntent);
                        break;
                  /*  case 1:
                        Intent myIntent1 = new Intent(getActivity(), AssesmentReport.class);
                        startActivity(myIntent1);
                        break;*/
                 /*   case 2:
                        Intent myIntent2 = new Intent(getActivity(), SessionReport.class);
                        startActivity(myIntent2);
                        break;*/
                    case 1:
                        Intent myIntent3 = new Intent(EducationalFragment.this, EquipmentReport.class);
                        Bundle mBundle1 = new Bundle();
                        mBundle1.putString("id" ,id);
                        myIntent3.putExtras(mBundle1);
                        startActivity(myIntent3);
                        break;
                  /*  case 4:
                        Intent myIntent4 = new Intent(getActivity(), TalentReport.class);
                        startActivity(myIntent4);
                        break;*/

                }
            }
        });



    }


}

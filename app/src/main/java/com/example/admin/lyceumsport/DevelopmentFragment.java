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

public class DevelopmentFragment extends Activity {

        String id;

    String[] itemname ={
            "Students Registration",
           // "Fee Deposite",
          //  "Fee Pending",
           // "Student Attendance",
           // "Assesment Report",
            "Equipment Report",
           // "Talent Identification Report",
           // "Event Participation & Performance Report"
    };
    String[] des={
            "To register new Student for registration",
           // "To deposit collected fee",
          //  "To view pending fee list",
           // "To mark attendance of student for tracking",
          //  "To submit assesment report for sports education center",
            "To submit equipment report for sports education center",
            //"To submit talent identification report for sports education center",
           // "To submit event participation & performance report for sports education center"
    };

    Integer[] imgid={
            R.drawable.student,
           // R.drawable.feedeposite,
           // R.drawable.back,
          //  R.drawable.studentatten,
            R.drawable.equipment,
           /* R.drawable.back,
            R.drawable.back,
            R.drawable.back,*/
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.develop);

        Bundle extras=getIntent().getExtras();
        id=extras.getString("id");


        CustomAdapter adapter=new CustomAdapter(DevelopmentFragment.this, itemname,des, imgid);
        ListView  list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id1) {
                int itm = parent.getPositionForView(view);
                switch(itm) {

                    case 0:
                        Intent myIntent = new Intent(DevelopmentFragment.this, StudentRegistration.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("id" ,id);
                        myIntent.putExtras(mBundle);
                        startActivity(myIntent);
                        break;
                  /*  case 1:
                        Intent myIntent3 = new Intent(DevelopmentFragment.this, FeeDeposite.class);
                        startActivity(myIntent3);
                        break;
                    case 2:
                        Intent myIntent2 = new Intent(DevelopmentFragment.this, FeePending.class);
                        startActivity(myIntent2);
                        break;
                    case 3:
                        Intent myIntent4 = new Intent(DevelopmentFragment.this, StudentAttendance.class);
                        startActivity(myIntent4);
                        break;*/
                  /*  case 5:
                        Intent myIntent5 = new Intent(getActivity(), AssesmentReport.class);
                        startActivity(myIntent5);
                        break;*/
                    case 1:
                        Intent myIntent6 = new Intent(DevelopmentFragment.this, EquipmentReport.class);
                        Bundle mBundle1 = new Bundle();
                        mBundle1.putString("id" ,id);
                        myIntent6.putExtras(mBundle1);
                        startActivity(myIntent6);
                        break;
                  /*  case 7:
                        Intent myIntent7 = new Intent(getActivity(), TalentReport.class);
                        startActivity(myIntent7);
                        break;
                    case 8:
                        Intent myIntent8 = new Intent(getActivity(), PerformanceReport.class);
                        startActivity(myIntent8);
                        break;*/
                }
            }
        });


    }


}


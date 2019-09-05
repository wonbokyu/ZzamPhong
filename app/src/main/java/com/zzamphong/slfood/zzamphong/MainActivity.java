package com.zzamphong.slfood.zzamphong;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.zzamphong.slfood.zzamphong.R.id.edittext_input_cash_date_main;
import static com.zzamphong.slfood.zzamphong.R.id.edittext_main_output_cash_date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public TableLayout mTableLayout;
    private DatabaseReference mFirebaseDatabaseRef_InputCash;
    private DatabaseReference mFirebaseDatabaseRef_OutputCash;
    private FirebaseDatabase mFirebaseInstance;
    private  ArrayList<InputCash> mInputCashList;
    private  ArrayList<InputCash> mOutputCashList;


    private MonthYearPicker myp;
    EditText mEdittext_input_cash_date;
    int mYear,mMonth,mDay;


    private MonthYearPicker myp_output;
    EditText medittext_main_output_cash_date;
    int moutputYear,moutputMonth,moutputDay;

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.button_input_cash_date_main:
                    show(view);
                    break;
                case R.id.button_main_input_cash_search:
                    search();
                    break;

                case R.id.button_main_ouput_date:
                    show_output(view);
                    break;
                case R.id.button_main_output_cash_search:
                    search_output();
                    break;

            }
        }
    }
    public void search(){
        getInputCashFromDB();

    }

    public void search_output(){
        getOutputCashFromDB();

    }
    public void show(View view) {
        myp.show();
    }
    public void show_output(View view) {
        myp_output.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initResource();
        getInputCashFromDB();
        getOutputCashFromDB();

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        myp = new MonthYearPicker(this);
        myp.build(new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //textView1.setText(myp.getSelectedMonthName() + " >> " + myp.getSelectedYear());
                //Toast.makeText(getApplicationContext(),myp.getSelectedMonthName() + " >> " + myp.getSelectedYear(),Toast.LENGTH_SHORT).show();
                mYear = myp.getSelectedYear();
                mMonth = myp.getSelectedMonth()+1;

                mEdittext_input_cash_date.setText(String.format("%d년 %d월",mYear,mMonth));
            }
        }, null);


        myp_output = new MonthYearPicker(this);
        myp_output.build(new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //textView1.setText(myp.getSelectedMonthName() + " >> " + myp.getSelectedYear());
                //Toast.makeText(getApplicationContext(),myp.getSelectedMonthName() + " >> " + myp.getSelectedYear(),Toast.LENGTH_SHORT).show();
                moutputYear = myp_output.getSelectedYear();
                moutputMonth = myp_output.getSelectedMonth()+1;
                medittext_main_output_cash_date.setText(String.format("%d년 %d월",moutputYear,moutputMonth));
            }
        }, null);

        /////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    public void upDateData(){
        display_Input_cash();
        display_Output_cash();
    }
    public void initResource(){
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabaseRef_InputCash = mFirebaseInstance.getReference("subsidiary").child("input_cash");
        mFirebaseDatabaseRef_OutputCash = mFirebaseInstance.getReference("subsidiary").child("_cash");
        mInputCashList = new ArrayList<InputCash>();
        mOutputCashList = new ArrayList<InputCash>();

        BtnOnClickListener onClickListener = new BtnOnClickListener() ;
        Button button_input_cash_date_main =(Button) findViewById(R.id.button_input_cash_date_main);
        button_input_cash_date_main.setOnClickListener(onClickListener);
        Button button_main_input_cash_search =(Button) findViewById(R.id.button_main_input_cash_search);
        button_main_input_cash_search.setOnClickListener(onClickListener);


        Button button_main_ouput_date =(Button) findViewById(R.id.button_main_ouput_date);
        button_main_ouput_date.setOnClickListener(onClickListener);

        Button button_main_output_cash_search =(Button) findViewById(R.id.button_main_output_cash_search);
        button_main_output_cash_search.setOnClickListener(onClickListener);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        mYear = cal.get ( cal.YEAR );
        mMonth = cal.get ( cal.MONTH ) + 1 ;
        mDay = cal.get ( cal.DATE ) ;

        mEdittext_input_cash_date = (EditText)findViewById(edittext_input_cash_date_main);
        mEdittext_input_cash_date.setText(mYear + "년" + mMonth + "월") ;


        moutputYear = cal.get ( cal.YEAR );
        moutputMonth = cal.get ( cal.MONTH ) + 1 ;
        moutputDay = cal.get ( cal.DATE ) ;

        medittext_main_output_cash_date = (EditText)findViewById(edittext_main_output_cash_date);
        medittext_main_output_cash_date.setText(moutputYear + "년" + moutputMonth + "월") ;

    }
    public void getInputCashFromDB() {


        //Query query = mFirebaseDatabaseRef_InputCash.orderByChild("date").startAt(mYear+mMonth+"1").endAt(mYear+mMonth+"31");
        //Query query = mFirebaseDatabaseRef_InputCash.orderByChild("date").startAt(keyStartDate).endAt(keyEndDate);
        Query query = mFirebaseDatabaseRef_InputCash;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String keyStartDate ;
                String keyEndDate ;
                String keyMonth ;

                keyStartDate = Integer.toString(mYear)  + Integer.toString(mMonth) +"1" ;
                keyEndDate = Integer.toString(mYear)  + Integer.toString(mMonth) +"31" ;
                keyMonth = Integer.toString(mYear)  + Integer.toString(mMonth);

                Toast.makeText(getApplicationContext(),"현금 입금 내역을 불어왔습니다.(" + keyStartDate + "~" + keyEndDate  +")",Toast.LENGTH_SHORT).show();


                mInputCashList.clear();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    //  Toast.makeText(getApplicationContext(),dsp.getKey(),Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getApplicationContext(),String.valueOf(dsp.child("user").getValue()),Toast.LENGTH_SHORT).show();
                    if(String.valueOf(dsp.child("user").getValue()) != null && String.valueOf(dsp.child("amount").getValue())  != null )
                    {
                        if(String.valueOf(dsp.child("date").getValue()).contains(keyMonth))
                        {// InputCash inputCash = dsp.getValue(InputCash.class);
                            InputCash inputCash =new InputCash();
                            inputCash.setDate(String.valueOf(dsp.child("date").getValue()));
                            inputCash.setUser(String.valueOf(dsp.child("user").getValue()));
                            inputCash.setAmount(String.valueOf(dsp.child("amount").getValue()));
                            inputCash.setRemark(String.valueOf(dsp.child("remark").getValue()));
                            mInputCashList.add(inputCash); //add result into array list

                        }

                    }
                }
                upDateData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


        /*
        // User data change listener
        mFirebaseDatabaseRef_InputCash.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Toast.makeText(getApplicationContext(),"현금 입금 내역을 불러왔습니다.",Toast.LENGTH_SHORT).show();
                mInputCashList.clear();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                  //  Toast.makeText(getApplicationContext(),dsp.getKey(),Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(getApplicationContext(),String.valueOf(dsp.child("user").getValue()),Toast.LENGTH_SHORT).show();
                    if(String.valueOf(dsp.child("user").getValue()) != null && String.valueOf(dsp.child("amount").getValue())  != null )
                    {
                       // InputCash inputCash = dsp.getValue(InputCash.class);
                        InputCash inputCash =new InputCash();
                        inputCash.setDate(String.valueOf(dsp.child("date").getValue()));
                        inputCash.setUser(String.valueOf(dsp.child("user").getValue()));
                        inputCash.setAmount(String.valueOf(dsp.child("amount").getValue()));
                        inputCash.setRemark(String.valueOf(dsp.child("remark").getValue()));
                        mInputCashList.add(inputCash); //add result into array list
                    }
                }
                upDateData();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });

        */

    }
    public void getOutputCashFromDB() {
        // User data change listener
        mFirebaseDatabaseRef_OutputCash.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String keyStartDate ;
                String keyEndDate ;
                String keyMonth ;

                keyStartDate = Integer.toString(moutputYear)  + Integer.toString(moutputMonth) +"1" ;
                keyEndDate = Integer.toString(moutputYear)  + Integer.toString(moutputMonth) +"31" ;
                keyMonth = Integer.toString(moutputYear)  + Integer.toString(moutputMonth);

                Toast.makeText(getApplicationContext(),"현금 지출 내역을 불어왔습니다.(" + keyStartDate + "~" + keyEndDate  +")",Toast.LENGTH_SHORT).show();

                mOutputCashList.clear();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    //  Toast.makeText(getApplicationContext(),dsp.getKey(),Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getApplicationContext(),String.valueOf(dsp.child("user").getValue()),Toast.LENGTH_SHORT).show();
                    if(String.valueOf(dsp.child("user").getValue()) != null && String.valueOf(dsp.child("amount").getValue())  != null )
                    {
                        if(String.valueOf(dsp.child("date").getValue()).contains(keyMonth))
                        {
                            // InputCash inputCash = dsp.getValue(InputCash.class);
                            InputCash inputCash =new InputCash();
                            inputCash.setDate(String.valueOf(dsp.child("date").getValue()));
                            inputCash.setUser(String.valueOf(dsp.child("user").getValue()));
                            inputCash.setAmount(String.valueOf(dsp.child("amount").getValue()));
                            inputCash.setRemark(String.valueOf(dsp.child("remark").getValue()));
                            mOutputCashList.add(inputCash); //add result into array list

                        }
                    }

                }
                upDateData();


            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_input_cash) {
            // Start Input cash activity
            Intent intent = new Intent(getApplicationContext(),InputCashActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_output_cash) {
            // Start Input cash activity
            Intent intent = new Intent(getApplicationContext(),OutputCashActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void display_Input_cash(){
        int sum = 0 ;
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout_input_cash);
        tl.removeAllViews();
        /////////////////////////////////////////
        TableRow tr_head = new TableRow(this);
        //tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);

        TableRow.LayoutParams tr_head_params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        tr_head.setLayoutParams(tr_head_params);

        /////////////////////////////////////////
        TextView label_date = new TextView(this);
        //label_date.setId(20);
        label_date.setText("날짜");
        label_date.setTextColor(Color.WHITE);
        label_date.setPadding(5, 5, 5, 5);
        tr_head.addView(label_date);// add the column to the table row here

        TextView label_amount = new TextView(this);
       //label_amount.setId(21);// define id that must be unique
        label_amount.setText("금액"); // set the text for the header
        label_amount.setTextColor(Color.WHITE); // set the color
        label_amount.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_amount); // add the column to the table row here


        TextView label_remark = new TextView(this);
        //label_weight_kg.setId(21);// define id that must be unique
        label_remark.setText("비고"); // set the text for the header
        label_remark.setTextColor(Color.WHITE); // set the color
        label_remark.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_remark); // add the column to the table row here

        //////////////////////////////////////////
        tl.addView(tr_head, tl.getLayoutParams());

        Iterator<InputCash> iter = mInputCashList.iterator();
        while (iter.hasNext()) {

            InputCash inputCash =  iter.next() ;
            String date = inputCash.date;// get the first variable
            String amount = inputCash.amount;// get the second variable

            sum = sum +  Integer.parseInt(amount);

            String remark = inputCash.remark;// get the second variable

            // Create the table row
            TableRow tr = new TableRow(this);
            tr.setBackgroundColor(Color.GRAY);

            TableRow.LayoutParams tr_params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tr.setLayoutParams(tr_params);

            //Create two columns to add as table data
            // Create a TextView to add date
            TextView labelDATE = new TextView(this);
            labelDATE.setText(date);
            labelDATE.setPadding(2, 0, 5, 0);
            labelDATE.setTextColor(Color.WHITE);
            tr.addView(labelDATE);

            TextView labelAMOUNT = new TextView(this);
            labelAMOUNT.setText(amount);
            labelAMOUNT.setTextColor(Color.WHITE);
            tr.addView(labelAMOUNT);

            TextView labelREMARK = new TextView(this);
            labelREMARK.setText(remark);
            labelREMARK.setTextColor(Color.WHITE);
            tr.addView(labelREMARK);

// finally add this to the table row
            tl.addView(tr, tl.getLayoutParams());
        }

        // summury
        /////////////////////////////////////////
        TableRow tr_sum_head = new TableRow(this);
        tr_sum_head.setBackgroundColor(Color.BLUE);

        TableRow.LayoutParams tr_sum_head_params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        tr_sum_head.setLayoutParams(tr_sum_head_params);

        TextView label_sum = new TextView(this);
        //label_date.setId(20);
        label_sum.setText("합계");
        label_sum.setTextColor(Color.WHITE);
        label_sum.setPadding(5, 5, 5, 5);
        tr_sum_head.addView(label_sum);// add the column to the table row here

        TextView label_sum_final = new TextView(this);
        //label_amount.setId(21);// define id that must be unique
        label_sum_final.setText(String.valueOf(sum)); // set the text for the header
        label_sum_final.setTextColor(Color.WHITE); // set the color
        label_sum_final.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_sum_head.addView(label_sum_final); // add the column to the table row here
        //////////////////////////////////////////
        tl.addView(tr_sum_head, tl.getLayoutParams());


    }

    public void display_Output_cash(){

        int sum = 0;
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout_output_cash);
        tl.removeAllViews();
        /////////////////////////////////////////
        TableRow tr_head = new TableRow(this);
        //tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);

        TableRow.LayoutParams tr_head_params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        tr_head.setLayoutParams(tr_head_params);

        /////////////////////////////////////////
        TextView label_date = new TextView(this);
        //label_date.setId(20);
        label_date.setText("날짜");
        label_date.setTextColor(Color.WHITE);
        label_date.setPadding(5, 5, 5, 5);
        tr_head.addView(label_date);// add the column to the table row here

        TextView label_amount = new TextView(this);
        //label_amount.setId(21);// define id that must be unique
        label_amount.setText("금액"); // set the text for the header
        label_amount.setTextColor(Color.WHITE); // set the color
        label_amount.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_amount); // add the column to the table row here


        TextView label_remark = new TextView(this);
        //label_weight_kg.setId(21);// define id that must be unique
        label_remark.setText("비고"); // set the text for the header
        label_remark.setTextColor(Color.WHITE); // set the color
        label_remark.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_remark); // add the column to the table row here

        //////////////////////////////////////////
        tl.addView(tr_head, tl.getLayoutParams());

        Iterator<InputCash> iter = mOutputCashList.iterator();
        while (iter.hasNext()) {

            InputCash inputCash =  iter.next() ;
            String date = inputCash.date;// get the first variable
            String amount = inputCash.amount;// get the second variable
            sum = sum + Integer.parseInt(amount);

            String remark = inputCash.remark;// get the second variable

            // Create the table row
            TableRow tr = new TableRow(this);
            tr.setBackgroundColor(Color.GRAY);

            TableRow.LayoutParams tr_params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tr.setLayoutParams(tr_params);

            //Create two columns to add as table data
            // Create a TextView to add date
            TextView labelDATE = new TextView(this);
            labelDATE.setText(date);
            labelDATE.setPadding(2, 0, 5, 0);
            labelDATE.setTextColor(Color.WHITE);
            tr.addView(labelDATE);

            TextView labelAMOUNT = new TextView(this);
            labelAMOUNT.setText(amount);
            labelAMOUNT.setTextColor(Color.WHITE);
            tr.addView(labelAMOUNT);

            TextView labelREMARK = new TextView(this);
            labelREMARK.setText(remark);
            labelREMARK.setTextColor(Color.WHITE);
            tr.addView(labelREMARK);

            // finally add this to the table row
            tl.addView(tr, tl.getLayoutParams());
        }


        // summury
        /////////////////////////////////////////
        TableRow tr_sum_head = new TableRow(this);
        tr_sum_head.setBackgroundColor(Color.BLUE);

        TableRow.LayoutParams tr_sum_head_params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        tr_sum_head.setLayoutParams(tr_sum_head_params);

        TextView label_sum = new TextView(this);
        //label_date.setId(20);
        label_sum.setText("합계");
        label_sum.setTextColor(Color.WHITE);
        label_sum.setPadding(5, 5, 5, 5);
        tr_sum_head.addView(label_sum);// add the column to the table row here

        TextView label_sum_final = new TextView(this);
        //label_amount.setId(21);// define id that must be unique
        label_sum_final.setText(String.valueOf(sum)); // set the text for the header
        label_sum_final.setTextColor(Color.WHITE); // set the color
        label_sum_final.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_sum_head.addView(label_sum_final); // add the column to the table row here
        //////////////////////////////////////////
        tl.addView(tr_sum_head, tl.getLayoutParams());

    }

}

package com.zzamphong.slfood.zzamphong;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.zzamphong.slfood.zzamphong.R.id.button_output_cash_date;
import static com.zzamphong.slfood.zzamphong.R.id.button_output_cash_save;
import static com.zzamphong.slfood.zzamphong.R.id.edittext_output_cash_amount;
import static com.zzamphong.slfood.zzamphong.R.id.edittext_output_cash_date;
import static com.zzamphong.slfood.zzamphong.R.id.edittext_output_cash_etc;

public class OutputCashActivity extends AppCompatActivity implements Button.OnClickListener {
    int mYear,mMonth,mDay;
    Button mButton_output_cash_date;
    EditText mEdittext_output_cash_date;
    EditText mEdittext_output_cash_amount;
    EditText mEdittext_output_cash_etc;
    Button mButton_output_cash_save;


    private DatabaseReference mFirebaseDatabaseRef;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_cash);

        init();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case button_output_cash_date :
                new DatePickerDialog(OutputCashActivity.this,mDateSetListener,mYear,mMonth,mDay).show();
                break;

            case button_output_cash_save:
                saveDataToDatabase();
                finish();
                break;
        }

    }
    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth){
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                mEdittext_output_cash_date.setText(String.format("%d%d%d",mYear,mMonth+1,mDay));
        }
    };
    protected void init(){

        java.util.Calendar cal = java.util.Calendar.getInstance();
        mYear = cal.get ( cal.YEAR );
        mMonth = cal.get ( cal.MONTH ) ;
        mDay = cal.get ( cal.DATE ) ;

        //String create_date = sYear + sMonth + sDay;
        mButton_output_cash_date = (Button)findViewById(button_output_cash_date);
        mButton_output_cash_date.setOnClickListener(this);

        mEdittext_output_cash_date = (EditText)findViewById(edittext_output_cash_date);
        mEdittext_output_cash_amount = (EditText)findViewById(edittext_output_cash_amount);
        mEdittext_output_cash_etc = (EditText)findViewById(edittext_output_cash_etc);

        mButton_output_cash_save = (Button)findViewById(button_output_cash_save);
        mButton_output_cash_save.setOnClickListener(this);

        // init database
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabaseRef = mFirebaseInstance.getReference("subsidiary").child("output_cash");

    }

    protected void saveDataToDatabase(){


        java.util.Calendar cal = java.util.Calendar.getInstance();
        int tempMinuteSecond = cal.get ( cal.MINUTE ) + cal.get ( cal.SECOND );
        String dateKey = mEdittext_output_cash_date.getText().toString() + String.valueOf(tempMinuteSecond) ;
        String dateValue = mEdittext_output_cash_date.getText().toString() ;

        String amount = mEdittext_output_cash_amount.getText().toString();
        String remark = mEdittext_output_cash_etc.getText().toString();

        OutputCash outputCash = new OutputCash(dateValue,amount,remark,"Mobile");
        mFirebaseDatabaseRef.child(dateKey).setValue(outputCash) ;

        //createTest();
    }
}

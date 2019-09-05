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

import static com.zzamphong.slfood.zzamphong.R.id.button_input_cash_date;
import static com.zzamphong.slfood.zzamphong.R.id.button_input_cash_save;
import static com.zzamphong.slfood.zzamphong.R.id.edittext_input_cash_amount;
import static com.zzamphong.slfood.zzamphong.R.id.edittext_input_cash_date;
import static com.zzamphong.slfood.zzamphong.R.id.edittext_input_cash_etc;

public class InputCashActivity extends AppCompatActivity implements Button.OnClickListener {
    int mYear,mMonth,mDay;
    Button mButton_input_cash_date;
    EditText mEdittext_input_cash_date;
    EditText mEdittext_input_cash_amount;
    EditText mEdittext_input_cash_etc;
    Button mButton_input_cash_save;


    private DatabaseReference mFirebaseDatabaseRef;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_cash);

        init();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case button_input_cash_date :
                new DatePickerDialog(InputCashActivity.this,mDateSetListener,mYear,mMonth,mDay).show();
                break;

            case button_input_cash_save:
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
                mEdittext_input_cash_date.setText(String.format("%d%d%d",mYear,mMonth+1,mDay));
        }
    };
    protected void init(){

        java.util.Calendar cal = java.util.Calendar.getInstance();
        mYear = cal.get ( cal.YEAR );
        mMonth = cal.get ( cal.MONTH ) ;
        mDay = cal.get ( cal.DATE ) ;

        //String create_date = sYear + sMonth + sDay;
        mButton_input_cash_date = (Button)findViewById(button_input_cash_date);
        mButton_input_cash_date.setOnClickListener(this);

        mEdittext_input_cash_date = (EditText)findViewById(edittext_input_cash_date);
        mEdittext_input_cash_amount = (EditText)findViewById(edittext_input_cash_amount);
        mEdittext_input_cash_etc = (EditText)findViewById(edittext_input_cash_etc);

        mButton_input_cash_save = (Button)findViewById(button_input_cash_save);
        mButton_input_cash_save.setOnClickListener(this);

        // init database
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabaseRef = mFirebaseInstance.getReference("subsidiary").child("input_cash");

    }

    protected void saveDataToDatabase(){
        String date = mEdittext_input_cash_date.getText().toString();
        String amount = mEdittext_input_cash_amount.getText().toString();
        String remark = mEdittext_input_cash_etc.getText().toString();

        InputCash inputCash = new InputCash(date,amount,remark,"Mobile");
        mFirebaseDatabaseRef.child(date).setValue(inputCash) ;

        //createTest();
    }
}

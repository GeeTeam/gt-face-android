package com.geetest.slientidnumber;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NumberActivity extends AppCompatActivity {

    private EditText editTextName, editTextIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_slient_idnumber);
        editTextIdCard = (EditText) findViewById(R.id.et_idcard);
        editTextName = (EditText) findViewById(R.id.et_name);
        findViewById(R.id.btn_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextIdCard.getText()) || TextUtils.isEmpty(editTextName.getText())) {
                    Toast.makeText(getApplicationContext(), "姓名或身份证号未输入", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
                    intent.putExtra("name", editTextName.getText().toString());
                    intent.putExtra("idcard", editTextIdCard.getText().toString());
                    startActivityForResult(intent,0);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Intent intent = new Intent(data);
                    intent.setClass(NumberActivity.this, ResultActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

}


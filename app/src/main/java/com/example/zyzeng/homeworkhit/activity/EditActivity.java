package com.example.zyzeng.homeworkhit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zyzeng.homeworkhit.MyDB;
import com.example.zyzeng.homeworkhit.R;

import static com.example.zyzeng.homeworkhit.MyDB.COLUMN_NAME_CONTENT;
import static com.example.zyzeng.homeworkhit.MyDB.COLUMN_NAME_DATE;
import static com.example.zyzeng.homeworkhit.MyDB.COLUMN_NAME_SUBJECT;
import static com.example.zyzeng.homeworkhit.MyDB.TABLE_NAME_HW;

public class EditActivity extends AppCompatActivity {
    private Button saveButton;
    private Button cancelButton;
    private EditText editSub;
    private EditText editCont;
    private Button timeBtn;
    private TextView timeShowText;
    private int year;
    private int mouth;
    private int day;
    private MyDB DB;
    private SQLiteDatabase dataBase;
    private int id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        saveButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);
        editSub = findViewById(R.id.subject);
        editCont = findViewById(R.id.content);
        timeBtn = findViewById(R.id.timeBtn);
        timeShowText = findViewById(R.id.timeShow);
        DB = new MyDB(this);
        dataBase = DB.getReadableDatabase();
        
        //选择日期
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(EditActivity.this);
                localBuilder.setTitle("选择时间").setIcon(R.mipmap.ic_launcher);
                //
                final LinearLayout layout_alert= (LinearLayout) getLayoutInflater().inflate(R.layout.timepicker, null);
                localBuilder.setView(layout_alert);
                localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                    {
                        DatePicker datepicker1= layout_alert.findViewById(R.id.datepicker);
                        year=datepicker1.getYear();
                        mouth=datepicker1.getMonth()+1;
                        day=datepicker1.getDayOfMonth();
                        timeShowText.setText(year+"-"+mouth+"-"+day); //  获取时间
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                    {

                    }
                }).create().show();
            }
        });
        //选择日期结束

        //保存按钮监听
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取edit里的值
                String sub = editSub.getText().toString();
                String con = editCont.getText().toString();
                String date = String.valueOf(year)+"年"+String.valueOf(mouth)+"月"+String.valueOf(day)+"日";
                
                //通过select count(*)语句获取表中的行数。执行添加和更新语句
                String sql_count = "SELECT COUNT(*) FROM hw";
                SQLiteStatement statement = dataBase.compileStatement(sql_count);
                long count = statement.simpleQueryForLong();
                String sqlAdd = "insert into "+TABLE_NAME_HW+" values ("+count+","+"'"+
                        sub+"'"+","+"'"+con+"'"+","+"'"+date+"'"+")";
                String update = "update "+TABLE_NAME_HW+" set "+COLUMN_NAME_SUBJECT+","+COLUMN_NAME_DATE+","+COLUMN_NAME_CONTENT+
                        " where _id="+id;
                dataBase.execSQL(sqlAdd);
                
                //resultCode配合fragment中的requestCode，finish返回后用来刷新listview界面。
                Intent data = new Intent();       
                setResult(2, data); 
                finish();
            }
        });
        
        //取消按钮
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

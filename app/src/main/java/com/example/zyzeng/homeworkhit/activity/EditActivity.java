package com.example.zyzeng.homeworkhit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zyzeng.homeworkhit.database.MyDB;
import com.example.zyzeng.homeworkhit.R;

import static com.example.zyzeng.homeworkhit.database.MyDB.TABLE_NAME_HW;

public class EditActivity extends AppCompatActivity {
    public static int CLICK_WAY = 0;
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
    private Bundle bundle;
    
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
                
                //查询取得最后一条数据的id，并加1为新增的条目赋得id值。
                Cursor cursor = dataBase.query(TABLE_NAME_HW,null,null,null,null,null,null);
                if (cursor.moveToLast()){
                    String idstr = cursor.getString(cursor.getColumnIndex("_id"));
                    id = Integer.valueOf(idstr)+1;
                }
//                //之前这样做会导致删除了一条中间数据后再添加数据导致id冲突。
//                String sql_count = "SELECT COUNT(*) FROM hw";
//                SQLiteStatement statement = dataBase.compileStatement(sql_count);
//                long count = statement.simpleQueryForLong();
                if(CLICK_WAY == 0){
                    String sqlAdd = "insert into "+TABLE_NAME_HW+" values ("+id+","+"'"+
                            sub+"'"+","+"'"+con+"'"+","+"'"+date+"'"+")";
                    dataBase.execSQL(sqlAdd);
                }else if(CLICK_WAY == 1){
                    //更新数据时，date项不能取上面的，因为有可能不会修改日期而产生空的时间。
                    String update = "update hw set subject='"
                            + sub + "',date='"+ timeShowText.getText() +"',content='"+con+"' where subject='"+bundle.getString("subject")+"'";
//                    String update = "update hw set subject='"
//                            + sub + "',date='"+ day +"',content='"+con+"' where _id='"+String.valueOf(id)+"'";
                    dataBase.execSQL(update);
                }

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

        //判断intent是否来自listView的条目，是则将该item内的数据写入编辑界面
        Intent intent = getIntent();
        int from = intent.getIntExtra("from",0);
        if (from ==2){
            bundle = intent.getExtras();
            editCont.setText(bundle.getString("content"));
            editSub.setText(bundle.getString("subject"));
            timeShowText.setText(bundle.getString("day"));
        }
        //id = bundle.getInt("itemID");   //通过主键id取值会造成无法删除，因为主键不会自动更新
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3){

        }
    }
}

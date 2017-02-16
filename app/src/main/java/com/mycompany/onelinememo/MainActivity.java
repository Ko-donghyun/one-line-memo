package com.mycompany.onelinememo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {
    ArrayList<MyItem> Items;
    MyListAdapter Adapter;
    EditText memo_context;
    EditText sub_memo_context;
    TextView appname;
    TextView each_title;
    Button export_button;
    Button activity_main_insert_button;
    Button activity_main_insert_button2;
    ListView listview;
    ViewFlipper viewFlipper;
    final String dbName = "OneLIneMemo.db";
    final int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    int myposition;
    public InputMethodManager imm;
    Toast myToast = null;
    Spinner spinner;
    ArrayAdapter<CharSequence> aaspin;
    int mySelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        export_button = (Button)findViewById(R.id.export_button);
        activity_main_insert_button = (Button) findViewById(R.id.activity_main_insert_button);
        activity_main_insert_button2 = (Button) findViewById(R.id.activity_main_insert_button2);
        listview = (ListView) findViewById(R.id.activity_main_listview);
        each_title = (TextView) findViewById(R.id.activity_main_each_title);
        appname = (TextView) findViewById(R.id.question_tab_each_title);
        viewFlipper = (ViewFlipper) findViewById(R.id.activity_main_viewflipper);
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        spinner = (Spinner) findViewById(R.id.spinner);
        Items = new ArrayList<MyItem>();
        reload();
        aaspin = ArrayAdapter.createFromResource(this, R.array.spinner_menu, android.R.layout.simple_spinner_dropdown_item);
        aaspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aaspin);

        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //listview.setOnItemClickListener(myItemClickListener);
        listview.setOnItemClickListener(myItemClickListener);
//        listview.setOnItemLongClickListener(myItemLongClickListener);
        listview.setDivider(new ColorDrawable(Color.DKGRAY));
        listview.setDividerHeight(3);



        memo_context = (EditText) findViewById(R.id.activity_main_context);
        sub_memo_context = (EditText) findViewById(R.id.activity_main_sub_memo);
        memo_context.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {  }
            @Override
            public void afterTextChanged(Editable s) {
                String text = memo_context.getText().toString().toLowerCase(Locale.getDefault());
                Adapter.filter(text);
            }
        });
        memo_context.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && KeyEvent.ACTION_DOWN == event.getAction()) {
                    String title = memo_context.getText().toString();
                    if ( title.length() == 0) {
                        return true;
                    }
                    else {
                        if ( viewFlipper.getDisplayedChild() == 0 ) {
                            insert(title);
                            memo_context.setText(null);
                            imm.hideSoftInputFromWindow(memo_context.getWindowToken(),0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //초기화 작업
                if (viewFlipper.getDisplayedChild() == 1) {
                    if ( position == 1 ) {
                        spinner.setSelection(0);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("원하시는 색을 선택하세요")
                                .setSingleChoiceItems(R.array.color_select, mySelect, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mySelect = which;
                                    }
                                })
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        update(mySelect);
                                        switch (mySelect) {
                                            case 0:
                                                each_title.setTextColor(Color.parseColor("#FFFFFF"));
                                                break;
                                            case 1:
                                                each_title.setTextColor(Color.parseColor("#FF0000"));
                                                break;
                                            case 2:
                                                each_title.setTextColor(Color.parseColor("#FFFF00"));
                                                break;
                                            case 3:
                                                each_title.setTextColor(Color.parseColor("#00FF00"));
                                                break;
                                            case 4:
                                                each_title.setTextColor(Color.parseColor("#0000FF"));
                                                break;
                                            case 5:
                                                each_title.setTextColor(Color.parseColor("#FF00FF"));
                                                break;
                                        }
                                    }
                                })
                                .setNegativeButton("취소", null)
                                .show();

                    }
                    else if ( position == 2 ) {
                        //delete();
                        spinner.setSelection(0);
                        //Toast.makeText(MainActivity.this, aaspin.getItem(position) + "을 좋아합니다.", Toast.LENGTH_SHORT).show();

                        sub_memo_context.setFocusableInTouchMode(true);
                        sub_memo_context.setFocusable(true);
                        sub_memo_context.setClickable(true);
                    }
                    else if ( position == 3 ) {
                        delete();
                        //Toast.makeText(MainActivity.this, aaspin.getItem(position) + "을 좋아합니다.", Toast.LENGTH_SHORT).show();


                        spinner.setSelection(0);
                    }
                }
                //Toast.makeText(MainActivity.this, aaspin.getItem(position) + "을 좋아합니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sub_memo_context.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sub_memo_context.setFocusableInTouchMode(true);
                sub_memo_context.setFocusable(true);
                sub_memo_context.setClickable(true);
                imm.showSoftInput(sub_memo_context, InputMethodManager.SHOW_FORCED);
                return false;
            }
        });

        appname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewFlipper.getDisplayedChild() == 1) {
                    sub_memo_context.setText(null);
                    memo_context.setText(null);
                    memo_context.setVisibility(View.VISIBLE);
                    export_button.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    viewFlipper.showPrevious();
                }
                else {

                    Animation myAni = null;
                    myAni = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                    myAni.setDuration(500);
                    appname.startAnimation(myAni);

                }
            }
        });

        export_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_outerfile();
                if (myToast != null) {
                    myToast.cancel();
                }
                myToast = Toast.makeText(MainActivity.this, "파일로 내보냈습니다. 'OneLineMemo.txt' 파일을 확인하세요.",Toast.LENGTH_SHORT);
                myToast.show();

                Animation myAni = null;
                myAni = AnimationUtils.loadAnimation(MainActivity.this, R.anim.hyperspace_out);
                myAni.setDuration(500);
                export_button.startAnimation(myAni);

            }
        });

    }

    public void myOnclick(View v) {
        switch ( v.getId() ) {
            case R.id.activity_main_insert_button:

                String title = memo_context.getText().toString();
                if ( viewFlipper.getDisplayedChild() == 0 ) {
                    if ( title.length() != 0 ) {
                        Animation myAni = null;
                        myAni = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                        myAni.setDuration(800);
                        activity_main_insert_button.startAnimation(myAni);
                        insert(title);
                        memo_context.setText(null);
                        imm.hideSoftInputFromWindow(memo_context.getWindowToken(),0);
                    }
                }
                //Adapter.notifyDataSetChanged();
                break;
            case R.id.activity_main_insert_button2:
                if  ( viewFlipper.getDisplayedChild() == 1 ) {
                    Animation myAni = null;
                    myAni = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                    myAni.setDuration(800);
                    activity_main_insert_button2.startAnimation(myAni);
                    sub_memo_context = (EditText) findViewById(R.id.activity_main_sub_memo);
                    String sub_memo = sub_memo_context.getText().toString();
                    update(sub_memo);
                    imm.hideSoftInputFromWindow(sub_memo_context.getWindowToken(),0);
                    if (myToast != null) {
                        myToast.cancel();
                    }
                    sub_memo_context.setFocusableInTouchMode(false);
                    sub_memo_context.setFocusable(false);
                    sub_memo_context.setClickable(false);
                    myToast = Toast.makeText(MainActivity.this, "저장 되었습니다.",Toast.LENGTH_SHORT);
                    myToast.show();
                }
                //Adapter.notifyDataSetChanged();
                break;
        }
    }
    public void insert(String memo) {
        db = dbHelper.getWritableDatabase();
        Date now = new Date();
        SimpleDateFormat simpleD = new SimpleDateFormat(" - yyy.MM.dd HH:mm");
        String time = simpleD.format(now) ;

        String sql = String.format("INSERT INTO person VALUES(NULL, '%s', '%s', '%s', %d);", memo, "", time, 0);
        db.execSQL(sql);
        //MyItem myItem = new MyItem(memo, 1);
        //Items.add(myItem);
        dbHelper.close();
        reload();
    }
    public void update(String sub_memo) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String str1 = Items.get(myposition).Memo;
        String str2 = Items.get(myposition).Date;
        String str3 = Items.get(myposition).Time;
        int color = Items.get(myposition).Color;
        //Toast.makeText(MainActivity.this, str1 + "  "+ str2, Toast.LENGTH_SHORT ).show();

        String updateString[] = new String[4];
        updateString[0] = str1;
        updateString[1] = str2;
        updateString[2] = str3;
        updateString[3] = Integer.toString(color);

        //values.put("memo",title);
        values.put("date", sub_memo);
        db.update("person", values, "memo=? AND date=? AND time=? AND color=?", updateString);
        //Toast.makeText(MainActivity.this, str1 + "  "+ str2, Toast.LENGTH_SHORT ).show();

        dbHelper.close();
        reload();
    }
    public void update(int select_color) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String str1 = Items.get(myposition).Memo;
        String str2 = Items.get(myposition).Date;
        String str3 = Items.get(myposition).Time;
        int color = Items.get(myposition).Color;
        //Toast.makeText(MainActivity.this, str1 + "  "+ str2, Toast.LENGTH_SHORT ).show();

        String updateString[] = new String[4];
        updateString[0] = str1;
        updateString[1] = str2;
        updateString[2] = str3;
        updateString[3] = Integer.toString(color);

        //values.put("memo",title);
        values.put("color", select_color);
        db.update("person", values, "memo=? AND date=? AND time=? AND color=?", updateString);

        //Toast.makeText(MainActivity.this, str1 + "  "+ str2, Toast.LENGTH_SHORT ).show();

        dbHelper.close();
        reload();
    }

    public void delete() {
        db = dbHelper.getWritableDatabase();
        viewFlipper.showPrevious();
        memo_context.setVisibility(View.VISIBLE);
        export_button.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        String str1 = Items.get(myposition).Memo;
        String str2 = Items.get(myposition).Date;
        String str3 = Items.get(myposition).Time;
        int color = Items.get(myposition).Color;

        String deleteString[] = new String[4];
        deleteString[0] = str1;
        deleteString[1] = str2;
        deleteString[2] = str3;
        deleteString[3] = Integer.toString(color);


//        db.delete("person", "memo='" + str1 + "'AND date='" + str2 + "'AND time='" + str3 + "'AND color='" + color + "'", null);
        db.delete("person", "memo=? AND date=? AND time=? AND color=?", deleteString);

        dbHelper.close();
        reload();
    }

    public void reload() {
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM person", null);
        Items.clear();
        cursor.moveToLast();
        do {
           // Log.e("tag",  Integer.toString(cursor.getPosition()) );
            if ( cursor.getPosition() >= 0 ) {
                MyItem myItem = new MyItem(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                Items.add(myItem);
            }
        } while( cursor.moveToPrevious() );
        db.close();
        cursor.close();
        Adapter = new MyListAdapter(this, R.layout.listview_item, Items);
        listview.setAdapter(Adapter);
    }

/*
    AdapterView.OnItemClickListener myItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String message;
            message = "Select Item = " + Items.get(position).Memo;
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
*/

    AdapterView.OnItemClickListener myItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            each_title.setText(Items.get(position).Memo);
            sub_memo_context.setText(Items.get(position).Date);
            switch (Items.get(position).Color) {
                case 0:
                    each_title.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 1:
                    each_title.setTextColor(Color.parseColor("#FF0000"));
                    break;
                case 2:
                    each_title.setTextColor(Color.parseColor("#FFFF00"));
                    break;
                case 3:
                    each_title.setTextColor(Color.parseColor("#00FF00"));
                    break;
                case 4:
                    each_title.setTextColor(Color.parseColor("#0000FF"));
                    break;
                case 5:
                    each_title.setTextColor(Color.parseColor("#FF00FF"));
                    break;
            }
            spinner.setVisibility(View.VISIBLE);
            export_button.setVisibility(View.INVISIBLE);
            memo_context.setVisibility(View.INVISIBLE);
            myposition = position;
            sub_memo_context.setFocusableInTouchMode(false);
            sub_memo_context.setFocusable(false);
            sub_memo_context.setClickable(false);
            viewFlipper.showNext();
        }
    };

    AdapterView.OnItemLongClickListener myItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            each_title.setText(Items.get(position).Memo);
            sub_memo_context.setText(Items.get(position).Date);
            switch (Items.get(position).Color) {
                case 0:
                    each_title.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 1:
                    each_title.setTextColor(Color.parseColor("#FF0000"));
                    break;
                case 2:
                    each_title.setTextColor(Color.parseColor("#FFFF00"));
                    break;
                case 3:
                    each_title.setTextColor(Color.parseColor("#00FF00"));
                    break;
                case 4:
                    each_title.setTextColor(Color.parseColor("#0000FF"));
                    break;
                case 5:
                    each_title.setTextColor(Color.parseColor("#FF00FF"));
                    break;
            }
            spinner.setVisibility(View.VISIBLE);
            export_button.setVisibility(View.INVISIBLE);
            memo_context.setVisibility(View.INVISIBLE);
            myposition = position;
            sub_memo_context.setFocusableInTouchMode(false);
            sub_memo_context.setFocusable(false);
            sub_memo_context.setClickable(false);
            viewFlipper.showNext();
            return false;
        }
    };

    public void make_outerfile() {
        String myPath;
        String ext = Environment.getExternalStorageState();

        if ( ext.equals(Environment.MEDIA_MOUNTED)) {
            myPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            myPath = Environment.MEDIA_UNMOUNTED;
        }
        File dir = new File(myPath + "/OneLineMemo");
        dir.mkdir();
        File file = new File(myPath + "/OneLineMemo/OneLineMemo.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            String str = null;
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM person", null);
            cursor.moveToLast();
            do {
                // Log.e("tag",  Integer.toString(cursor.getPosition()) );
                if ( cursor.getPosition() >= 0 ) {
//                    MyItem myItem = new MyItem(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                    str = ( "메모 내용 : " + cursor.getString(1) + "\n세부메모 내용: " + cursor.getString(2) + "\n\n");
            //        str = "이 파일은 SD카드에 저장되어 있습니다.";

                    fos.write(str.getBytes());
                }
            } while( cursor.moveToPrevious() );
            db.close();
            cursor.close();

            fos.close();
        } catch (FileNotFoundException e) {
//            Toast.makeText(MainActivity.this,"111",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
//            Toast.makeText(MainActivity.this,"222",Toast.LENGTH_SHORT).show();

        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                if (viewFlipper.getDisplayedChild() == 1) {
                    sub_memo_context.setText(null);
                    memo_context.setText(null);
                    memo_context.setVisibility(View.VISIBLE);
                    export_button.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    viewFlipper.showPrevious();
                }
                else {
                    long tempTime = System.currentTimeMillis();
                    long intervalTime = tempTime - backPressedTime;

                    if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
                        super.onBackPressed();
                    }
                    else {
                          backPressedTime = tempTime;
                          Toast.makeText(MainActivity.this,"'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }
}

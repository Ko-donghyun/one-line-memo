package com.mycompany.onelinememo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by LG on 2015-07-13.
 */
// Adapter 클래스
public class MyListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    List<MyItem> mList = null;
    ArrayList<MyItem> mArrayList;
    int mlayout;

    public MyListAdapter(Context context, int layout, List<MyItem> list) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
        mArrayList = new ArrayList<MyItem>();
        mArrayList.addAll(list);
        mlayout = layout;
    }

    public int getCount() {
        return mArrayList.size();
    }

    public String getItem(int position) {
        return mArrayList.get(position).Memo;
    }
    public long getItemId(int position) {
        return position;
    }
    // 출력할 항목의 뷰를 생성
    public View getView(int position, View convertview, ViewGroup parent) {
        if ( convertview == null ) {
            convertview = mInflater.inflate(mlayout, parent, false);
        }
        TextView textView = (TextView) convertview.findViewById(R.id.memo_content);
        TextView textView2 = (TextView) convertview.findViewById(R.id.memo_content2);
        //Log.e("tag",mArrayList.get(position).Memo);
        textView.setText(mArrayList.get(position).Memo);
        textView2.setText(mArrayList.get(position).Time);
        switch (mArrayList.get(position).Color) {
            case 0:
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case 1:
                textView.setTextColor(Color.parseColor("#FF0000"));
                break;
            case 2:
                textView.setTextColor(Color.parseColor("#FFFF00"));
                break;
            case 3:
                textView.setTextColor(Color.parseColor("#00FF00"));
                break;
            case 4:
                textView.setTextColor(Color.parseColor("#0000FF"));
                break;
            case 5:
                textView.setTextColor(Color.parseColor("#FF00FF"));
                break;
        }

  /*      Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.hyperspace_out);
        animation.setDuration(500);
        convertview.startAnimation(animation);
        animation = null;
        */
        return convertview;
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mArrayList.clear();
        if (charText.length() == 0) {
            mArrayList.addAll(mList);
        }
        else
        {
            for (MyItem myItem : mList)
            {
                if (myItem.getMemo().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mArrayList.add(myItem);
                }
            }
        }
        notifyDataSetChanged();
    }

}
package com.example.hairsee;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hairsee.listClass.Hair;

import java.util.ArrayList;
import java.util.List;

public class Hairlist extends BaseAdapter {
    private static final String TAG = "SingleAdapter";

    private List<Hair> items=new ArrayList<>();

    public Hairlist() {
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("써니",R.drawable.common_google_signin_btn_icon_dark));
    }


    @Override
    public int getCount() { //최초에 화면의 갯수를 설정함
        Log.d(TAG, "getCount: ");
        return items.size();
    }

    @Override
    public Object getItem(int position) { //아이템이 클릭될 때 아이템의 데이터를 도출
        Log.d(TAG, "getItem: "+position);
        return items.get(position);
    }

    @Override
    public long getItemId(int position) { //필수 아님
        Log.d(TAG, "getItemId: "+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: "+position);
        //레이아웃 인플레이터로 인플레이터 객체 접근
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        //메모리에 아이템 하나 인플레이팅
        View itemView=inflater.inflate(R.layout.choiceitem,parent,false);
        //뷰 찾기
        TextView tv=itemView.findViewById(R.id.tv_title);
        ImageView imageView=itemView.findViewById(R.id.iv_img_resource);
        //뷰 교체
        String title=((Hair)getItem(position)).getTitle();
        int imgResource=((Hair)getItem(position)).getImgResource();
        tv.setText(title);
        imageView.setImageResource(imgResource);
        return itemView;
    }
}
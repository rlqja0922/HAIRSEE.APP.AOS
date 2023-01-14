package com.example.hairsee.menu.gallarysub;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.lights.LightState;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.hairsee.MyUtils;
import com.example.hairsee.R;
import com.example.hairsee.menu.Galleryfragment;
import com.example.hairsee.utils.MyImageUtils;
import com.example.hairsee.utils.OnSingleClickListener;
import com.example.hairsee.utils.SharedStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class gallImageFragment extends BaseAdapter {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String mBasePath;
    private String[] mImgList;
    private View view;
    private Bitmap bm;
    private Context context;
    public ImageView imageView;
    int CustomGalleryItemBg;

    DataSetObservable mDataSetObservable = new DataSetObservable(); // DataSetObservable(DataSetObserver)의 생성
    public gallImageFragment(Context context, String basepath){
        this.context = context;
        this.mBasePath = basepath;
        File file = new File(mBasePath);
        if(!file.exists()){
            if(!file.mkdirs()){
                Log.d(TAG, "failed to create directory"); }
        }
        mImgList = file.list();
        TypedArray array = context.obtainStyledAttributes(R.styleable.BaseProgressIndicator);
        CustomGalleryItemBg = array.getResourceId(R.styleable.BackgroundStyle_android_selectableItemBackground, 0);
        array.recycle();
    }



    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public int getCount() {
        File dir = new File(mBasePath);
        mImgList = dir.list();
        return mImgList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    public int getViewTypeCount() {
        if(getCount() > 0){
            return getCount();
        }else{
            return super.getViewTypeCount();
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attribute
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
        if (position > 0 || SharedStore.getGallery0(context)){
            bm = BitmapFactory.decodeFile(mBasePath + File.separator + mImgList[mImgList.length-1-position]);
            Bitmap mThumbnail = ThumbnailUtils.extractThumbnail(bm, 300, 300);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
            imageView.setImageBitmap(mThumbnail);
            imageView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("bitmap", MyImageUtils.bitmapToByteArray(bm));
                    bundle.putString("path",mBasePath + File.separator + mImgList[mImgList.length-1-position]);
                    //path확인해야됨
                    AppCompatActivity activity = (AppCompatActivity) context;
                    Full_Image full_image = new Full_Image();
                    full_image.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, full_image).addToBackStack(null).commit();
                }
            });
        }


        if (position == 0){
            SharedStore.setGallery0(context,false);
        }
        return imageView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void notifyDataSetChanged(){ // 위에서 연결된 DataSetObserver를 통한 변경 확인
        mDataSetObservable.notifyChanged();
    }
}
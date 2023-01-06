package com.example.hairsee;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairsee.listClass.Hair;
import com.example.hairsee.menu.CamSub.Camera3Fragment;
import com.example.hairsee.utils.OnSingleClickListener;

import java.util.ArrayList;
import java.util.List;

public class Hairlist extends RecyclerView.Adapter<Hairlist.ViewHolder>{
    private static final String TAG = "SingleAdapter";

    private List<Hair> items=new ArrayList<>();
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;
        TextView tv;
        FrameLayout frame;
        int type;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv= itemView.findViewById(R.id.tv_title);
            imgView_item= itemView.findViewById(R.id.iv_img_resource);
            frame = itemView.findViewById(R.id.frame);
        }
    }
    public Hairlist(ArrayList<Hair> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.choiceitem, parent, false);
        Hairlist.ViewHolder vh = new Hairlist.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hair item = items.get(position);
        holder.type = item.getType();
        holder.imgView_item.setImageResource(item.getImgResource());   // 사진 없어서 기본 파일로 이미지 띄움
        holder.tv.setText(item.getTitle());
        holder.frame.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Camera3Fragment.type = holder.type;

            }

        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
package com.example.hair__See;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hair__See.listClass.Hair;
import com.example.hair__See.menu.CamSub.Camera3Fragment;
import com.example.hair__See.utils.OnSingleClickListener;
import com.example.hair__See.utils.SharedStore;

import java.util.ArrayList;
import java.util.List;

public class Hairlist extends RecyclerView.Adapter<Hairlist.ViewHolder>{
    private static final String TAG = "SingleAdapter";

    private List<Hair> items=new ArrayList<>();
    private Context context;
    private ViewHolder viewholder;
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item,hairPick;
        TextView tv;
        FrameLayout frame;
        int type;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv= itemView.findViewById(R.id.tv_title);
            imgView_item= itemView.findViewById(R.id.iv_img_resource);
            frame = itemView.findViewById(R.id.frame);
            hairPick = imgView_item.findViewById(R.id.hairPick);
        }
    }
    public Hairlist(ArrayList<Hair> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.choiceitem, parent, false);
        Hairlist.ViewHolder vh = new Hairlist.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hair item = items.get(position);
        viewholder = holder;
        viewholder.type = item.getType();
        viewholder.imgView_item.setImageResource(item.getImgResource());   // 사진 없어서 기본 파일로 이미지 띄움
        viewholder.tv.setText(item.getTitle());
        viewholder.frame.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Camera3Fragment.type = holder.type;
                SharedStore.setHairType(context,String.valueOf(holder.type));
                ViewChange();
                viewholder.hairPick.setVisibility(View.VISIBLE);
            }

        });
    }
    public void ViewChange(){
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

}
package com.example.hair__See;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
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
import com.example.hair__See.listClass.Hair2;
import com.example.hair__See.menu.CamSub.Camera3Fragment;
import com.example.hair__See.utils.OnSingleClickListener;
import com.example.hair__See.utils.SharedStore;

import java.util.ArrayList;
import java.util.List;

public class Hairlist2 extends RecyclerView.Adapter<Hairlist2.ViewHolder>{
    private static final String TAG = "SingleAdapter";

    private List<Hair2> items=new ArrayList<>();
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
            hairPick = itemView.findViewById(R.id.hairPick);
        }
    }
    public Hairlist2(ArrayList<Hair2> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.choiceitem, parent, false);
        Hairlist2.ViewHolder vh = new Hairlist2.ViewHolder(view);
        return vh;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hair2 item = items.get(position);
        viewholder = holder;
        viewholder.type = item.getType();
        viewholder.imgView_item.setImageResource(item.getImgResource());   // 사진 없어서 기본 파일로 이미지 띄움
        viewholder.tv.setText(item.getTitle());
        if (item.getPick()){
            viewholder.hairPick.setVisibility(View.VISIBLE);
        }else{
            viewholder.hairPick.setVisibility(View.GONE);
        }
        viewholder.frame.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (item.getTitle().equals("준비중")){

                }else{
                    Camera3Fragment.type = holder.type;
                    SharedStore.setHairType(context,String.valueOf(holder.type));
                    for (int i =0;i < items.size(); i++){
                        Hair2 item1 = items.get(i);
                        if (i == position){
                            items.set(position,new Hair2(item.getTitle(), item.getImgResource(),item.getType(),true));
                        }else{
                            items.set(i,new Hair2(item1.getTitle(),item1.getImgResource(),item1.getType(),false));
                        }
                    }
                    ViewChange();
                }
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
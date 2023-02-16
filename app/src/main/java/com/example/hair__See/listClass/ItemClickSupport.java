package com.example.hair__See.listClass;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hair__See.R;

public class ItemClickSupport {
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                return mOnItemLongClickListener.onItemLongClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    private ItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        String viewClass = mRecyclerView.getAdapter().getClass().toString();
        if (viewClass.equals("com.example.hair__See.Hairlist")){
            mRecyclerView.setTag(R.id.hairChoice1, this);
        }else if (viewClass.equals("com.example.hair__See.Hairlist2")){
            mRecyclerView.setTag(R.id.hairChoice2, this);
        }
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickSupport addTo(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.hairChoice1);
        if (view.getAdapter().getClass().toString().equals("com.example.hair__See.Hairlist")){
            support = (ItemClickSupport) view.getTag(R.id.hairChoice1);
        }else if(view.getAdapter().getClass().toString().equals("com.example.hair__See.Hairlist2")){
            support = (ItemClickSupport) view.getTag(R.id.hairChoice2);
        }
        if (support == null) {
            support = new ItemClickSupport(view);
        }
        return support;
    }

    public static ItemClickSupport removeFrom(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.hairChoice1);

        if (view.getAdapter().getClass().toString().equals("com.example.hair__See.Hairlist")){
            support = (ItemClickSupport) view.getTag(R.id.hairChoice1);
        }else if(view.getAdapter().getClass().toString().equals("com.example.hair__See.Hairlist2")){
            support = (ItemClickSupport) view.getTag(R.id.hairChoice2);
        }

        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);

        if (view.getAdapter().getClass().toString().equals("com.example.hair__See.Hairlist")){
            view.setTag(R.id.hairChoice1, null);
        }else if(view.getAdapter().getClass().toString().equals("com.example.hair__See.Hairlist2")){
            view.setTag(R.id.hairChoice2, null);
        }
    }

    public interface OnItemClickListener {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
    }
}
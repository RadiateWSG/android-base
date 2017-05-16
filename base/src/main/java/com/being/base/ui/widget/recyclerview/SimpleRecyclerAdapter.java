package com.being.base.ui.widget.recyclerview;

import android.support.annotation.LayoutRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用于RecyclerView的Adapter
 * Created by zhangpeng on 16/8/23.
 */

@SuppressWarnings({"unused", "JavaDoc", "unchecked"})
public abstract class SimpleRecyclerAdapter<T, K extends AbstractViewHolder> extends BaseQuickAdapter<T, K> {

    private SimpleRecyclerAdapter(int layoutResId) {
        super(layoutResId, new ArrayList<T>());
    }

    public SimpleRecyclerAdapter() {
        super(new ArrayList<T>());
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        K k = onCreateAbstractViewHolder(parent, viewType);
        if (k == null) {
            k = super.onCreateDefViewHolder(parent, viewType);
        }
        return k;
    }

    protected abstract K onCreateAbstractViewHolder(ViewGroup parent, int viewType);

    @Override
    protected K createBaseViewHolder(View view) {
        return (K) new VH<T>(view);
    }

    @Override
    protected void convert(K helper, T item) {
        helper.bindDate(item);
    }

    public void setData(List<T> data) {
        if (data == null) return;
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    public void addSomeData(List<T> data) {
        if (data == null) return;
        int index = getRealPosition(data.size());
        mData.addAll(data);
        notifyItemInserted(index);
    }

    @SuppressWarnings("unchecked")
    public void addNewData(List<T> data) {
        addNewData(data, null);
    }

    @SuppressWarnings("unchecked")
    public void addNewData(List<T> data, DiffUtil.Callback cb) {
        mData.addAll(0, data);
        notifyItemInserted(getRealPosition(0));
    }

    public void addSomeData(T data) {
        super.add(getRealPosition(0), data);
    }

    public boolean remove(T data) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i) == data) {
                super.remove(getRealPosition(i));
                return true;
            }
        }
        return false;
    }

    /**
     * 只获取数据的数量,不包括头尾等其他view占用的position
     * @return
     */
    public int getDataCount() {
        return getData() != null ? getData().size() : 0;
    }

    protected int getRealPosition(int p) {
        return p + getHeaderLayoutCount();
    }

    private void caculateDiff(List<T> oldData, List<T> newData, DiffUtil.Callback cb) {
        DiffUtil.Callback callback = cb == null ? new DataDiffCallback<>(oldData, newData) : cb;
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        result.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(getRealPosition(position), count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(getRealPosition(position), count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(getRealPosition(fromPosition), getRealPosition(toPosition));
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                notifyItemRangeChanged(getRealPosition(position), count, payload);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static class DataDiffCallback<T> extends DiffUtil.Callback {

        private List<T> mOldData = Collections.EMPTY_LIST;

        private List<T> mNewData = Collections.EMPTY_LIST;

        DataDiffCallback(List<T> oldData, List<T> newData) {
            mOldData = oldData;
            mNewData = newData;
        }

        @Override
        public int getOldListSize() {
            return mOldData.size();
        }

        @Override
        public int getNewListSize() {
            return mNewData.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldData.get(oldItemPosition).equals(mNewData.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }
    }

    private static class VH<T> extends AbstractViewHolder<T> {

        public VH(View view) {
            super(view);
        }

        public VH(ViewGroup parent, @LayoutRes int id) {
            super(parent, id);
        }

        @Override
        public void bindDate(T data) {

        }
    }

}

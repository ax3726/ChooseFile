package choose.lm.com.fileselector.adapters.selectrecycleview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import choose.lm.com.fileselector.R;
import choose.lm.com.fileselector.model.FileGroupInfo;
import choose.lm.com.fileselector.model.FileInfo;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class ChoosePhotoAdapter extends SectionedRecyclerViewAdapter<ChoosePhotoAdapter.GroupHolder, ChoosePhotoAdapter.ChildHolder, RecyclerView.ViewHolder> {

    public List<FileGroupInfo> mDataList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private Map<Integer, SparseBooleanArray> map1;
    private SparseBooleanArray mBooleanMap;
    private int group = -1, child = -1, position11 = -1;
    private boolean  mIsMultiselect=false;
    private  RequestManager mRequestManager;
    public ChoosePhotoAdapter(Context context,boolean isMultiselect) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBooleanMap = new SparseBooleanArray();
        map1 = new HashMap<>();
        mIsMultiselect=isMultiselect;
        mRequestManager = Glide.with(mContext);
        mRequestManager.onLowMemory();
        width = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth();
    }

    int width = 0;

    public void setData(List<FileGroupInfo> dataList) {
        mDataList = dataList;
        for (int i = 0; i < dataList.size(); i++) {
            map1.put(i, new SparseBooleanArray());
        }
        notifyDataSetChanged();

    }

    public List<FileGroupInfo> getDataList() {
        return mDataList;
    }
    public void clearState()
    {
        for (int i = 0; i < mDataList.size(); i++) {
            map1.put(i, new SparseBooleanArray());
        }
        notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        return mDataList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
       /* int count = allTagList.get(section).tagInfoList.size();
        if (count >= 8 && !mBooleanMap.get(section)) {
            count = 8;
        }

        return HotelUtils.isEmpty(allTagList.get(section).tagInfoList) ? 0 : count;*/
        boolean b = mBooleanMap.get(section);

        return !mBooleanMap.get(section) ? 0 : mDataList.get(section).getmFileList().size();
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected GroupHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new GroupHolder(mInflater.inflate(R.layout.lib_item_image_group, parent, false));
    }


    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ChildHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.lib_item_photo_fragment, parent, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 4, width / 4);
        inflate.setLayoutParams(layoutParams);
        return new ChildHolder(inflate);
    }


    @Override
    protected void onBindSectionHeaderViewHolder(final GroupHolder holder, final int section) {

        final boolean b = mBooleanMap.get(section);
        if (b) {
            holder.img_choose.setSelected(true);
        } else {
            holder.img_choose.setSelected(false);
        }
        holder.tv_group_name.setText(mDataList.get(section).getGroup_name());
        holder.lly_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBooleanMap.put(section, !b);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onGroupClick(section);
                }
                notifyDataSetChanged();

            }
        });
    }
    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ChildHolder holder, final int section, final int position, final int position1) {
        FileInfo fileInfo = mDataList.get(section).getmFileList().get(position);
        if (!TextUtils.isEmpty(fileInfo.getFile_path()) && fileInfo.getFile_path().contains(".gif")) {
            mRequestManager.load(new File(fileInfo.getFile_path())).asBitmap().thumbnail(0.7f).placeholder(R.drawable.img_preloading).into(holder.img);

        } else {
            mRequestManager.load(new File(fileInfo.getFile_path())).thumbnail(0.7f).placeholder(R.drawable.img_preloading).into(holder.img);
        }

        final SparseBooleanArray sparseBooleanArray = map1.get(section);
        if (sparseBooleanArray.get(position)) {
            holder.imgV_select.setSelected(true);
            holder.rl_choose.setVisibility(View.VISIBLE);
        } else {
            holder.imgV_select.setSelected(false);
            holder.rl_choose.setVisibility(View.GONE);
        }

        holder.fl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onChildClick(section, position,!sparseBooleanArray.get(position));

                    SparseBooleanArray sparseBooleanArray = map1.get(section);
                    if (sparseBooleanArray.get(position)) {
                        sparseBooleanArray.put(position, false);
                    } else {
                        sparseBooleanArray.put(position, true);
                    }
                    map1.put(section, sparseBooleanArray);
                    if (!mIsMultiselect) {//单选模式
                        if (group != -1 && !(group == section && child == position)) {
                            map1.get(group).put(child, false);
                            notifyItemChanged(position11);
                        }

                        group = section;
                        child = position;
                        position11 = position1;
                    }


                    notifyItemChanged(position1);
                }
            }
        });
    }

    public class GroupHolder extends RecyclerView.ViewHolder {
        public ImageView img_choose;
        public TextView tv_group_name;
        public LinearLayout lly_group;
        public GroupHolder(View itemView) {
            super(itemView);
            img_choose= (ImageView) itemView.findViewById(R.id.img_choose);
            tv_group_name= (TextView) itemView.findViewById(R.id.tv_group_name);
            lly_group= (LinearLayout) itemView.findViewById(R.id.lly_group);
        }
    }

    public class ChildHolder extends RecyclerView.ViewHolder {
       public RelativeLayout rl_choose;
       public ImageView imgV_select;
       public ImageView img;
       public FrameLayout fl_item;
        public ChildHolder(View itemView) {
            super(itemView);
            rl_choose= (RelativeLayout) itemView.findViewById(R.id.rl_choose);
            imgV_select= (ImageView) itemView.findViewById(R.id.imgV_select);
            img= (ImageView) itemView.findViewById(R.id.img);
            fl_item= (FrameLayout) itemView.findViewById(R.id.fl_item);
        }
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onGroupClick(int group);

        void onChildClick(int group, int child,boolean is_choose);
    }
}
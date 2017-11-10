package choose.lm.com.fileselector.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import choose.lm.com.fileselector.R;
import choose.lm.com.fileselector.activitys.ChooseFileActivity;
import choose.lm.com.fileselector.base.BaseFragment;
import choose.lm.com.fileselector.base.MyApplication;
import choose.lm.com.fileselector.base.recyclerview.CommonAdapter;
import choose.lm.com.fileselector.base.recyclerview.MultiItemTypeAdapter;
import choose.lm.com.fileselector.base.recyclerview.base.ViewHolder;
import choose.lm.com.fileselector.databinding.LibVideoFragmentBinding;
import choose.lm.com.fileselector.model.FileGroupInfo;
import choose.lm.com.fileselector.model.FileInfo;
import choose.lm.com.fileselector.utils.DateUtil;
import choose.lm.com.fileselector.utils.FileUtil;
import choose.lm.com.fileselector.utils.LoadFiles;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class OtherFragment extends BaseFragment<LibVideoFragmentBinding> {

    List<FileGroupInfo> mDataList = new ArrayList<>();
    private CommonAdapter<FileGroupInfo> mGroupAdapter;

    private boolean isChoose = false;

    /**
     * 单选模式   清楚状态
     */
    public void clearState() {
        if (isChoose) {

            for (int i = 0; i < mDataList.size(); i++) {
                for (int j = 0; j < mDataList.get(i).getmFileList().size(); j++) {
                    mDataList.get(i).getmFileList().get(j).setIs_select(false);
                }
            }
            mGroupAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.lib_video_fragment;
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mGroupAdapter = new CommonAdapter<FileGroupInfo>(aty,R.layout.lib_item_file_group,mDataList) {
            @Override
            protected void convert(final ViewHolder helper, final FileGroupInfo item, final int position) {
                if (mDataList.get(position).is_choose()) {
                    helper.setVisible(R.id.rc_group_content, true);
                    helper.setSelected(R.id.img_choose, true);
                    mDataList.get(position).setIs_choose(true);
                } else {
                    helper.setVisible(R.id.rc_group_content, false);
                    helper.setSelected(R.id.img_choose, false);
                    mDataList.get(position).setIs_choose(false);
                }
                helper.setText(R.id.tv_group_name, item.getGroup_name());
                RecyclerView content = helper.getView(R.id.rc_group_content);
                content.setLayoutManager(new LinearLayoutManager(aty));
                content.setAdapter(getmAdapter(item.getmFileList(), position));
                helper.setOnClickListener(R.id.lly_choose, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDataList.get(position).is_choose()) {
                            helper.setVisible(R.id.rc_group_content, false);
                            helper.setSelected(R.id.img_choose, false);
                            mDataList.get(position).setIs_choose(false);
                        } else {
                            helper.setSelected(R.id.img_choose, true);
                            mDataList.get(position).setIs_choose(true);
                            helper.setVisible(R.id.rc_group_content, true);

                        }

                    }
                });

            }
        };
        mBinding.rcVideo.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rcVideo.setAdapter(mGroupAdapter);

        loadData();
    }

    public void loadData()
    {
        if (MyApplication.getInstance().getContentResolver()!=null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    LoadFiles loadFiles = new LoadFiles();
                    loadFiles.loadOtherFile(MyApplication.getInstance().getContentResolver());
                    setDataList(loadFiles.getOtherFiles());
                    handler.sendEmptyMessage(0);
                }
            }.start();
        }
    }
    private int groupPo = -1, childPo = -1;
    private CommonAdapter<FileInfo> mCurAdapter;

    public CommonAdapter<FileInfo> getmAdapter(List<FileInfo> data, final int po) {

        final CommonAdapter<FileInfo> mAdapter = new CommonAdapter<FileInfo>(aty,R.layout.lib_item_video_fragment,data) {
            @Override
            protected void convert(ViewHolder helper, FileInfo item, int position) {
                if (FileInfo.FileType.RAR.equals(item.getFile_type())) {
                    helper.setImageResource(R.id.img, R.drawable.ico_rar);
                } else if (FileInfo.FileType.TEXT.equals(item.getFile_type())) {
                    helper.setImageResource(R.id.img, R.drawable.ico_txt);
                } else {
                    helper.setImageResource(R.id.img, R.drawable.ico_other);
                }
                helper.setText(R.id.tv_time, DateUtil.getCommunityTime(item.getFile_modified_time()));
                helper.setText(R.id.tv_name, item.getFile_name());
                helper.setText(R.id.tv_size, FileUtil.bytes2kb(item.getFile_size()));
                if (item.is_select()) {
                    helper.setSelected(R.id.imgV_select, true);
                    helper.getConvertView().setSelected(true);
                } else {
                    helper.setSelected(R.id.imgV_select, false);
                    helper.getConvertView().setSelected(false);
                }


            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                FileInfo item = mDataList.get(po).getmFileList().get(position);
                if (!((ChooseFileActivity) aty).mIsMultiselect) {//单选
                    ((ChooseFileActivity) aty).clearFile();

                    if (item.is_select()) {
                        view.findViewById(R.id.imgV_select).setSelected(false);
                        mDataList.get(po).getmFileList().get(position).setIs_select(false);
                        ((ChooseFileActivity) aty).removeFile(item.getId());
                    } else {
                        view.findViewById(R.id.imgV_select).setSelected(true);
                        mDataList.get(po).getmFileList().get(position).setIs_select(true);
                        ((ChooseFileActivity) aty).addFile(item);
                    }

                    if (groupPo != -1 && !(groupPo == po && childPo == position)) {
                        mDataList.get(groupPo).getmFileList().get(childPo).setIs_select(false);
                        if (groupPo == po) {
                            int fristPos = ((LinearLayoutManager) mBinding.rcVideo.getLayoutManager()).findFirstVisibleItemPosition();
                            int lastPos = ((LinearLayoutManager) mBinding.rcVideo.getLayoutManager()).findLastVisibleItemPosition();
                            if (groupPo >= fristPos && lastPos <= lastPos) {//当上一项为可见项的时候
                                View v = mBinding.rcVideo.getChildAt(groupPo - fristPos);
                                if (v != null) {
                                    RecyclerView rc_content = (RecyclerView) v.findViewById(R.id.rc_group_content);
                                    int fristPos1 = ((LinearLayoutManager) mBinding.rcVideo.getLayoutManager()).findFirstVisibleItemPosition();
                                    int lastPos1 = ((LinearLayoutManager) mBinding.rcVideo.getLayoutManager()).findLastVisibleItemPosition();
                                    if (childPo >= fristPos1 && childPo <= lastPos1) {//当上一项为可见项的时候
                                        View v1 = rc_content.getChildAt(childPo - fristPos1);
                                        v1.findViewById(R.id.imgV_select).setSelected(false);
                                    }
                                }

                            }
                        } else if (mCurAdapter != null) {
                            mCurAdapter.notifyDataSetChanged();
                        }

                    }
                    groupPo = po;
                    childPo = position;
                    mCurAdapter = mAdapter;

                } else {
                    if (item.is_select()) {
                        view.findViewById(R.id.imgV_select).setSelected(false);
                        mDataList.get(po).getmFileList().get(position).setIs_select(false);
                        ((ChooseFileActivity) aty).removeFile(item.getId());
                    } else {
                        view.findViewById(R.id.imgV_select).setSelected(true);
                        mDataList.get(po).getmFileList().get(position).setIs_select(true);
                        ((ChooseFileActivity) aty).addFile(item);
                    }
                }
                isChoose = true;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        
        

        return mAdapter;
    }

    private void setDataList(List<FileInfo> files) {
        mDataList.clear();
        FileGroupInfo rars = new FileGroupInfo("压缩文件");
        FileGroupInfo txts = new FileGroupInfo("电子书");

        for (int i = 0; i < files.size(); i++) {
            FileInfo item = files.get(i);
            if (FileInfo.FileType.RAR.equals(item.getFile_type())) {
                rars.addFile(item);
            } else if (FileInfo.FileType.TEXT.equals(item.getFile_type())) {
                txts.addFile(item);
            }
        }
        if (rars.getmFileList().size() > 0) {
            mDataList.add(rars);
        }
        if (txts.getmFileList().size() > 0) {
            mDataList.add(txts);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mGroupAdapter.notifyDataSetChanged();

        }
    };
}

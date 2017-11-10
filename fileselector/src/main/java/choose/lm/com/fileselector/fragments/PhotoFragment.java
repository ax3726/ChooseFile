package choose.lm.com.fileselector.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import choose.lm.com.fileselector.R;
import choose.lm.com.fileselector.activitys.ChooseFileActivity;
import choose.lm.com.fileselector.adapters.selectrecycleview.ChoosePhotoAdapter;
import choose.lm.com.fileselector.adapters.selectrecycleview.SectionedSpanSizeLookup;
import choose.lm.com.fileselector.base.BaseFragment;
import choose.lm.com.fileselector.base.MyApplication;
import choose.lm.com.fileselector.databinding.LibPhotoFragmentBinding;
import choose.lm.com.fileselector.model.FileGroupInfo;
import choose.lm.com.fileselector.model.FileInfo;
import choose.lm.com.fileselector.utils.LoadFiles;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class PhotoFragment extends BaseFragment<LibPhotoFragmentBinding> {

    List<FileGroupInfo> mDataList = new ArrayList<>();
    private ChoosePhotoAdapter mGroupAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.lib_photo_fragment;
    }


    private boolean isChoose = false;

    /**
     * 单选模式   清楚状态
     */
    public void clearState() {
        if (isChoose) {
            mGroupAdapter.clearState();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        width = aty.getWindowManager().getDefaultDisplay().getWidth();
    }

    int width = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mGroupAdapter=new ChoosePhotoAdapter(aty,((ChooseFileActivity)aty).mIsMultiselect);
        GridLayoutManager manager = new GridLayoutManager(aty,4);
        //设置header
        manager.setSpanSizeLookup(new SectionedSpanSizeLookup(mGroupAdapter, manager));
        mBinding.rcPhoto.setLayoutManager(manager);
        mBinding.rcPhoto.setAdapter(mGroupAdapter);
        mGroupAdapter.setOnItemClickListener(new ChoosePhotoAdapter.onItemClickListener() {
            @Override
            public void onGroupClick(int group) {

            }

            @Override
            public void onChildClick(int group, int child,boolean is_choose) {
                FileInfo item = mDataList.get(group).getmFileList().get(child);
                if (!((ChooseFileActivity) aty).mIsMultiselect) {//单选
                    ((ChooseFileActivity) aty).clearFile();
                }
                if (!is_choose) {
                    ((ChooseFileActivity) aty).removeFile(item.getId());
                } else {
                    ((ChooseFileActivity) aty).addFile(item);
                }
                isChoose = true;
            }
        });
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
                    loadFiles.loadImages(MyApplication.getInstance().getContentResolver());
                    setDataList(loadFiles.getImages());
                    handler.sendEmptyMessage(0);
                }
            }.start();
        }
    }


    public String getFolder(String folder) {
        if (folder.contains("ylwj_expert/image/")) {
            return "已下载的图片";
        }
        if (folder.contains("Camera/")) {
            return "相册";
        }
        String[] split = folder.split("/");
        if (split.length >= 2) {
            return split[split.length - 2];
        }
        return "";
    }

    private void setDataList(List<FileInfo> files) {
        mDataList.clear();
        List<String> folders = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            String folder = getFolder(files.get(i).getFile_path());
            if (!folders.contains(folder)) {
                if ("相册".equals(folder)) {
                    folders.add(0, folder);
                    mDataList.add(0, new FileGroupInfo(folder));
                } else {
                    folders.add(folder);
                    mDataList.add(new FileGroupInfo(folder));
                }
            }
        }
        for (int i = 0; i < files.size(); i++) {
            String folder = getFolder(files.get(i).getFile_path());
            for (int j = 0; j < folders.size(); j++) {
                if (folders.get(j).equals(folder)&&!files.get(i).getFile_path().contains(".gif")) {
                    mDataList.get(j).getmFileList().add(files.get(i));
                }
            }
        }

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mGroupAdapter.setData(mDataList);
        }
    };

}

package choose.lm.com.choosefile;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;

import choose.lm.com.choosefile.databinding.ActivityMainBinding;
import choose.lm.com.fileselector.activitys.ChooseFileActivity;
import choose.lm.com.fileselector.base.BaseActivity;
import choose.lm.com.fileselector.model.FileInfo;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mBinding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送文件
                Intent intent = ChooseFileActivity.newIntent(aty, true);//第二个参数为是否多选
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            ArrayList<FileInfo> list = data.getParcelableArrayListExtra(ChooseFileActivity.FILELISTDATA);

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                FileInfo fileInfo = list.get(i);
                str.append(fileInfo.getFile_path() + "\n");
            }
            mBinding.tvShow.setText(str.toString());


        }

    }
}

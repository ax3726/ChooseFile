package choose.lm.com.fileselector.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by zk on 2017/6/1.
 * Description:
 */

public abstract class BaseActivity<B extends ViewDataBinding> extends FragmentActivity {


    protected B mBinding;
    protected Activity aty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aty = this;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutId(), null, false);
        setContentView(mBinding.getRoot());
        initTitle();
        initData();
        initEvent();
        initView(savedInstanceState);
    }

    protected abstract int getLayoutId();
    protected void initTitle() {

    }

    protected void initEvent() {

    }

    protected void initView(Bundle savedInstanceState) {

    }

    protected void initData() {

    }


}

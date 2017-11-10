package choose.lm.com.fileselector.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zk on 2017/6/1.
 * Description:
 */

public abstract class BaseFragment< B extends ViewDataBinding> extends Fragment  {

    /**
     * Fragment根视图
     */
    protected View mFragmentRootView;

    protected B mBinding;
    protected Activity aty;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentRootView = inflaterView(inflater, container, savedInstanceState);
        aty = getActivity();


        initTitle();
        initData();
        initEvent();
        initView(savedInstanceState);
        return mFragmentRootView;
    }


    /**
     * 加载View
     *
     * @param inflater  LayoutInflater
     * @param container ViewGroup
     * @param bundle    Bundle
     * @return
     */
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mBinding.getRoot();
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

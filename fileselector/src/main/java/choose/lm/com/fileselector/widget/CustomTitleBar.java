package choose.lm.com.fileselector.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import choose.lm.com.fileselector.R;
import choose.lm.com.fileselector.databinding.LibBaseTitlebarLayoutBinding;


/**
 * Created by wjq on 2016/7/26.
 */
public class CustomTitleBar extends LinearLayout implements View.OnClickListener {

    /**
     * 标题栏右侧区域的按钮集合
     */
    private HashMap<String, TextView> _btns = new HashMap<String, TextView>();
    private LibBaseTitlebarLayoutBinding mBinding;

    private Button _btn = null;

    /**
     * 标题栏左侧返回区域点击事件
     */
    private IBackClickListener _backClickListener = null;

    public CustomTitleBar(Context context) {
        this(context, null);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.lib_base_titlebar_layout, this, true);


        if (isInEditMode()) {
            return;
        }

        mBinding.tvTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        mBinding.lyBack.setOnClickListener(this);
        mBinding.ivBack.setOnClickListener(this);
        //this.setBackButtonVisiblity(AppManager.create().getCount()>1);
    }

    /**
     * 设置标题栏左侧返回按钮点击事件
     *
     * @param listener
     */
    public void setOnBackClickListener(IBackClickListener listener) {
        this._backClickListener = listener;
    }

    /**
     * 获取标题
     *
     * @return
     */
    public String getTitle() {
        return mBinding.tvTitle.getText().toString();
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mBinding.tvTitle.setText(title);
    }

    /**
     * 设置标题
     *
     * @param resId
     */
    public void setTitle(@StringRes int resId) {
        mBinding.tvTitle.setText(resId);
    }

    /**
     * 在标题栏右侧区域添加一个按钮（TextView类型的对象按钮）
     *
     * @param name     按钮的唯一名称(如果添加的按钮名称已经存在，则会抛出异常)
     * @param text     按钮显示的文本
     * @param listener 按钮的点击事件响应动作对象(null表示不做任何响应)
     * @throws Exception 相同名称的按钮已经存在
     */
    public void addRightAreaButton(String name, String text,
                                   OnClickListener listener) throws Exception {
        if (this._btns.containsKey(name)) {
            throw new Exception("该名称的按钮已经存在");
        } else {
            TextView txtV = new TextView(this.getContext());
            txtV.setText(text);
            txtV.setTextColor(Color.WHITE);

            txtV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            txtV.setClickable(true);
            txtV.setOnClickListener(listener);
            txtV.setGravity(Gravity.CENTER);
            LayoutParams pa = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            txtV.setPadding(20, 10, 20, 10);
            pa.gravity = Gravity.CENTER;
            if (this._btns.size() != 0) {
                pa.rightMargin = 20;
            }
           mBinding.lyRight.addView(txtV, 0, pa);
            this._btns.put(name, txtV);
        }
    }

    /**
     * 获取标题栏右侧区域的按钮对象
     *
     * @param name 按钮的唯一名称
     * @return
     */
    public TextView getRightAreaButton(String name) {
        return this._btns.get(name);
    }

    /**
     * 设置指定名称的按钮是否可用
     *
     * @param name 按钮名称
     * @param bool 是否可用
     */
    public void setButtonEnable(String name, Boolean bool) {
        if (this._btns.containsKey(name)) {
            this._btns.get(name).setEnabled(bool);
        }
    }

    /**
     * 设置指定名称的按钮的可见方式
     *
     * @param name      按钮名称
     * @param visiblity View.GONE(占位不可见)/View.INVISIBLE(不可见且不占位)/View.VISIBLE(可见)
     */
    public void setButtonVisible(String name, int visiblity) {
        if (this._btns.containsKey(name)) {
            this._btns.get(name).setVisibility(visiblity);
        }
    }

    /**
     * 设置指定名称的按钮事件响应对象
     *
     * @param name     按钮名称
     * @param listener 点击事件响应对象
     */
    public void setButtonClickListener(String name,
                                       OnClickListener listener) {
        if (this._btns.containsKey(name)) {
            this._btns.get(name).setOnClickListener(listener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ly_back || v.getId() == R.id.iv_back) {
            if (this._backClickListener == null) {
                //AppManager.create().topActivity().finish();
            } else {
                this._backClickListener.OnBackClick();
            }
        }
    }

    /**
     * 设置标题栏返回按钮的显示或隐藏
     *
     * @param bool 显示：true 隐藏：false
     */
    public void setBackButtonVisiblity(boolean bool) {
        if (bool) {
            mBinding.lyBack.setVisibility(View.VISIBLE);
            mBinding.lyBack.setVisibility(View.VISIBLE);
        } else {
            mBinding.lyBack.setVisibility(View.INVISIBLE);
            mBinding.lyBack.setVisibility(View.INVISIBLE);
        }
    }

    public interface IBackClickListener {
        public void OnBackClick();
    }
}

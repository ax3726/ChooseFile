package choose.lm.com.fileselector.base;

import android.app.Application;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class MyApplication extends Application {
    public static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}

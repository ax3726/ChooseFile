package choose.lm.com.fileselector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2 0002.
 */
public class FileGroupInfo {
    private String group_name;
    private  boolean is_choose=false;
    private List<FileInfo> mFileList=new ArrayList<>();

    public FileGroupInfo(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<FileInfo> getmFileList() {
        return mFileList;
    }
    public void addFile(FileInfo info)
    {
        mFileList.add(info);
    }

    public boolean is_choose() {
        return is_choose;
    }

    public void setIs_choose(boolean is_choose) {
        this.is_choose = is_choose;
    }
}

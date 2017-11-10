package choose.lm.com.fileselector.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class FileInfo implements Parcelable {
    public interface FileType {//文件类型
        String VIDEO = "video";
        String IMAGE = "image";
        String RAR = "rar";
        String EXCEL = "excel";
        String PDF = "pdf";
        String PPT = "ppt";
        String WORD = "word";
        String MP3 = "mp3";
        String TEXT = "txt";
    }
    private int file_id;
    private int id;
    private String file_name;
    private String file_path;
    private String file_type;
    private long file_size;
    private String file_thumnbail;
    private long file_modified_time;
    private boolean is_thumnbail;
    private boolean is_select=false;

    public FileInfo(int id, int file_id, String file_name, String file_path, long file_size, long file_modified_time, boolean is_thumnbail, String file_type) {
        this.id = id;
        this.file_id = file_id;
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_size = file_size;
        this.file_modified_time = file_modified_time;
        this.is_thumnbail = is_thumnbail;
        this.file_type = file_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public boolean is_thumnbail() {
        return is_thumnbail;
    }

    public void setIs_thumnbail(boolean is_thumnbail) {
        this.is_thumnbail = is_thumnbail;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public String getFile_thumnbail() {
        return file_thumnbail;
    }

    public void setFile_thumnbail(String file_thumnbail) {
        this.file_thumnbail = file_thumnbail;
    }

    public long getFile_modified_time() {
        return file_modified_time;
    }

    public void setFile_modified_time(long file_modified_time) {
        this.file_modified_time = file_modified_time;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.file_id);
        dest.writeInt(this.id);
        dest.writeString(this.file_name);
        dest.writeString(this.file_path);
        dest.writeString(this.file_type);
        dest.writeLong(this.file_size);
        dest.writeString(this.file_thumnbail);
        dest.writeLong(this.file_modified_time);
        dest.writeByte(this.is_thumnbail ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_select ? (byte) 1 : (byte) 0);
    }

    protected FileInfo(Parcel in) {
        this.file_id = in.readInt();
        this.id = in.readInt();
        this.file_name = in.readString();
        this.file_path = in.readString();
        this.file_type = in.readString();
        this.file_size = in.readLong();
        this.file_thumnbail = in.readString();
        this.file_modified_time = in.readLong();
        this.is_thumnbail = in.readByte() != 0;
        this.is_select = in.readByte() != 0;
    }

    public static final Creator<FileInfo> CREATOR = new Creator<FileInfo>() {
        @Override
        public FileInfo createFromParcel(Parcel source) {
            return new FileInfo(source);
        }

        @Override
        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };
}

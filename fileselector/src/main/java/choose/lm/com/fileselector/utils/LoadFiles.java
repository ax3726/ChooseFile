package choose.lm.com.fileselector.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.util.SparseArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import choose.lm.com.fileselector.model.FileInfo;

/**
 * 读取本地文件
 * Created by Administrator on 2016/11/28 0028.
 */
public class LoadFiles {
    private List<FileInfo> Videos = new ArrayList<FileInfo>();
    private List<FileInfo> Images = new ArrayList<FileInfo>();
    private List<FileInfo> Files = new ArrayList<FileInfo>();
    private List<FileInfo> OtherFiles = new ArrayList<FileInfo>();
    private int mId = 0;

    public void loadImages(ContentResolver contentResolver) {

        SparseArray<FileInfo> tempArray = new SparseArray<FileInfo>();
        String[] projection = {Media._ID, Media.DATA, Media.DISPLAY_NAME, Media.SIZE, Media.DATE_MODIFIED};
        String where = Media.BUCKET_DISPLAY_NAME + " = 'Camera' or " + Media.BUCKET_DISPLAY_NAME + " <> 'audio'";
        String sortOrder = Media._ID + " desc";

        StringBuilder inImageIds = new StringBuilder(Thumbnails.IMAGE_ID);
        inImageIds.append(" in ( ");
        Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, where, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor.getColumnIndex(Media._ID));
                String imagePath = cursor.getString(cursor.getColumnIndex(Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
                long size = cursor.getLong(cursor.getColumnIndex(Media.SIZE));
                long time = cursor.getLong(cursor.getColumnIndex(Media.DATE_MODIFIED));
                if (!TextUtils.isEmpty(imagePath)) {
                    if (TextUtils.isEmpty(name)) {
                        name = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                    }
                    FileInfo FileInfo = new FileInfo(mId, imageId, name, imagePath, size, time * 1000, true, "image");
                    mId++;
                    Images.add(FileInfo);
                    tempArray.put(imageId, FileInfo);
                    inImageIds.append(imageId).append(",");
                }
            } while (cursor.moveToNext());
        }

        inImageIds.deleteCharAt(inImageIds.length() - 1);
        inImageIds.append(" ) ");

        String[] thumbnailProjection = {Thumbnails.DATA, Thumbnails.IMAGE_ID};
        cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, thumbnailProjection, inImageIds.toString(), null, null);
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor.getColumnIndex(Thumbnails.IMAGE_ID));
                String thumnbailPath = cursor.getString(cursor.getColumnIndex(Media.DATA));
                FileInfo FileInfo = tempArray.get(imageId);
                if (FileInfo != null) {
                    FileInfo.setFile_thumnbail(thumnbailPath);
                }
            } while (cursor.moveToNext());
        }
        tempArray = null;
        cursor.close();


    }

    public void loadMP3(ContentResolver contentResolver) {
        String sortOrder = MediaStore.Audio.Media._ID + " desc";
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                //  歌曲ID：MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                // 歌曲的名称：MediaStore.Audio.Media.TITLE
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                // 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

                // 歌曲的歌手名：MediaStore.Audio.Media.ARTIST
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                // 歌曲文件的路径：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));
                // 歌曲的总播放时长：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //  歌曲文件的大小：MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (!TextUtils.isEmpty(url)) {
                    if (TextUtils.isEmpty(name)) {
                        name = url.substring(url.lastIndexOf("/") + 1);
                    }
                    FileInfo FileInfo = new FileInfo(mId, id, name, url, size, time * 1000, false, "mp3");
                    mId++;
                    Videos.add(FileInfo);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

    }


    public void loadVideo(ContentResolver contentResolver) {
        String sortOrder = MediaStore.Video.Media._ID + " desc";
        SparseArray<FileInfo> tempArray = new SparseArray();
        StringBuilder videoIds = new StringBuilder(MediaStore.Video.Thumbnails.VIDEO_ID);
        videoIds.append(" in ( ");
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                null, null, sortOrder);
        cursor.moveToFirst();
        int fileNum = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //  歌曲ID：MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                // 歌曲的名称：MediaStore.Audio.Media.TITLE
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                // 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));

                // 歌曲的歌手名：MediaStore.Audio.Media.ARTIST
                //  String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));

                // 歌曲文件的路径：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                // 歌曲的总播放时长：MediaStore.Audio.Media.DURATION
                //   int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                //  歌曲文件的大小：MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
                if (!TextUtils.isEmpty(url)) {
                    if (TextUtils.isEmpty(name)) {
                        name = url.substring(url.lastIndexOf("/") + 1);
                    }
                    FileInfo FileInfo = new FileInfo(mId, id, name, url, size, time * 1000, true, "video");
                    mId++;
                    Videos.add(FileInfo);
                    tempArray.put(id, FileInfo);
                    videoIds.append(id).append(",");
                }

            } while (cursor.moveToNext());
        }
        videoIds.deleteCharAt(videoIds.length() - 1);
        videoIds.append(" ) ");
        String[] thumbnailProjection = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};
        cursor = contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbnailProjection, videoIds.toString(), null, null);
        if (cursor.moveToFirst()) {
            do {
                int videoId = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID));
                String thumnbailPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                FileInfo FileInfo = tempArray.get(videoId);
                if (FileInfo != null) {
                    FileInfo.setFile_thumnbail(thumnbailPath);
                }
            } while (cursor.moveToNext());
        }
        tempArray = null;
        cursor.close();
    }

    public  static  void insertFile(ContentResolver contentResolver, File file) {

        if (!isExist(contentResolver,file.getAbsolutePath())) {//文件路径不存在 则添加
            //创建一个空的ContentValues
            ContentValues values = new ContentValues();
            values.put(Media.DISPLAY_NAME, file.getName());
            values.put(Media.DATA, file.getAbsolutePath());
            values.put(Media.DATE_MODIFIED, file.lastModified());
            try {
                values.put(Media.SIZE, new FileInputStream(file).available());
            } catch (IOException e) {
                e.printStackTrace();
            }
            contentResolver.insert(MediaStore.Files.getContentUri("external"), values);
        }
    }

    public void loadFile(ContentResolver contentResolver) {
        String sortOrder = MediaStore.Files.FileColumns._ID + " desc";

        Cursor cursor = contentResolver.query(
                //数据源
                MediaStore.Files.getContentUri("external"),
                //查询ID和名称
                new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.MIME_TYPE},
                //条件为文件类型
                MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? ",
                new String[]{"application/pdf"
                        , "application/msword",//word
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
                        "application/vnd.ms-excel",//excel
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",//ppt
                        "application/vnd.openxmlformats-officedocument.presentationml.template",
                        "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
                        "application/vnd.ms-powerpoint"
                },

            //按ID向下排序
                sortOrder);

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED));
                if (!TextUtils.isEmpty(data)) {
                    if (TextUtils.isEmpty(name)) {
                        name = data.substring(data.lastIndexOf("/") + 1);
                    }
                    if ("application/msword".equals(type)
                            ||"application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(type)
                            ||"application/vnd.openxmlformats-officedocument.wordprocessingml.template".equals(type)
                            ) {
                        type="word";
                    } else if ("application/vnd.ms-excel".equals(type)
                            ||"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(type)
                            ||"application/vnd.openxmlformats-officedocument.spreadsheetml.template".equals(type)) {
                        type="excel";
                    } else if ("application/pdf".equals(type)) {
                        type="pdf";
                    } else if ("application/vnd.ms-powerpoint".equals(type)
                            ||"application/vnd.openxmlformats-officedocument.presentationml.presentation".equals(type)
                            ||"application/vnd.openxmlformats-officedocument.presentationml.template".equals(type)
                            ||"application/vnd.openxmlformats-officedocument.presentationml.slideshow".equals(type)
                            ) {
                        type="ppt";
                    }
                    FileInfo FileInfo = new FileInfo(mId, id, name, data, size, time * 1000, false, type);
                    mId++;
                    Files.add(FileInfo);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void loadOtherFile(ContentResolver contentResolver) {

        String sortOrder = MediaStore.Files.FileColumns._ID + " desc";
        Cursor cursor = contentResolver.query(
                //数据源
                MediaStore.Files.getContentUri("external"),
                //查询ID和名称
                new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.MIME_TYPE},
                //条件为文件类型
                MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ? ",

                new String[]{
                        "application/zip",
                        "application/rar",
                        "text/plain",
                },
                sortOrder);

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED));
                if (!TextUtils.isEmpty(data)) {
                    if (TextUtils.isEmpty(name)) {
                        name = data.substring(data.lastIndexOf("/") + 1);
                    }
                    if ("application/zip".equals(type) || "application/rar".equals(type)) {
                        type = "rar";
                    } else  if("text/plain".equals(type)){
                        type = "txt";
                    }
                    FileInfo FileInfo = new FileInfo(mId, id, name, data, size, time * 1000, false,type);
                    mId++;
                    OtherFiles.add(FileInfo);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private static boolean isExist(ContentResolver contentResolver, String filepath)
    {
        String sortOrder = MediaStore.Files.FileColumns._ID + " desc";
        Cursor cursor = contentResolver.query(
                //数据源
                MediaStore.Files.getContentUri("external"),
                //查询ID和名称
                new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.MIME_TYPE},
                //条件为文件类型
                MediaStore.Files.FileColumns.DATA + "= ? " ,

                new String[]{
                        filepath,
                },
                sortOrder);
        int count=0;
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
      return count!=0;
    }

    public List<FileInfo> getVideos() {
        return Videos;
    }

    public List<FileInfo> getImages() {
        return Images;
    }

    public List<FileInfo> getFiles() {
        return Files;
    }

    public List<FileInfo> getOtherFiles() {
        return OtherFiles;
    }

}

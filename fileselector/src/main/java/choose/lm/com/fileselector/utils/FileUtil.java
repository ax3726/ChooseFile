package choose.lm.com.fileselector.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 文件与流处理工具类
 * <p/>
 * Created by xw on 2016-8-1.
 */
public class FileUtil
{

	public static final String IMESSAGE_VOICE = getExternalStorePath() + "/ylwj_expert/voice";
	public static final String IMESSAGE_IMAGE = getExternalStorePath() + "/ylwj_expert/image";
	public static final String IMESSAGE_FILE = getExternalStorePath() + "/ylwj_expert/file";
	public static final String IMESSAGE_MOVES = getExternalStorePath() + "/ylwj_expert/moves";

	/**
	 * 初始化应用文件夹目录
	 */
	public static void initFileAccess() {

		File imessageDir = new File(IMESSAGE_VOICE);
		if (!imessageDir.exists()) {
			imessageDir.mkdir();
		}

		File imageDir = new File(IMESSAGE_IMAGE);
		if (!imageDir.exists()) {
			imageDir.mkdir();
		}
        File movesDir = new File(IMESSAGE_MOVES);
        if (! movesDir.exists()) {
            movesDir.mkdirs();
        }

	}

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static boolean fileIsExists(String filePath)
    {
        try
        {
            File f = new File(filePath);
            if (!f.exists())
            {
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath)
    {
        return getBitmapByPath(filePath, null);
    }

    /**
     * 获取突变通过Path
     *
     * @param filePath
     * @param opts
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath,
                                         BitmapFactory.Options opts)
    {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try
        {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                fis.close();
            } catch (Exception e)
            {
            }
        }
        return bitmap;
    }

    public static String path2StrByBase64(String path)
    {
        Bitmap bit = getBitmapByPath(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 图片转为base64
     * @param path    图片路径
     * @param isCompress  是否压缩
     * @return
     */
    public static String path2StrByBase64(String path, boolean isCompress)
    {
        Bitmap bit = getBitmapByPath(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(isCompress) {
            bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        }else{
            bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        }
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * 图片转为base64
     * @param bit    图片
     * @param isCompress  是否压缩
     * @return
     */
    public static String path2StrByBase64(Bitmap bit, boolean isCompress)
    {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(isCompress) {
            bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        }else{
            bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        }
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * <p>将文件转成base64 字符串</p>
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 是否有外存卡
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 外置存储卡的路径
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }


	/**
	 * 获取语音文件存储目录
	 * @return
	 */
	public static File getVoicePathName() {
		if (!isExistExternalStore()) {

			return null;
		}

		File directory = new File(IMESSAGE_VOICE);
		if (!directory.exists() && !directory.mkdirs()) {
			return null;
		}

		return directory;
	}

    public static String getFileNameFromUrl(String url){
        if ((url != null) && (url.length() > 0)) {
            int dot = url.lastIndexOf("/");
            if ((dot > -1) && (dot < (url.length() - 1))) {
                return url.substring(dot + 1);
            }
        }
        return "";
    }

    public static String getFileExtensionNameFromUrl(String url){
        if ((url != null) && (url.length() > 0)) {
            int dot = url.lastIndexOf('.');
            if ((dot > -1) && (dot < (url.length() - 1))) {
                return url.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 返回文件名
     * @param uri
     * @return
     */
    public static String getFilename(Uri uri) {
        File file = new File(uri.getPath());
        if(!file.exists()) {
            return "";
        }
        return file.getName();
    }

	/**
	 * Java文件操作 获取文件扩展名
	 * Get the file extension, if no extension or file name
	 *
	 */
	public static String getExtensionName(Uri uri) {
		String filename=uri.getPath();
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return "";
	}

    /**
	 * 返回图片存放目录
	 * @return
	 */
	public static File getImagePathName() {
		if (!isExistExternalStore()) {



			return null;
		}

		File directory = new File(IMESSAGE_IMAGE);
		if (!directory.exists() && !directory.mkdirs()) {

			return null;
		}

		return directory;
	}


	/**
	 * 返回文件存放目录
	 * @return
	 */
	public static File getFilePathName() {
		if (!isExistExternalStore()) {

			return null;
		}

		File directory = new File(IMESSAGE_FILE);
		if (!directory.exists() && !directory.mkdirs()) {

			return null;
		}

		return directory;
	}
    /**
     * 返回拍摄视频存放目录
     * @return
     */
    public static File getMovesPathName() {
        if (!isExistExternalStore()) {

            return null;
        }

        File directory = new File(IMESSAGE_MOVES);
        if (!directory.exists() && !directory.mkdirs()) {

            return null;
        }

        return directory;
    }
    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }
}

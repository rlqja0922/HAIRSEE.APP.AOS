package com.example.hairsee;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.hairsee.utils.SharedStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyImageUtils {
    public static String format_y_m_d = "yyyyMMdd";
    private static final String TAG = "ImageUtils";

    public static boolean saveBitMapImg(Bitmap imageData, String name, String extension, Context c) {//extension : jpg, png
        try {
            //저장할 파일 경로
            ContextWrapper contextWrapper = new ContextWrapper(c);
            String saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/헤보자";
            File file = new File(saveDir);
            if (!file.exists()) {
                file.mkdir();
            }

            String fileName = name + ".jpg";
            File tempFile = new File(saveDir, fileName);
            FileOutputStream output = null;

            try {
                if (tempFile.createNewFile()) {
                    output = new FileOutputStream(tempFile);
                    // 이미지 줄이기
                    // TODO : 사진 비율로 압축하도록 수정할 것
                    Bitmap newBitmap = imageData.createScaledBitmap(imageData, 500, 500, true);
                    // 이미지 압축. 압축된 파일은 output stream에 저장. 2번째 인자는 압축률인데 100으로 해도 많이 깨진다..
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                } else {
                    // 같은 이름의 파일 존재
                    Log.d("TEST_LOG", "같은 이름의 파일 존재:" + name);

                    return false;
                }
            } catch (FileNotFoundException e) {
                Log.d("TEST_LOG", "파일을 찾을 수 없음");
                return false;

            } catch (IOException e) {
                Log.d("TEST_LOG", "IO 에러");
                e.printStackTrace();
                return false;

            } finally {
                if (output != null) {
                    try {
                        output.flush();
                        output.close();
                        SharedStore.setImgPath(c,tempFile.getPath());
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Bitmap> getRecognitionFace(Context c, String employeeNum){
        List<Bitmap> imgList = new ArrayList<Bitmap>();
        for (int i = 0; i < getFileCount(c,employeeNum,"jpg"); i++){
            imgList.add(i, getBitmapInFileDir(c,employeeNum+"_"+i));
        }
        return imgList;
    }

    public static Bitmap getBitmapInFileDir(Context c, String name){
            File file = new File(c.getFilesDir()+"/capture/"+name+".jpg");
            Bitmap Bm = BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory.Options());
            return Bm;
    }

    public static int getFileCount(Context c, String name, String extension){
        int count = 0;
        final int countFileMax = 3;
        for (int i = 0; i < countFileMax; i++){
            File files = new File(c.getFilesDir() + "/capture/" + name + "_" + i + "." + extension);
            //파일 유무를 확인합니다.
            if(files.exists()==true) {
                //파일이 있을시
                count++;
            }
        }
        return count;
    }

    public static String bitmapToBase64(Bitmap bitmap){
        String base64 = "";
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d(TAG, "bitmapToBase64 성공 : ");
        }catch (Exception e){
            Log.d(TAG, "bitmapToBase64 실패 : " + e.getMessage());
        }
        return base64;
    }

    public static Bitmap base64ToBitmap(String base64Str){
        Bitmap bitmap = null;
        try{
            byte[] decodedByteArray = Base64.decode(base64Str, Base64.NO_WRAP);
            bitmap =  BitmapFactory.decodeByteArray(decodedByteArray, 0, base64Str.length());
            Log.d(TAG, "base64ToBitmap 성공 : " + bitmap.toString());
        }catch (Exception e){
            Log.d(TAG, "base64ToBitmap 실패 : " + e.getMessage());
        }
        return bitmap;
    }
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap imgRotate(Bitmap bitmap, String mCurrentPhotoPath){
        ExifInterface ei = null;
        Bitmap rotatedBitmap = null;//촬영 각도에 따른 조정
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    //카메라에 맞게 이미지 로테이션
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static boolean saveBitmapToJpeg(Context c, Bitmap bm, String imgName) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(c.getCacheDir(), imgName + ".jpg");    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bm.compress(Bitmap.CompressFormat.JPEG, 30, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            Log.d(TAG, "saveBitmapToJpeg: 성공");
            return true;
        } catch (Exception e) {
            Log.d(TAG, "saveBitmapToJpeg: 실패");
            return false;
        }
    }

    public static Bitmap getUserImgFromCacheDir(Context c, String imgName){
        Bitmap bm = null;
        try {
            String imgpath = c.getCacheDir() + "/" + imgName + ".jpg";   // 내부 저장소에 저장되어 있는 이미지 경로
            bm = BitmapFactory.decodeFile(imgpath);
        } catch (Exception e) { }
        return bm;
    }

}
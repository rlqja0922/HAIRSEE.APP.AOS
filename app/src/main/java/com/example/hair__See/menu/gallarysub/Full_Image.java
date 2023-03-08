package com.example.hair__See.menu.gallarysub;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleKt;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hair__See.MainActivity;
import com.example.hair__See.R;
import com.example.hair__See.ResultActivity;
import com.example.hair__See.utils.OnSingleClickListener;
import com.example.hair__See.utils.SharedStore;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.share.ShareClient;
import com.kakao.sdk.share.model.KakaoTalkSharingAttachment;
import com.kakao.sdk.share.model.SharingResult;
import com.kakao.sdk.talk.TalkApi;
import com.kakao.sdk.talk.TalkApiClient;
import com.kakao.sdk.talk.model.Friend;
import com.kakao.sdk.talk.model.Friends;
import com.kakao.sdk.template.model.CommerceTemplate;
import com.kakao.sdk.template.model.Content;
import com.kakao.sdk.template.model.FeedTemplate;
import com.kakao.sdk.template.model.Link;
import com.kakao.sdk.template.model.TextTemplate;
import com.kakao.sdk.user.UserApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function2;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Full_Image extends Fragment implements MainActivity.OnBackPressedListener{


    // TODO: Rename and change types of parameters
    private ConstraintLayout const_share,const_trash;
    private byte[] bitmapArr;
    private String path;
    private Bitmap bm;
    private Context context;
    private String mBasePath;
    private String Position;
    private View view;
    private ImageView fullimg,afterImg,beforeImg;
    String[] array,array2;
    String fileName,folderPath,folderPath_state,KakaoHash;
    private TextTemplate defaultText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bitmapArr = getArguments().getByteArray("bitmap");
            path = getArguments().getString("path");
            KakaoSdk.init(getContext(), "0578de7c972d4b11390d819f5ca1e59e");
            KakaoHash = KakaoSdk.INSTANCE.getKeyHash();
            Log.d("kakao", KakaoHash);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_full__image, container, false);
        if (getArguments() != null) {
            bitmapArr = getArguments().getByteArray("bitmap");
            path = getArguments().getString("path");
        }
        //  String saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/헤보자/"+status;
        bm = BitmapFactory.decodeFile(path);
        array = path.split("/");
        array2 = path.split("after");
        folderPath = array2[0];
        fileName = array[array.length-1];
        fullimg = view.findViewById(R.id.iv_full);
        fullimg.setImageBitmap(bm);
        afterImg = view.findViewById(R.id.afterImg);
        beforeImg = view.findViewById(R.id.beforeImg);
        const_share = view.findViewById(R.id.const_share);
        const_trash = view.findViewById(R.id.const_trash);
        folderPath_state = folderPath +"after/";

        const_share.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //카카오 링크 .....
//                if(ShareClient.getInstance().isKakaoTalkSharingAvailable(getContext())) {
//                File file = new File(folderPath_state, fileName);
//                Uri imageUri = FileProvider.getUriForFile(getContext(),"com.example.hair__see.fileprovider",file);
//                    Link link = new Link("https://developers.kakao.com");
//
//                    FeedTemplate feedTemplate = new FeedTemplate(new Content("title",imageUri.toString(),    //메시지 제목, 이미지 url
//                            new Link("https://www.naver.com"),"description",                    //메시지 링크, 메시지 설명
//                            300,300));
//                    FeedTemplate fdde;
//                    fdde = new FeedTemplate(new Content("",imageUri.toString(),link));
//                    defaultText = new TextTemplate(imageUri.toString(),  link);
//                    ShareClient.getInstance().shareDefault(getContext(), feedTemplate, null, (SharingResult sharingResult, Throwable error) -> {
//                                if (error != null) {
//                                    Log.e(TAG, "카카오톡 공유 실패", error);
//                                } else if (sharingResult != null) {
//                                    Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}");
//                                    startActivity(sharingResult.getIntent());
//
//                                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
//                                    Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}");
//                                    Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}");
//                                }
//                                return null;
//                            }
//                    );
//                }


                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                File file = new File(folderPath_state, fileName);
                File file1 = file;
                FileOutputStream fileOutputStream = null;
                FileOutputStream fileOutputStream1 = null;
                Uri imageUri = FileProvider.getUriForFile(getContext(),"com.example.hair__see.fileprovider",file);
                try {
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream1 = new FileOutputStream(file);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG,0,fileOutputStream);
                    imageUri = FileProvider.getUriForFile(getContext(),"com.example.hair__see.fileprovider",file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sharingIntent.setType("image/*");
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sharingIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                sharingIntent.putExtra("return-data", true);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent chooser = Intent.createChooser(sharingIntent, "Share File");

                List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getContext().grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                startActivity(chooser);
            }
        });
        const_trash.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                File file = new File(folderPath_state, fileName);
                boolean deleted = file.delete();
                Log.w(TAG, "기존 이미지 삭제 : " + deleted);
                FileOutputStream output = null;
            }
        });
        afterImg.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                afterImg.setImageResource(R.drawable.after1);
                beforeImg.setImageResource(R.drawable.before2);
                folderPath_state = folderPath +"after/";
                bm = BitmapFactory.decodeFile(folderPath_state+fileName);
                fullimg.setImageBitmap(bm);

            }
        });
        beforeImg.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                afterImg.setImageResource(R.drawable.after2);
                beforeImg.setImageResource(R.drawable.before1);
                folderPath_state = folderPath +"before/";
                bm = BitmapFactory.decodeFile(folderPath_state+fileName);
                fullimg.setImageBitmap(bm);
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {

    }
}
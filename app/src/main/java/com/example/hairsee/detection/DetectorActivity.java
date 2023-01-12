
/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.hairsee.detection;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hairsee.MyImageUtils;
import com.example.hairsee.MyUtils;
import com.example.hairsee.R;
import com.example.hairsee.ResultActivity;
import com.example.hairsee.detection.customview.OverlayView;
import com.example.hairsee.detection.env.BorderedText;
import com.example.hairsee.detection.env.ImageUtils;
import com.example.hairsee.detection.tflite.SimilarityClassifier;
import com.example.hairsee.detection.tflite.TFLiteObjectDetectionAPIModel;
import com.example.hairsee.detection.tracking.MultiBoxTracker;
import com.example.hairsee.utils.MyAlert;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
  public static int facesSize;
  public static String recogImg;
  public static DetectorActivity detectorActivity;

  public static Context mContext;

  // FaceNet
//  private static final int TF_OD_API_INPUT_SIZE = 160;
//  private static final boolean TF_OD_API_IS_QUANTIZED = false;
//  private static final String TF_OD_API_MODEL_FILE = "facenet.tflite";
//  //private static final String TF_OD_API_MODEL_FILE = "facenet_hiroki.tflite";

  // MobileFaceNet
  private static final int TF_OD_API_INPUT_SIZE = 112;
  private static final boolean TF_OD_API_IS_QUANTIZED = false;
  private static final String TF_OD_API_MODEL_FILE = "mobile_face_net.tflite";

  private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt";

  private static final DetectorMode MODE = DetectorMode.TF_OD_API;
  // Minimum detection confidence to track a detection.
  private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
  private static final boolean MAINTAIN_ASPECT = false;

  private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
  //private static final int CROP_SIZE = 320;
  //private static final Size CROP_SIZE = new Size(320, 320);


  private static final boolean SAVE_PREVIEW_BITMAP = false;
  private static final float TEXT_SIZE_DIP = 10;
  OverlayView trackingOverlay;
  private Integer sensorOrientation;

  private SimilarityClassifier detector;

  private long lastProcessingTimeMs;
  private Bitmap rgbFrameBitmap = null;
  private Bitmap croppedBitmap = null;
  private Bitmap cropCopyBitmap = null;
  private boolean computingDetection = false;
  private boolean addPending = false;

  private long timestamp = 0;
  private Matrix frameToCropTransform;
  private Matrix cropToFrameTransform;

  private MultiBoxTracker tracker;

  private BorderedText borderedText;

  // Face detector
  private com.google.mlkit.vision.face.FaceDetector faceDetector;

  // here the preview image is drawn in portrait way
  private Bitmap portraitBmp = null;
  // here the face is cropped and drawn
  private Bitmap faceBmp = null;

  private ImageView fab_cambt,fab_switchcam;

  //private HashMap<String, Classifier.Recognition> knownFaces = new HashMap<>();

  private String flagBtnRoute = "";//등록버튼, 출입 버튼 가르기 위한 변수
  private Map<String, float[]> userList = new HashMap<String, float[]>();
  private MyUtils myUtil = new MyUtils();
  private long recognitionTimeCount;
  private String prevName = "";
  private Toast toast;
  private static int SaveInfoNum = 5;
  private String TAG = "DetectorActivity";
  private ArrayList<SimilarityClassifier.Recognition> userRegisterFaceList = new ArrayList<>();
  public static ArrayList<SimilarityClassifier.Recognition> mappedRecognitionsList = new ArrayList<>();
  private boolean isSuccessUserFace;
  private TextView tvNoticeCamera;
  private int mCount;
  private boolean isShowDial;
  private static String result = "";
  private Camera.CameraInfo mCameraInfo;
  public String format_y_m_d = "yyMMddHHmmss";
  private Integer useFacing = null;
  private static final String KEY_USE_FACING = "use_facing";
  /**
   * 전체 프로세스 정리 등록
   * 1. 등록 버튼 클릭 [ fabAdd ] 하여 저장 플레그 값을 변경 [ addPending = true ]
   * 2. processImage() 함수가 계속 실행되고 있음
   * 3. 위 함수에서 얼굴 검출 성공 시 onFacesDetected() 함수 실행됨
   * 4. onFacesDetected() 함수는 검출된 얼굴에 UI로 표시해주는 함수. 매개변수로 currTimestamp(현재 시간), faces(얼굴 정보), addPending(저장 여부)
   * 5. onFacesDetected() 중에서도 recognizeImage() 에서 얼굴 인식을 진행함 매개변수로 bitmap 이미지를 넘김
   */

  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    ActionBar actionBar = getSupportActionBar();
//    getSupportActionBar().hide();
    detectorActivity = DetectorActivity.this;//외부에서 사용함
//    MainActivityRB.DetectorActivityFlag = true;
    myUtil.startVibrator(DetectorActivity.this);
    Intent intent = getIntent();
    useFacing = intent.getIntExtra(KEY_USE_FACING, CameraCharacteristics.LENS_FACING_FRONT);
//    tvNoticeCamera = findViewById(R.id.tvNoticeCamera);
    fab_cambt = findViewById(R.id.fab_cambt);
    fab_switchcam = findViewById(R.id.fab_switchcam);
    mContext = this;
    fab_cambt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d(TAG, "facessize1 : " + facesSize);
        onAddClick();
      }
    });
    fab_switchcam.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = getIntent();

        if (useFacing == CameraCharacteristics.LENS_FACING_FRONT) {
          useFacing = CameraCharacteristics.LENS_FACING_BACK;
        } else {
          useFacing = CameraCharacteristics.LENS_FACING_FRONT;
        }

        intent.putExtra(KEY_USE_FACING, useFacing);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        restartWith(intent);
      }
    });

    // Real-time contour detection of multiple faces
    FaceDetectorOptions options =
            new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                    .build();


    FaceDetector detector = FaceDetection.getClient(options);

    faceDetector = detector;


    //checkWritePermission();

    //ui 플래그 확인
    flagBtnRoute = getIntent().getStringExtra("btnFlag");
    Log.d(TAG, "onCreate: " + flagBtnRoute);
    if (flagBtnRoute.split("_")[0].equals("access")) {
      fab_cambt.setVisibility(View.INVISIBLE);

    }
  }
  private void restartWith(Intent intent) {
    finish();
    overridePendingTransition(0, 0);
    startActivity(intent);
    overridePendingTransition(0, 0);
  }


  private void onAddClick() {
    processImage();
    myUtil.startVibrator(DetectorActivity.this);
    addPending = true;
    if (facesSize >= 2) {
      Log.d(TAG, "facessize" + facesSize);
      MyAlert.MyDialog_single(DetectorActivity.this,  "2명 이상 감지 되었습니다.\n화면에 한 사람만 나타나게 해주세요", v -> {
        facesSize = 0;
        MyAlert.dialogrem.dismiss();
      });
    }else if (facesSize == 1){
      SimpleDateFormat dateformat = new SimpleDateFormat(format_y_m_d, Locale.KOREA);
      boolean save =  MyImageUtils.saveBitMapImg(croppedBitmap,dateformat.format(new Date())+
              ".jpg","before",DetectorActivity.this);
      if (save){
        Intent intent = new Intent(DetectorActivity.this, WaitActivity.class);
        intent.putExtra("hairType","1");
        intent.putExtra("hairColor","1");
        startActivity(intent);
      }
      if (croppedBitmap == null) {
        return;
      }

    }else if(facesSize == 0){
      MyAlert.MyDialog_single(DetectorActivity.this,  "얼굴 인식에 실패하였습니다.\n다시 촬영해주세요.",null);

      }
  }
  @Override
  public void onPreviewSizeChosen(final Size size, final int rotation) {
    final float textSizePx =
            TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
    borderedText.setTypeface(Typeface.MONOSPACE);

    tracker = new MultiBoxTracker(this);

    try {
      detector =
              TFLiteObjectDetectionAPIModel.create(
                      getAssets(),
                      TF_OD_API_MODEL_FILE,
                      TF_OD_API_LABELS_FILE,
                      TF_OD_API_INPUT_SIZE,
                      TF_OD_API_IS_QUANTIZED);
      //cropSize = TF_OD_API_INPUT_SIZE;
    } catch (final IOException e) {
      e.printStackTrace();
      Log.e("Err", "Exception initializing classifier!");
      detectorActivityFinish();
    }

    previewWidth = size.getWidth();
    previewHeight = size.getHeight();

    sensorOrientation = rotation - getScreenOrientation();
    Log.e("I","Camera orientation relative to screen canvas: "+ sensorOrientation );

    Log.e("I","Initializing at size " + previewWidth+ "x"+ previewHeight);
    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);

    int targetW, targetH;
    if (sensorOrientation == 90 || sensorOrientation == 270) {
      targetH = previewWidth;
      targetW = previewHeight;
    } else {
      targetW = previewWidth;
      targetH = previewHeight;
    }
//    int cropW = (int) (targetW / 2.0);
//    int cropH = (int) (targetH / 2.0);

    croppedBitmap = Bitmap.createBitmap(targetW, targetH, Config.ARGB_8888);

    portraitBmp = Bitmap.createBitmap(targetW, targetH, Config.ARGB_8888);
    faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Config.ARGB_8888);

    frameToCropTransform =
            ImageUtils.getTransformationMatrix(
                    previewWidth, previewHeight,
                    targetW, targetH,
                    sensorOrientation, MAINTAIN_ASPECT);

//    frameToCropTransform =
//            ImageUtils.getTransformationMatrix(
//                    previewWidth, previewHeight,
//                    previewWidth, previewHeight,
//                    sensorOrientation, MAINTAIN_ASPECT);

    cropToFrameTransform = new Matrix();
    frameToCropTransform.invert(cropToFrameTransform);


    Matrix frameToPortraitTransform =
            ImageUtils.getTransformationMatrix(
                    previewWidth, previewHeight,
                    targetW, targetH,
                    sensorOrientation, MAINTAIN_ASPECT);


    trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
    trackingOverlay.addCallback(
            new OverlayView.DrawCallback() {
              @Override
              public void drawCallback(final Canvas canvas) {
                tracker.draw(canvas);
                if (isDebug()) {
                  tracker.drawDebug(canvas);
                }
              }
            });

    tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
  }
  Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
      //이미지의 너비와 높이 결정
      int w = camera.getParameters().getPictureSize().width;
      int h = camera.getParameters().getPictureSize().height;


      //Log.d("MyTag","이미지 캡처 시 -> orientation : " + orientation);

      //byte array 를 bitmap 으로 변환
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inPreferredConfig = Bitmap.Config.ARGB_8888;
      Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
      //이미지를 디바이스 방향으로 회전
      Matrix matrix = new Matrix();
      matrix.postRotate(sensorOrientation);
      bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

//            SharedStore.setBitmap(getContext(), "MyData",bitmap);
//            Log.d(TAG,"bitmap"+SharedStore.getBitmap(getContext(),"MyData"));

      String CarImg = MyImageUtils.bitmapToBase64(bitmap);
//            String getImg = SharedStore.getCarImg(getContext());
//            Log.d(TAG, "이미지값 저장 :"+getImg);

      //bitmap을 byte array로 변환
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byte[] currentData = stream.toByteArray();
      Log.d(TAG, "Byte를 bitmap으로 : " + bitmap);
    }
  };
  /**
   * 안드로이드 디바이스 방향에 맞는 카메라 프리뷰를 화면에 보여주기 위해 계산합니다.
   */
  public static int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
    int degrees = 0;

    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360;  // compensate the mirror
    } else {  // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }

    return result;
  }
  @Override
  protected void processImage() {
    ++timestamp;
    final long currTimestamp = timestamp;
    trackingOverlay.postInvalidate();

    // No mutex needed as this method is not reentrant.
    if (computingDetection) {
      readyForNextImage();
      return;
    }
    computingDetection = true;


    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

    readyForNextImage();

    final Canvas canvas = new Canvas(croppedBitmap);
    canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
    // For examining the actual TF input.
    if (SAVE_PREVIEW_BITMAP) {
      ImageUtils.saveBitmap(croppedBitmap);
    }

    InputImage image = InputImage.fromBitmap(croppedBitmap, 0);
    faceDetector
            .process(image)
            .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
              @Override
              public void onSuccess(List<Face> faces) {// 첫 얼굴정보가 들어오는 곳. 이미 얼굴 정보가 여기애 들어 있을 것으로 추측함
                if (faces.size() == 0) {
                  updateResults(currTimestamp, new LinkedList<>(), false);
                  return;
                }
                runInBackground(
                        new Runnable() {
                          @Override
                          public void run() {
                            Log.d(TAG, "add 1 : " + addPending);
                            onFacesDetected(currTimestamp, faces, addPending);// 얼굴 저장 여부
                          }
                        });
              }
            });
  }

  @Override
  protected int getLayoutId() {
    return R.layout.tfe_od_camera_connection_fragment_tracking;
  }

  @Override
  protected Size getDesiredPreviewFrameSize() {
    return DESIRED_PREVIEW_SIZE;
  }

  // Which detection model to use: by default uses Tensorflow Object Detection API frozen
  // checkpoints.
  private enum DetectorMode {
    TF_OD_API;
  }

  @Override
  protected void setUseNNAPI(final boolean isChecked) {
    runInBackground(() -> detector.setUseNNAPI(isChecked));
  }

  @Override
  protected void setNumThreads(final int numThreads) {
    runInBackground(() -> detector.setNumThreads(numThreads));
  }

  // Face Processing
  private Matrix createTransform(
          final int srcWidth,
          final int srcHeight,
          final int dstWidth,
          final int dstHeight,
          final int applyRotation) {

    Matrix matrix = new Matrix();
    if (applyRotation != 0) {
      if (applyRotation % 90 != 0) {

      }

      // Translate so center of image is at origin.
      matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

      // Rotate around origin.
      matrix.postRotate(applyRotation);
    }

//        // Account for the already applied rotation, if any, and then determine how
//        // much scaling is needed for each axis.
//        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;
//        final int inWidth = transpose ? srcHeight : srcWidth;
//        final int inHeight = transpose ? srcWidth : srcHeight;

    if (applyRotation != 0) {

      // Translate back from origin centered reference to destination frame.
      matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
    }
    return matrix;

  }




  //얼굴 정보 저장하는 곳
  private boolean saveUserInfo(String name, SimilarityClassifier.Recognition rec) {
    String str = "";
    for (float f : ((float[][]) rec.getExtra())[0]) {
      str += (f + " ");
    }
//    Log.d("aaaaaaaaaa", "extra : "+str);


    synchronized (DetectorActivity.this) {
      getApplicationContext().getSharedPreferences("UserList", 0).edit().putString(name, str).apply();
    }
    return true;
  }


  private void updateResults(long currTimestamp, final List<SimilarityClassifier.Recognition> mappedRecognitions, boolean isAdd) {
    Log.d(TAG, "add 4 : " + isAdd);
    Log.d(TAG, "updateResults : " + mappedRecognitions.size());
    tracker.trackResults(mappedRecognitions, currTimestamp);
    trackingOverlay.postInvalidate();
    computingDetection = false;

    if (mappedRecognitions.size() > 0) {
      SimilarityClassifier.Recognition rec = mappedRecognitions.get(0);

      if (isAdd && rec.getExtra() != null) {

        if (userRegisterFaceList.size() >= 5) {
          userRegisterFaceList.set(SaveInfoNum - 1, rec);
        } else {
          userRegisterFaceList.add(rec);
        }

        isSuccessUserFace = true;

        if (userRegisterFaceList.size() >= SaveInfoNum && !isShowDial) {
          if (facesSize < 2) {
            facesSize = 0;
            isShowDial = true;
          }
        }
      }
    }

    runOnUiThread(
            new Runnable() {
              @Override
              public void run() {
                showFrameInfo(previewWidth + "x" + previewHeight);
                showCropInfo(croppedBitmap.getWidth() + "x" + croppedBitmap.getHeight());
                showInference(lastProcessingTimeMs + "ms");
              }
            });
  }

  private void onFacesDetected(long currTimestamp, List<Face> faces, boolean add) {

    if (add == true) {
      mCount++;
      if (mCount > 5) {
        addPending = false;
        mCount = 0;
      }
    }
    Log.d(TAG, "add 2 : " + add + ", mCount : " + mCount);
    Log.d(TAG, "onFacesDetected");
    cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
    final Canvas canvas = new Canvas(cropCopyBitmap);
    final Paint paint = new Paint();
    paint.setColor(Color.RED);
    paint.setStyle(Style.STROKE);
    paint.setStrokeWidth(2.0f);

    float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
    switch (MODE) {
      case TF_OD_API:
        minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
        break;
    }

    final List<SimilarityClassifier.Recognition> mappedRecognitions =
            new LinkedList<SimilarityClassifier.Recognition>();


    //final List<Classifier.Recognition> results = new ArrayList<>();

    // Note this can be done only once
    int sourceW = rgbFrameBitmap.getWidth();
    int sourceH = rgbFrameBitmap.getHeight();
    int targetW = portraitBmp.getWidth();
    int targetH = portraitBmp.getHeight();
    Matrix transform = createTransform(
            sourceW,
            sourceH,
            targetW,
            targetH,
            sensorOrientation);
    final Canvas cv = new Canvas(portraitBmp);

    // draws the original image in portrait mode.
    cv.drawBitmap(rgbFrameBitmap, transform, null);

    final Canvas cvFace = new Canvas(faceBmp);

    boolean saved = false;

    for (Face face : faces) {

      //results = detector.recognizeImage(croppedBitmap);

      final RectF boundingBox = new RectF(face.getBoundingBox());//사진에서 얼굴의 좌표를 만듬?

      //final boolean goodConfidence = result.getConfidence() >= minimumConfidence;
      final boolean goodConfidence = true; //face.get;
      if (boundingBox != null && goodConfidence) {

        // maps crop coordinates to original
        cropToFrameTransform.mapRect(boundingBox);

        // maps original coordinates to portrait coordinates
        RectF faceBB = new RectF(boundingBox);
        transform.mapRect(faceBB);

        // translates portrait to origin and scales to fit input inference size
        //cv.drawRect(faceBB, paint);
        float sx = ((float) TF_OD_API_INPUT_SIZE) / faceBB.width();
        float sy = ((float) TF_OD_API_INPUT_SIZE) / faceBB.height();
        Matrix matrix = new Matrix();
        matrix.postTranslate(-faceBB.left, -faceBB.top);
        matrix.postScale(sx, sy);

        cvFace.drawBitmap(portraitBmp, matrix, null);

        //canvas.drawRect(faceBB, paint);

        String label = "";
        float confidence = -1f;
        Integer color = Color.BLUE;
        Object extra = null;
        Bitmap crop = null;

        final long startTime = SystemClock.uptimeMillis();
        final List<SimilarityClassifier.Recognition> resultsAux = detector.recognizeImage(faceBmp, add);//여기서 얼굴 인식
        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

        if (resultsAux.size() > 0) {//인식된 얼굴이 한개 이상 있으면 실행

          SimilarityClassifier.Recognition result = resultsAux.get(0);

          extra = result.getExtra();
//          Object extra = result.getExtra();
//          if (extra != null) {
//            LOGGER.i("embeeding retrieved " + extra.toString());
//          }

          float conf = result.getDistance();
          if (conf < 1.0f) {// 등록된 사람인지 여부는 여기서 가름?
            confidence = conf;
            label = result.getTitle();//이름
            if (result.getId().equals("0")) {
              color = Color.WHITE;
            } else {
              color = Color.RED;
            }
          }
        }

        if (getCameraFacing() == CameraCharacteristics.LENS_FACING_FRONT) {

          // camera is frontal so the image is flipped horizontally
          // flips horizontally
          Matrix flip = new Matrix();
          if (sensorOrientation == 90 || sensorOrientation == 270) {
            flip.postScale(1, -1, previewWidth / 2.0f, previewHeight / 2.0f);
          } else {
            flip.postScale(-1, 1, previewWidth / 2.0f, previewHeight / 2.0f);
          }
          //flip.postScale(1, -1, targetW / 2.0f, targetH / 2.0f);
          flip.mapRect(boundingBox);
        }

        if (add) {//크롭 이미지 생성하는 곳
          try {
            crop = Bitmap.createBitmap(portraitBmp,
                    (int) faceBB.left,
                    (int) faceBB.top,
                    (int) faceBB.width(),
                    (int) faceBB.height());
          } catch (IllegalArgumentException e) {
            MyAlert.MyDialog_single(DetectorActivity.this, "얼굴을 흰 가이드 라인에 맞춰 주시고\n흔들리지 않게 주의 해주세요", null);
//            showDialogInfo("얼굴을 흰 가이드 라인에 맞춰주시고\n흔들리지 않게 주의 해주세요.");
          }
        }

        if (faces.size() >= 2) {
          color = Color.RED;
          label = "2명 이상 감지!!";
          facesSize = faces.size();
//          result = "two";
//          switch (result) {
//            case "two" :
//              MyAlert.MyDialog_single(DetectorActivity.this, "경고!!", "2명 이상 감지 되었습니다.\n화면에 한 사람만 나타나게 해주세요", null);
//              break;
//          }


          if (flagBtnRoute.equals("registraion") && !isShowDial) {
            facesSize = faces.size();
//                MyAlert.MyDialog_single(DetectorActivity.this, "경고!!", "2명 이상 감지 되었습니다.\n화면에 한 사람만 나타나게 해주세요", null);
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                  @Override
//                  public void run() {
//                    finish();
//                  }
//                }, 1000);
          }
          facesSize = faces.size();
//                  Intent intent = new Intent(DetectorActivity.this, MainActivity.class);
//                  mContext.startActivity(intent);
        }
        facesSize = faces.size();

        final SimilarityClassifier.Recognition result = new SimilarityClassifier.Recognition(
                "0", label, confidence, boundingBox);//label 이 이름 같음

        result.setColor(color);
        result.setLocation(boundingBox);
        result.setExtra(extra);//여기서 얼굴 정보 저장되는 곳
        result.setCrop(crop);
        mappedRecognitions.add(result);

        if (mappedRecognitionsList.size() == 0) {
          mappedRecognitionsList.add(result);
        } else {
          mappedRecognitionsList.set(0, result);
        }

        //여기서 안면 체크함
      }
    }
//    if (faces.size() >= 2) {
//      MyAlert.MyDialog_single(DetectorActivity.this, "경고!!", "2명 이상 감지 되었습니다.\n화면에 한 사람만 나타나게 해주세요", null);
//      Handler handler = new Handler();
//      handler.postDelayed(new Runnable() {
//        @Override
//        public void run() {
//          finish();
//        }
//      }, 1000);
//    }
    facesSize = faces.size();
    updateResults(currTimestamp, mappedRecognitions, add);//얼굴인식된 결과를 뿌리는 곳?
  }


  private void showDialogInfo(String s) {
    addPending = false;
    isShowDial = true;
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("경고!!");
    builder.setMessage(s);
    builder.setNegativeButton(Html.fromHtml("<font color='#000000'>" + "확인" + "</font>"), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dlg, int i) {
        isShowDial = false;
        userRegisterFaceList.clear();
      }
    });
    builder.setCancelable(false);
    builder.show();
  }

  private Pair<String, Float> matchFace(float[] emb) {
    Pair<String, Float> ret = null;
    for (String name : userList.keySet()) {               //등록된 모든 사람과 비교함
      final float[] knownEmb = userList.get(name);

      float distance = 0;
      for (int i = 0; i < emb.length; i++) {
        float diff = emb[i] - knownEmb[i];
        distance += diff * diff;
      }
      distance = (float) Math.sqrt(distance);
      if (ret == null || distance < ret.second) {
        ret = new Pair<>(name.substring(1), distance);
      }
    }
    return ret;
  }

  @Override
  public void onBackPressed() {

    detectorActivityFinish();
  }

  public void showToastMsg(String msg) {
    try {
      toast.cancel();
    } catch (NullPointerException e) {

    }
    toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
    toast.show();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  public static void detectorActivityFinish(){
    detectorActivity.finish();
  }
  public boolean saveImage(Bitmap bitmap, String saveImageName) {
    String saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/directoryName";
    File file = new File(saveDir);
    if (!file.exists()) {
      file.mkdir();
    }

    String fileName = saveImageName + ".png";
    File tempFile = new File(saveDir, fileName);
    FileOutputStream output = null;

    try {
      if (tempFile.createNewFile()) {
        output = new FileOutputStream(tempFile);
        // 이미지 줄이기
        // TODO : 사진 비율로 압축하도록 수정할 것
        Bitmap newBitmap = bitmap.createScaledBitmap(bitmap, 200, 200, true);
        // 이미지 압축. 압축된 파일은 output stream에 저장. 2번째 인자는 압축률인데 100으로 해도 많이 깨진다..
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
      } else {
        // 같은 이름의 파일 존재
        Log.d("TEST_LOG", "같은 이름의 파일 존재:"+saveImageName);

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
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return true;
  }

}
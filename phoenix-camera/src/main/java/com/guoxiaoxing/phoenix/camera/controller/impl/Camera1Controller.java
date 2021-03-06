package com.guoxiaoxing.phoenix.camera.controller.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;

import com.guoxiaoxing.phoenix.camera.config.CameraConfig;
import com.guoxiaoxing.phoenix.camera.config.CameraConfigProvider;
import com.guoxiaoxing.phoenix.camera.controller.CameraController;
import com.guoxiaoxing.phoenix.camera.controller.view.CameraView;
import com.guoxiaoxing.phoenix.camera.listener.CameraResultListener;
import com.guoxiaoxing.phoenix.camera.manager.CameraManager;
import com.guoxiaoxing.phoenix.camera.manager.impl.Camera1Manager;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraCloseListener;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraOpenListener;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraPictureListener;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraVideoListener;
import com.guoxiaoxing.phoenix.camera.util.CameraHelper;
import com.guoxiaoxing.phoenix.camera.util.Size;
import com.guoxiaoxing.phoenix.camera.widget.AutoFitSurfaceView;

import java.io.File;

/**
 * The camera controller for camera1
 * <p>
 * For more information, you can visit https://github.com/guoxiaoxing or contact me by
 * guoxiaoxingse@163.com.
 *
 * @author guoxiaoxing
 */
@SuppressWarnings("deprecation")
public class Camera1Controller implements CameraController<Integer>, CameraOpenListener<Integer, SurfaceHolder.Callback>
        , CameraPictureListener, CameraCloseListener<Integer>, CameraVideoListener {

    private final static String TAG = "Camera1Controller";

    private final Context mContext;
    private Integer mCameraId;
    private File mOutputFile;
    private CameraView mCameraView;
    private CameraConfigProvider mCameraConfigProvider;
    private CameraManager<Integer, SurfaceHolder.Callback> mCameraManager;

    public Camera1Controller(Context context, CameraView cameraView, CameraConfigProvider cameraConfigProvider) {
        this.mContext = context;
        this.mCameraView = cameraView;
        this.mCameraConfigProvider = cameraConfigProvider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mCameraManager = new Camera1Manager();
        mCameraManager.initializeCameraManager(mCameraConfigProvider, mContext);
        setmCameraId(mCameraManager.getFaceBackCameraId());
    }

    private void setmCameraId(Integer cameraId) {
        this.mCameraId = cameraId;
        mCameraManager.setCameraId(cameraId);
    }

    @Override
    public void onResume() {
        mCameraManager.openCamera(mCameraId, this);
    }

    @Override
    public void onPause() {
        mCameraManager.closeCamera(null);
    }

    @Override
    public void onDestroy() {
        mCameraManager.releaseCameraManager();
    }

    @Override
    public void takePhoto(CameraResultListener callback) {
        takePhoto(callback, null, null);
    }

    @Override
    public void takePhoto(CameraResultListener callback, @Nullable String direcoryPath, @Nullable String fileName) {
        mOutputFile = CameraHelper.getOutputMediaFile(mContext, CameraConfig.MEDIA_ACTION_PHOTO, direcoryPath, fileName);
        mCameraManager.takePicture(mOutputFile, this, callback);
    }

    @Override
    public void startVideoRecord() {
        startVideoRecord(null, null);
    }

    @Override
    public void startVideoRecord(@Nullable String direcoryPath, @Nullable String fileName) {
        mOutputFile = CameraHelper.getOutputMediaFile(mContext, CameraConfig.MEDIA_ACTION_VIDEO, direcoryPath, fileName);
        mCameraManager.startVideoRecord(mOutputFile, this);
    }

    @Override
    public void stopVideoRecord(CameraResultListener callback) {
        mCameraManager.stopVideoRecord(callback);
    }

    @Override
    public boolean isVideoRecording() {
        return mCameraManager.isVideoRecording();
    }

    @Override
    public void switchCamera(@CameraConfig.CameraFace final int cameraFace) {
        final Integer backCameraId = mCameraManager.getFaceBackCameraId();
        final Integer frontCameraId = mCameraManager.getFaceFrontCameraId();
        final Integer currentCameraId = mCameraManager.getCurrentCameraId();

        if (cameraFace == CameraConfig.CAMERA_FACE_REAR && backCameraId != null) {
            setmCameraId(backCameraId);
            mCameraManager.closeCamera(this);
        } else if (frontCameraId != null && !frontCameraId.equals(currentCameraId)) {
            setmCameraId(frontCameraId);
            mCameraManager.closeCamera(this);
        }
    }

    @Override
    public void setFlashMode(@CameraConfig.FlashMode int flashMode) {
        mCameraManager.setFlashMode(flashMode);
    }

    @Override
    public void switchQuality() {
        mCameraManager.closeCamera(this);
    }

    @Override
    public int getNumberOfCameras() {
        return mCameraManager.getNumberOfCameras();
    }

    @Override
    public int getMediaAction() {
        return mCameraConfigProvider.getMediaAction();
    }

    @Override
    public File getOutputFile() {
        return mOutputFile;
    }

    @Override
    public Integer getCameraId() {
        return mCameraId;
    }

    @Override
    public void onCameraOpened(Integer cameraId, Size previewSize, SurfaceHolder.Callback surfaceCallback) {
        mCameraView.updateUiForMediaAction(mCameraConfigProvider.getMediaAction());
        mCameraView.updateCameraPreview(previewSize, new AutoFitSurfaceView(mContext, surfaceCallback));
        mCameraView.updateCameraSwitcher(getNumberOfCameras());
    }

    @Override
    public void onCameraOpenError() {
        Log.e(TAG, "onCameraOpenError");
    }

    @Override
    public void onCameraClosed(Integer closedCameraId) {
        mCameraView.releaseCameraPreview();

        mCameraManager.openCamera(mCameraId, this);
    }

    @Override
    public void onPictureTaken(byte[] bytes, File photoFile, CameraResultListener callback) {
        mCameraView.onPictureTaken(bytes, callback);
    }

    @Override
    public void onPictureTakeError() {
    }

    @Override
    public void onVideoRecordStarted(Size videoSize) {
        mCameraView.onVideoRecordStart(videoSize.getWidth(), videoSize.getHeight());
    }

    @Override
    public void onVideoRecordStopped(File videoFile, @Nullable CameraResultListener callback) {
        mCameraView.onVideoRecordStop(callback);
    }

    @Override
    public void onVideoRecordError() {

    }

    @Override
    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    @Override
    public CharSequence[] getVideoQualityOptions() {
        return mCameraManager.getVideoQualityOptions();
    }

    @Override
    public CharSequence[] getPhotoQualityOptions() {
        return mCameraManager.getPictureQualityOptions();
    }
}

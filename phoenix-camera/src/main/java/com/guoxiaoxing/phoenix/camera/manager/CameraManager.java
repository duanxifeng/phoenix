package com.guoxiaoxing.phoenix.camera.manager;

import android.content.Context;

import com.guoxiaoxing.phoenix.camera.config.CameraConfig;
import com.guoxiaoxing.phoenix.camera.config.CameraConfigProvider;
import com.guoxiaoxing.phoenix.camera.listener.CameraResultListener;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraCloseListener;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraOpenListener;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraPictureListener;
import com.guoxiaoxing.phoenix.camera.manager.listener.CameraVideoListener;
import com.guoxiaoxing.phoenix.camera.util.Size;

import java.io.File;

/**
 * The camera manager for manage camera resource
 * <p>
 * For more information, you can visit https://github.com/guoxiaoxing or contact me by
 * guoxiaoxingse@163.com.
 *
 * @author guoxiaoxing
 */
public interface CameraManager<CameraId, SurfaceListener> {

    void initializeCameraManager(CameraConfigProvider cameraConfigProvider, Context context);

    void releaseCameraManager();

    void openCamera(CameraId cameraId, CameraOpenListener<CameraId, SurfaceListener> cameraOpenListener);

    void closeCamera(CameraCloseListener<CameraId> cameraCloseListener);

    void takePicture(File photoFile, CameraPictureListener cameraPictureListener, CameraResultListener callback);

    void startVideoRecord(File videoFile, CameraVideoListener cameraVideoListener);

    void stopVideoRecord(CameraResultListener callback);

    boolean isVideoRecording();

    void setCameraId(CameraId cameraId);

    void setFlashMode(@CameraConfig.FlashMode int flashMode);

    CameraId getCurrentCameraId();

    CameraId getFaceFrontCameraId();

    CameraId getFaceBackCameraId();

    int getNumberOfCameras();

    int getFaceFrontCameraOrientation();

    int getFaceBackCameraOrientation();

    Size getPictureSizeForQuality(@CameraConfig.MediaQuality int mediaQuality);

    CharSequence[] getPictureQualityOptions();

    CharSequence[] getVideoQualityOptions();
}

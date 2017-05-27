/*
 * Copyright (C) 2017 Peng fei Pan <sky@xiaopan.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xiaopan.sketch.viewfun;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import me.xiaopan.sketch.SLog;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.ImageFrom;
import me.xiaopan.sketch.viewfun.large.LargeImageViewer;
import me.xiaopan.sketch.viewfun.zoom.ImageZoomer;

/**
 * 这个类负责提供各种function开关和属性设置
 */
public abstract class FunctionPropertyView extends FunctionCallbackView {

    public FunctionPropertyView(Context context) {
        super(context);
    }

    public FunctionPropertyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FunctionPropertyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 是否开启了暂停下载时点击重试功能
     */
    @SuppressWarnings("unused")
    public boolean isClickRetryOnPauseDownloadEnabled() {
        return getFunctions().clickRetryFunction != null && getFunctions().clickRetryFunction.isClickRetryOnPauseDownloadEnabled();
    }

    /**
     * 设置当暂停下载的时候点击显示图片
     */
    public void setClickRetryOnPauseDownloadEnabled(boolean clickRetryOnPauseDownloadEnabled) {
        if (getFunctions().clickRetryFunction == null) {
            getFunctions().clickRetryFunction = new ClickRetryFunction(this);
        }
        getFunctions().clickRetryFunction.setClickRetryOnPauseDownloadEnabled(clickRetryOnPauseDownloadEnabled);
        updateClickable();
    }

    /**
     * 设置当暂停下载的时候点击显示图片
     */
    @Deprecated
    // TODO: 2017/5/27 类似的这种方法，改名
    public void setClickRetryOnPauseDownload(boolean clickRetryOnPauseDownload) {
        setClickRetryOnPauseDownloadEnabled(clickRetryOnPauseDownload);
    }

    /**
     * 是否开启了显示失败时点击重试功能
     */
    @SuppressWarnings("unused")
    public boolean isClickRetryOnDisplayErrorEnabled() {
        return getFunctions().clickRetryFunction != null && getFunctions().clickRetryFunction.isClickRetryOnDisplayErrorEnabled();
    }

    /**
     * 设置当失败的时候点击重新显示图片
     */
    public void setClickRetryOnDisplayErrorEnabled(boolean clickRetryOnDisplayErrorEnabled) {
        if (getFunctions().clickRetryFunction == null) {
            getFunctions().clickRetryFunction = new ClickRetryFunction(this);
        }
        getFunctions().clickRetryFunction.setClickRetryOnDisplayErrorEnabled(clickRetryOnDisplayErrorEnabled);
        updateClickable();
    }

    /**
     * 设置当失败的时候点击重新显示图片
     */
    @Deprecated
    public void setClickRetryOnError(boolean clickRetryOnError) {
        setClickRetryOnDisplayErrorEnabled(clickRetryOnError);
    }

    /**
     * 是否开启了点击播放gif功能
     */
    @SuppressWarnings("unused")
    public boolean isClickPlayGifEnabled() {
        return getFunctions().clickPlayGifFunction != null;
    }

    /**
     * 设置开启点击播放gif功能
     *
     * @param playIconDrawable 播放图标
     */
    public void setClickPlayGifEnabled(Drawable playIconDrawable) {
        if (playIconDrawable != null) {
            getFunctions().clickPlayGifFunction = new ClickPlayGifFunction(this, playIconDrawable);
        } else {
            getFunctions().clickPlayGifFunction = null;
        }
        updateClickable();
        invalidate();
    }

    /**
     * 设置开启点击播放gif功能
     *
     * @param playIconResId 播放图标资源ID
     */
    public void setClickPlayGifEnabled(int playIconResId) {
        setClickPlayGifEnabled(playIconResId > 0 ? getResources().getDrawable(playIconResId) : null);
    }

    /**
     * 是否显示下载进度
     */
    @SuppressWarnings("unused")
    public boolean isShowDownloadProgress() {
        return getFunctions().showProgressFunction != null;
    }

    /**
     * 设置是否显示下载进度，开启后会在ImageView表面覆盖一层默认为黑色半透明的蒙层来显示进度
     */
    public void setShowDownloadProgress(boolean showDownloadProgress) {
        if (showDownloadProgress) {
            if (getFunctions().showProgressFunction == null) {
                getFunctions().showProgressFunction = new ShowProgressFunction(this, getFunctions().imageShapeFunction);
            }
        } else {
            getFunctions().showProgressFunction = null;
        }
    }

    /**
     * 设置下载进度蒙层的颜色
     */
    @SuppressWarnings("unused")
    public void setDownloadProgressColor(int downloadProgressColor) {
        if (getFunctions().showProgressFunction != null) {
            getFunctions().showProgressFunction.setDownloadProgressColor(downloadProgressColor);
        }
    }

    /**
     * 是否显示按下状态
     */
    @SuppressWarnings("unused")
    public boolean isShowPressedStatus() {
        return getFunctions().showPressedFunction != null;
    }

    /**
     * 设置是否显示按下状态，开启后按下的时候会在ImageView表面覆盖一个黑色半透明图层，长按的时候还会有类似Android5.0的涟漪效果。此功能需要注册点击事件或设置Clickable为true
     */
    public void setShowPressedStatus(boolean showPressedStatus) {
        if (showPressedStatus) {
            if (getFunctions().showPressedFunction == null) {
                getFunctions().showPressedFunction = new ShowPressedFunction(this, getFunctions().imageShapeFunction);
            }
        } else {
            getFunctions().showPressedFunction = null;
        }
    }

    /**
     * 设置按下状态的颜色
     */
    @SuppressWarnings("unused")
    public void setPressedStatusColor(int pressedStatusColor) {
        if (getFunctions().showPressedFunction != null) {
            getFunctions().showPressedFunction.setPressedStatusColor(pressedStatusColor);
        }
    }

    /**
     * 是否显示图片来源
     */
    @SuppressWarnings("unused")
    public boolean isShowImageFrom() {
        return getFunctions().showImageFromFunction != null;
    }

    /**
     * 设置是否显示图片来源，开启后会在View的左上角显示一个纯色三角形，红色代表本次是从网络加载的，黄色代表本次是从本地加载的，绿色代表本次是从内存缓存加载的，绿色代表本次是从内存加载的
     */
    public void setShowImageFrom(boolean showImageFrom) {
        ShowImageFromFunction oldShowImageFromFunction = getFunctions().showImageFromFunction;

        if (showImageFrom) {
            getFunctions().showImageFromFunction = new ShowImageFromFunction(this);
            getFunctions().showImageFromFunction.onDrawableChanged("setShowImageFrom", null, getDrawable());
        } else {
            getFunctions().showImageFromFunction = null;
        }

        if (oldShowImageFromFunction != getFunctions().showImageFromFunction) {
            invalidate();
        }
    }

    /**
     * 是否显示GIF标识
     */
    @SuppressWarnings("unused")
    public boolean isShowGifFlag() {
        return getFunctions().showGifFlagFunction != null;
    }

    /**
     * 设置GIF标识图片
     */
    @SuppressWarnings("unused")
    public void setShowGifFlag(int gifFlagDrawableResId) {
        setShowGifFlag(gifFlagDrawableResId > 0 ? getResources().getDrawable(gifFlagDrawableResId) : null);
    }

    /**
     * 设置是否显示GIF标识
     */
    @SuppressWarnings("unused")
    public void setShowGifFlag(Drawable gifFlagDrawable) {
        if (gifFlagDrawable != null) {
            getFunctions().showGifFlagFunction = new ShowGifFlagFunction(this, gifFlagDrawable);
            getFunctions().showGifFlagFunction.onDrawableChanged("setShowGifFlag", null, getDrawable());
        } else {
            getFunctions().showGifFlagFunction = null;
        }
        invalidate();
    }

    /**
     * 获取图片形状，下载进度和按下效果的蒙层会适应此形状
     */
    @SuppressWarnings("unused")
    public SketchImageView.ImageShape getImageShape() {
        return getFunctions().imageShapeFunction != null ? getFunctions().imageShapeFunction.getImageShape() : null;
    }

    /**
     * 设置图片形状，下载进度和按下效果的蒙层会适应此形状
     */
    public void setImageShape(SketchImageView.ImageShape imageShape) {
        if (getFunctions().imageShapeFunction != null) {
            getFunctions().imageShapeFunction.setImageShape(imageShape);
        }
    }

    /**
     * 获取图片形状的圆角角度，只有图片形状是ROUNDED_RECT的时候此参数才有用
     */
    @SuppressWarnings("unused")
    public float[] getImageShapeCornerRadius() {
        return getFunctions().imageShapeFunction != null ? getFunctions().imageShapeFunction.getCornerRadius() : null;
    }

    /**
     * 设置图片形状的圆角角度，只有图片形状是ROUNDED_RECT的时候此参数才有用
     */
    @SuppressWarnings("unused")
    public void setImageShapeCornerRadius(float radius) {
        if (getFunctions().imageShapeFunction != null) {
            getFunctions().imageShapeFunction.setCornerRadius(radius);
        }
    }

    /**
     * 设置图片形状的圆角角度，只有图片形状是ROUNDED_RECT的时候此参数才有用
     */
    @SuppressWarnings("unused")
    public void setImageShapeCornerRadius(float[] radiis) {
        if (getFunctions().imageShapeFunction != null) {
            getFunctions().imageShapeFunction.setCornerRadius(radiis);
        }
    }

    /**
     * 设置图片形状的圆角角度，只有图片形状是ROUNDED_RECT的时候此参数才有用
     */
    @SuppressWarnings("unused")
    public void setImageShapeCornerRadius(float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius) {
        if (getFunctions().imageShapeFunction != null) {
            getFunctions().imageShapeFunction.setCornerRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        }
    }

    /**
     * 获取图片来源
     */
    @SuppressWarnings("unused")
    public ImageFrom getImageFrom() {
        return getFunctions().showImageFromFunction != null ? getFunctions().showImageFromFunction.getImageFrom() : null;
    }

    /**
     * 是否支持缩放
     */
    public boolean isSupportZoom() {
        return getFunctions().zoomFunction != null;
    }

    /**
     * 设置是否支持缩放
     */
    public void setSupportZoom(boolean supportZoom) {
        if (!supportZoom && isSupportLargeImage()) {
            SLog.w("You can't close the zoom function, because of large image function need it");
            return;
        }

        if (getFunctions().zoomFunction != null) {
            getFunctions().zoomFunction.setFromLargeImageFunction(false);
        }

        if (supportZoom == isSupportZoom()) {
            return;
        }

        if (supportZoom) {
            getFunctions().zoomFunction = new ImageZoomFunction(this);
            getFunctions().zoomFunction.onDrawableChanged("setSupportZoom", null, getDrawable());
        } else {
            getFunctions().zoomFunction.recycle();
            ScaleType scaleType = getFunctions().zoomFunction.getScaleType();
            getFunctions().zoomFunction = null;

            // 恢复ScaleType
            setScaleType(scaleType);
        }
    }

    /**
     * 获取缩放功能控制对象
     */
    public ImageZoomer getImageZoomer() {
        return getFunctions().zoomFunction != null ? getFunctions().zoomFunction.getImageZoomer() : null;
    }

    @Override
    public boolean isSupportLargeImage() {
        return getFunctions().largeImageFunction != null;
    }

    /**
     * 设置是否支持大图片
     */
    public void setSupportLargeImage(boolean supportLargeImage) {
        if (supportLargeImage == isSupportLargeImage()) {
            return;
        }

        if (supportLargeImage) {
            // 要想使用大图功能就必须开启缩放功能
            if (!isSupportZoom()) {
                setSupportZoom(true);
                getFunctions().zoomFunction.setFromLargeImageFunction(true);
            }

            getFunctions().largeImageFunction = new LargeImageFunction(this);
            getFunctions().largeImageFunction.bindImageZoomer(getImageZoomer());

            // 大图功能开启后对ImageZoomer计算缩放比例有影响，因此要重置一下
            getFunctions().zoomFunction.onDrawableChanged("setSupportLargeImage", null, getDrawable());

            getFunctions().largeImageFunction.onDrawableChanged("setSupportLargeImage", null, getDrawable());
        } else {
            getFunctions().largeImageFunction.recycle("setSupportLargeImage");
            getFunctions().largeImageFunction = null;

            if (isSupportZoom()) {
                // 大图功能关闭后对ImageZoomer计算缩放比例有影响，因此要重置一下
                getFunctions().zoomFunction.onDrawableChanged("setSupportLargeImage", null, getDrawable());

                if (getFunctions().zoomFunction.isFromLargeImageFunction()) {
                    setSupportZoom(false);
                }
            }
        }
    }

    /**
     * 获取大图查看器
     */
    @SuppressWarnings("unused")
    public LargeImageViewer getLargeImageViewer() {
        return getFunctions().largeImageFunction != null ? getFunctions().largeImageFunction.getLargeImageViewer() : null;
    }
}
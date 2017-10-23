/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package me.xiaopan.sketch.zoom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.OverScroller;
import android.widget.Scroller;

public abstract class ScrollerProxy {

    @SuppressLint("ObsoleteSdkInt")
    public static ScrollerProxy getScroller(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new IcsScroller(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return new GingerScroller(context);
        } else {
            return new PreGingerScroller(context);
        }
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY,
                               int maxY, int overX, int overY);

    public abstract void forceFinished(boolean finished);

    public abstract boolean isFinished();

    public abstract int getCurrX();

    public abstract int getCurrY();

    @TargetApi(9)
    public static class GingerScroller extends ScrollerProxy {

        protected final OverScroller mScroller;
        private boolean mFirstScroll = false;

        public GingerScroller(Context context) {
            mScroller = new OverScroller(context);
        }

        @Override
        public boolean computeScrollOffset() {
            // Workaround for first scroll returning 0 for the direction of the edge it hits.
            // Simply recompute values.
            if (mFirstScroll) {
                mScroller.computeScrollOffset();
                mFirstScroll = false;
            }
            return mScroller.computeScrollOffset();
        }

        @Override
        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY,
                          int overX, int overY) {
            mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
        }

        @Override
        public void forceFinished(boolean finished) {
            mScroller.forceFinished(finished);
        }

        @Override
        public boolean isFinished() {
            return mScroller.isFinished();
        }

        @Override
        public int getCurrX() {
            return mScroller.getCurrX();
        }

        @Override
        public int getCurrY() {
            return mScroller.getCurrY();
        }
    }

    @TargetApi(14)
    public static class IcsScroller extends GingerScroller {

        public IcsScroller(Context context) {
            super(context);
        }

        @Override
        public boolean computeScrollOffset() {
            return mScroller.computeScrollOffset();
        }
    }

    public static class PreGingerScroller extends ScrollerProxy {

        private final Scroller mScroller;

        public PreGingerScroller(Context context) {
            mScroller = new Scroller(context);
        }

        @Override
        public boolean computeScrollOffset() {
            return mScroller.computeScrollOffset();
        }

        @Override
        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY,
                          int overX, int overY) {
            mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
        }

        @Override
        public void forceFinished(boolean finished) {
            mScroller.forceFinished(finished);
        }

        public boolean isFinished() {
            return mScroller.isFinished();
        }

        @Override
        public int getCurrX() {
            return mScroller.getCurrX();
        }

        @Override
        public int getCurrY() {
            return mScroller.getCurrY();
        }
    }
}

package org.mini.frame.toolkit.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

public class MiniKeyBoardListener
{
    private static float OBSERVED_KEYBOARD_HEIGHT = 200;

    private static MiniKeyboardObserver observer = new MiniKeyboardObserver();

    public static void removeListener()
    {
        observer.removeListener();
    }

    public static void setKeyboardListener(@NonNull Context context, final KeyboardListener listener) {
        if (context instanceof Activity && listener != null) {
            Activity _activity = (Activity)context;
            ViewTreeObserver.OnGlobalLayoutListener layoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    Activity activity = observer.getActivity();
                    if (activity == null) {
                        return;
                    }
                    View decorView = activity.getWindow().getDecorView();
                    Rect outRect = new Rect();
                    decorView.getWindowVisibleDisplayFrame(outRect);

                    if (observer.rootViewVisibleHeight == 0) {
                        observer.rootViewVisibleHeight = outRect.bottom;
                    }
                    int keyboardHeight = observer.rootViewVisibleHeight - outRect.bottom;
                    if (keyboardHeight == 0) {
                        return;
                    }
                    if (keyboardHeight > OBSERVED_KEYBOARD_HEIGHT) {
                        observer.rootViewVisibleHeight = outRect.bottom;
                        observer.keyboardHeight = keyboardHeight;
                        observer.isKeyboardShow = true;
                        observer.onKeyboardShow();
                    }
                    else if (keyboardHeight < OBSERVED_KEYBOARD_HEIGHT) {
                        observer.rootViewVisibleHeight = outRect.bottom;
                        observer.isKeyboardShow = false;
                        observer.onKeyboardHide();
                    }
                }
            };
            observer.setListener(listener, _activity, layoutListener);
            _activity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        }
    }

    private static class MiniKeyboardObserver
    {
        private int keyboardHeight = 0;
        private int rootViewVisibleHeight = 0;
        private boolean isKeyboardShow = false;
        private static WeakReference<KeyboardListener> keyboardListenerWeakReference;
        private static WeakReference<Activity> activityWeakReference;
        private static WeakReference<ViewTreeObserver.OnGlobalLayoutListener> layoutListenerWeakReference;
        public MiniKeyboardObserver() {
        }

        public void setListener(KeyboardListener listener, Activity activity, ViewTreeObserver.OnGlobalLayoutListener layoutListener) {
            this.keyboardListenerWeakReference = new WeakReference<>(listener);
            this.activityWeakReference = new WeakReference<>(activity);
            this.layoutListenerWeakReference = new WeakReference<>(layoutListener);
        }

        public Activity getActivity()
        {
            if (this.activityWeakReference != null) {
                return this.activityWeakReference.get();
            }
            return null;
        }

        public ViewTreeObserver.OnGlobalLayoutListener getLayoutListener()
        {
            if (layoutListenerWeakReference != null) {
                return layoutListenerWeakReference.get();
            }
            return null;
        }

        protected void onKeyboardShow() {
            KeyboardListener listener = keyboardListenerWeakReference.get();
            if (listener != null) {
                listener.onKeyboardShow();
            }
        }

        protected void onKeyboardHide() {
            KeyboardListener listener = keyboardListenerWeakReference.get();
            if (listener != null) {
                listener.onKeyboardHide();
            }
        }

        public final int getKeyboardHeight() {
            return this.keyboardHeight;
        }

        public final boolean isKeyboardShow() {
            return this.isKeyboardShow;
        }

        public void removeListener()
        {
            Activity activity = getActivity();
            if (activity != null) {
                ViewTreeObserver.OnGlobalLayoutListener layoutListener = getLayoutListener();
                if (layoutListener != null) {
                    activity.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
                }
            }
            if (keyboardListenerWeakReference != null) {
                keyboardListenerWeakReference.clear();
                keyboardListenerWeakReference = null;
            }
            if (activityWeakReference != null) {
                activityWeakReference.clear();
                activityWeakReference = null;
            }
            if (layoutListenerWeakReference != null) {
                layoutListenerWeakReference.clear();
                layoutListenerWeakReference = null;
            }
        }
    }

    public interface KeyboardListener {

        void onKeyboardShow() ;

        void onKeyboardHide();
    }
}

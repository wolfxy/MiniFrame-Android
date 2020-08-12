package org.mini.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.frame.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.mini.frame.photo.HackyViewPager;
import org.mini.frame.photo.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gassion on 15/4/23 22：00.
 */
public class MiniUIImageBrowserActivity extends MiniUIActivity {
    private HackyViewPager mViewPager; // pager
    private int mCurrentPager;
    private List<PhotoView> mPagerViews;
    private ViewpagerAdapter mAdapter;//构建多张显示图片的视图
    private PhotoView mPhotoView;//显示图片的视图
    private ArrayList<String> urls;//传递过来的图片集合
    private TextView mTextTotalImageNum;//图片的总数
    private TextView mTextCurrentImageNum;//显示当前查看的张数

    public static void startImageBrowser(Context context, int position, String ... urlArray) {
        Intent intent = new Intent();
        intent.setClass(context, MiniUIImageBrowserActivity.class);
        ArrayList<String> urls = new ArrayList<>(1);
        for (String url : urlArray) {
            urls.add(url);
        }
        intent.putExtra("urls", urls);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public static void startImageBrowser(Context context, ArrayList<String> urls, int position)
    {
        Intent intent = new Intent();
        intent.setClass(context, MiniUIImageBrowserActivity.class);
        intent.putExtra("urls", urls);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public static void startImageBrowser(Context context, String fileName) throws Exception {
        Intent intent = new Intent();
        intent.setClass(context, MiniUIImageBrowserActivity.class);
        ArrayList<String> urls = new ArrayList<>(1);
        urls.add(fileName);
        intent.putExtra("urls", urls);
        intent.putExtra("position", 0);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser);
        final RelativeLayout saveLayout = (RelativeLayout) findViewById(R.id.photo_view_save_rl);
        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        mTextTotalImageNum = (TextView) findViewById(R.id.photo_view_tv_total);
        mTextCurrentImageNum = (TextView) findViewById(R.id.photo_view_tv_current);
        mPagerViews = new ArrayList<PhotoView>();
        Intent intent = getIntent();
        urls = intent.getStringArrayListExtra("urls");
        mCurrentPager = intent.getIntExtra("position", 0);
        if (urls != null) {
            for (int i = 0; i < urls.size(); i++) {
                mPhotoView = new PhotoView(this);
                mPagerViews.add(mPhotoView);
            }
            mViewPager = (HackyViewPager) findViewById(R.id.photo_view_viewpager);
            mAdapter = new ViewpagerAdapter(mPagerViews);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(mCurrentPager);
            mTextTotalImageNum.setText(urls.size() + "");
            mTextCurrentImageNum.setText((mCurrentPager + 1) + "");
            mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageSelected(int arg0) {
                    mTextCurrentImageNum.setText((arg0 + 1) + "");
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }


    public class ViewpagerAdapter extends PagerAdapter {

        private List<PhotoView> views;

        public ViewpagerAdapter(List<PhotoView> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final PhotoView photoView = new PhotoView(container.getContext());

            photoView.setSingleClickLister(new PhotoView.OnSingleClick() {
                @Override
                public void singleClick() {
                    finish();
                }
            });

            mPagerViews.remove(position);
            mPagerViews.add(position, photoView);
            photoView.setBackgroundColor(Color.BLACK);

            final LinearLayout pLayout = (LinearLayout) LayoutInflater.from(MiniUIImageBrowserActivity.this).inflate(
                    R.layout.image_browser_progress, null);
            pLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            final ProgressBar bar = (ProgressBar) pLayout.findViewById(R.id.photo_view_progress_bar);
            photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            RelativeLayout layout = new RelativeLayout(MiniUIImageBrowserActivity.this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(params);
            layout.addView(photoView);
            layout.addView(pLayout);
            container.addView(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            String url = urls.get(position);
            if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
                url = "file://" + url;
            }
            try {
                DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().
                        showImageOnFail(R.drawable.image_browser_default_icon)
                        .showImageForEmptyUri(R.drawable.image_browser_default_icon)
                        .showImageOnLoading(R.drawable.image_browser_default_icon).considerExifParams(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).build();
                ImageLoader.getInstance().displayImage(url, photoView, displayImageOptions,
                        new ImageLoadingListener() {

                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                pLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                bar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                pLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return layout;
        }

    }

    private void saveImage() {

        String imageUrl = urls.get(mViewPager.getCurrentItem());
        // 判断如果是本地图片需要添加"file://"
        if (!imageUrl.startsWith("http://")) {
            imageUrl = "file://" + imageUrl;
        }
        ImageLoader.getInstance().loadImage(imageUrl, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                showMessage("保存失败！");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                saveImageToGallery(loadedImage);
                showMessage("保存成功！");
            }
        });

    }


    public void saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "peiyu");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }
}

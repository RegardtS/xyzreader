package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;

import java.util.List;
import java.util.Map;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mCursor;
    private long mStartId;

    private long mSelectedItemId;

    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    SharedElementCallback tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.postponeEnterTransition(this);

        setContentView(R.layout.activity_article_detail);

        mPagerAdapter = new MyPagerAdapter(getFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));



        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(final int position) {
                if (mCursor != null) {
                    mCursor.moveToPosition(position);
                    mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);

                    tmp = new SharedElementCallback(){
                        @Override
                        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {

                            ArticleDetailFragment current = (ArticleDetailFragment)mPagerAdapter.getItem(position);

                            try {
                                if (current.getTopImage()!=null){
                                    names.clear();
                                    names.add(current.getTopImage().getTransitionName());
                                    sharedElements.clear();
                                    sharedElements.put(current.getTopImage().getTransitionName(), current.getTopImage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    setEnterSharedElementCallback(tmp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                mStartId = ItemsContract.Items.getItemId(getIntent().getData());
                mSelectedItemId = mStartId;
            }
        }
            getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;
        mPagerAdapter.notifyDataSetChanged();


        final int position = findCursor(mCursor,0,mCursor.getCount(),mStartId).getPosition();
        mPager.setCurrentItem(position, false);

        // Select the start ID
//        if (mStartId > 0) {
//            mCursor.moveToFirst();
//
//
//            // TODO: optimize
//            while (!mCursor.isAfterLast()) {
//                if (mCursor.getLong(ArticleLoader.  Query._ID) == mStartId) {
//                    final int position = mCursor.getPosition();
//                    mPager.setCurrentItem(position, false);
//                    break;
//                }
//                mCursor.moveToNext();
//            }
//            mStartId = 0;
//        }
    }

    public Cursor findCursor(Cursor data, int low, int high, long searchID) {

        int mid = (low + high) / 2;

        if (low > high) {
            return null;
        } else if (mCursor.getLong(ArticleLoader.  Query._ID) == searchID) {
            return data;
        } else if (mCursor.getLong(ArticleLoader.  Query._ID) < searchID) {
            return findCursor(data, mid + 1, high, searchID);
        } else {
            return findCursor(data, low, mid - 1, searchID);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        mPagerAdapter.notifyDataSetChanged();
    }



    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {

            mCursor.moveToPosition(position);
            return ArticleDetailFragment.newInstance(mCursor.getLong(ArticleLoader.Query._ID));
        }

        @Override
        public int getCount() {
            return (mCursor != null) ? mCursor.getCount() : 0;
        }
    }
}

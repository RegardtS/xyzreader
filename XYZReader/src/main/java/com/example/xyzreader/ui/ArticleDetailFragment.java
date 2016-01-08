package com.example.xyzreader.ui;

import android.app.ActionBar;
import android.app.Activity;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.example.xyzreader.Article;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_ITEM_ID = "item_id";
    private static final float PARALLAX_FACTOR = 1.25f;

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private int mMutedColor = 0xFF333333;
    private ObservableScrollView mScrollView;
    //private DrawInsetsFrameLayout mDrawInsetsFrameLayout;
    private ColorDrawable mStatusBarColorDrawable;

    private int mTopInset;
    private View mPhotoContainerView;
    private ImageView mPhotoView;
    private int mScrollY;
    private boolean mIsCard = false;
    private int mStatusBarFullOpacityBottom;

    ImageView test;

    static Toolbar toolbar;

    static Article article;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(Article article){
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("article",article);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            mItemId = getArguments().getLong(ARG_ITEM_ID);
//        }

        article = getArguments().getParcelable("article");

        mIsCard = getResources().getBoolean(R.bool.detail_is_card);
        mStatusBarFullOpacityBottom = getResources().getDimensionPixelSize(R.dimen.detail_card_top_margin);
        setHasOptionsMenu(true);

//        getLoaderManager().initLoader(0, null, this);

        ActivityCompat.startPostponedEnterTransition(getActivity());

    }

    @Override
    public void onStart() {
        super.onStart();

        bindViews();

    }

    public ArticleDetailActivity getActivityCast() {
        return (ArticleDetailActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
//        getLoaderManager().initLoader(0, null, this);
//        bindViews();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

        test = (ImageView) mRootView.findViewById(R.id.imageMain);
        test.setTransitionName(mItemId+"");

        toolbar = (Toolbar) mRootView.findViewById(R.id.mainToolbar);

        getActivityCast().setSupportActionBar(toolbar);


        return mRootView;
    }

    public View getTopImage() throws Exception{
        if(test == null){
            //throw new Exception("testerr is null");
        }
        return test;
    }


    private void bindViews() {

        if (mRootView == null) {
            Log.wtf("regi","rootView is null");
            return;
        }

        Glide.with(getActivity())
                .load(article.getPhoto_url())
//                    .load(R.drawable.temp)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .dontTransform()
                .error(R.drawable.no_image_available)
                .into(test);



        getActivityCast().setTitle(article.getTitle());



        String temp = "NEVER";



        if (mCursor != null) {
            //toolbar.setTitle(mCursor.getString(ArticleLoader.Query.TITLE));

            temp = mCursor.getString(ArticleLoader.Query.TITLE);

            Log.wtf("regi","temp == "+ temp);

//            Log.wtf("regi","temp ==  "+ temp);

            getActivityCast().setTitle(">>>"+temp);

            toolbar.setTitle(temp);

        }else{
            Log.wtf("regi","this is null");
        }








//
//        TextView titleView = (TextView) mRootView.findViewById(R.id.article_title);
//        TextView bylineView = (TextView) mRootView.findViewById(R.id.article_byline);
//        bylineView.setMovementMethod(new LinkMovementMethod());
//        TextView bodyView = (TextView) mRootView.findViewById(R.id.article_body);
//        bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));
//
//        if (mCursor != null) {
//            mRootView.setAlpha(0);
//            mRootView.setVisibility(View.VISIBLE);
//            mRootView.animate().alpha(1);
//            titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
//            bylineView.setText(Html.fromHtml(
//                    DateUtils.getRelativeTimeSpanString(
//                            mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
//                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
//                            DateUtils.FORMAT_ABBREV_ALL).toString()
//                            + " by <font color='#ffffff'>"
//                            + mCursor.getString(ArticleLoader.Query.AUTHOR)
//                            + "</font>"));
//            bodyView.setText(Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)));
//            ImageLoaderHelper.getInstance(getActivity()).getImageLoader()
//                    .get(mCursor.getString(ArticleLoader.Query.PHOTO_URL), new ImageLoader.ImageListener() {
//                        @Override
//                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                            Bitmap bitmap = imageContainer.getBitmap();
//                            if (bitmap != null) {
//                                Palette p = Palette.generate(bitmap, 12);
//                                mMutedColor = p.getDarkMutedColor(0xFF333333);
//                                mPhotoView.setImageBitmap(imageContainer.getBitmap());
//                                mRootView.findViewById(R.id.meta_bar)
//                                        .setBackgroundColor(mMutedColor);
//                                updateStatusBar();
//                            }
//                        }
//
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//
//                        }
//                    });
//        } else {
//            mRootView.setVisibility(View.GONE);
//            titleView.setText("N/A");
//            bylineView.setText("N/A" );
//            bodyView.setText("N/A");
//        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;//
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor mCursor != null: " + (mCursor != null) + " !mCursor.moveToFirst():" + (!mCursor.moveToFirst()));
            mCursor.close();
            mCursor = null;
            getActivityCast().finish();
        }else{



//            bindViews();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
//        bindViews();
    }
}

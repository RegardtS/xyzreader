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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.example.xyzreader.CoolDeviceChecker;
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

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;

    ImageView test;

    Toolbar toolbar;

    TextView tvAuthor, tvDate, tvTitle, tvBody;

    FloatingActionButton fab;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
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

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }

        setHasOptionsMenu(true);

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
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

        test = (ImageView) mRootView.findViewById(R.id.imageMain);

        if(CoolDeviceChecker.isCool()){
            test.setTransitionName(mItemId + "");
        }

        toolbar = (Toolbar) mRootView.findViewById(R.id.mainToolbar);


        getActivityCast().setSupportActionBar(toolbar);
        getActivityCast().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivityCast().getSupportActionBar().setDisplayShowTitleEnabled(false);


        tvAuthor = (TextView) mRootView.findViewById(R.id.author);
        tvDate = (TextView) mRootView.findViewById(R.id.date);
        tvTitle = (TextView) mRootView.findViewById(R.id.title);
        tvBody = (TextView) mRootView.findViewById(R.id.body);

        fab = (FloatingActionButton) mRootView.findViewById(R.id.fab);

        return mRootView;
    }

    public View getTopImage() throws Exception {
        if (test == null) {
            //throw new Exception("testerr is null");
        }
        return test;
    }

    private void bindViews() {
        if (mRootView != null && mCursor != null) {

            tvAuthor.setText(mCursor.getString(ArticleLoader.Query.AUTHOR));
            tvDate.setText(Html.fromHtml(DateUtils.getRelativeTimeSpanString(mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE), System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString()));
            tvTitle.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            tvBody.setText(Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                            .setType("text/plain")
                            .setText("Some sample text")
                            .getIntent(), getString(R.string.action_share)));
                }
            });
        }

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
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
            getActivityCast().finish();
        } else {

            Glide.with(getActivity())
                    .load(mCursor.getString(ArticleLoader.Query.PHOTO_URL))
                    .placeholder(R.drawable.loading)
                    .centerCrop()
                    .dontTransform()
                    .error(R.drawable.no_image_available)
                    .into(test);

            ActivityCompat.startPostponedEnterTransition(getActivity());

            bindViews();
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {}

}

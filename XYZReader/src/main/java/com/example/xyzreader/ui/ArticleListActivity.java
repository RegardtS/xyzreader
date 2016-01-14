package com.example.xyzreader.ui;

import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.UpdaterService;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private boolean mIsRefreshing = false;

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());




        adapter = new Adapter();
        mRecyclerView.setAdapter(adapter);
//        adapter.setHasStableIds(true);


        mSwipeRefreshLayout.setOnRefreshListener(this);



        getLoaderManager().initLoader(0, null, this);

//        if (savedInstanceState == null) {
//            refresh();
//        }



    }

    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        registerReceiver(mRefreshingReceiver, new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(mRefreshingReceiver);
    }

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.wtf("regi","onReceive");
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                Log.wtf("regi","onReceive2");
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }
        }
    };

    private void updateRefreshingUI() {
//        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.setAdapterCursor(cursor);
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private Cursor mCursor;

        public Adapter(){}

        public void setAdapterCursor(Cursor cursor) {
            mCursor = cursor;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(ArticleLoader.Query._ID);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item_article, parent, false);
            final ViewHolder vh = new ViewHolder(view);





            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(ArticleListActivity.this, vh.thumbnailView, vh.thumbnailView.getTransitionName()).toBundle();
//                    getApplicationContext().startActivity();

//                    Intent x = new Intent(ArticleListActivity.this, NewDetail.class);

                    Intent old = new Intent(Intent.ACTION_VIEW, ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition())));

//                    startActivity(x,bundle);

//                    Log.wtf("regi",">>" + ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition())));

//                    startActivity(old,bundle);

                    startActivity(old);

                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {



            mCursor.moveToPosition(position);

            holder.thumbnailView.setTransitionName(getItemId(position)+"");

            holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));

//            holder.thumbnailView.setRatio(mCursor.getString(ArticleLoader.Query.ASPECT_RATIO));




            String x = getString(R.string.card_sub_header,
                    DateUtils.getRelativeTimeSpanString(
                            mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                            System.currentTimeMillis(),
                            DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL)
                            .toString(),
                    mCursor.getString(ArticleLoader.Query.AUTHOR));

            holder.subtitleView.setText(x);

            holder.thumbnailView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));



//            holder.thumbnailView.setImageUrl(
//                    mCursor.getString(ArticleLoader.Query.PHOTO_URL),
//                    ImageLoaderHelper.getInstance(ArticleListActivity.this).getImageLoader());

            Glide.with(getApplicationContext())
                    .load(mCursor.getString(ArticleLoader.Query.PHOTO_URL))
//                    .load(R.drawable.temp)
//                    .placeholder(R.drawable.loading)
                    .crossFade()
                    .error(R.drawable.no_image_available)
                    .thumbnail(0.5f)

                    .into(holder.thumbnailView);


        }

        @Override
        public int getItemCount() {
            if (mCursor == null){
                return 0;
            }
            return mCursor.getCount();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public DynamicHeightNetworkImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }
}

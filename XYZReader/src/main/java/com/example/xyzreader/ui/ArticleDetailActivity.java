package com.example.xyzreader.ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ArticleDetailActivity.class.getSimpleName();

    private Cursor mCursor;
    private long mSelectedItemId;

    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.image)
    ImageView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        supportPostponeEnterTransition();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                mSelectedItemId = ItemsContract.Items.getItemId(getIntent().getData());
            }
        } else {
            mSelectedItemId = savedInstanceState.getLong(ItemsContract.Items._ID);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ItemsContract.Items._ID, mSelectedItemId);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;
        buildView();
    }

    private void buildView() {
        if (mSelectedItemId > 0) {
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                if (mCursor.getLong(ArticleLoader.Query._ID) == mSelectedItemId) {
                    break;
                }
                mCursor.moveToNext();
            }
        }
        buildImage();
        String title = mCursor.getString(ArticleLoader.Query.TITLE);
        mToolbar.setTitle(title);
        getSupportActionBar().setTitle(title);
//        Date publishedDate = parsePublishedDate();
//        if (!publishedDate.before(START_OF_EPOCH.getTime())) {
//            mSubtitleView.setText(Html.fromHtml(
//                    DateUtils.getRelativeTimeSpanString(
//                            publishedDate.getTime(),
//                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
//                            DateUtils.FORMAT_ABBREV_ALL).toString()
//                            + " by <font color='#ffffff'>"
//                            + mCursor.getString(ArticleLoader.Query.AUTHOR)
//                            + "</font>"));
//
//        } else {
//            mSubtitleView.setText(Html.fromHtml(
//                    outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
//                            + mCursor.getString(ArticleLoader.Query.AUTHOR)
//                            + "</font>"));
//
//        }
//        ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(mCursor.getLong(ArticleLoader.Query._ID));
//        getSupportFragmentManager().beginTransaction().add(R.id.article_container,fragment).commit();
    }

    private void buildImage() {
//        Picasso.get()
//                .load(mCursor.getString(ArticleLoader.Query.THUMB_URL))
//                .into(mPhotoView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        supportStartPostponedEnterTransition();
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        supportStartPostponedEnterTransition();
//                    }
//                });
//
//        ImageLoaderHelper.getInstance(this).getImageLoader()
//                .get(mCursor.getString(ArticleLoader.Query.PHOTO_URL), new ImageLoader.ImageListener() {
//                    @Override
//                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                        Bitmap bitmap = imageContainer.getBitmap();
//                        if (bitmap != null) {
//                            mPhotoView.setImageBitmap(imageContainer.getBitmap());
//                        }
//                    }
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {}
//                });
    }

    private Date parsePublishedDate() {
        try {
            String date = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
            return ArticleListActivity.dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> cursorLoader) {
        mCursor = null;
    }
}

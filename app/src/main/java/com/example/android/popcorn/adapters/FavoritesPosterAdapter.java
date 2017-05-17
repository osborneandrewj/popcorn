package com.example.android.popcorn.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popcorn.R;
import com.example.android.popcorn.data.FavoritesContract;
import com.example.android.popcorn.models.Movie;
import com.example.android.popcorn.utilites.MyNetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andrew Osborne on 3/17/2017.
 *
 */


public class FavoritesPosterAdapter extends RecyclerView.Adapter<FavoritesPosterAdapter.PosterAdapterViewHolder> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflater;
    private Cursor mCursor;
    /* Used to set the height of poster images */
    private int mViewContainerHeight;
    /* An on-click handler for interacting with the PosterAdapter */
    private final PosterAdapterOnClickHandler mClickHandler;

    /**
     * Interface that receives the onClick messages
     */
    public interface PosterAdapterOnClickHandler {
        // Clicks to general poster image
        void onClick(View view, TextView aDetailTextView, String aMovieTitle);

        // Clicks to detail button
        void onDetailButtonClick(TextView aDetailTextView,
                                 int aMovieId);
    }

    /**
     * Creates a FavoritesPosterAdapter and passes data into it
     *
     * @param context      The context of the containing activity.
     * @param clickHandler The on-click handler that is called when a poster image is clicked.
     */
    public FavoritesPosterAdapter(Context context,
                                  Cursor cursor,
                                  PosterAdapterOnClickHandler clickHandler) {
        mInflater = LayoutInflater.from(context);
        mCursor = cursor;
        mClickHandler = clickHandler;
    }

    /**
     * Inflates the cell layout from xml when needed. This is called when every new
     * PosterAdapterViewHolder is created.
     *
     * It is important that the size of each container is resized when the screen orientation is
     * changed.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public PosterAdapterViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = mInflater.inflate(R.layout.poster_item, parent, false);
        final PosterAdapterViewHolder viewHolder = new PosterAdapterViewHolder(view);
        mViewContainerHeight = parent.getMeasuredHeight();

        // Change the poster size depending on the orientation of the screen
        // Note: this might change depending on landscape version
        int desiredHeight = 0;
        int screenSize = mContext.getResources().getConfiguration().screenWidthDp;
        Log.v(LOG_TAG, "screensize: " + screenSize);
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            desiredHeight = mViewContainerHeight / 2;
        } else {
            if (screenSize >= 1024) {
                desiredHeight = mViewContainerHeight / 2;
            } else {
                desiredHeight = mViewContainerHeight;
            }
        }
        viewHolder.mContainer.getLayoutParams().height = desiredHeight;

        return viewHolder;
    }

    /**
     * Binds the data to the textview in each cell after inflation
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {

        // Get the current object
        //Movie currentMovie = mMovieList.get(position);

        // test picture
//        Picasso.with(mContext)
//                .load(currentMovie.getPosterUri())
//                .into(holder.mPosterImage);

        mCursor.moveToPosition(position);
        String posterString = mCursor.getString(
                mCursor.getColumnIndexOrThrow(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH));
        Uri posterUri = MyNetworkUtils.getUriFromPosterPath(posterString);
        Picasso.with(mContext)
                .load(posterUri)
                .into(holder.mPosterImage);
    }

    /**
     * Returns the total number of cells needed
     *
     * @return the number of cells.
     */
    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Inner class to store and recycle views as they are scrolled off screen
     */
    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public ImageView mPosterImage;
        public RelativeLayout mContainer;
        public TextView mDetailTextView;

        public PosterAdapterViewHolder(View view) {
            super(view);
            mPosterImage = (ImageView) view.findViewById(R.id.img_poster);
            mContainer = (RelativeLayout) view.findViewById(R.id.image_container);
            mDetailTextView = (TextView) view.findViewById(R.id.detail_text_view);
            view.setOnClickListener(this);
            mDetailTextView.setOnClickListener(this);
        }

        /**
         * This will be called when a child view is clicked
         *
         * @param view The the movie poster that was clicked.
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int currentMovieId = mCursor.getInt(mCursor.getColumnIndexOrThrow(
                    FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));

            // Did the user click A) the poster image or click B) the detail button?
            if (view == mDetailTextView) {
                // B) The user clicked the detail button
                mClickHandler.onDetailButtonClick(
                        mDetailTextView,
                        currentMovieId);
            } else {
                // A) The user clicked on the poster itself
                mClickHandler.onClick(view, mDetailTextView, "Testing");
            }
        }
    }

    /**
     * This method is used to pass a new list of Movie objects onto the PosterAdapter and
     * refresh the view.
     *
     */
    public void setMoviePosterData(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

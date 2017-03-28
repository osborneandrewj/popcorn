package com.example.android.popcorn;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andrew Osborne on 3/17/2017.
 *
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Movie> mMovieList;
    /* Used to set the height of poster images */
    private int mHeight;
    /* An on-click handler for interacting with the PosterAdapter */
    private final PosterAdapterOnClickHandler mClickHandler;

    /**
     * Interface that receives the onClick messages
     */
    public interface PosterAdapterOnClickHandler {
        // Clicks to general poster image
        void onClick(View view, TextView aDetailTextView, String aMovieTitle);

        // Clicks to detail button
        void onDetailButtonClick(TextView aDetailTextView, String aMovieTitle, Uri aPosterUri,
                                 Uri aBackdropUri, String aSynopsis, String aReleaseYear,
                                 String aVoteAverage);
    }

    /**
     * Creates a PosterAdapter and passing data into it
     *
     * @param context      The context of the containing activity.
     * @param aList        A list of Movie objects to display.
     * @param clickHandler The on-click handler that is called when a poster image is clicked.
     */
    public PosterAdapter(Context context, ArrayList<Movie> aList,
                         PosterAdapterOnClickHandler clickHandler) {
        mInflater = LayoutInflater.from(context);
        mMovieList = aList;
        mClickHandler = clickHandler;
    }

    /**
     * Inflates the cell layout from xml when needed. This is called when every new
     * PosterAdapterViewHolder is created.
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

        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Make each poster take up half the size of the screen
            // This is accomplished by measuring the LinearLayout containing the ImageView and
            // dividing that by half
            parent.post(new Runnable() {
                @Override
                public void run() {
                    mHeight = parent.getMeasuredHeight() / 2;
                    View view = viewHolder.mContainer;
                    view.getLayoutParams().height = mHeight;
                }
            });
        } else {
            // Make each poster take up the entire screen
            parent.post(new Runnable() {
                @Override
                public void run() {
                    mHeight = parent.getMeasuredHeight();
                    View view = viewHolder.mContainer;
                    view.getLayoutParams().height = mHeight;
                }
            });
        }

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
        Movie currentMovie = mMovieList.get(position);

        // test picture
        Picasso.with(mContext).load(currentMovie.getPosterUri()).into(holder.mPosterImage);
    }

    /**
     * Returns the total number of cells needed
     *
     * @return the number of cells.
     */
    @Override
    public int getItemCount() {
        return mMovieList.size();
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
            Movie currentMovie = mMovieList.get(adapterPosition);

            // Did the user click the poster image or click the detail button?
            if (view == mDetailTextView) {
                // The user clicked the detail button
                mClickHandler.onDetailButtonClick(
                        mDetailTextView,
                        currentMovie.getMovieTitle(),
                        currentMovie.getPosterUri(),
                        currentMovie.getBackdropUri(),
                        currentMovie.getMovieSynopsis(),
                        currentMovie.getReleaseYear(),
                        currentMovie.getVoteAverage());
            } else {
                // The user clicked on the poster itself
                mClickHandler.onClick(view, mDetailTextView, currentMovie.getMovieTitle());
            }
        }
    }

    /**
     * This method is used to pass a new list of Movie objects onto the PosterAdapter and
     * refresh the view.
     *
     * @param aList The new list of Movie objects to display.
     */
    public void setMoviePosterData(ArrayList<Movie> aList) {
        mMovieList = aList;
        notifyDataSetChanged();
    }


}

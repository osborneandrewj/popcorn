package com.example.android.popcorn.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popcorn.R;
import com.example.android.popcorn.models.MovieReviews;

import java.util.List;

/**
 * Created by Andrew Osborne on 5/8/17.
 *
 */

public class ReviewAdapter
        extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private Context mContext;
    private List<MovieReviews> mMovieReviews;
    private LayoutInflater mInflater;

    /**
     * Creates a ReviewAdapter and passes data into it
     *
     * @param context
     * @param aList
     */
    public ReviewAdapter(Context context, List<MovieReviews> aList) {
        mInflater = LayoutInflater.from(context);
        mMovieReviews = aList;

    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = mInflater.inflate(R.layout.review_item, parent, false);

        final ReviewAdapterViewHolder viewHolder = new ReviewAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        // Get the current object
        MovieReviews currentReview = mMovieReviews.get(position);

        holder.mAuthor.setText(currentReview.getAuthor());
        holder.mContent.setText(currentReview.getContent());

    }

    @Override
    public int getItemCount() {
        return mMovieReviews.size();
    }

    /**
     * Inner class to store and recycle views as they are scrolled off screen
     */
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView mAuthor;
        public TextView mContent;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthor = (TextView) view.findViewById(R.id.tv_review_author);
            mContent = (TextView) view.findViewById(R.id.tv_review_content);

        }
    }

    /**
     * This method is used to pass a new list of MovieReviews objects into the adapter and
     * refresh the view.
     *
     * @param aList The new list of MovieReviews objects to display.
     */
    public void setReviewsData(List<MovieReviews> aList) {
        mMovieReviews = aList;
        notifyDataSetChanged();
    }
}

package me.cmmps.androidchallenge;

import android.support.v7.widget.RecyclerView;

import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;

import java.util.ArrayList;

import me.cmmps.androidchallenge.interfaces.RVAdapterListener;
import me.cmmps.androidchallenge.models.GitRepoCommitDO;

/**
 * Created by csantos on 08/08/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<GitRepoCommitDO> gitCommits;

    private RVAdapterListener rvAdapterListener;

    public RecyclerViewAdapter(ArrayList<GitRepoCommitDO> gitCommits) {
        this.gitCommits = gitCommits;
    }

    public RecyclerViewAdapter setRVAdapterListener(RVAdapterListener rvAdapterListener) {
        this.rvAdapterListener = rvAdapterListener;
        return this;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCommitRowCommitter;
        public TextView mCommitRowDateTime;
        public ViewHolder(View v) {
            super(v);
            mCommitRowCommitter = (TextView) v.findViewById(R.id.textCommitRowCommitter);
            mCommitRowDateTime = (TextView) v.findViewById(R.id.textCommitRowDateTime);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_commit_row, parent, false);

        return new ViewHolder(v);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mCommitRowCommitter.setText(gitCommits.get(position).getCommiterName());
        holder.mCommitRowDateTime.setText(gitCommits.get(position).getCommiterDate());

        if (rvAdapterListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rvAdapterListener.onItemClick(position);
                }
            });
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return gitCommits.size();
    }


}

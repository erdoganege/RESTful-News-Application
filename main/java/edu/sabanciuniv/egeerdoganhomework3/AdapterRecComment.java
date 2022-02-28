package edu.sabanciuniv.egeerdoganhomework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecComment extends RecyclerView.Adapter<AdapterRecComment.CommentsViewHolder> {

    Context context;
    List<CommentItem> commentss;

    public AdapterRecComment(Context context, List<CommentItem> commentss) {
        this.context = context;
        this.commentss = commentss;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.comment_row_layout, parent, false);

        CommentsViewHolder holder = new CommentsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        holder.person_name.setText(commentss.get(position).getName());
        holder.itscomment.setText(commentss.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return commentss.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{

        TextView person_name;
        TextView itscomment;
        LinearLayout root;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            person_name = itemView.findViewById(R.id.name);
            itscomment = itemView.findViewById(R.id.comment);
            root = itemView.findViewById(R.id.containerrr);
        }
    }
}

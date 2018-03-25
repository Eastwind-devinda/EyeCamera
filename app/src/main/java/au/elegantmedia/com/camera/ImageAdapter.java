package au.elegantmedia.com.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Devinda on 9/18/17.
 */


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private List<File> imageList;
    private OnClickImage mOnClickImage;

    public ImageAdapter(Context mContext, List<File> imageList, OnClickImage mOnClickImage) {
        this.mContext = mContext;
        this.imageList = imageList;
        this.mOnClickImage = mOnClickImage;
    }

    public void addAll(final List<File> dataSet) {
        imageList.clear();
        notifyDataSetChanged();

        imageList.addAll(dataSet);
        notifyDataSetChanged();
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        File file = imageList.get(position);
        Picasso.with(mContext).load(file).resize(Util.getDisplayWidthPixel(mContext) / 3, Util.getDisplayWidthPixel(mContext) / 3).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickImage.onClickImageCallback(imageList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnClickImage{
        void onClickImageCallback(File file);
    }
}

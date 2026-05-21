package rbxquest.rbux.rbxcal.meme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import rbxquest.rbux.rbxcal.R;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.VH> {

    private final int[] images;

    public MemeAdapter(int[] images) {
        this.images = images;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meme_page, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.ivMeme.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView ivMeme;

        VH(@NonNull View itemView) {
            super(itemView);
            ivMeme = itemView.findViewById(R.id.ivMemePage);
        }
    }
}

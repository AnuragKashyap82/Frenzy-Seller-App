package kashyap.anurag.frenzyseller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.frenzyseller.Models.ModelCategory;
import kashyap.anurag.frenzyseller.R;


public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory> {

    private Context context;
    private ArrayList<ModelCategory> categoryArrayList;

    public AdapterCategory(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.row_category_items, parent, false);
        return new HolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        final ModelCategory modelCategory = categoryArrayList.get(position);
        String categoryIcon = modelCategory.getCategoryIcon();
        String categoryName = modelCategory.getCategoryName();

        holder.categoryName.setText(categoryName);
        try {
            Picasso.get().load(categoryIcon).placeholder(R.drawable.ic_cart_black).into(holder.categoryIcon);
        } catch (Exception e) {
            holder.categoryIcon.setImageResource(R.drawable.ic_cart_black);
        }
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class HolderCategory extends RecyclerView.ViewHolder {

        private ImageView categoryIcon;
        private TextView categoryName;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);

            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}

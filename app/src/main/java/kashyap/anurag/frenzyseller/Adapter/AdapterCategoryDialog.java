package kashyap.anurag.frenzyseller.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.frenzyseller.Models.ModelCategory;
import kashyap.anurag.frenzyseller.R;

import static kashyap.anurag.frenzyseller.Activities.AddProductCategoryActivity.addProductCategoryBinding;
import static kashyap.anurag.frenzyseller.Activities.EditProductCategoryActivity.editProductCategoryBinding;

public class AdapterCategoryDialog extends RecyclerView.Adapter<AdapterCategoryDialog.HolderCategoryDialog> {

    private Context context;
    private ArrayList<ModelCategory> categoryArrayList;
    private String LAYOUT_CODE;

    public AdapterCategoryDialog(Context context, ArrayList<ModelCategory> categoryArrayList, String LAYOUT_CODE) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.LAYOUT_CODE = LAYOUT_CODE;
    }

    @NonNull
    @Override
    public HolderCategoryDialog onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_category_dialog, parent, false);
        return new HolderCategoryDialog(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategoryDialog holder, int position) {
        final ModelCategory modelCategory = categoryArrayList.get(position);
        String categoryName = modelCategory.getCategoryName();

        holder.categoryName.setText(categoryName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LAYOUT_CODE.equals("ADD")){
                    addProductCategoryBinding.categoryTv.setText(categoryName);
                }else if (LAYOUT_CODE.equals("EDIT")){
                    editProductCategoryBinding.categoryTv.setText(categoryName);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class HolderCategoryDialog extends RecyclerView.ViewHolder {

        private TextView categoryName;

        public HolderCategoryDialog(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}

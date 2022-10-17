package kashyap.anurag.frenzyseller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.frenzyseller.Models.ModelOutOfStockProducts;
import kashyap.anurag.frenzyseller.Activities.ProductsDetailsActivity;
import kashyap.anurag.frenzyseller.R;

public class AdapterOutOfStockProducts extends RecyclerView.Adapter<AdapterOutOfStockProducts.HolderOutOfStockProducts> {

    private Context context;
    private ArrayList<ModelOutOfStockProducts> outOfStockProductsArrayList;

    public AdapterOutOfStockProducts(Context context, ArrayList<ModelOutOfStockProducts> outOfStockProductsArrayList) {
        this.context = context;
        this.outOfStockProductsArrayList = outOfStockProductsArrayList;
    }

    @NonNull
    @Override
    public HolderOutOfStockProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_out_of_stock_items, parent, false);
        return new HolderOutOfStockProducts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOutOfStockProducts holder, int position) {
        final ModelOutOfStockProducts modelOutOfStockProducts = outOfStockProductsArrayList.get(position);
        String productId = modelOutOfStockProducts.getProductId();

        loadProductDetails(productId, holder);

    }

    private void loadProductDetails(String productId, HolderOutOfStockProducts holder) {
        DocumentReference documentReference  = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                String productTitle = snapshot.getString("productTitle");
                String productImage = snapshot.getString("productImage");

                holder.productTitle.setText(productTitle);
                try {
                    Picasso.get().load(productImage).placeholder(R.drawable.ic_cart_black).into(holder.productImage);
                } catch (Exception e) {
                    holder.productImage.setImageResource(R.drawable.ic_cart_black);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProductsDetailsActivity.class);
                        intent.putExtra("productId", ""+productId);
                        intent.putExtra("productTitle", ""+productTitle);
                        context.startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return outOfStockProductsArrayList.size();
    }

    public class HolderOutOfStockProducts extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;

        public HolderOutOfStockProducts(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
        }
    }
}

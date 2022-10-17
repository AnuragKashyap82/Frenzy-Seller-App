package kashyap.anurag.frenzyseller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.frenzyseller.Models.ModelAllProducts;
import kashyap.anurag.frenzyseller.Activities.ProductsDetailsActivity;
import kashyap.anurag.frenzyseller.R;

public class AdapterAllProducts extends RecyclerView.Adapter<AdapterAllProducts.HolderAllSellerProducts> {

    private Context context;
    public ArrayList<ModelAllProducts> allProductArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public AdapterAllProducts(Context context, ArrayList<ModelAllProducts> allProductArrayList) {
        this.context = context;
        this.allProductArrayList = allProductArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public HolderAllSellerProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_grid_item, parent, false);
        return new HolderAllSellerProducts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAllSellerProducts holder, int position) {
        final ModelAllProducts modelAllProduct = allProductArrayList.get(position);
        String productTitle = modelAllProduct.getProductTitle();
        String productImage = modelAllProduct.getProductImage();
        String productDescription = modelAllProduct.getProductDescription();
        String productId = modelAllProduct.getProductId();
        String productPrice = modelAllProduct.getProductPrice();

        holder.productTitle.setText(productTitle);
        holder.productDescription.setText(productDescription);
        holder.productPrice.setText("Rs."+productPrice);

        try {
            Picasso.get().load(productImage).fit().centerCrop().placeholder(R.drawable.ic_cart_black).into(holder.productImage);
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

    @Override
    public int getItemCount() {
        return allProductArrayList.size();
    }

    public class HolderAllSellerProducts extends RecyclerView.ViewHolder {

        private TextView productTitle, productDescription, productPrice;
        private ImageView productImage;

        public HolderAllSellerProducts(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}

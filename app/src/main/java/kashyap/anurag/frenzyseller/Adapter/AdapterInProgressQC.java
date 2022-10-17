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
import kashyap.anurag.frenzyseller.Activities.ProductsDetailsActivity;
import kashyap.anurag.frenzyseller.Models.ModelInCompleteDetails;
import kashyap.anurag.frenzyseller.R;

public class AdapterInProgressQC extends RecyclerView.Adapter<AdapterInProgressQC.HolderInProgressQc> {

    private Context context;
    private ArrayList<ModelInCompleteDetails> inProgressQcArrayList;

    public AdapterInProgressQC(Context context, ArrayList<ModelInCompleteDetails> inProgressQcArrayList) {
        this.context = context;
        this.inProgressQcArrayList = inProgressQcArrayList;
    }

    @NonNull
    @Override
    public HolderInProgressQc onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_added_item, parent, false);
        return new HolderInProgressQc(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderInProgressQc holder, int position) {
        final ModelInCompleteDetails modelInCompleteDetails = inProgressQcArrayList.get(position);
        String productId = modelInCompleteDetails.getProductId();

        loadProductDetails(productId, holder);
    }

    private void loadProductDetails(String productId, HolderInProgressQc holder) {
        DocumentReference documentReference  = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                String productTitle = snapshot.getString("productTitle");
                String productImage = snapshot.getString("productImage");
                String processedDate = snapshot.getString("processedDate");
                String processedTime = snapshot.getString("processedTime");
                String category = snapshot.getString("category");

                holder.dateTv.setText(processedDate+" - "+processedTime);
                holder.categoryTv.setText(category);
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
        return inProgressQcArrayList.size();
    }

    public class HolderInProgressQc extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView categoryTv, brandTv, dateTv;

        public HolderInProgressQc(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            categoryTv = itemView.findViewById(R.id.categoryTv);
            brandTv = itemView.findViewById(R.id.brandTv);
            dateTv = itemView.findViewById(R.id.dateTv);
        }
    }
}

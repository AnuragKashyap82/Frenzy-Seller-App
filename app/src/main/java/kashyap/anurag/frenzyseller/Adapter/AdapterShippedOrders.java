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
import kashyap.anurag.frenzyseller.Models.ModelAllOrders;
import kashyap.anurag.frenzyseller.Activities.OrderDetailsActivity;
import kashyap.anurag.frenzyseller.R;


public class AdapterShippedOrders extends RecyclerView.Adapter<AdapterShippedOrders.HolderShippedOrders> {

    private Context context;
    private ArrayList<ModelAllOrders> allOrdersArrayList;

    public AdapterShippedOrders(Context context, ArrayList<ModelAllOrders> allOrdersArrayList) {
        this.context = context;
        this.allOrdersArrayList = allOrdersArrayList;
    }

    @NonNull
    @Override
    public HolderShippedOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new HolderShippedOrders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderShippedOrders holder, int position) {
        final ModelAllOrders modelAllOrders = allOrdersArrayList.get(position);
        String productId = modelAllOrders.getProductId();
        String orderId = modelAllOrders.getOrderId();
        String orderBy = modelAllOrders.getOrderBy();
        String shopId = modelAllOrders.getShopId();

        loadProductDetails(productId, holder);
        loadOrderDetails(orderBy, orderId, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderId", ""+orderId);
                intent.putExtra("orderBy", ""+orderBy);
                intent.putExtra("BUTTON_CODE", "SHIPPED_ORDER");
                context.startActivity(intent);
            }
        });
    }
    private void loadOrderDetails(String orderBy, String orderId, HolderShippedOrders holder) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        String orderDate = snapshot.get("orderDate").toString();
                        String shippedDate = snapshot.get("orderShippedDate").toString();
                        String shippedTime = snapshot.get("orderShippedTime").toString();

                        holder.orderDate.setText("Order Date: "+orderDate);
                        holder.deliveryWithinTv.setText("Shipped on: "+ shippedTime+" - "+shippedDate);
                        holder.deliveryWithinTv.setTextColor(context.getResources().getColor(R.color.gray02));
                    }
                });
    }

    private void loadProductDetails(String productId, HolderShippedOrders holder) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                String productTitle = snapshot.get("productTitle").toString();
                String productImage = snapshot.get("productImage").toString();
                String deliveryWithin = snapshot.get("deliveryWithin").toString();

                holder.productTitle.setText(productTitle);

                try {
                    Picasso.get().load(productImage).fit().centerCrop().placeholder(R.drawable.ic_cart_black).into(holder.productImage);
                } catch (Exception e) {
                    holder.productImage.setImageResource(R.drawable.ic_cart_black);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return allOrdersArrayList.size();
    }

    public class HolderShippedOrders extends RecyclerView.ViewHolder {

        private ImageView productImage, orderIndicator;
        private TextView productTitle, deliveryWithinTv, orderDate;

        public HolderShippedOrders(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            deliveryWithinTv = itemView.findViewById(R.id.deliveryWithinTv);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderIndicator = itemView.findViewById(R.id.orderIndicator);
        }
    }
}

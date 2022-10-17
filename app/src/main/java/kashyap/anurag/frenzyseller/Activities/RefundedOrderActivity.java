package kashyap.anurag.frenzyseller.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterRefundedOrders;
import kashyap.anurag.frenzyseller.Models.ModelAllOrders;
import kashyap.anurag.frenzyseller.databinding.ActivityRefundedOrderBinding;


public class RefundedOrderActivity extends AppCompatActivity {
    private ActivityRefundedOrderBinding binding;
    private FirebaseAuth firebaseAuth;
    private AdapterRefundedOrders adapterRefundedOrders;
    private ArrayList<ModelAllOrders> allOrdersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRefundedOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadAllRefundedOrders();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void loadAllRefundedOrders() {

        allOrdersArrayList = new ArrayList<>();

        binding.progressBar.setVisibility(View.VISIBLE);
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(firebaseAuth.getUid());
        documentReference.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelAllOrders modelAllOrders = document.toObject(ModelAllOrders.class);
                                checkOrderStatus(modelAllOrders);

                            }

                        }else{
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(RefundedOrderActivity.this, "Not Found!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void checkOrderStatus(ModelAllOrders modelAllOrders) {
        String orderBy = modelAllOrders.getOrderBy();
        String orderId = modelAllOrders.getOrderId();

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        String orderStatus = snapshot.getString("orderStatus");
                        if (orderStatus.equals("refunded")){

                            allOrdersArrayList.add(modelAllOrders);

                            Collections.sort(allOrdersArrayList, new Comparator<ModelAllOrders>() {
                                @Override
                                public int compare(ModelAllOrders t1, ModelAllOrders t2) {
                                    return t1.getOrderId().compareToIgnoreCase(t2.getOrderId());
                                }
                            });
                            Collections.reverse(allOrdersArrayList);

                            adapterRefundedOrders = new AdapterRefundedOrders(RefundedOrderActivity.this, allOrdersArrayList);
                            binding.refundedOrdersRv.setAdapter(adapterRefundedOrders);
                            adapterRefundedOrders.notifyDataSetChanged();
                            binding.progressBar.setVisibility(View.GONE);

                        }else {
                            Toast.makeText(RefundedOrderActivity.this, "No Returned Orders!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
package kashyap.anurag.frenzyseller.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterAllOrders;
import kashyap.anurag.frenzyseller.Models.ModelAllOrders;
import kashyap.anurag.frenzyseller.databinding.ActivityAllOrdersBinding;

public class AllOrdersActivity extends AppCompatActivity {
    private ActivityAllOrdersBinding binding;
    private FirebaseAuth firebaseAuth;
    private AdapterAllOrders adapterAllOrders;
    private ArrayList<ModelAllOrders> allOrdersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        loadAllOrders();
    }

    private void loadAllOrders() {

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
                                allOrdersArrayList.add(modelAllOrders);
                            }
                            Collections.sort(allOrdersArrayList, new Comparator<ModelAllOrders>() {
                                @Override
                                public int compare(ModelAllOrders t1, ModelAllOrders t2) {
                                    return t1.getOrderId().compareToIgnoreCase(t2.getOrderId());
                                }
                            });
                            Collections.reverse(allOrdersArrayList);

                            adapterAllOrders = new AdapterAllOrders(AllOrdersActivity.this, allOrdersArrayList);
                            binding.allOrdersRv.setAdapter(adapterAllOrders);
                            adapterAllOrders.notifyDataSetChanged();
                            binding.progressBar.setVisibility(View.GONE);
                        }else{
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AllOrdersActivity.this, "Not Found!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
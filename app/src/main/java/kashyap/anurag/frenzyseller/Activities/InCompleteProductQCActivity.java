package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import kashyap.anurag.frenzyseller.Adapter.AdapterAllOrders;
import kashyap.anurag.frenzyseller.Adapter.AdapterCategory;
import kashyap.anurag.frenzyseller.Adapter.AdapterInCompleteQc;
import kashyap.anurag.frenzyseller.Models.ModelCategory;
import kashyap.anurag.frenzyseller.Models.ModelInCompleteDetails;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityInCompleteProductQcactivityBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class InCompleteProductQCActivity extends AppCompatActivity {
    private ActivityInCompleteProductQcactivityBinding binding;
    private AdapterInCompleteQc adapterInCompleteQc;
    private ArrayList<ModelInCompleteDetails> inCompleteDetailsArrayList;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityInCompleteProductQcactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadAllInCompleteDetailsProduct();
    }

    private void loadAllInCompleteDetailsProduct() {
        binding.progressBar.setVisibility(View.VISIBLE);
        inCompleteDetailsArrayList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelInCompleteDetails modelInCompleteDetails = document.toObject(ModelInCompleteDetails.class);

                                checkIdDetailsCompleted(modelInCompleteDetails);
                            }

                        }else {

                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(InCompleteProductQCActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkIdDetailsCompleted(ModelInCompleteDetails modelInCompleteDetails) {
        String productId = modelInCompleteDetails.getProductId();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                boolean isDetailsCompleted = (boolean) snapshot.get("isDetailsComplete");
                if (isDetailsCompleted){
                    binding.progressBar.setVisibility(View.GONE);
                }else {
                    loadMyShopProductOnly(modelInCompleteDetails);

                }
            }
        });

    }

    private void loadMyShopProductOnly(ModelInCompleteDetails modelInCompleteDetails) {
        binding.progressBar.setVisibility(View.VISIBLE);
        String shopId = modelInCompleteDetails.getShopId();
        if (shopId.equals(firebaseAuth.getUid())){
            inCompleteDetailsArrayList.add(modelInCompleteDetails);
            adapterInCompleteQc = new AdapterInCompleteQc(InCompleteProductQCActivity.this, inCompleteDetailsArrayList);
            binding.inCompleteProductDetailsRv.setAdapter(adapterInCompleteQc);
            adapterInCompleteQc.notifyDataSetChanged();
            binding.progressBar.setVisibility(View.GONE);


        }else {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(InCompleteProductQCActivity.this, "Uid Not Matches!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
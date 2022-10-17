package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterInCompleteQc;
import kashyap.anurag.frenzyseller.Adapter.AdapterInProgressQC;
import kashyap.anurag.frenzyseller.Models.ModelInCompleteDetails;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityInProgressQcactivityBinding;

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

public class InProgressQCActivity extends AppCompatActivity {
    private ActivityInProgressQcactivityBinding binding;
    private AdapterInProgressQC adapterInProgressQC;
    private ArrayList<ModelInCompleteDetails> inCompleteDetailsArrayList;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInProgressQcactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadInProgressQCProducts();
    }
    private void loadInProgressQCProducts() {
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

                                checkIdActive(modelInCompleteDetails);
                            }

                        }else {

                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(InProgressQCActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkIdActive(ModelInCompleteDetails modelInCompleteDetails) {
        String productId = modelInCompleteDetails.getProductId();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                boolean isActive = (boolean) snapshot.get("active");
                if (isActive){

                    binding.progressBar.setVisibility(View.GONE);
                }else {
                    boolean isDetailsCompleted = (boolean) snapshot.get("isDetailsComplete");
                    if (isDetailsCompleted){
                        loadMyShopProductOnly(modelInCompleteDetails);

                    }else{
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
    private void loadMyShopProductOnly(ModelInCompleteDetails modelInCompleteDetails) {
        binding.progressBar.setVisibility(View.VISIBLE);
        String shopId = modelInCompleteDetails.getShopId();
        if (shopId.equals(firebaseAuth.getUid())){
            inCompleteDetailsArrayList.add(modelInCompleteDetails);
            adapterInProgressQC = new AdapterInProgressQC(InProgressQCActivity.this, inCompleteDetailsArrayList);
            binding.qcProgressRv.setAdapter(adapterInProgressQC);
            adapterInProgressQC.notifyDataSetChanged();
            binding.progressBar.setVisibility(View.GONE);


        }else {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(InProgressQCActivity.this, "Uid Not Matches!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
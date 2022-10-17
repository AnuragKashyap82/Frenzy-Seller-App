package kashyap.anurag.frenzyseller.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterOutOfStockProducts;
import kashyap.anurag.frenzyseller.Models.ModelOutOfStockProducts;
import kashyap.anurag.frenzyseller.databinding.ActivityOutOfStockProductsBinding;

public class OutOfStockProductsActivity extends AppCompatActivity {
    private ActivityOutOfStockProductsBinding binding;
    private AdapterOutOfStockProducts adapterOutOfStockProducts;
    private ArrayList<ModelOutOfStockProducts> outOfStockProductsArrayList;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutOfStockProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        loadAllOutOfStockProducts();
    }

    private void loadAllOutOfStockProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        outOfStockProductsArrayList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelOutOfStockProducts modelOutOfStockProducts = document.toObject(ModelOutOfStockProducts.class);
                                loadMyShopProductOnly(modelOutOfStockProducts);
                                binding.progressBar.setVisibility(View.GONE);
                            }

                        }else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(OutOfStockProductsActivity.this, "No out of !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void loadMyShopProductOnly(ModelOutOfStockProducts modelOutOfStockProducts) {
        binding.progressBar.setVisibility(View.VISIBLE);
        String shopId = modelOutOfStockProducts.getShopId();
        if (shopId.equals(firebaseAuth.getUid())){

            String productId = modelOutOfStockProducts.getProductId();
            checkOutOfStock(productId, modelOutOfStockProducts);


        }else {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(OutOfStockProductsActivity.this, "Uid Not Matches!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkOutOfStock(String productId, ModelOutOfStockProducts modelOutOfStockProducts) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products").child(productId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    boolean inStock = (boolean) snapshot.child("inStock").getValue();

                    if (inStock){

                        Toast.makeText(OutOfStockProductsActivity.this, "No Out of Stock Products....", Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);

                    }else{

                        outOfStockProductsArrayList.add(modelOutOfStockProducts);

                        adapterOutOfStockProducts = new AdapterOutOfStockProducts(OutOfStockProductsActivity.this, outOfStockProductsArrayList);
                        binding.outOfStockProductRv.setAdapter(adapterOutOfStockProducts);
                        adapterOutOfStockProducts.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(OutOfStockProductsActivity.this, "Snapshot not Found!!!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
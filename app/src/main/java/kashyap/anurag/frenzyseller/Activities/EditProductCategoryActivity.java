package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterCategoryDialog;
import kashyap.anurag.frenzyseller.Models.ModelCategory;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityEditProductCategoryBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditProductCategoryActivity extends AppCompatActivity {
    public static ActivityEditProductCategoryBinding editProductCategoryBinding;
    private String productId;
    private AdapterCategoryDialog adapterCategoryDialog;
    private ArrayList<ModelCategory> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProductCategoryBinding = ActivityEditProductCategoryBinding.inflate(getLayoutInflater());
        setContentView(editProductCategoryBinding.getRoot());

        productId = getIntent().getStringExtra("productId");

        loadProductDetails(productId);
        loadAllCategories();

        editProductCategoryBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editProductCategoryBinding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }
    private void validateData() {
        if (editProductCategoryBinding.categoryTv.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "SelectCategory!!!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(EditProductCategoryActivity.this, EditProductFirstActivity.class);
            intent.putExtra("category", editProductCategoryBinding.categoryTv.getText().toString());
            intent.putExtra("productId", ""+productId);
            startActivity(intent);
        }
    }

    private void loadProductDetails(String productId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot.exists()) {
                    String category = snapshot.getString("category");
                    editProductCategoryBinding.categoryTv.setText(category);
                }
            }
        });
    }
    private void loadAllCategories() {
        categoryArrayList =new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelCategory modelCategory = document.toObject(ModelCategory.class);
                                categoryArrayList.add(modelCategory);
                            }

                            adapterCategoryDialog = new AdapterCategoryDialog(EditProductCategoryActivity.this, categoryArrayList, "EDIT");
                            editProductCategoryBinding.categoriesRv.setAdapter(adapterCategoryDialog);
                            adapterCategoryDialog.notifyDataSetChanged();

                        }else {
                            Toast.makeText(EditProductCategoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterCategoryDialog;
import kashyap.anurag.frenzyseller.Models.ModelCategory;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityAddProductCategoryBinding;
import kashyap.anurag.frenzyseller.databinding.ActivityAddProductFirstBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddProductCategoryActivity extends AppCompatActivity {
    public static ActivityAddProductCategoryBinding addProductCategoryBinding;
    private AdapterCategoryDialog adapterCategoryDialog;
    private ArrayList<ModelCategory> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addProductCategoryBinding = ActivityAddProductCategoryBinding.inflate(getLayoutInflater());
        setContentView(addProductCategoryBinding.getRoot());

        loadAllCategories();

        addProductCategoryBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addProductCategoryBinding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        if (addProductCategoryBinding.categoryTv.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "SelectCategory!!!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(AddProductCategoryActivity.this, AddProductFirstActivity.class);
            intent.putExtra("category", addProductCategoryBinding.categoryTv.getText().toString());
            startActivity(intent);
        }
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

                            adapterCategoryDialog = new AdapterCategoryDialog(AddProductCategoryActivity.this, categoryArrayList, "ADD");
                            addProductCategoryBinding.categoriesRv.setAdapter(adapterCategoryDialog);
                            adapterCategoryDialog.notifyDataSetChanged();

                        }else {
                            Toast.makeText(AddProductCategoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
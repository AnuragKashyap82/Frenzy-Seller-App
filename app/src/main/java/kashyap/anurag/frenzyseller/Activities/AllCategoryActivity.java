package kashyap.anurag.frenzyseller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import kashyap.anurag.frenzyseller.Adapter.AdapterCategory;
import kashyap.anurag.frenzyseller.Models.ModelCategory;
import kashyap.anurag.frenzyseller.databinding.ActivityAllCategoryBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllCategoryActivity extends AppCompatActivity {
    private ActivityAllCategoryBinding binding;
    private AdapterCategory adapterCategory;
    private ArrayList<ModelCategory> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAllCategory();
    }

    private void loadAllCategory() {
        binding.progressBar.setVisibility(View.VISIBLE);
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
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(AllCategoryActivity.this, 4);
                            binding.categoryRv.setLayoutManager(gridLayoutManager);


                            adapterCategory = new AdapterCategory(AllCategoryActivity.this, categoryArrayList);
                            binding.categoryRv.setAdapter(adapterCategory);
                            binding.progressBar.setVisibility(View.GONE);

                        }else {

                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AllCategoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
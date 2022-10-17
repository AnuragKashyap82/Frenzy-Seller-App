package kashyap.anurag.frenzyseller.Activities;

import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityMyProductsBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyProducts extends AppCompatActivity {
    private ActivityMyProductsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProducts.this, AddProductCategoryActivity.class));
            }
        });
        binding.completeProductAddRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProducts.this, InCompleteProductQCActivity.class));
            }
        });
        binding.inProgressRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProducts.this, InProgressQCActivity.class));
            }
        });
        binding.activeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProducts.this, ActiveProductsActivity.class));
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
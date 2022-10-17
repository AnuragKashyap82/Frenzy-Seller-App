package kashyap.anurag.frenzyseller.Activities;

import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityBankDetailsBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BankDetailsActivity extends AppCompatActivity {
    private ActivityBankDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBankDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        startActiity();
    }

    private void startActiity() {
        startActivity(new Intent(BankDetailsActivity.this, SellerVerificationActivity.class));
        finishAffinity();
        binding.progressBar.setVisibility(View.GONE);
    }
}
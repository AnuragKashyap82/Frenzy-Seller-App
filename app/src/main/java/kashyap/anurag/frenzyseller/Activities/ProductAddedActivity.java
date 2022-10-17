package kashyap.anurag.frenzyseller.Activities;

import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;

import android.os.Bundle;

public class ProductAddedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_added);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
package kashyap.anurag.frenzyseller.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import kashyap.anurag.frenzyseller.Activities.InProgressQCActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterAllProducts;
import kashyap.anurag.frenzyseller.Activities.CancelledOrderActivity;
import kashyap.anurag.frenzyseller.Activities.DeliveredOrdersActivity;
import kashyap.anurag.frenzyseller.Adapter.AdapterInCompleteQc;
import kashyap.anurag.frenzyseller.Models.ModelAllProducts;
import kashyap.anurag.frenzyseller.Activities.NewOrdersActivity;
import kashyap.anurag.frenzyseller.Activities.PackedOrderActivity;
import kashyap.anurag.frenzyseller.Activities.RefundedOrderActivity;
import kashyap.anurag.frenzyseller.Activities.ReturnedOrderActivity;
import kashyap.anurag.frenzyseller.Activities.ShippedOrderActivity;
import kashyap.anurag.frenzyseller.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private AdapterAllProducts adapterAllProducts;
    private ArrayList<ModelAllProducts> allProductArrayList;
    private FirebaseAuth firebaseAuth;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        firebaseAuth = FirebaseAuth.getInstance();
        loadSellerAllProducts();
        loadMyDetails();

        binding.newOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewOrdersActivity.class));
            }
        });
        binding.cancelledTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CancelledOrderActivity.class));
            }
        });
        binding.packedOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PackedOrderActivity.class));
            }
        });
        binding.shippedOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShippedOrderActivity.class));
            }
        });
        binding.deliveredTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DeliveredOrdersActivity.class));
            }
        });
        binding.returnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReturnedOrderActivity.class));
            }
        });
        binding.refundTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RefundedOrderActivity.class));
            }
        });

        return binding.getRoot();
    }

    private void loadMyDetails() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                String shopName = snapshot.getString("shopName");
                binding.shopNameTv.setText(shopName+" All Activity");
                binding.shopNameTv2.setText(shopName+" All Products");
            }
        });
    }

    private void loadSellerAllProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        allProductArrayList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelAllProducts modelAllProduct = document.toObject(ModelAllProducts.class);
                                loadMyShopProductOnly(modelAllProduct);
                                binding.progressBar.setVisibility(View.GONE);
                            }

                        }else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "No products!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void loadMyShopProductOnly(ModelAllProducts modelAllProduct) {
        binding.progressBar.setVisibility(View.VISIBLE);
        String shopId = modelAllProduct.getShopId();
        if (shopId.equals(firebaseAuth.getUid())){
            String productId  = modelAllProduct.getProductId();
            checkIsActive(productId, modelAllProduct);

        }else {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Uid Not Matches!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIsActive(String productId, ModelAllProducts modelAllProduct) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                boolean isActive = (boolean) snapshot.get("active");
                if (isActive){
                    allProductArrayList.add(modelAllProduct);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    binding.sellerAllProductsRv.setLayoutManager(gridLayoutManager);

                    adapterAllProducts = new AdapterAllProducts(getActivity(), allProductArrayList);
                    binding.sellerAllProductsRv.setAdapter(adapterAllProducts);

                    binding.progressBar.setVisibility(View.GONE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


}
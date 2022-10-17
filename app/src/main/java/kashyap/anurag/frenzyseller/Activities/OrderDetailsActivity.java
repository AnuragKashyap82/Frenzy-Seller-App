package kashyap.anurag.frenzyseller.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityOrderDetailsBinding;


public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;
    private String orderId;
    private TextView productTitle, productPrice, productQuantity, orderedDate, packedDate,
            shippedDate, deliveryDate, packedTv, shippedTv, deliveredTv, nameTv, addressTv, phoneNoTv,
            totalItemsPrice, deliveryPrice, totalPrice, savedAmount, productDescriptionTv, priceTv, discountPrice,
            packedTextIndicator, shippedTextIndicator, deliveredTextIndicator, orderIdTv, shopNameTv, returnDate, returnTv, refundDate, refundTv;
    private ImageView productImage, packedIndicator, shippingIndicator, deliveredIndicator, refundIndicator, returnIndicator, orderTrackerBtn;
    private View packedShippingProgressBar, shippedDeliveryProgressBar, orderedPackedProgressBar, returnProgressBar, refundProgressBar;
    private String productId, orderBy;
    private FirebaseAuth firebaseAuth;
    private RelativeLayout returnRl, refundRl;
    private boolean isPacked, isShipped, isDelivered;
    private String BUTTON_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        orderId = getIntent().getStringExtra("orderId");
        orderBy = getIntent().getStringExtra("orderBy");
        BUTTON_CODE = getIntent().getStringExtra("BUTTON_CODE");

        if (BUTTON_CODE.equals("NEW_ORDER")) {
            binding.orderPackedBtn.setVisibility(View.VISIBLE);
        } else if (BUTTON_CODE.equals("CANCELLED_ORDER")) {

        } else if (BUTTON_CODE.equals("ALL_ORDERS")) {

        } else if (BUTTON_CODE.equals("PACKED_ORDER")) {
            binding.orderShippedBtn.setVisibility(View.VISIBLE);
        } else if (BUTTON_CODE.equals("SHIPPED_ORDER")) {
            binding.orderDeliveredBtn.setVisibility(View.VISIBLE);
        } else if (BUTTON_CODE.equals("DELIVERED_ORDER")) {

        } else if (BUTTON_CODE.equals("RETURNED_ORDER")) {
            binding.orderRefundBtn.setVisibility(View.VISIBLE);
        }else if (BUTTON_CODE.equals("REFUNDED_ORDER")) {

        }

        checkOrderStatus(orderId);
        loadOrderDetails(orderId);

        productTitle = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        productQuantity = findViewById(R.id.productQuantity);
        orderedDate = findViewById(R.id.orderedDate);
        packedDate = findViewById(R.id.packedDate);
        shippedDate = findViewById(R.id.shippedDate);
        deliveryDate = findViewById(R.id.deliveryDate);
        packedTv = findViewById(R.id.packedTv);
        shippedTv = findViewById(R.id.shippedTv);
        deliveredTv = findViewById(R.id.deliveredTv);
        nameTv = findViewById(R.id.nameTv);
        addressTv = findViewById(R.id.addressTv);
        phoneNoTv = findViewById(R.id.phoneNoTv);
        totalItemsPrice = findViewById(R.id.totalItemsPrice);
        deliveryPrice = findViewById(R.id.deliveryPrice);
        totalPrice = findViewById(R.id.totalPrice);
        savedAmount = findViewById(R.id.savedAmount);
        productImage = findViewById(R.id.productImage);
        packedIndicator = findViewById(R.id.packedIndicator);
        shippingIndicator = findViewById(R.id.shippingIndicator);
        deliveredIndicator = findViewById(R.id.deliveredIndicator);
        packedShippingProgressBar = findViewById(R.id.packedShippingProgressBar);
        shippedDeliveryProgressBar = findViewById(R.id.shippedDeliveryProgressBar);
        productDescriptionTv = findViewById(R.id.productDescriptionTv);
        packedTextIndicator = findViewById(R.id.packedTextIndicator);
        shippedTextIndicator = findViewById(R.id.shippedTextIndicator);
        deliveredTextIndicator = findViewById(R.id.deliveredTextIndicator);
        orderedPackedProgressBar = findViewById(R.id.orderedPackedProgressBar);
        priceTv = findViewById(R.id.priceTv);
        discountPrice = findViewById(R.id.discountPrice);
        orderIdTv = findViewById(R.id.orderIdTv);
        shopNameTv = findViewById(R.id.shopNameTv);
        returnProgressBar = findViewById(R.id.returnProgressBar);
        refundProgressBar = findViewById(R.id.refundProgressBar);
        refundIndicator = findViewById(R.id.refundIndicator);
        returnIndicator = findViewById(R.id.returnIndicator);
        returnRl = findViewById(R.id.returnRl);
        refundRl = findViewById(R.id.refundRl);
        returnDate = findViewById(R.id.returnDate);
        returnTv = findViewById(R.id.returnTv);
        orderTrackerBtn = findViewById(R.id.orderTrackerBtn);
        refundDate = findViewById(R.id.refundDate);
        refundTv = findViewById(R.id.refundTv);

        orderIdTv.setText("Order ID: " + orderId);

        binding.orderPackedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog logoutDialog = new Dialog(OrderDetailsActivity.this);
                logoutDialog.setContentView(R.layout.logout_dialog);
                logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView cancelBtn = logoutDialog.findViewById(R.id.cancelBtn);
                TextView logoutBtn = logoutDialog.findViewById(R.id.logoutBtn);
                TextView textView = logoutDialog.findViewById(R.id.textView4);
                logoutDialog.setCancelable(true);

                textView.setText("Mark this order as Packed");
                logoutBtn.setText("Packed");
                cancelBtn.setText("Not Packed");

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                    }
                });
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                        markOrderStatusPacked(orderId, orderBy);
                    }
                });
                logoutDialog.show();
            }
        });
        binding.orderShippedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog logoutDialog = new Dialog(OrderDetailsActivity.this);
                logoutDialog.setContentView(R.layout.logout_dialog);
                logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView cancelBtn = logoutDialog.findViewById(R.id.cancelBtn);
                TextView logoutBtn = logoutDialog.findViewById(R.id.logoutBtn);
                TextView textView = logoutDialog.findViewById(R.id.textView4);
                logoutDialog.setCancelable(true);

                textView.setText("Mark this order as Shipped");
                logoutBtn.setText("Shipped");
                cancelBtn.setText("Not Shipped");

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                    }
                });
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                        markOrderStatusShipped(orderId, orderBy);
                    }
                });
                logoutDialog.show();
            }
        });
        binding.orderDeliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog logoutDialog = new Dialog(OrderDetailsActivity.this);
                logoutDialog.setContentView(R.layout.logout_dialog);
                logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView cancelBtn = logoutDialog.findViewById(R.id.cancelBtn);
                TextView logoutBtn = logoutDialog.findViewById(R.id.logoutBtn);
                TextView textView = logoutDialog.findViewById(R.id.textView4);
                logoutDialog.setCancelable(true);

                textView.setText("Mark this order as Delivered");
                logoutBtn.setText("Delivered");
                cancelBtn.setText("Not Delivered");

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                    }
                });
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                        markOrderStatusDelivered(orderId, orderBy);
                    }
                });
                logoutDialog.show();
            }
        });
        binding.orderRefundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog logoutDialog = new Dialog(OrderDetailsActivity.this);
                logoutDialog.setContentView(R.layout.logout_dialog);
                logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView cancelBtn = logoutDialog.findViewById(R.id.cancelBtn);
                TextView logoutBtn = logoutDialog.findViewById(R.id.logoutBtn);
                TextView textView = logoutDialog.findViewById(R.id.textView4);
                logoutDialog.setCancelable(true);

                textView.setText("Mark this order as Redunded");
                logoutBtn.setText("Refunded");
                cancelBtn.setText("Not Refunded");

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                    }
                });
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                        markOrderStatusRefunded(orderId, orderBy);
                    }
                });
                logoutDialog.show();
            }
        });
        orderTrackerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, OrderTrackerActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("orderBy", orderBy);
                startActivity(intent);
            }
        });
    }

    private void checkOrderStatus(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        String orderStatus = snapshot.getString("orderStatus");
                        if (orderStatus.equals("cancelled")) {
                            packedIndicator.setVisibility(View.GONE);
                            packedTv.setVisibility(View.GONE);
                            packedDate.setVisibility(View.GONE);
                            packedTextIndicator.setVisibility(View.GONE);
                            packedShippingProgressBar.setVisibility(View.GONE);
                            shippingIndicator.setVisibility(View.GONE);
                            shippedDate.setVisibility(View.GONE);
                            shippedTv.setVisibility(View.GONE);
                            shippedTextIndicator.setVisibility(View.GONE);
                            orderedPackedProgressBar.setVisibility(View.GONE);

                            shippedDeliveryProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            deliveredIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            deliveredTextIndicator.setText("Cancelled");
                            deliveredTv.setText("Your Ordered has been Cancelled");
                            loadCancelledDetails(orderId);

                        } else if (orderStatus.equals("packed")) {

                            packedShippingProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedTv.setText("Your order has been packed");
                            loadPackedDetails(orderId);

                        } else if (orderStatus.equals("shipped")) {

                            packedShippingProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedTv.setText("Your order has been packed");
                            loadPackedDetails(orderId);

                            shippedDeliveryProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippingIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippedTv.setText("Your order has been shipped");
                            loadShippedDetails(orderId);
                        } else if (orderStatus.equals("delivered")) {

                            packedShippingProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedTv.setText("Your order has been packed");
                            loadPackedDetails(orderId);

                            shippedDeliveryProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippingIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippedTv.setText("Your order has been shipped");
                            loadShippedDetails(orderId);

                            deliveredIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            deliveredTv.setText("Your order has been Delivered");
                            loadDeliveredDetails(orderId);
                        } else if (orderStatus.equals("returned")) {

                            returnProgressBar.setVisibility(View.VISIBLE);
                            refundProgressBar.setVisibility(View.VISIBLE);
                            returnIndicator.setVisibility(View.VISIBLE);
                            refundIndicator.setVisibility(View.VISIBLE);
                            refundProgressBar.setVisibility(View.VISIBLE);
                            returnRl.setVisibility(View.VISIBLE);
                            refundRl.setVisibility(View.VISIBLE);

                            packedIndicator.setVisibility(View.GONE);
                            packedTv.setVisibility(View.GONE);
                            packedDate.setVisibility(View.GONE);
                            packedTextIndicator.setVisibility(View.GONE);
                            packedShippingProgressBar.setVisibility(View.GONE);
                            shippingIndicator.setVisibility(View.GONE);
                            shippedDate.setVisibility(View.GONE);
                            shippedTv.setVisibility(View.GONE);
                            shippedTextIndicator.setVisibility(View.GONE);
                            orderedPackedProgressBar.setVisibility(View.GONE);

                            packedShippingProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedTv.setText("Your order has been packed");
                            loadPackedDetails(orderId);

                            shippedDeliveryProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippingIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippedTv.setText("Your order has been shipped");
                            loadShippedDetails(orderId);

                            deliveredIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            deliveredTv.setText("Your order has been Delivered");
                            loadDeliveredDetails(orderId);

                            returnProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            returnIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            returnTv.setText("Your Order has been returned");
                            loadReturnDetails(orderId);
                        }else if (orderStatus.equals("refunded")){
                            returnProgressBar.setVisibility(View.VISIBLE);
                            refundProgressBar.setVisibility(View.VISIBLE);
                            returnIndicator.setVisibility(View.VISIBLE);
                            refundIndicator.setVisibility(View.VISIBLE);
                            refundProgressBar.setVisibility(View.VISIBLE);
                            returnRl.setVisibility(View.VISIBLE);
                            refundRl.setVisibility(View.VISIBLE);

                            packedIndicator.setVisibility(View.GONE);
                            packedTv.setVisibility(View.GONE);
                            packedDate.setVisibility(View.GONE);
                            packedTextIndicator.setVisibility(View.GONE);
                            packedShippingProgressBar.setVisibility(View.GONE);
                            shippingIndicator.setVisibility(View.GONE);
                            shippedDate.setVisibility(View.GONE);
                            shippedTv.setVisibility(View.GONE);
                            shippedTextIndicator.setVisibility(View.GONE);
                            orderedPackedProgressBar.setVisibility(View.GONE);

                            packedShippingProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedTv.setText("Your order has been packed");
                            loadPackedDetails(orderId);

                            shippedDeliveryProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippingIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippedTv.setText("Your order has been shipped");
                            loadShippedDetails(orderId);

                            deliveredIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            deliveredTv.setText("Your order has been Delivered");
                            loadDeliveredDetails(orderId);

                            returnProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            returnIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            returnTv.setText("Your Order has been returned");
                            loadReturnDetails(orderId);

                            refundProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            refundIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            refundTv.setText("Your Amount has been Refunded");
                            loadRefundDetails(orderId);
                        }
                    }
                });
    }
    private void loadCancelledDetails(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {

                            String orderCancelledDate = "" + snapshot.get("orderCancelledDate");
                            String orderCancelledTime = "" + snapshot.get("orderCancelledTime");

                            deliveryDate.setText(orderCancelledDate + "  " + orderCancelledTime);
                        }
                    }
                });
    }

    private void loadPackedDetails(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {

                            String orderPackedDate = "" + snapshot.get("orderPackedDate");
                            String orderPackedTime = "" + snapshot.get("orderPackedTime");

                            packedDate.setText(orderPackedDate + "  " + orderPackedTime);
                        }
                    }
                });
    }

    private void loadShippedDetails(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {

                            String orderShippedDate = "" + snapshot.get("orderShippedDate");
                            String orderShippedTime = "" + snapshot.get("orderShippedTime");

                            shippedDate.setText(orderShippedDate + "  " + orderShippedTime);
                        }
                    }
                });
    }

    private void loadDeliveredDetails(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {

                            String orderDeliveredDate = "" + snapshot.get("orderDeliveredDate");
                            String orderDeliveredTime = "" + snapshot.get("orderDeliveredTime");

                            deliveryDate.setText(orderDeliveredDate + "  " + orderDeliveredTime);
                        }
                    }
                });
    }

    private void loadReturnDetails(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {

                            String orderReturnDate = "" + snapshot.get("orderReturnDate");
                            String orderReturnTime = "" + snapshot.get("orderReturnTime");

                            returnDate.setText(orderReturnDate + "  " + orderReturnTime);
                        }
                    }
                });
    }
    private void loadRefundDetails(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {

                            String orderRefundedDate = "" + snapshot.get("orderRefundedDate");
                            String orderRefundedTime = "" + snapshot.get("orderRefundedTime");

                            refundDate.setText(orderRefundedDate + "  " + orderRefundedTime);
                        }
                    }
                });
    }

    private void loadOrderDetails(String orderId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {
                            String productTitle1 = snapshot.get("productTitle").toString();
                            String purchasedPrice = snapshot.get("purchasedPrice").toString();
                            String originalPrice = snapshot.get("originalPrice").toString();
                            String productPrice1 = snapshot.get("productPrice").toString();
                            String deliveryFee1 = snapshot.get("deliveryFee").toString();
                            String discount = snapshot.get("discount").toString();
                            String orderDate = snapshot.get("orderDate").toString();
                            String orderTime = snapshot.get("orderTime").toString();
                            String name = snapshot.get("name").toString();
                            String quantity = snapshot.get("quantity").toString();
                            String shippingAddress = snapshot.get("shippingAddress").toString();
                            String phoneNo = snapshot.get("phoneNo").toString();
                            String orderFrom = snapshot.get("orderFrom").toString();
                            productId = snapshot.get("productId").toString();

                            productTitle.setText(productTitle1);
                            productPrice.setText(purchasedPrice);
                            orderedDate.setText(orderDate + "   " + orderTime);
                            nameTv.setText(name);
                            addressTv.setText(shippingAddress);
                            phoneNoTv.setText(phoneNo);
                            totalItemsPrice.setText(originalPrice);
                            priceTv.setText(productPrice1);
                            totalPrice.setText(purchasedPrice);
                            deliveryPrice.setText(deliveryFee1);
                            discountPrice.setText(discount);
                            productQuantity.setText("Qty: " + quantity);
                            savedAmount.setText("You saved Rs." + discount + " on this Order");

                            binding.codAmountTv.setText("COD: " + purchasedPrice + " to be paid");

                            loadProductDetails(productId);
                            loadShopName(orderFrom);
                        }
                    }
                });
    }

    private void loadShopName(String orderFrom) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(orderFrom);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                String shopName = snapshot.getString("shopName");

                shopNameTv.setText("Seller: " + shopName);
            }
        });
    }

    private void loadProductDetails(String productId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot.exists()) {
                    String productImage1 = snapshot.get("productImage").toString();
                    String productDescription = snapshot.get("productDescription").toString();

                    productDescriptionTv.setText(productDescription);

                    try {
                        Picasso.get().load(productImage1).fit().centerCrop().placeholder(R.drawable.ic_cart_black).into(productImage);
                    } catch (Exception e) {
                        productImage.setImageResource(R.drawable.ic_cart_black);
                    }
                }
            }
        });
    }

    private void markOrderStatusPacked(String orderId, String orderBy) {

        binding.orderPackedBtn.setEnabled(false);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String orderPackedDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        String orderPackedTime = currentTimeFormat.format(calForTime.getTime());

        isPacked = true;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus", "packed");
        hashMap.put("isPacked", isPacked);
        hashMap.put("orderPackedDate", "" + orderPackedDate);
        hashMap.put("orderPackedTime", "" + orderPackedTime);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            packedShippingProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            packedTv.setText("Your order has been packed");
                            packedDate.setText(orderPackedDate + "  " + orderPackedTime);

                        } else {
                            Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void markOrderStatusShipped(String orderId, String orderBy) {

        binding.orderShippedBtn.setEnabled(false);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String orderShippedDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        String orderShippedTime = currentTimeFormat.format(calForTime.getTime());

        isShipped = true;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus", "shipped");
        hashMap.put("isShipped", isShipped);
        hashMap.put("orderShippedDate", "" + orderShippedDate);
        hashMap.put("orderShippedTime", "" + orderShippedTime);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            shippedDeliveryProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippingIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            shippedTv.setText("Your order has been shipped");
                            shippedDate.setText(orderShippedDate + "  " + orderShippedTime);

                        } else {
                            Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void markOrderStatusDelivered(String orderId, String orderBy) {

        binding.orderDeliveredBtn.setEnabled(false);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String orderDeliveredDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        String orderDeliveredTime = currentTimeFormat.format(calForTime.getTime());

        isDelivered = true;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus", "delivered");
        hashMap.put("isDelivered", isDelivered);
        hashMap.put("orderDeliveredDate", "" + orderDeliveredDate);
        hashMap.put("orderDeliveredTime", "" + orderDeliveredTime);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            deliveredIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            deliveredTv.setText("Your order has been Delivered");
                            deliveryDate.setText(orderDeliveredDate + "  " + orderDeliveredTime);
                            updateSellingCount();

                        } else {
                            Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void markOrderStatusRefunded(String orderId, String orderBy) {
        binding.orderRefundBtn.setEnabled(false);
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String orderRefundedDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        String orderRefundedTime = currentTimeFormat.format(calForTime.getTime());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus", "refunded");
        hashMap.put("isRefunded", true);
        hashMap.put("orderRefundedDate", "" + orderRefundedDate);
        hashMap.put("orderRefundedTime", "" + orderRefundedTime);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(orderBy);
        documentReference.collection("myOrders").document(orderId)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            refundProgressBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            refundIndicator.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                            refundDate.setText(orderRefundedDate + "  " + orderRefundedTime);
                            refundTv.setText("Your Amount has been Refunded");


                        } else {
                            Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateSellingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("products");
        ref.child(productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String sellingCount = "" + snapshot.child("sellingCount").getValue();
                        if (sellingCount.equals("") || sellingCount.equals("null")) {
                            sellingCount = "0";
                        }
                        long newSellingCount = Long.parseLong(sellingCount) + 1;
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("sellingCount", "" + newSellingCount);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products");
                        reference.child(productId)
                                .updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(OrderDetailsActivity.this, "Selling Count Updated Successfully!!!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(OrderDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
package kashyap.anurag.frenzyseller.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.frenzyseller.Fragments.HomeFragment;
import kashyap.anurag.frenzyseller.R;
import kashyap.anurag.frenzyseller.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ActivityMainBinding binding;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private RelativeLayout noDataRl;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TextView headerNameTv, headerEmailTv;
    private ImageView profilePic;

    private static final int HOME_FRAGMENT = 0;
    private static int currentFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");

        frameLayout = findViewById(R.id.mainFrameLayout);

        noDataRl = findViewById(R.id.noDataRl);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {


            loadMyInfo();



            DrawerLayout drawer = binding.drawerLayout;
            navigationView = findViewById(R.id.nav_view);

            navigationView.setNavigationItemSelectedListener(MainActivity.this);
            navigationView.getMenu().getItem(0).setChecked(true);

            View headerView = navigationView.getHeaderView(0);
            headerNameTv = (TextView) headerView.findViewById(R.id.nameTv);
            headerEmailTv = (TextView) headerView.findViewById(R.id.emailTv);
            profilePic = (ImageView) headerView.findViewById(R.id.profileIv);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            setFragment(new HomeFragment(), HOME_FRAGMENT);
        } else {
            noDataRl.setVisibility(View.VISIBLE);
        }
    }

    private void loadMyInfo() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Sellers").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot.exists()){
                    String shopName = snapshot.get("shopName").toString();
                    String email = snapshot.get("email").toString();
                    String profileImage = snapshot.get("profileImage").toString();


                    headerNameTv.setText(shopName);
                    headerEmailTv.setText(email);

                    try {
                        Picasso.get().load(profileImage).fit().centerCrop().placeholder(R.drawable.ic_person_black).into(profilePic);
                    } catch (Exception e) {
                        profilePic.setImageResource(R.drawable.ic_person_black);
                    }
                }


            }
        });
    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (currentFragment != fragmentNo) {
            currentFragment = fragmentNo;
            invalidateOptionsMenu();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(this, "On that fragment Only!!!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) binding.drawerLayout;
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                invalidateOptionsMenu();
                setFragment(new HomeFragment(), HOME_FRAGMENT);
                navigationView.getMenu().getItem(0).setChecked(true);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navMyFrenzy) {
            invalidateOptionsMenu();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
            Toast.makeText(this, "Home!!!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.allCategory) {
            startActivity(new Intent(MainActivity.this, AllCategoryActivity.class));

        } else if (id == R.id.navOrders) {
            startActivity(new Intent(MainActivity.this, AllOrdersActivity.class));

        }else if (id == R.id.navOutOfStock) {
            startActivity(new Intent(MainActivity.this, OutOfStockProductsActivity.class));

        }else if (id == R.id.navMyProducts) {
            startActivity(new Intent(MainActivity.this, MyProducts.class));

        } else if (id == R.id.navRewards) {

            Toast.makeText(this, "Rewards!!!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.navCart) {

            Toast.makeText(this, "Carts!!!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.navWishlist) {

            Toast.makeText(this, "Wishlist!!!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.navAccount) {

        } else if (id == R.id.navLogout) {
            showLogoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void showLogoutDialog() {
        Dialog logoutDialog = new Dialog(MainActivity.this);
        logoutDialog.setContentView(R.layout.logout_dialog);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView cancelBtn = logoutDialog.findViewById(R.id.cancelBtn);
        TextView logoutBtn = logoutDialog.findViewById(R.id.logoutBtn);
        logoutDialog.setCancelable(true);

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
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finishAffinity();
            }
        });
        logoutDialog.show();
    }
}
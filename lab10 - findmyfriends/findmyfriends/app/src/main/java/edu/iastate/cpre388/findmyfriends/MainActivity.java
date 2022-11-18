package edu.iastate.cpre388.findmyfriends;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Map;

import edu.iastate.cpre388.findmyfriends.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private FMFViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new FMFViewModelFactory(getApplicationContext(),
                ((FMFApplication)getApplication()).executor)).get(FMFViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        ArrayList<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN);
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        } else {
            permissions.add(Manifest.permission.BLUETOOTH);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        ArrayList<String> neededPermissions = new ArrayList<>();
        for(String perm : permissions) {
            if(ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(perm);
            }
        }

        if(neededPermissions.size() > 0) {
            ActivityResultLauncher<String[]> launcher =
                    registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            for(String key : result.keySet()) {
                                if (!result.get(key)) {
                                    Log.d(TAG, "onActivityResult: Permissions not granted for " + key);
                                    Toast.makeText(getApplicationContext(), R.string.no_bluetooth_text, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            launcher.launch(permissions.toArray(new String[] { "hi" }));
        } else {
            Log.d(TAG, "onCreate: All permissions already granted!");
        }
    }
}
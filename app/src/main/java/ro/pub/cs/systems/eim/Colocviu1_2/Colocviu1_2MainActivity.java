package ro.pub.cs.systems.eim.Colocviu1_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Colocviu1_2MainActivity extends AppCompatActivity {

    Button Add, Compute;
    EditText nextTerm;
    TextView allTerms;
    Integer sum = 0;
    String lastOperation = "";
    String serviceStatus = "";
    ActivityResultLauncher<Intent> activityResultLauncher;

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedMessage = intent.getStringExtra("BROADCAST_RECEIVER_EXTRA");
            Log.d("BROADCAST_RECEIVER_TAG", receivedMessage);
            Toast.makeText(getApplicationContext(), receivedMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test_01_2_main);

        Add = findViewById(R.id.add_button);
        Compute = findViewById(R.id.compute_button);
        nextTerm = findViewById(R.id.next_term);
        allTerms = findViewById(R.id.all_terms);

        Add.setOnClickListener(v -> {
            try {
                int nextTermValue = Integer.parseInt(nextTerm.getText().toString());
                allTerms.setText(allTerms.getText().toString() + (allTerms.getText().toString().isEmpty() ? "" : "+") + nextTermValue);
                nextTerm.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                sum = intent.getIntExtra("sum", 0);
                Toast.makeText(getApplicationContext(), "Sum: " + sum, Toast.LENGTH_SHORT).show();

                if (sum > 10 && !serviceStatus.equals("service_started")) {
                    Intent serviceIntent = new Intent(getApplicationContext(), Colocviu1_2Service.class);
                    serviceIntent.putExtra("sum", sum);
                    startForegroundService(serviceIntent);
                    serviceStatus = "service_started";
                }
            }
        });

        Compute.setOnClickListener(v -> {
            if (allTerms.getText().toString().equals(lastOperation)) {
                Toast.makeText(getApplicationContext(), "Sum already calculated", Toast.LENGTH_SHORT).show();
                allTerms.setText(Integer.toString(sum));
                return;
            }

            Intent intent = new Intent(this, Colocviu1_2SecondaryActivity.class);
            lastOperation = allTerms.getText().toString();
            intent.putExtra("all_terms", lastOperation);
            activityResultLauncher.launch(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sum", sum);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("sum")) {
            sum = savedInstanceState.getInt("sum");
            allTerms.setText(Integer.toString(sum));
        }
    }

    protected void onDestroy() {
        Intent intent = new Intent(this, Colocviu1_2Service.class);
        stopService(intent);
        serviceStatus = "service_stopped";
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action_string");
        registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }
}
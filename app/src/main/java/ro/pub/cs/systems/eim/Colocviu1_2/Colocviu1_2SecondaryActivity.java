package ro.pub.cs.systems.eim.Colocviu1_2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Colocviu1_2SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String allTerms = intent.getStringExtra("all_terms");
        String[] terms = allTerms.split("\\+");
        int sum = 0;
        for (String term : terms) {
            sum += Integer.parseInt(term);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("sum", sum);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
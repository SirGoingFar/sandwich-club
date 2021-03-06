package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = DEFAULT_POSITION;
        try{
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        //reference all the views to populate
        TextView mainNameTextView = findViewById(R.id.name_tv);
        TextView alsoKnownAsTextView = findViewById(R.id.also_known_tv);
        TextView originTextView = findViewById(R.id.origin_tv);
        TextView descriptionTextView = findViewById(R.id.description_tv);
        TextView ingredientsTextView = findViewById(R.id.ingredients_tv);

        if(sandwich != null){
            //set name
            mainNameTextView.setText(sandwich.getMainName());

            //set other names
            populateTextView(sandwich.getAlsoKnownAs(), alsoKnownAsTextView, getString(R.string.place_holder_no_other_name));

            //set origin
            originTextView.setText(sandwich.getPlaceOfOrigin().equalsIgnoreCase("") ?
                    getString(R.string.place_holder_not_specified) : sandwich.getPlaceOfOrigin());

            //set description
            descriptionTextView.setText(sandwich.getDescription());

            //set ingredient(s)
            populateTextView(sandwich.getIngredients(), ingredientsTextView, getString(R.string.place_holder_not_specified));
        }
    }

    private void populateTextView(List<String> alsoKnownAs, TextView currentTextView, String emptyStateValue) {
        for(int count = 0; count < alsoKnownAs.size(); count++) {
            currentTextView.append(alsoKnownAs.get(count));

            //separate items reasonably
            if(count == alsoKnownAs.size() - 2 && alsoKnownAs.size() > 1)
                currentTextView.append(" and ");
            else if(count < alsoKnownAs.size() - 1)
                currentTextView.append(", ");
        }

        //ensure a proper empty state text and as when due
        if(alsoKnownAs.size() == 0)
            currentTextView.setText(emptyStateValue);
    }
}

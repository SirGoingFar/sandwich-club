package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        final Sandwich sandwich = new Sandwich();

        try{
            JSONObject baseJson = new JSONObject(json);

            //get name
            JSONObject nameObject = baseJson.getJSONObject("name");
                sandwich.setMainName(nameObject.getString("mainName"));
                sandwich.setAlsoKnownAs(convertArrayToArrayList(nameObject.getJSONArray("alsoKnownAs")));

            //get others information
            sandwich.setPlaceOfOrigin(baseJson.getString("placeOfOrigin"));
            sandwich.setDescription(baseJson.getString("description"));
            sandwich.setImage(baseJson.getString("image"));
            sandwich.setIngredients(convertArrayToArrayList(baseJson.getJSONArray("ingredients")));
        }catch (JSONException e){
            e.printStackTrace();
        }

        return sandwich;
    }

    private static ArrayList<String> convertArrayToArrayList(JSONArray alsoKnownAsArray) {
        ArrayList<String> alsoKnownAsList = new ArrayList<>();
        String otherName = null;
        int count = 0;
        boolean arrayHasNext = true;

        while (arrayHasNext){
            try {
                otherName = alsoKnownAsArray.getString(count);
            }catch (Exception ex){
                ex.printStackTrace();
                break;
            }

            if(!otherName.isEmpty()) {
                alsoKnownAsList.add(otherName);
                count++;
            }

            arrayHasNext = !otherName.isEmpty();
        }

        return alsoKnownAsList;
    }
}

package com.apps.onlinegroceriesapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apps.onlinegroceriesapps.CustomView.MyRadioButton;
import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.api.ApiClient;
import com.apps.onlinegroceriesapps.api.CommanApiCall.CartCommonApiCall;
import com.apps.onlinegroceriesapps.api.interfaces.CartInterfaces;
import com.apps.onlinegroceriesapps.api.network.response.ImageSlider;
import com.apps.onlinegroceriesapps.api.network.response.ProductDetialsResponse;
import com.apps.onlinegroceriesapps.api.network.response.ProductResponse;
import com.apps.onlinegroceriesapps.api.network.services.ShopFragmentInterfaces;
import com.apps.onlinegroceriesapps.databinding.ActivityProductDetailsBinding;
import com.apps.onlinegroceriesapps.models.ChoiceOption;
import com.apps.onlinegroceriesapps.models.ProductDetails;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.models.VariantResponse;
import com.apps.onlinegroceriesapps.utils.Constent;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.apps.onlinegroceriesapps.utils.Util;
import com.apps.onlinegroceriesapps.utils.helper.SliderAdapter;
import com.apps.onlinegroceriesapps.utils.helper.SliderItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    UserResponseHelper helper;
    LoadingSpinner loadingSpinner;
    private VariantResponse variantResponse;
    ProductDetialsResponse productDetialsResponse;
    private ShopFragmentInterfaces apiService;
    UserPrefs userPrefs ;
    UserModel userModel ;
    RadioGroup dynamicRadiogroup;
    ProductDetails productDetails;
    MyRadioButton radioButton;
    LayoutInflater inflater;
    SliderAdapter adapter;
    String id;
    boolean isVariantResponse;
    CartCommonApiCall cartCommonApiCall;
    private HashMap<String, String> maps = new HashMap<>();
    private HashMap<String, String> selectedChoices = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialized();
        if(getIntent().getStringExtra("id")!=null){
            id = getIntent().getStringExtra("id");
            getProductDetails();
        }

        clickMethods();
        setupSliderView();
    }

    private void clickMethods() {
        binding.detailsCv.setOnClickListener(v -> {
            if(binding.descriptions.getVisibility()==View.VISIBLE){
                binding.detailsCv.setIconRotate(180);
                binding.descriptions.setVisibility(View.GONE);
            }else{
                binding.detailsCv.setIconRotate(270);
                binding.descriptions.setVisibility(View.VISIBLE);
            }
        });
        binding.Nutritions.setOnClickListener(v -> {
            if(binding.NutritionsTv.getVisibility()==View.VISIBLE){
                binding.Nutritions.setIconRotate(180);
                binding.NutritionsTv.setVisibility(View.GONE);
            }else{
                binding.Nutritions.setIconRotate(270);
                binding.NutritionsTv.setVisibility(View.VISIBLE);
            }
        });
        binding.caution.setOnClickListener(v -> {
            if(binding.cautionTv.getVisibility()==View.VISIBLE){
                binding.caution.setIconRotate(180);
                binding.cautionTv.setVisibility(View.GONE);
            }else{
                binding.caution.setIconRotate(270);
                binding.cautionTv.setVisibility(View.VISIBLE);
            }
        });
        binding.addToCartOne.setOnClickListener(v -> {
            int qty = (int) Integer.valueOf(binding.qty.getText().toString());
            int final_qty = qty+1;
            binding.qty.setText(String.valueOf(final_qty));
        });
        binding.descQty.setOnClickListener(v -> {
            int qty = (int) Integer.valueOf(binding.qty.getText().toString());
            if(qty==0){
                helper.showError("Max qty not less then 0");
                return;
            }
            int final_qty = qty-1;
            binding.qty.setText(String.valueOf(final_qty));
        });
        binding.AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtys = (int) Integer.valueOf(binding.qty.getText().toString());
                if(qtys==0){
                    helper.showError("Please increases qty ");
                    return;
                }
                if(isVariantResponse){
                    helper.showError("Select more option or variant ");
                    return;
                }
                if(variantResponse!=null){
                    cartCommonApiCall.updateCartWithQty(loadingSpinner, String.valueOf(productDetails.getId()),Util.nullString(variantResponse.getVariant_id()),userModel,binding.qty.getText().toString());
                }else{
                    cartCommonApiCall.updateCartWithQty(loadingSpinner, String.valueOf(productDetails.getId()),"0",userModel,binding.qty.getText().toString());
                }
            }
        });
    }

    private void setupSliderView() {
        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(3);
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.startAutoCycle();


        binding.imageSlider.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + binding.imageSlider.getCurrentPagePosition());
            }
        });
    }

    private void initialized() {
        helper = new UserResponseHelper(this);
        loadingSpinner = new LoadingSpinner(this);
        apiService = ApiClient.getClient().create(ShopFragmentInterfaces.class);
        userPrefs = new UserPrefs(this);
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new SliderAdapter(this);

        cartCommonApiCall = new CartCommonApiCall(this, body -> {
            if(body.isError()){
                helper.showError(body.getMessage());
            }else{
                helper.showSuccess(body.getMessage());
            }
        });
    }

    private void getProductDetails() {
        loadingSpinner.showLoading();

        Call<ProductDetialsResponse> call =  apiService.getProductById(id);

        call.enqueue(new Callback<ProductDetialsResponse>() {
            @Override
            public void onResponse(Call<ProductDetialsResponse> call, Response<ProductDetialsResponse> response) {
                loadingSpinner.cancelLoading();

                productDetialsResponse = response.body();
                if (!response.isSuccessful()) {
                    helper.showError("Some thing going wrong try after some time");
                    return;
                }
                System.out.println("dhfgcvfhdghjxvbghjjxghkjdhkjghdfkj"+ productDetialsResponse.getData().get(0).getChoiceOptions().size());
                System.out.println("sdjhfkjdhsjhkjghdfkjsghlkjhdsfkgjh"+ productDetialsResponse.getData().get(0));
                //set product data
//                mProducts.addAll(response.body().getData());
//                System.out.println("loading canceled");
//                bestSellingAdapter.notifyDataSetChanged();
//                if (mProducts.size() <= 0) {
//                    System.out.println("products_empty_text show");
//                }
                productDetails = response.body().getData().get(0);
                setProductDetails(response.body().getData().get(0));
            }

            @Override
            public void onFailure(Call<ProductDetialsResponse> call, Throwable t) {
                loadingSpinner.cancelLoading();
                System.out.println("error " + t.getMessage());
            }
        });
    }

    private void setProductDetails(ProductDetails data) {

        List<SliderItem> sliderItemList = new ArrayList<>();
        if(data.getPhotos().size() > 0) {
            for (int i = 0; i < data.getPhotos().size(); i++) {
                SliderItem sliderItem = new SliderItem();
                sliderItem.setDescription("");
                sliderItem.setImageUrl(data.getPhotos().get(i));
                sliderItemList.add(sliderItem);
            }
            adapter.renewItems(sliderItemList);
        }else{
            binding.imageSlider.setVisibility(View.GONE);
        }


        binding.productName.setText(data.getName());

        if(data.getChoiceOptions() != null && (data.getChoiceOptions().size() > 0)){
            binding.productVarient.setText(data.getId().toString());
        }else{
            binding.productVarient.setText("1 QTY , "+Util.convertPrice(this,data.getPriceHigher()));
        }
        binding.descriptions.setText(data.getDescription());
        if(data.getNutritions()!=null){
            binding.NutritionsTv.setText(data.getNutritions());
        }
        if(data.getCaution()!=null){
            binding.cautionTv.setText(data.getCaution());
        }

        if(data.getPriceLower().equals(data.getPriceHigher())){
            binding.price.setText(Util.convertPrice(this, data.getPriceLower()));
        }
        else {
            binding.price.setText(Util.convertPrice(this, productDetails.getPriceLower())+"-"+Util.convertPrice(this, productDetails.getPriceHigher()));
        }

        for (ChoiceOption choiceOption : data.getChoiceOptions()){
            isVariantResponse = true;
            dynamicRadiogroup = new RadioGroup(this);

            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutparams.setMargins(0,0,16,0);
            dynamicRadiogroup.setLayoutParams(layoutparams);

            for (String optionvalue : choiceOption.getOptions()){
                radioButton = (MyRadioButton) inflater.inflate(R.layout.my_radio_button, null);
                radioButton.setText(optionvalue);
                radioButton.setLayoutParams(layoutparams);
                radioButton.setTextSize(16);
                maps.put(optionvalue, choiceOption.getName());
                dynamicRadiogroup.addView(radioButton);
            }

            TextView textview = new TextView(this);
            textview.setText(choiceOption.getTitle());
            textview.setBackgroundColor(Color.parseColor("#F1F1F5"));
            textview.setTextColor(Color.BLACK);
            textview.setPadding(16, 25,0,25);
            textview.setTextSize(20);

            binding.test.addView(textview);
            binding.test.addView(dynamicRadiogroup);

            dynamicRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @SuppressLint("ResourceType")
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // TODO Auto-generated method stub
                    loadingSpinner.showLoading();
                    MyRadioButton radiochecked = (MyRadioButton) findViewById(checkedId);
                    createSelectedChoiceList(radiochecked.getText().toString());
                }
            });
        }
    }
    private void createSelectedChoiceList(String value){
        String choiceName = maps.get(value);
        if(!choiceName.equals("color")){
            selectedChoices.put(choiceName, value);
        }
        else {
            selectedChoices.put("color", value);
        }


        int id = productDetails.getId();
        String color = selectedChoices.get("color") != null ? selectedChoices.get("color") : null;
        JsonArray choicesArray = new JsonArray();

        for (ChoiceOption choiceOption : productDetails.getChoiceOptions()){
            if(selectedChoices.get(choiceOption.getName()) != null){
                JsonObject choice = new JsonObject();
                choice.addProperty("section", choiceOption.getName());
                choice.addProperty("title", choiceOption.getTitle());
                choice.addProperty("name", selectedChoices.get(choiceOption.getName()));
                choicesArray.add(choice);
            }
        }

        //Log.d("Test", choicesArray.toString());

        getVariantPrice(id, color, choicesArray);
    }

    private void getVariantPrice(int id, String color, JsonArray choicesArray) {

        Call<VariantResponse> call =  apiService.getVariantPrice(id,color,choicesArray.toString());

        System.out.println("dhfjhdj"+id);
        System.out.println("dhfjhdj"+color);
        System.out.println("dhfjhdj"+choicesArray.toString());
        call.enqueue(new Callback<VariantResponse>() {
            @Override
            public void onResponse(Call<VariantResponse> call, Response<VariantResponse> response) {
                loadingSpinner.cancelLoading();

                if (!response.isSuccessful()) {
                    helper.showError("Some thing going wrong try after some time");
                    return;
                }

                loadingSpinner.cancelLoading();
                variantResponse = response.body();
                binding.productVarient.setText(variantResponse.getVariant()+" / "+ Util.convertPrice(ProductDetailsActivity.this,variantResponse.getPrice()));
                binding.price.setText(Util.convertPrice(ProductDetailsActivity.this, variantResponse.getPrice()));
                System.out.println("hdghsgdjhgsdhgsdg"+variantResponse.toString());
            }

            @Override
            public void onFailure(Call<VariantResponse> call, Throwable t) {
                loadingSpinner.cancelLoading();
                System.out.println("error " + t.getMessage());
            }
        });
    }
}
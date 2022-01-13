package com.apps.onlinegroceriesapps.activity.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.activity.MainActivity;
import com.apps.onlinegroceriesapps.api.ApiClient;
import com.apps.onlinegroceriesapps.api.CommanApiCall.LoginByPhoneApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.UserProfileCommonApiCall;
import com.apps.onlinegroceriesapps.api.network.services.LoginPhoneApiInterfaces;
import com.apps.onlinegroceriesapps.api.network.services.UserProfileInterfaces;
import com.apps.onlinegroceriesapps.databinding.ActivityUpdateUserProfileBinding;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.UserAddressList;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.utils.BottomSheet.AddressList;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserProfileActivity extends AppCompatActivity {

    UserPrefs userPrefs ;
    UserModel userModel ;
    LoadingSpinner loadingSpinner;
    UserResponseHelper helper;
    ActivityUpdateUserProfileBinding binding;
    public static int RESULT_LOAD_IMAGE = 1;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private LoginPhoneApiInterfaces loginPhoneApiInterfaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialization();
        setInputLayoutData();
        clickMethod();
        setUI();
    }
    private void setUI() {
        if(userModel!=null){
            Glide.with(this).load(userModel.getUser().getAvatar()).into(binding.circleImageView);
        }

    }
    private void clickMethod() {

        binding.myLocations.setOnClickListener(v -> {
            new AddressList(this, loadingSpinner, data -> binding.address.setText(data.getAddress())).open();
        });
        binding.circleImageView.setOnClickListener(v -> {
//                pickFromGallery();
            Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });
        binding.updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name",binding.name.getText().toString());
                jsonObject.addProperty("email",binding.email.getText().toString());
                new UserProfileCommonApiCall(UpdateUserProfileActivity.this, new UserProfileCommonApiCall.CallBackUpdateProfileInfo() {
                    @Override
                    public void onResult(CommonGlobalMessageModel body) {
                        if(body.isError()){
                            helper.showError(body.getMessage());
                        }else{
                            helper.showSuccess(body.getMessage());
                            getUser();
                        }
                    }
                }).updateProfile(loadingSpinner,userModel,jsonObject);
            }
        });
    }

    private void getUser() {
        new LoginByPhoneApiCall(UpdateUserProfileActivity.this, (LoginByPhoneApiCall.LoginInterfaces) body -> new UserPrefs(UpdateUserProfileActivity.this).setAuthPreferenceObject(body,UserPrefs.users)).loginByUserId(loadingSpinner,loginPhoneApiInterfaces,userModel);
    }

    //TODO: we will work latter this code also bee fine
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        startActivityForResult(Intent.createChooser(intent, "Select image from "), RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && null != data){
            if (requestCode == RESULT_LOAD_IMAGE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCrop(selectedUri);
                } else {
                    helper.showError(getString(R.string.cannot_retrieve_cropped_image));
                }

            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }

    }
    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            binding.circleImageView.setImageURI(resultUri);
            processToUpload(resultUri);
        } else {
            helper.showError(getString(R.string.cannot_retrieve_cropped_image));
        }
    }


    private void processToUpload(Uri data) {
        loadingSpinner.showLoading();
        UserProfileInterfaces apiService = ApiClient.getClient().create(UserProfileInterfaces.class);
        File file = new File(data.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody =MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<CommonGlobalMessageModel> responseBodyCall = apiService.updateUserProfilePicture(userModel.getToken_type()+" "+userModel.getAccess_token(),  multipartBody);

        responseBodyCall.enqueue(new Callback<CommonGlobalMessageModel>() {
            @Override
            public void onResponse(Call<CommonGlobalMessageModel> call, Response<CommonGlobalMessageModel> response) {
                loadingSpinner.cancelLoading();
                if(userModel!=null){
                    getUser();
                }
                if(response.body().isError()){
                    helper.showError(response.body().getMessage());
                }else{
                    helper.showSuccess(response.body().getMessage());
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<CommonGlobalMessageModel> call, Throwable t) {
                Log.d("CommonGlobalMessageModel", "message = " + t.getMessage());
                Log.d("failurefailurefailurefailure", "cause = " + t.getCause());
            }
        });
    }

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME+".png";

        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                    .start(this);

    }

    private void initialization() {
        userPrefs = new UserPrefs(this);

        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        loadingSpinner = new LoadingSpinner(this);
        helper = new UserResponseHelper(this);
        loginPhoneApiInterfaces = ApiClient.getClient().create(LoginPhoneApiInterfaces.class);
    }

    private void setInputLayoutData() {
        binding.email.setText(userModel.getUser().getEmail());
        binding.phone.setText(userModel.getUser().getPhone());
        binding.name.setText(userModel.getUser().getName());
        if(userPrefs.getDefaultAddress()!=null){
            binding.address.setText(userPrefs.getDefaultAddress().getAddress());
        }

    }
}
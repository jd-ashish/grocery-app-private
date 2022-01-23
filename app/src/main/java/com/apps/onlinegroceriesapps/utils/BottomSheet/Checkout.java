package com.apps.onlinegroceriesapps.utils.BottomSheet;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_CVV;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_HOLDER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_MM;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_NUMBER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_YYYY;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.apps.onlinegroceriesapps.BuildConfig;
import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.activity.order.SuccessActivity;
import com.apps.onlinegroceriesapps.api.CommanApiCall.OrderCommonApiCall;
import com.apps.onlinegroceriesapps.api.CommanApiCall.payment.CashFreeApiCall;
import com.apps.onlinegroceriesapps.api.network.response.AppSettingsResponse;
import com.apps.onlinegroceriesapps.databinding.BottomCheckoutLayoutBinding;
import com.apps.onlinegroceriesapps.models.AppSettings;
import com.apps.onlinegroceriesapps.models.CommonGlobalMessageModel;
import com.apps.onlinegroceriesapps.models.PaymentModel;
import com.apps.onlinegroceriesapps.models.UserAddressList;
import com.apps.onlinegroceriesapps.models.UserModel;
import com.apps.onlinegroceriesapps.models.payment.cashfree.CFTokenModel;
import com.apps.onlinegroceriesapps.utils.Constent;
import com.apps.onlinegroceriesapps.utils.LoadingSpinner;
import com.apps.onlinegroceriesapps.utils.UniqueId;
import com.apps.onlinegroceriesapps.utils.UserPrefs;
import com.apps.onlinegroceriesapps.utils.UserResponseHelper;
import com.apps.onlinegroceriesapps.utils.Util;
import com.cashfree.pg.CFPaymentService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Checkout {
    Context context;
    LoadingSpinner loadingSpinner;
    UserResponseHelper helper;
    BottomCheckoutLayoutBinding binding;
    double totalAmount;
    UserAddressList userAddressList;
    PaymentModel paymentModel;
    UserPrefs userPrefs ;
    UserModel userModel ;
    JsonObject jsonObject;
    FragmentActivity activity;
    String stage = "TEST";
    String OrderId;

    //Atm card details
    String accountHolderNames = null;
    String cardNumbers = null;
    String months = null;
    String years = null;
    String cvvs = null;
    AppSettings appSettings;
    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }
    SeamlessMode currentMode = SeamlessMode.CARD;
    public Checkout(Context context, LoadingSpinner loadingSpinner, double totalAmount, FragmentActivity activity) {
        this.context = context;
        this.loadingSpinner = loadingSpinner;
        this.totalAmount = totalAmount;
        this.activity = activity;
    }

    public void open(){
        OrderId = String.valueOf(new Date().getTime());
        System.out.println("OrderIdOrderIdOrderId "+OrderId);
        jsonObject = new JsonObject();
        userPrefs = new UserPrefs(context);
        userModel = userPrefs.getAuthPreferenceObjectJson(UserPrefs.users);
        helper = new UserResponseHelper(context);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        binding = BottomCheckoutLayoutBinding.inflate(LayoutInflater.from(context));
        bottomSheetDialog.setContentView(binding.getRoot());
        AppSettingsResponse appSettingsResponse = userPrefs.getAppSettingsPreferenceObjectJson();
        appSettings = appSettingsResponse.getData().get(0);

        if(userPrefs.getDefaultAddress()!=null){
            userAddressList = userPrefs.getDefaultAddress();
            binding.delivery.setRight_text_icon(userPrefs.getDefaultAddress().getKnownName());
        }
        binding.delivery.setOnClickListener(v -> {
            new AddressList(context, loadingSpinner, new AddressList.GetAddressList() {
                @Override
                public void getAddressList(UserAddressList data) {
                    userAddressList = data;
                    binding.delivery.setRight_text_icon(data.getKnownName());
                }
            }).open();
        });
        binding.close.setOnClickListener(v -> bottomSheetDialog.dismiss());
        binding.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingSpinner.showLoading();
                new SelectPaymentOptions(context, loadingSpinner, helper, new SelectPaymentOptions.SelectPaymentOptionsInterfaces() {
                    @Override
                    public void CallBack(PaymentModel paymentModelOption) {
                        loadingSpinner.cancelLoading();
                        paymentModel = paymentModelOption;
                        binding.payment.setRight_text_icon(paymentModelOption.getPayment_text());
                    }
                },userPrefs).Open();
            }
        });
        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingSpinner.showLoading();
//                new AtmCard(context, new AtmCard.AtmCardInterfaces() {
//                    @Override
//                    public void CallBack(String accountHolderName, String cardNumber, String month, String year, String cvv) {
//
//                        accountHolderNames = accountHolderName;
//                        cardNumbers = cardNumber;
//                        months = month;
//                        years = year;
//                        cvvs = cvv;
//
////                        CashFree(totalAmount);
//                    }
//                },helper).Open();
//                context.startActivity(new Intent(context, SuccessActivity.class));



                if(userAddressList==null){
                    helper.showError("Please Select delivery address");
                    return;
                }
                if(paymentModel==null){
                    helper.showError("Please Select payment method");
                    return;
                }
                jsonObject.addProperty("address_id",userAddressList.getId());
                jsonObject.addProperty("payment_type","Cod");
                jsonObject.addProperty("payment_status","unpaid");
                jsonObject.addProperty("grand_total",totalAmount);
                if(paymentModel.getPayment_method().equals("cod")){
                    PlaceCodOrder();
                }
                if(paymentModel.getPayment_method().equals(Constent.RAZORPAY)){
                    userPrefs.setCheckoutList(jsonObject);
                    rzp(totalAmount);
                }
                if(paymentModel.getPayment_method().equals(Constent.CARD)){
                    userPrefs.setCheckoutList(jsonObject);

                    new AtmCard(context, new AtmCard.AtmCardInterfaces() {
                        @Override
                        public void CallBack(String accountHolderName, String cardNumber, String month, String year, String cvv) {

                            loadingSpinner.cancelLoading();
                            accountHolderNames = accountHolderName;
                            cardNumbers = cardNumber;
                            months = month;
                            years = year;
                            cvvs = cvv;

                            CashFree(totalAmount);
                        }
                    },helper).Open();
                }

            }
        });

        binding.totalAmount.setRight_text_icon(Util.convertPrice(context,totalAmount));
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.show();
    }

    private void CashFree(double totalAmount) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orderId",OrderId);
        jsonObject.addProperty("orderAmount",totalAmount);
        jsonObject.addProperty("orderCurrency","INR");

        new CashFreeApiCall(context, new CashFreeApiCall.CashFreeApiCallInterfaces.CFToken() {
            @Override
            public void onResult(CFTokenModel body) {
                CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
                cfPaymentService.setOrientation(0);

//                System.out.println("hcghjxghfjgvxdcjvgdfkljglkjghh"+new Gson().toJson(getSeamlessCheckoutParams()));
                cfPaymentService.doPayment(activity,getSeamlessCheckoutParams(), body.getCftoken(), stage);
            }
        }).GenerateCFToken(loadingSpinner,jsonObject);
    }

    private Map<String, String> getInputParams() {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = Constent.client_id;
        String orderAmount = String.valueOf(totalAmount);
        String orderNote = "";
        String customerName = userModel.getUser().getName();
        String customerPhone = userModel.getUser().getPhone().toString();
        String customerEmail = userModel.getUser().getEmail().toString();

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, OrderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }

    private Map<String, String> getSeamlessCheckoutParams() {
        Map<String, String> params = getInputParams();
        switch (currentMode) {
            case CARD:
                params.put(PARAM_PAYMENT_OPTION, "card");
                params.put(PARAM_CARD_NUMBER, cardNumbers);
                params.put(PARAM_CARD_YYYY, years);
                params.put(PARAM_CARD_MM, months);
                params.put(PARAM_CARD_HOLDER, accountHolderNames);
                params.put(PARAM_CARD_CVV, cvvs);
                break;

//            case WALLET:
//                params.put(PARAM_PAYMENT_OPTION, "wallet");
//                params.put(PARAM_WALLET_CODE, "4007"); // Put one of the wallet codes mentioned here https://dev.cashfree.com/payment-gateway/payments/wallets
//                break;
//            case NET_BANKING:
//                params.put(PARAM_PAYMENT_OPTION, "nb");
//                params.put(PARAM_BANK_CODE, "3333"); // Put one of the bank codes mentioned here https://dev.cashfree.com/payment-gateway/payments/netbanking
//                break;
//            case UPI_COLLECT:
//                params.put(PARAM_PAYMENT_OPTION, "upi");
//                params.put(PARAM_UPI_VPA, "VALID_VPA");
//                break;
//            case PAY_PAL:
//                params.put(PARAM_PAYMENT_OPTION, "paypal");
//                break;
        }
        return params;
    }

    private void PlaceCodOrder() {
        new OrderCommonApiCall(context, new OrderCommonApiCall.PlaceOrderApiCall() {
            @Override
            public void CallBack(CommonGlobalMessageModel body) {
                if(body.isError()){
                    helper.showError(body.getMessage());
                }else{
                    context.startActivity(new Intent(context, SuccessActivity.class));
                }
            }
        }).placeOrder(loadingSpinner,jsonObject,userModel);
    }

    public void rzp(Double total){


        final com.razorpay.Checkout co = new com.razorpay.Checkout();
        co.setKeyID(appSettings.getRzpDetails().getRZP_KEY());
//        co.setKeyID(Constent.RZP_KEY);
//        co.setKeyID(Constant_Api.RZP_KEY_ID);
        String preorder_id = null;

        // Get the double value
        double data = total;

        int main_amt = (int)data;
        int amt = main_amt*100;
        try {

            preorder_id = new DownloadWebpageTask(amt).execute().get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject options = new JSONObject();
            options.put("name", context.getResources().getString (R.string.app_name) );
            options.put("description", "#order_"+preorder_id);
            options.put("order_id", preorder_id);//from response of step 3.
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
//            options.put("status", "created");

            options.put("receipt", preorder_id);
            options.put("note", "58798748595");


            options.put("amount", amt);

            JSONObject preFill = new JSONObject();
            if(userModel.getUser()!=null){
                if(userModel.getUser().getEmail()!=null){
                    if(!userModel.getUser().getEmail().equals("")){
                        preFill.put("email", userModel.getUser().getEmail());
                    }
                }
            }
            if(userModel.getUser()!=null){
                if(userModel.getUser().getPhone()!=null){
                    if(!userModel.getUser().getPhone().equals("")){
                        preFill.put("contact", userModel.getUser().getPhone());
                    }
                }
            }



            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            System.out.println("dfghjdgvhjxcvhjbcjvbcbvjhvjxck "+e.getMessage());
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            //e.printStackTrace();

            Log.v("ErrorRazorPay : ", e.getMessage());
        }


    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        int amt = 0*100;

        public DownloadWebpageTask(int amt) {
            this.amt = amt;
        }

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.

            String id = "";
            RazorpayClient razorpay = null;
            try {
                razorpay = new RazorpayClient(appSettings.getRzpDetails().getRZP_KEY(),appSettings.getRzpDetails().getRZP_SECRT());
            } catch (RazorpayException e) {
                e.printStackTrace();
            }
            try {
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", amt); // amount in the smallest currency unit
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "order_rcptid_11");

                Order order = razorpay.Orders.create(orderRequest);

                JSONObject jsonObject = new JSONObject(String.valueOf(order));
                id = jsonObject.getString("id");
                System.out.println(id);
            } catch (RazorpayException | JSONException e) {
                // Handle Exception
                System.out.println(e.getMessage());
            }
            return id;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            OID.setText("#"+result);
        }
    }
}

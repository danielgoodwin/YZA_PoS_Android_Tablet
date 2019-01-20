package com.pos.yza.yzapos.newtransaction;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Response;
import com.pos.yza.yzapos.Injection;
import com.pos.yza.yzapos.R;
import com.pos.yza.yzapos.data.representations.LineItem;
import com.pos.yza.yzapos.data.representations.Payment;
import com.pos.yza.yzapos.data.representations.Product;
import com.pos.yza.yzapos.data.representations.ProductCategory;
import com.pos.yza.yzapos.data.representations.Transaction;
import com.pos.yza.yzapos.data.source.PaymentsRepository;
import com.pos.yza.yzapos.data.source.ProductsDataSource;
import com.pos.yza.yzapos.data.source.ProductsRepository;
import com.pos.yza.yzapos.data.source.TransactionsDataSource;
import com.pos.yza.yzapos.data.source.TransactionsRepository;
import com.pos.yza.yzapos.newtransaction.cart.CartFragment;
import com.pos.yza.yzapos.newtransaction.cart.CartActions;
import com.pos.yza.yzapos.newtransaction.cart.CartPresenter;
import com.pos.yza.yzapos.newtransaction.categoryselection.CategorySelectionFragment;
import com.pos.yza.yzapos.newtransaction.categoryselection.CategorySelectionPresenter;
import com.pos.yza.yzapos.newtransaction.staffandcustomerdetails.StaffAndCustomerDetailsFragment;
import com.pos.yza.yzapos.newtransaction.staffandcustomerdetails.StaffAndCustomerDetailsPresenter;
import com.pos.yza.yzapos.newtransaction.payment.PaymentFragment;
import com.pos.yza.yzapos.newtransaction.payment.PaymentPresenter;
import com.pos.yza.yzapos.newtransaction.productselection.ProductSelectionFragment;
import com.pos.yza.yzapos.newtransaction.productselection.ProductSelectionPresenter;
import com.pos.yza.yzapos.util.ActivityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import lib.printer.thermal.Printer;
import lib.printer.thermal.bluetooth.BluetoothPrinters;

public class NewTransactionActivity extends AppCompatActivity
        implements OnFragmentInteractionListener{

    private static final String CART = "CART";
    private static final String CATEGORY_SELECTION = "CATEGORY_SELECTION";
    private static final String PRODUCT_SELECTION = "PRODUCT_SELECTION";
    private static final String CUSTOMER_DETAILS = "CUSTOMER_DETAILS";
    private static final String PAYMENT = "PAYMENT";

    private CartPresenter mCartPresenter;
    private StaffAndCustomerDetailsPresenter mStaffAndCustomerDetailsPresenter;
    private PaymentPresenter mPaymentPresenter;
    private CategorySelectionPresenter mCategorySelectionPresenter;
    private ProductSelectionPresenter mProductSelectionPresenter;

    private NewTransaction transaction = new NewTransaction();
    private TransactionsRepository mTransactionsRepository;
    private ProductsRepository mProductRepository;
    private PaymentsRepository mPaymentRepository;

    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        this.parentLayout = findViewById(android.R.id.content);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle(R.string.new_transaction);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CartFragment cartFragment =
                (CartFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cartFragment == null) {
            // Create the fragment
            cartFragment = CartFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), cartFragment, R.id.contentFrame, CART,
                    false);
        }

        // Create the presenter
        mCartPresenter = new CartPresenter(cartFragment);
        // Initialise the repo
        mTransactionsRepository = Injection.provideTransactionsRepository(this);
        mProductRepository = Injection.provideProductsRepository(this);
        mPaymentRepository = Injection.providePaymentsRepository(this);

    }

    @Override
    public boolean onSupportNavigateUp () {
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentMessage(String TAG, Object data) {
        switch (TAG) {
            case CART:
                handleCartActions(mCartPresenter.getAction(), data);
                break;
            case CATEGORY_SELECTION:
                handleCategorySelectionActions((ProductCategory)data);
                break;
            case PRODUCT_SELECTION:
                handleProductSelectionActions((LineItem)data);
                break;
            case PAYMENT:
                completeTransaction(data);
                break;
            case CUSTOMER_DETAILS:
                handleCustomerDetailsActions(data);
                break;
            default:
                break;
        }

    }

    private void handleCustomerDetailsActions(Object data){
        transaction.setCustomerDetails(data);
        HashMap<String, String> dataMap = (HashMap) data;
        transaction.setStaff(Integer.parseInt(dataMap.get("staff_id")));
        PaymentFragment paymentFragment =
                (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PAYMENT);
        if (paymentFragment == null) {
            // Create the fragment
            paymentFragment = PaymentFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), paymentFragment,
                    R.id.contentFrame, PAYMENT, true);
        }

        // Create the presenter
        mPaymentPresenter = new PaymentPresenter(paymentFragment, transaction);
    }

    private void handleCartActions(CartActions action, Object data) {
        switch (action) {
            case ADD_PRODUCT:

                CategorySelectionFragment categorySelectionFragment =
                        (CategorySelectionFragment) getSupportFragmentManager().
                                findFragmentByTag(CATEGORY_SELECTION);

                if (categorySelectionFragment == null) {
                    categorySelectionFragment = CategorySelectionFragment.newInstance();
                    ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                            categorySelectionFragment, R.id.contentFrame, CATEGORY_SELECTION,
                            true);
                }

                mCategorySelectionPresenter =
                        new CategorySelectionPresenter(categorySelectionFragment,
                                Injection.provideCategoriesRepository(this));

                break;
            case GO_TO_CUSTOMER_DETAILS:
                transaction.setCart(data);
                StaffAndCustomerDetailsFragment staffAndCustomerDetailsFragment =
                        (StaffAndCustomerDetailsFragment) getSupportFragmentManager().
                                findFragmentByTag(CUSTOMER_DETAILS);

                if (staffAndCustomerDetailsFragment == null) {
                    // Create the fragment
                    staffAndCustomerDetailsFragment = StaffAndCustomerDetailsFragment.newInstance();
                    ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                            staffAndCustomerDetailsFragment, R.id.contentFrame, CUSTOMER_DETAILS,
                            true);
                }
                // Create the presenter
                mStaffAndCustomerDetailsPresenter = new StaffAndCustomerDetailsPresenter(staffAndCustomerDetailsFragment);

                break;
            default:
                break;
        }

    }

    private void handleCategorySelectionActions(ProductCategory category) {
        Log.i("CATEGORY_SELECTION", "In New Transactions Activity");
        ProductSelectionFragment productSelectionFragment =
                (ProductSelectionFragment) getSupportFragmentManager().
                        findFragmentByTag(PRODUCT_SELECTION);

        if (productSelectionFragment == null) {
            // Create the fragment
            productSelectionFragment = ProductSelectionFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                    productSelectionFragment, R.id.contentFrame, PRODUCT_SELECTION, true);
        }

        // Create the presenter
        mProductSelectionPresenter = new ProductSelectionPresenter(productSelectionFragment,
                Injection.provideProductsRepository(this), category);

    }

    private void handleProductSelectionActions(LineItem data) {
        Log.i("PRODUCT_SELECTION", "In New Transactions Activity");
        mCartPresenter.addLineItem(data);
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        
    }

    private void completeTransaction(Object data) {
        transaction.setPayment(data);
        mTransactionsRepository.saveTransaction(transaction.createTransaction(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("saveTransaction", "success");
                        Log.i("saveTransaction", response.toString());
                        Snackbar mySnackbar;
                        try {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        printReceipt(response.getInt("transaction_id"));
                                    } catch (Exception ex)
                                    {
                                        //
                                        Log.w("PRINT", "Error printing receipt " + ex.getMessage());
                                    }
                                }
                            });

                            mySnackbar = Snackbar.make(parentLayout,
                                    getString(R.string.transaction_created) + " #"
                                            + response.getInt("transaction_id"),
                                    Snackbar.LENGTH_LONG);

                        }
                        catch (JSONException e) {
                            mySnackbar = Snackbar.make(parentLayout,
                                    getString(R.string.transaction_created),
                                    Snackbar.LENGTH_LONG);
                            e.printStackTrace();
                        }
                        mySnackbar.show();
                        int finishTime = 1;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                finish();
                            }
                        }, finishTime * 1000);



                    }
                });
        //printReceipt(-1);


    }

    private void printReceipt(int transid)
    {
        try {

            mTransactionsRepository.getTransactionById(String.valueOf(transid),
                            new TransactionsDataSource.GetTransactionCallback(){
                @Override
                public void onTransactionLoaded(Transaction transaction) {
                    Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(),
                            203, 48f, 32);

                    StringBuffer receiptText = new StringBuffer();
                    receiptText.append( "[C]<font size='big'>ORDER " + transaction.getTransactionId() + "</font>\n");
                    receiptText.append("[L]\n");
                    receiptText.append("[C]================================\n" );


                    for (int idx =0; idx < transaction.getLineItems().size(); idx++)
                    {
                        LineItem item = transaction.getLineItems().get(idx);

                        mProductRepository.getProduct(String.valueOf(item.getProductId()),
                                            new ProductsDataSource.GetProductCallback() {
                            @Override
                            public void onProductLoaded(Product product) {
                                receiptText.append("[L]" + product.getName() + " [R]" + item.getAmount() + "\n");
                            }

                            @Override
                            public void onDataNotAvailable() {
                                Log.e("GETPRODUCT", "Product id not found " + item.getProductId());
                            }
                        });
                    }

                    receiptText.append("[L]\n");
                    receiptText.append("[L]\n");
                    receiptText.append("[C]================================\n" );

                    if (transaction.getAmount() > 0) {
                        receiptText.append("[L]Total [R]" + transaction.getAmount() + "\n");
                    }

                    receiptText.append("[L]\n");
                    receiptText.append("[L]\n");
                    receiptText.append("[C]================================\n" );

                    if (transaction.getPayments().size() > 0) {
                        for (int idx = 0; idx < transaction.getPayments().size(); idx++) {
                            Payment item = transaction.getPayments().get(idx);

                            receiptText.append("[L]Paid [R]" + item.getAmount() + "\n");
                        }

                        receiptText.append("[L]\n");
                        receiptText.append("[L]\n");
                        receiptText.append("[C]================================\n");

                        receiptText.append("[L]Balance [R]" + transaction.getBalance() + "\n");
                        receiptText.append("[L]\n");
                        receiptText.append("[L]\n");
                        receiptText.append("[C]================================\n");
                    }

                    //receiptText.append("[C]<barcode type='ean13' height='10'>" + transaction.getTransactionId() + "</barcode>\n");
                    printer.printFormattedText(receiptText.toString());

                    //
                    // Feed before cut
                    for (int bidx =0; bidx< 100; bidx++)
                    {
                        receiptText.append("[L]\n");
                    }

                    printer.cutPaper();

                    printer.disconnectPrinter();

                }

                @Override
                public void onDataNotAvailable() {
                    Log.e("NEWTRANS", "Transaction not found");
                }
            });




        } catch (Exception ex)
        {
            Log.e("RECEIPT", "Error in receipt print " + ex.getMessage());
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }
}

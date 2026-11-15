package com.javy.pesapalclone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.javy.pesapalclone.adapters.AdapterListAccounts;
import com.javy.pesapalclone.adapters.AdapterListContacts;
import com.javy.pesapalclone.models.Account;
import com.javy.pesapalclone.models.Contact;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class SelectLeadAccountActivity extends AppCompatActivity implements OnSuccessListener {
    private RecyclerView recyclerView;
    private AdapterListAccounts mAdapter;
    private Button clockOutButton;
    private ProgressDialog pd;

    private List<Account> items;
    private Context context;
    private TypedArray drw_arr;
    private String name_arr[];
    private String mAuthToken;
    //    private SharedPreferences mPrefs;
    private TextView emptyView;
    private long lastIntentTime = 0;
    private static final long MIN_CLICK_INTERVAL = 1000; // 1000 milliseconds
    private RelativeLayout errorLoadingRelativeLayout;
    private LinearLayout lyt_no_connection;
    private TextView mNoAccountsTextView;
    private View view;

    private static final String ARG_URL = "arg_url";
    private String endpointUrl;
    private JSONArray accounts = null;
    private JSONArray leads = null;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private AdapterListContacts mAdapterLeads;
    private EditText searchEditText;
    private String selectedText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_lead_account);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Select Account");
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        emptyView = (TextView) findViewById(R.id.empty_view);
//        errorLoadingRelativeLayout = view.findViewById(R.id.errorLoading);
        mNoAccountsTextView = (TextView) findViewById(R.id.noAccountsText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectLeadAccountActivity.this));
        recyclerView.setHasFixedSize(true);

        // Find the RadioGroup by its ID
//        RadioGroup radioGroup = findViewById(R.id.group_basic);
        searchEditText = findViewById(R.id.et_search);




        // Set a listener for checked changes
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // Handle the checked change
//                RadioButton selectedButton = findViewById(checkedId);
//                if (selectedButton != null) {
//                    // Get the text of the selected RadioButton
//                    selectedText = selectedButton.getText().toString();
//
//                    // Show a toast with the selected option
////                    Toast.makeText(SelectLeadAccountActivity.this, "Selected: "
////                            + selectedText, Toast.LENGTH_SHORT).show();
//
//                    actionBar.setTitle("Select "+selectedText);
//                    searchEditText.setHint("Search "+selectedText+"s");
//                    searchEditText.setText("");
//
//
//
//
//                    // Perform further actions based on the selected button
//                    if (checkedId == R.id.rb_basic_1) {
//                        // Handle "Account" button selected
//                        NetworkUtils.
//                                fetchDataGet("accounts",
//                                        mAuthToken,SelectLeadAccountActivity.this,
//                                        SelectLeadAccountActivity.this);
//
//
//                    } else if (checkedId == R.id.rb_basic_2) {
//                        // Handle "Lead" button selected
//                        NetworkUtils.
//                                fetchDataGet("leads",
//                                        mAuthToken,SelectLeadAccountActivity.this,
//                                        SelectLeadAccountActivity.this);
//
//                    }
//                }
//            }
//        });

//        initToolbar();
//        initComponent();

//        lyt_no_connection = (LinearLayout) view.findViewById(R.id.lyt_no_connection);
        //lyt_no_connection.setVisibility(View.VISIBLE);

//        lyt_no_connection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                //   lyt_no_connection.setVisibility(View.GONE);
//                new JsonTask().execute(Constants.BASE_URL + endpointUrl,"GET");
//            }
//        });

//        new JsonTask().execute(Constants.BASE_URL + endpointUrl,"GET");
//        fetchData(mAuthToken);

        NetworkUtils.
                fetchDataGet("accounts",
                        mAuthToken,SelectLeadAccountActivity.this,
                        SelectLeadAccountActivity.this);
    }
    @Override
    public void onSuccess(ResponseBody responseBodyReceived) {

        Log.d("API Response", responseBodyReceived.toString());

        try {
//            Toasts.toastIconError(SelectLeadAccountActivity.this,selectedText);
            // Get the response body as a string
            String responseBody = responseBodyReceived.string();

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);
            List<Account> items = new ArrayList<>();
            List<Contact> itemsLeads = new ArrayList<>();

//            accounts = jsonObject.getJSONArray("accounts");
            if(jsonObject.has("accounts") &&
                    jsonObject.get("accounts") instanceof JSONArray)
            {
                accounts = jsonObject.getJSONArray("accounts");
                System.out.println("Accounts length: " + accounts.length());



//                        for (int i = 0; i < accounts.length(); i++) {
//                            Deal obj = new Deal();
//                            //obj.image = drw_arr.getResourceId(i, -1);
//                            obj.name = deals.getJSONObject(i).getString("name");
//                            obj.amount = deals.getJSONObject(i).getString("amount");
//                            obj.status = deals.getJSONObject(i).getString("status");
//
//                            String createdAt = deals.getJSONObject(i).getString("created_at");
//                            ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
//                            String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
//                            obj.date = formatted;
//                            //obj.imageDrw = drw_arr.getDrawable(obj.image);
//                            items.add(obj);
//
//                        }
                for (int i = 0; i < accounts.length(); i++) {
                    Account obj = new Account();
                    //obj.image = drw_arr.getResourceId(i, -1);
                    obj.account_name = accounts.getJSONObject(i).getString("name");
                    obj.address = accounts.getJSONObject(i).getString("address");
                    obj.phone = accounts.getJSONObject(i).getString("phone");
                    obj.website = accounts.getJSONObject(i).getString("website");
                    obj.id = accounts.getJSONObject(i).getString("id");

                    String createdAt = accounts.getJSONObject(i).getString("created_at");
                    ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                    String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
                    obj.date = formatted;
                    //obj.imageDrw = drw_arr.getDrawable(obj.image);
                    items.add(obj);

                }

                if (items.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setText("No accounts found");
                    emptyView.setVisibility(View.VISIBLE);

                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }


                //set data and list adapter
                mAdapter = new AdapterListAccounts(items);
                recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new AdapterListAccounts.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Account obj, int position) {

                        Account account = items.get(position);

                        Intent i = new Intent(SelectLeadAccountActivity.this,
                                AddDealActivity.class);
                        // passing array index
                        i.putExtra("account",account );
                        i.putExtra("accountOrLead","account");
                        startActivity(i);


                    }
                });





            }
            if(jsonObject.has("leads") &&
                    jsonObject.get("leads") instanceof JSONArray){
                leads = jsonObject.getJSONArray("leads");
                System.out.println("Leads length: "+leads.length());

                for (int i = 0; i < leads.length(); i++) {
                    Contact obj = new Contact();
                    //obj.image = drw_arr.getResourceId(i, -1);
                    obj.id = leads.getJSONObject(i).getString("id");
                    obj.name = leads.getJSONObject(i).getString("name");
                    obj.phone = leads.getJSONObject(i).getString("phone");
                    obj.account_name = leads.getJSONObject(i).getString("account_name");

                    String createdAt = leads.getJSONObject(i).getString("created_at");
                    ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                    String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy \nHH:mm"));
                    obj.date = formatted;
                    //obj.imageDrw = drw_arr.getDrawable(obj.image);
                    itemsLeads.add(obj);

                }

                if (itemsLeads.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setText("No leads found");
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }


                //set data and list adapter
                mAdapterLeads = new AdapterListContacts(itemsLeads);
                recyclerView.setAdapter(mAdapterLeads);

                mAdapterLeads.setOnItemClickListener(new AdapterListContacts.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Contact obj, int position) {

                        Contact lead = itemsLeads.get(position);
//                    Intent intent = new Intent(LeadsActivity.this,
//                            IndividualLeadActivity.class);
                        Intent i = new Intent(SelectLeadAccountActivity.this,
                                AddDealActivity.class);
                        // passing array index
                        i.putExtra("lead",lead );
                        i.putExtra("accountOrLead","lead");
                        startActivity(i);


                    }
                });

            }

            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Called before the text is changed
//                Toasts.toastIconError(SelectLeadAccountActivity.this,"p");

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Called while the text is being changed
                    Log.d("EditText", "Text changed: " + s);
//                Toasts.toastIconError(SelectLeadAccountActivity.this,"p");

                }

//                        @Override
//                        public void afterTextChanged(Editable editable) {
//
//                        }

                @Override
                public void afterTextChanged(Editable s) {
                    // Called after the text has been changed
                    String updatedText = s.toString();
                    Log.d("EditText", "Updated text: " + updatedText);
//                        Toasts.toastIconError(SelectLeadAccountActivity.this,updatedText);
//                        Toasts.toastIconError(SelectLeadAccountActivity.this,selectedText);

                    if(selectedText.equals("Lead")){
                        populateAdapterLeads(updatedText,itemsLeads);
                    }else{
                        populateAdapter(updatedText,items);
                    }

//                        Toasts.toastIconError(SelectLeadAccountActivity.this,
//                                "test"+items.size());


                    // Perform any action with the updated text, like a search query
                }
            });


        } catch (Exception e) {
            Toasts.toastIconError(SelectLeadAccountActivity.this,"Error Loading");
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }



    }

    @Override
    public void onComplete() {

    }

    private void populateAdapter(String query,List<Account> items) {
//        Toasts.toastIconError(SelectLeadAccountActivity.this,selectedText);
//        if(query.equals("")){
//            Toast.makeText(AccountsActivity.this,"Nothing",Toast.LENGTH_SHORT).show();
//
//        }
//        else {

        //final ArrayList<Integer> counterGroup = new ArrayList<Integer>();
        List<Account> newAccountsList =new ArrayList<>();

        for (int counter = 0; counter < items.size(); counter++) {
            if(query.contains(" ")){
                String[] querySplit = query.toLowerCase().split("\\s+");
                String account_name_searched=items.get(counter).account_name.toLowerCase();
//                    Toast.makeText(AccountsActivity.this,"Contains space",Toast.LENGTH_SHORT).show();


                if(containsAllWords(account_name_searched,querySplit)){
                    //counterGroup.add(counter);
                    newAccountsList.add(items.get(counter));
//                        Toast.makeText(AccountsActivity.this,outlet_name_searched+"-"+items.get(counter).name,Toast.LENGTH_SHORT).show();
                }

            }else {
                // Toast.makeText(AccountsActivity.this,"No space",Toast.LENGTH_SHORT).show();
                if (items.get(counter).account_name.toLowerCase().contains(query.toLowerCase())) {
                    //mNoProductsTextView.setVisibility(View.GONE);
                    //counterGroup.add(counter);
                    newAccountsList.add(items.get(counter));

                }

//                }

            }

            if (newAccountsList.size()>0) {
                mNoAccountsTextView.setVisibility(View.GONE);

            }else
            {
                mNoAccountsTextView.setText("No accounts found");
                mNoAccountsTextView.setVisibility(View.VISIBLE);
            }

            if(recyclerView==null) {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            }
            mAdapter = new AdapterListAccounts(newAccountsList);

            recyclerView.setAdapter(mAdapter);
//            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//
//                    // Sending image id to FullScreenActivity
//                    Intent i = new Intent(MainActivity.this, DisplayProductsActivity.class);
//                    // passing array index
//                    i.putExtra("productClicked",productsList.get(counterGroup.get(position)) );
//                    startActivity(i);
//
//                }
//            });
            mAdapter.setOnItemClickListener(new AdapterListAccounts.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Account obj, int position) {

                    Account account = newAccountsList.get(position);

                    Intent i = new Intent(SelectLeadAccountActivity.this,
                            AddDealActivity.class);
                    // passing array index
                    i.putExtra("account",account );
                    i.putExtra("accountOrLead","account");
                    startActivity(i);



                }
            });
        }
    }

    private void populateAdapterLeads(String query,List<Contact> items) {
//        Toasts.toastIconError(SelectLeadAccountActivity.this,selectedText);
//        if(query.equals("")){
//            Toast.makeText(AccountsActivity.this,"Nothing",Toast.LENGTH_SHORT).show();
//
//        }
//        else {

        //final ArrayList<Integer> counterGroup = new ArrayList<Integer>();
//        Toasts.toastIconError(SelectLeadAccountActivity.this,""+items.size());
        List<Contact> newLeadsList =new ArrayList<>();

        for (int counter = 0; counter < items.size(); counter++) {
            if(query.contains(" ")){

                String[] querySplit = query.toLowerCase().split("\\s+");
                String account_name_searched=items.get(counter).name.toLowerCase();
//                    Toast.makeText(AccountsActivity.this,"Contains space",Toast.LENGTH_SHORT).show();


                if(containsAllWords(account_name_searched,querySplit)){
                    //counterGroup.add(counter);
                    newLeadsList.add(items.get(counter));
//                        Toast.makeText(AccountsActivity.this,outlet_name_searched+"-"+items.get(counter).name,Toast.LENGTH_SHORT).show();
                }

            }else {
                // Toast.makeText(AccountsActivity.this,"No space",Toast.LENGTH_SHORT).show();
                if (items.get(counter).name.toLowerCase().contains(query.toLowerCase())) {
                    //mNoProductsTextView.setVisibility(View.GONE);
                    //counterGroup.add(counter);
                    newLeadsList.add(items.get(counter));

                }

//                }

            }

            if (newLeadsList.size()>0) {
                mNoAccountsTextView.setVisibility(View.GONE);
            }else
            {
                mNoAccountsTextView.setText("No leads found");
                mNoAccountsTextView.setVisibility(View.VISIBLE);
            }

            if(recyclerView==null) {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            }
            mAdapterLeads = new AdapterListContacts(newLeadsList);

            recyclerView.setAdapter(mAdapterLeads);
//            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//
//                    // Sending image id to FullScreenActivity
//                    Intent i = new Intent(MainActivity.this, DisplayProductsActivity.class);
//                    // passing array index
//                    i.putExtra("productClicked",productsList.get(counterGroup.get(position)) );
//                    startActivity(i);
//
//                }
//            });
            mAdapterLeads.setOnItemClickListener(new AdapterListContacts.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Contact obj, int position) {

                    Contact contact = newLeadsList.get(position);

                    Intent i = new Intent(SelectLeadAccountActivity.this,
                            AddDealActivity.class);
                    // passing array index
                    i.putExtra("lead",contact );
                    i.putExtra("accountOrLead","lead");
                    startActivity(i);



                }
            });
        }
    }

    public static boolean containsAllWords(String word, String ...keywords) {
        for (String k : keywords)
            if (!word.contains(k)) return false;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

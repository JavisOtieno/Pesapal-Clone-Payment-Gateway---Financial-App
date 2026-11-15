package com.javy.pesapalclone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.javy.pesapalclone.adapters.AdapterListDashboardProducts;
import com.javy.pesapalclone.models.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnSuccessListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton addDealButton;
    private String mAuthToken;
    private View view;

    private RecyclerView recyclerView;
    private AdapterListDashboardProducts mAdapter;
    private TextView emptyView;
    private TextView totalDealsThisMonth;
    private TextView totalDealsUnitsThisMonth;
//    private TextView targetTextView;
    private TextView monthTargetTitleTextView;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private static final int REQUEST_CODE = 100; // Request code for calendar permissions
    private long calendarId = -1; // Calendar ID

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
            }, REQUEST_CODE);
//            Toast.makeText(getContext(),"No Permissions",Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(getContext(),"Yes Permissions",Toast.LENGTH_SHORT).show();
            // Permission already granted, add the event
            Cursor cursor = getContext().getContentResolver().query(
                    CalendarContract.Calendars.CONTENT_URI,
                    new String[]{CalendarContract.Calendars._ID,
                            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String displayName = cursor.getString(1);
                    Log.d("Available Calendar ",cursor.getString(0)+
                            cursor.getString(1));
                    Log.d("Available Calendar", displayName+" "+calendarId); // Log each calendar's display name
                } while (cursor.moveToNext());
            } else {
                Log.d("Calendar Query", "No calendars found or cursor is null");
            }



            calendarId = getCalendarId();
//            Toast.makeText(getContext(),calendarId+"",Toast.LENGTH_SHORT).show();
//            insert();
//            addCalendar("TestCalendar");
//            addEventToCalendar("Meeting", "Discuss project updates", getStartTime(), getEndTime());
        }
        // Get the calendar ID


//        // Check for calendar permissions
//        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_CALENDAR) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_CALENDAR}, REQUEST_CODE);
//        }





    }
    public void insert() {
        Intent intent = new Intent(Intent.ACTION_INSERT,
                CalendarContract.Events.CONTENT_URI);
        // Add the calendar event details
        intent.putExtra(CalendarContract.Events.TITLE, "Launch!");
        intent.putExtra(CalendarContract.Events.DESCRIPTION,
                "Learn Java Android Coding");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,
                "Sanfoundry.com");
        Calendar startTime = Calendar.getInstance();
        startTime.set(2013, 8, 11);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        // Use the Calendar app to add the new event.
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);



        totalDealsThisMonth = (TextView) view.findViewById(R.id.totalDealsThisMonth);
        totalDealsUnitsThisMonth = (TextView) view.findViewById(R.id.totalDealsUnitsThisMonth);
//        targetTextView = (TextView) view.findViewById(R.id.targetTextView);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        monthTargetTitleTextView = (TextView) view.findViewById(R.id.monthTargetTextView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        NetworkUtils.
                fetchDataGet("dashboard",
                        mAuthToken,HomeFragment.this,getContext());


        addDealButton = view.findViewById(R.id.addDealButton);

        addDealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelectLeadAccountActivity.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private long getCalendarId() {
        Cursor cursor = getContext().getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                new String[]{CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String displayName = cursor.getString(1);
                // You can change this to the name of your calendar
                if (displayName.equals("Contacts")) {
                    long id = cursor.getLong(0);
                    cursor.close();
                    return id; // Return the calendar ID
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return -1; // Return -1 if no calendar found
    }
    public long getPrimaryCalendarId(Context context) {
        Cursor cursor = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection = CalendarContract.Calendars.IS_PRIMARY + " = ?";
        String[] selectionArgs = new String[]{"1"};

        try {
            cursor = cr.query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return -1; // or handle if no primary calendar exists
    }

    private void addEventToCalendar(String title, String description, long startTime, long endTime) {
        if (calendarId == -1) {
//            Toast.makeText(getContext(), "Calendar not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar startTime2 = Calendar.getInstance();
        startTime2.set(2024, Calendar.SEPTEMBER, 11, 10, 0);
        long startMillis = startTime2.getTimeInMillis();

        Calendar endTime2 = Calendar.getInstance();
        endTime2.set(2024, Calendar.SEPTEMBER, 11, 11, 0);
        long endMillis = endTime2.getTimeInMillis();

        ContentResolver cr = getContext().getContentResolver();
//        ContentValues values = new ContentValues();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, getPrimaryCalendarId(getContext()));
        values.put(CalendarContract.Events.TITLE, "Launch!");
        values.put(CalendarContract.Events.DESCRIPTION, "Learn Java Android Coding");
        values.put(CalendarContract.Events.EVENT_LOCATION, "Sanfoundry.com");
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles"); // Set an appropriate timezone

//        values.put(android.provider.CalendarContract.Events.ACCOUNT_NAME, "Local Calendar"); // Optional
////        values.put(android.provider.CalendarContract.Events.ACCOUNT_TYPE, "com.google"); // Your calendar type
//        values.put(android.provider.CalendarContract.Events.TITLE, title);
//        values.put(android.provider.CalendarContract.Events.DESCRIPTION, description);
//        values.put(android.provider.CalendarContract.Events.CALENDAR_ID, 1);
//        values.put(android.provider.CalendarContract.Events.DTSTART, startTime);
//        values.put(android.provider.CalendarContract.Events.DTEND, endTime);
//        values.put(android.provider.CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        // Insert event into calendar
//        cr.insert(android.provider.CalendarContract.Events.CONTENT_URI, values);
//        Toast.makeText(getContext(), "Event added to calendar",
//                Toast.LENGTH_SHORT).show();

        try {
            ContentResolver cr2 = getContext().getContentResolver();
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Log.d("CalendarEvent", "Event inserted successfully");
        } catch (Exception e) {
            Log.e("CalendarEvent", "Failed to insert event", e);
        }
    }

    private long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.OCTOBER, 20, 10, 0); // Set start date and time
        return calendar.getTimeInMillis();
    }

    private long getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.OCTOBER, 20, 11, 0); // Set end date and time
        return calendar.getTimeInMillis();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, add the event
                addEventToCalendar("Meeting",
                        "Discuss project updates",
                        getStartTime(), getEndTime());

                Toast.makeText(getContext(), "Added" , Toast.LENGTH_SHORT).show();

            } else {
                // Permission denied
                Toast.makeText(getContext(), "Permission to access " +
                        "calendar denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void addCalendar(String calendarName) {
//        // Check for permissions
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, 100);
//            return; // Exit if permission not granted
//        }
//
//        // Prepare values to insert
//        ContentValues values = new ContentValues();
//        values.put(CalendarContract.Calendars.ACCOUNT_NAME, "javisotieno@gmail.com"); // Replace with your account email
//        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google"); // Replace with relevant account type (Google, Local, etc.)
//        values.put(CalendarContract.Calendars.NAME, calendarName); // Calendar name
//        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarName); // Display name for the calendar
//        values.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.RED); // Optional: Set a color for the calendar
//        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER); // Set access level
//        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "your_email@example.com"); // Replace with your account email
//        values.put(CalendarContract.Calendars.IS_PRIMARY, 1); // Set as primary calendar (optional)
//        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1); // Allow syncing events
//
//        // Insert the new calendar
//        Uri uri = getContext().getContentResolver().insert(CalendarContract.Calendars.CONTENT_URI, values);
//        if (uri != null) {
//            long calendarId = ContentUris.parseId(uri); // Get the ID of the newly created calendar
//            Log.d("Calendar", "Calendar added with ID: " + calendarId);
//        } else {
//            Log.e("Calendar", "Failed to add calendar");
//        }
//    }

//    private void addCalendar(String calendarName) {
//        // Check for permissions
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, 100);
//            return; // Exit if permission not granted
//        }
//
//        // Prepare values to insert
//        ContentValues values = new ContentValues();
//        values.put(CalendarContract.Calendars.NAME, calendarName); // Calendar name
//        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarName); // Display name for the calendar
//        values.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.RED); // Optional: Set a color for the calendar
//        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER); // Set access level
//        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "your_email@example.com"); // Replace with your account email
//        values.put(CalendarContract.Calendars.IS_PRIMARY, 1); // Set as primary calendar (optional)
//        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1); // Allow syncing events
//        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google"); // This field can be set, but you should not write the ACCOUNT_NAME
//
//        // Insert the new calendar
//        Uri uri = getContext().getContentResolver().insert(CalendarContract.Calendars.CONTENT_URI, values);
//        if (uri != null) {
//            long calendarId = ContentUris.parseId(uri); // Get the ID of the newly created calendar
//            Log.d("Calendar", "Calendar added with ID: " + calendarId);
//        } else {
//            Log.e("Calendar", "Failed to add calendar");
//        }
//    }

    private void addCalendar(String calendarName) {
        // Check for permissions
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, 100);
            return; // Exit if permission not granted
        }

        // Prepare values to insert
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, "t3est");
        values.put(CalendarContract.Calendars.NAME, calendarName); // Calendar name
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarName); // Display name for the calendar
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.RED); // Optional: Set a color for the calendar
//        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER); // Set access level
        values.put(CalendarContract.Calendars.IS_PRIMARY, 1); // Set as primary calendar (optional)
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1); // Allow syncing events

        // Insert the new calendar
        Uri uri = getContext().getContentResolver().insert(CalendarContract.Calendars.CONTENT_URI, values);
        if (uri != null) {
            long calendarId = ContentUris.parseId(uri); // Get the ID of the newly created calendar
            Log.d("Calendar", "Calendar added with ID: " + calendarId);
            Toasts.toastIconError(getContext(),"Added Calender");
        } else {
            Log.e("Calendar", "Failed to add calendar");
            Toasts.toastIconError(getContext(),"Failed to add");
        }
    }


    @Override
    public void onSuccess(ResponseBody responseBodyObject) {

        Log.d("API Response", responseBodyObject.toString());
        System.out.println("result: "+responseBodyObject);

        try {
            // Get the response body as a string
            String responseBody = responseBodyObject.string();

            // Create a JSONObject from the response string
            JSONObject jsonObject = new JSONObject(responseBody);
            List<Product> items = new ArrayList<>();

            Integer dealsThisMonth = Integer.parseInt(jsonObject.getString("totalDealsKShThisMonth"));
            String dealsThisMonthStr = NumberFormat.getNumberInstance(Locale.US).format(dealsThisMonth);
            totalDealsThisMonth.setText(dealsThisMonthStr);

            Integer target = Integer.parseInt(jsonObject.getString("totalDealsUnitsThisMonth"));
            String targetStr = NumberFormat.getNumberInstance(Locale.US).format(target);
            totalDealsUnitsThisMonth.setText(targetStr);
//            targetTextView.setText("Total Qty : "+targetStr+" units");

//            String monthYear = jsonObject.getString("monthYear");
            monthTargetTitleTextView.setText("Payment Options Summary");




//            JSONArray deals = jsonObject.getJSONArray("recentdeals");
//            System.out.println("Accounts length: "+deals.length());
//
//            for (int i = 0; i < deals.length(); i++) {
//                Deal obj = new Deal();
//                //obj.image = drw_arr.getResourceId(i, -1);
//                obj.id = deals.getJSONObject(i).getString("id");
//                obj.name = deals.getJSONObject(i).getJSONObject("product").getString("name");
//                obj.amount =  "KSh. " + NumberFormat.getNumberInstance(Locale.US).format(
//                        Integer.valueOf(deals.getJSONObject(i).getString("amount")));
//                obj.status = deals.getJSONObject(i).getString("status");
//                obj.account_name = deals.getJSONObject(i).
//                        getJSONObject("account").getString("name");
//
//                String createdAt = deals.getJSONObject(i).getString("created_at");
//                ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
//                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
//                obj.date = formatted;
//                //obj.imageDrw = drw_arr.getDrawable(obj.image);
//                items.add(obj);
//
//            }

            JSONArray products = jsonObject.getJSONArray("products");
            System.out.println("Accounts length: "+products.length());


            for (int i = 0; i < products.length(); i++) {
                Product obj = new Product();
                //obj.image = drw_arr.getResourceId(i, -1);
                obj.id = products.getJSONObject(i).getString("id");
                obj.name = products.getJSONObject(i).getString("name");
                obj.target_type = products.getJSONObject(i).getString("target_type");
                Integer target_amount = Integer.valueOf(products.getJSONObject(i).getString("target_amount"));
                String dealsSumAmount = products.getJSONObject(i).getString("deals_sum_amount");
                Integer current_sold = 0;
                if(!dealsSumAmount.equals("null")){
                    current_sold = Integer.valueOf(dealsSumAmount);
                }

//                Integer current_sold = 0;'leadsThisMonth',
                Integer remainder = target_amount - current_sold;


                if(obj.target_type.equals("value")){
                    obj.remaining_amount =  "KSh. " + NumberFormat.getNumberInstance(Locale.US).format(
                            remainder<0?0:remainder);
                    obj.target_amount = "KSh. " + NumberFormat.getNumberInstance(Locale.US).format(
                            current_sold);
                }else{
                    obj.remaining_amount =   NumberFormat.getNumberInstance(Locale.US).format(
                            remainder<0?0:remainder)
                    +" units";
                    obj.target_amount = NumberFormat.getNumberInstance(Locale.US).format(
                            target_amount)+" units";
                }



                String createdAt = products.getJSONObject(i).getString("created_at");
                ZonedDateTime dateTime = Instant.parse(createdAt).atZone(ZoneId.of("Africa/Nairobi"));
                String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
                obj.date = formatted;
                //obj.imageDrw = drw_arr.getDrawable(obj.image);
                items.add(obj);

            }


            if (items.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


            //set data and list adapter
            mAdapter = new AdapterListDashboardProducts(items);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new AdapterListDashboardProducts.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Product obj, int position) {

                    Product product = items.get(position);

                    Intent intent = new Intent(getContext(), IndividualProductActivity.class);
                    intent.putExtra("productId", product.id);
                    System.out.println("productId: "+product.id);
                    startActivity(intent);



                }
            });


        } catch (Exception e) {
            Toasts.toastIconError(getContext(),"Error Loading");
            Log.e("API Response", "JSON Parsing error: " + e.getMessage());
        }

    }

    @Override
    public void onComplete() {

    }
}
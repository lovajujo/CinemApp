package hu.lova.cinemapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.bundle.BundleElement;

import java.util.ArrayList;

public class TicketActivity extends AppCompatActivity {
    private static final String LOG_TAG=TicketActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private CollectionReference itemCollectionRef;
    private RecyclerView recyclerView;
    private ArrayList<TicketItem> itemList;
    private TicketItemAdapter adapter;
    private FrameLayout redDot;
    private TextView contentTextView;
    private int gridNumber=1;
    private boolean viewRow=true;
    private  int cartItems=0;
    private int queryLimit=10;
    private NotificationHelper notificationHelper;


    @RequiresApi(api= Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        firebaseAuth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemList=new ArrayList<>();
        adapter=new TicketItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        firestore=FirebaseFirestore.getInstance();
        itemCollectionRef=firestore.collection("Items");
        queryData();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, filter);
        notificationHelper=new NotificationHelper(this);
    }


    BroadcastReceiver powerReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action==null){
                return;
            }
            if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
                queryLimit = 10;
            } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
                queryLimit = 5;
            }
        }
    };
    private void queryData(){
        itemList.clear();
        //rendezzük a termékeket az alapján, melyiket tettük be legtöbbször a kosárba
        itemCollectionRef.orderBy("cartCounter", Query.Direction.DESCENDING).limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                TicketItem item=document.toObject(TicketItem.class);
                item.setId(document.getId());
                itemList.add(item);
            }
            //ha nem volt adat
            if(itemList.size()==0){
                initializeData();
                queryData();
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        String[]itemsList=getResources().getStringArray(R.array.movie_titles);
        String[]itemType=getResources().getStringArray(R.array.movie_item_types);
        String[]itemsPrice=getResources().getStringArray(R.array.ticket_item_prices);
        TypedArray itemsImageResource=getResources().obtainTypedArray(R.array.movie_item_images);
        TypedArray itemsRate=getResources().obtainTypedArray(R.array.movie_item_rates);
        for (int i = 0; i < itemsList.length; i++) {
            itemCollectionRef.add(new TicketItem(
                    itemsList[i],
                    itemType[i],
                    itemsPrice[i],
                    itemsRate.getFloat(i, 0),
                    itemsImageResource.getResourceId(i, 0),
                    0));
        }
        itemsImageResource.recycle();
    }
    public void deleteItem(TicketItem item){
        DocumentReference ref=itemCollectionRef.document(item._getId());
        ref.delete().addOnSuccessListener(success->{
            Log.d(LOG_TAG, "Az elem sikeresen ki lett törölve"+item._getId());
        }).addOnFailureListener(fail->{
            Toast.makeText(this,"A "+item._getId()+" elemet nem lehet kitörölni!",Toast.LENGTH_LONG).show();
        });
        queryData();
        notificationHelper.cancel();//ha kitörölt termékhez van értesítés, akkor kilövi
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.movies_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search_bar);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //szöveg változik
                Log.d(LOG_TAG,s);
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
            case R.id.settings:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.cart:
                return true;
            case R.id.view_selector:
                if(viewRow){
                    changeSpan(item,R.drawable.icon_view_grid,1);
                }else{
                    changeSpan(item,R.drawable.icon_view_row,2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void changeSpan(MenuItem item, int drawableId, int i) {
        viewRow=!viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager=(GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanCount(i);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        redDot = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        //rootview-ra való klikkelés
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ha valaki rákattint, meghívódik az onOptionsItemSelected-ben a megfelelő case opció
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }
    public void updateAlertIcon(TicketItem item){
        cartItems=(cartItems+1);
        if(0<cartItems){
            contentTextView.setText(String.valueOf(cartItems));
        }else{
            contentTextView.setText("");
        }
        redDot.setVisibility((cartItems>0) ? VISIBLE:GONE);
        itemCollectionRef.document(item._getId()).update("cartCounter",item.getCartCounter()+1).addOnFailureListener(fail->{
            Toast.makeText(this,"A "+item._getId()+" elemet nem lehet megváltoztatni!",Toast.LENGTH_LONG).show();
        });
        notificationHelper.send(item.getName());
        queryData();
    }

    //vége van az Activitynek, akkor leregisztrálom a receivert
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }
}

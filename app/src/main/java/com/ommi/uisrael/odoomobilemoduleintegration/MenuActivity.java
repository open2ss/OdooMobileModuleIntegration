package com.ommi.uisrael.odoomobilemoduleintegration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void onProductoClick(View v){
        Log.d("MENU","parnert buttton clicked");

        Intent myIntent = new Intent(MenuActivity.this, ProductListActivity.class);
        MenuActivity.this.startActivity(myIntent);
    }


    public void onVentasClick(View v){
        Log.d("MENU","order buttton clicked");

       // Intent myIntent = new Intent(MenuActivity.this, CustomerListActivity.class);
      //  MenuActivity.this.startActivity(myIntent);
    }
}

/*
    public void onPedidoCompra(View v){
        Log.d("MENU","order buttton clicked");

        Intent myIntent = new Intent(MenusActivity.this, PedidoCompraListActivity.class);
        MenusActivity.this.startActivity(myIntent);
    }

    public void onPedidoVenta(View v){
        Log.d("MENU","order buttton clicked");

        Intent myIntent = new Intent(MenusActivity.this, PedidoVentaListActivity.class);
        MenusActivity.this.startActivity(myIntent);
    }

    public void onStockPicking(View v){
        Log.d("MENU","order buttton clicked");

        Intent myIntent = new Intent(MenusActivity.this, StockPickingListActivity.class);
        MenusActivity.this.startActivity(myIntent);
    }
    public void onExistencias1(View v){
        Log.d("MENU","order buttton clicked");

        Intent myIntent = new Intent(MenusActivity.this, ExistenciasListActivity.class);
        MenusActivity.this.startActivity(myIntent);
    }
    public void onInventarios(View v){
        Log.d("MENU","order buttton clicked");

        Intent myIntent = new Intent(MenusActivity.this, OdsStockListActivity.class);
        MenusActivity.this.startActivity(myIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {


            case R.id.action_settings3:
                Log.i("ActionBar", "Settings!");
                Intent myInten = new Intent(MenuActivity.this, MainActivity.class);
                MenuActivity.this.startActivity(myInten);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }
*/
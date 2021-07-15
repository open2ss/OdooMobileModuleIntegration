package com.ommi.uisrael.odoomobilemoduleintegration;

import android.IntentIntegrator;
import android.IntentResult;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Validator;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.ommi.uisrael.odoomobilemoduleintegration.datos.AppActivity;
import com.ommi.uisrael.odoomobilemoduleintegration.datos.OdooUtility;
import com.ommi.uisrael.odoomobilemoduleintegration.datos.SharedData;
import com.ommi.uisrael.odoomobilemoduleintegration.manejador.Product1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class productFormActivity extends AppActivity implements View.OnClickListener{


    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;


    private long searchTasProdkId;
    private long updateProductTaskId;
    private long createProductTaskId;
    private long deleteProductTaskId;

    @NotEmpty( message = "Ingrese el nombre del producto")
    EditText editNamePro;

    @NotEmpty( message = "Seleccione un tipo de producto")
    TextView editTipProd;

    EditText editDescPro;
    EditText editRefIntPro;

    @NotEmpty( message = "Ingrese el codigo del producto")
    EditText editBarcode;

    EditText editPrecVentPro;
    EditText editCostPro;

    Button butonScan;


    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;



    Product1 product1;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);


        initFields1();

        uid = SharedData.getKey(productFormActivity.this, "uid");
        password = SharedData.getKey(productFormActivity.this, "password");
        serverAddress = SharedData.getKey(productFormActivity.this, "serverAddress");
        database = SharedData.getKey(productFormActivity.this, "database");

        odoo = new OdooUtility(serverAddress, "object");




        product1 = new Product1();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        if (name != null)
            searchProductByName(name);
    }


    @Override
    public void onClick(View v) {
        // Se responde al evento click
        if (v.getId() == R.id.butonScan) {
            // Se instancia un objeto de la clase IntentIntegrator
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            // Se procede con el proceso de scaneo
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Se obtiene el resultado del proceso de scaneo y se parsea
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
        if (scanningResult != null) {
            // Quiere decir que se obtuvo resultado pro lo tanto:
            // Desplegamos en pantalla el contenido del c�digo de barra scaneado
            String scanContent = scanningResult.getContents();
            editBarcode.setText(scanContent);
            // Desplegamos en pantalla el nombre del formato del c�digo de barra
            // scaneado
            //String scanFormat = scanningResult.getFormatName();
            // formatTxt.setText("Formato: " + scanFormat);
        } else {
            // Quiere decir que NO se obtuvo resultado
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se ha recibido datos del scaneo!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    private void initFields1() {
        rb1 = (RadioButton)findViewById(R.id.rb1);
        rb2 = (RadioButton)findViewById(R.id.rb2);
        rb3 = (RadioButton)findViewById(R.id.rb3);
        View.OnClickListener list = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String opcion = "";
                switch(view.getId()) {
                    case R.id.rb1:
                        opcion = "product";
                        break;
                    case R.id.rb2:
                        opcion = "service";
                        break;
                    case R.id.rb3:
                        opcion = "consu";
                        break;
                }

                editTipProd.setText(opcion);
            }
        };
        rb1.setOnClickListener(list);
        rb2.setOnClickListener(list);
        rb3.setOnClickListener(list);



        butonScan = (Button) findViewById(R.id.butonScan);
        butonScan.setOnClickListener(this);
        editNamePro = (EditText) findViewById(R.id.editNamePro);

        /*editNamePro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editNamePro.getText().length()<1){
                    editNamePro.setError("Nombre del producto");
                }
            }
        });
        */


        editTipProd = (TextView) findViewById(R.id.editTipProd);
        editDescPro = (EditText) findViewById(R.id.editDescPro);
        editRefIntPro = (EditText) findViewById(R.id.editRefIntPro);
        editBarcode = (EditText) findViewById(R.id.editBarcode);
        editPrecVentPro = (EditText) findViewById(R.id.editPrecVentPro);
        editCostPro = (EditText) findViewById(R.id.editCostPro);
        validator = new Validator(this);
        validator.setValidationListener(this);

    }


    private void searchProductByName(String name) {
        List conditions = Arrays.asList(Arrays.asList(
                Arrays.asList("name", "=", name)));

        Map fields = new HashMap() {{
            put("fields", Arrays.asList(
                    "id",
                    "name",
                    "type",
                    "default_code",
                    "barcode",
                    "description_sale",
                    "list_price",
                    "standard_price"

            ));
        }};

        searchTasProdkId = odoo.search_read(listener, database, uid, password, "product.template", conditions, fields);
    }

    XMLRPCCallback listener = new XMLRPCCallback() {


        public void onResponse(long id, Object result) {

            Looper.prepare();

            if (id == searchTasProdkId) {

                Object[] classObjs = (Object[]) result;
                int length = classObjs.length;

                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> classObj =
                                (Map<String, Object>) classObjs[i];
                        product1.setData(classObj);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillProductForm();

                        }
                    });
                } else {
                    odoo.MessageDialog(productFormActivity.this, "producto no encontrado");
                }


            }

            else if (id==updateProductTaskId ){
                final Boolean updateResult = (Boolean) result;

                if (updateResult){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(productFormActivity.this);
                    String msg = "Producto Actualizado";
                    alertDialogBuilder.setMessage(msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(productFormActivity.this, ProductListActivity.class);
                            productFormActivity.this.startActivity(myIntent);
                            dialog.dismiss();
                        }
                    }).create().show();





                }else {
                    odoo.MessageDialog(productFormActivity.this, "update customer failed server return was false");
                }

            }

            else if(id == createProductTaskId){

                String createResult = result.toString();
                try

                {
                    if (createResult != null) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(productFormActivity.this);
                        String msg = "Producto Creado Correctamente ";
                        alertDialogBuilder.setMessage(msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(productFormActivity.this, ProductListActivity.class);
                                productFormActivity.this.startActivity(myIntent);
                                dialog.dismiss();
                            }
                        }).create().show();




                    }
                }catch(Exception ex){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(productFormActivity.this);
                    String msg = "Llene todos los Campos";
                    alertDialogBuilder.setMessage(msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    }).create().show();
                }




            }

            else if (id == deleteProductTaskId){
                final Boolean deleteResult = (Boolean) result;
                if (deleteResult){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(productFormActivity.this);
                    String msg = "Eliminado";
                    alertDialogBuilder.setMessage(msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(productFormActivity.this, ProductListActivity.class);
                            productFormActivity.this.startActivity(myIntent);
                            dialog.dismiss();
                        }
                    }).create().show();
                }
                else {
                    odoo.MessageDialog(productFormActivity.this, "error al eliminar el producto");

                }
            }

            Looper.loop();


        }

        public void onError(long id, XMLRPCException error) {
            Looper.prepare();

            Log.e("SEARCH ******", error.getMessage());
            odoo.MessageDialog(productFormActivity.this, error.getMessage());

            Looper.loop();

        }


        public void onServerError(long id, XMLRPCServerException error) {

            Looper.prepare();
            Log.e("SEARCH *****", error.getMessage());
            odoo.MessageDialog(productFormActivity.this, error.getMessage());

            Looper.loop();


        }
    };


    private void fillProductForm(){
        editNamePro.setText(product1.getName());
        editTipProd.setText(product1.getType());

        if(product1.getType().toString().equals("product"))
        {

            rb1.setChecked(true);
            rb2.setChecked(false);
            rb3.setChecked(false);

        }
        if(product1.getType().toString().equals("service"))
        {

            rb1.setChecked(false);
            rb2.setChecked(true);
            rb3.setChecked(false);

        }

        if(product1.getType().toString().equals("consu"))
        {

            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(true);

        }




        editDescPro.setText(product1.getDescription_sale());

        editRefIntPro.setText(product1.getDefault_code());
        editBarcode.setText(product1.getBarcode());
        editPrecVentPro.setText(product1.getList_price().toString());
        editCostPro.setText(product1.getStandard_price().toString());





    }

    private void updateProductModel(){




        String name = editNamePro.getText().toString();
        String type = editTipProd.getText().toString();
        String description_sale = editDescPro.getText().toString();
        String default_code = editRefIntPro.getText().toString();
        String barcode = editBarcode.getText().toString();
        Double list_price =  Double . valueOf ( editPrecVentPro.getText().toString() ). doubleValue ();
        Double standard_price = Double . valueOf ( editCostPro.getText().toString() ). doubleValue ();

        product1.setName(name);
        product1.setType(type);
        product1.setDescription_sale(description_sale);
        product1.setDefault_code(default_code);
        product1.setBarcode(barcode);
        product1.setList_price(list_price);
        product1.setStandard_price(standard_price);





    }



    public void onSaveProduct(View v){
        validator.validate();







    }


    private void  updateProductToOdoo()
    {

        List data = Arrays.asList(
                Arrays.asList(product1.getId()),
                new HashMap(){{
                    put("name", product1.getName());
                    put("type", product1.getType());
                    put("default_code", product1.getDefault_code());
                    put("barcode", product1.getBarcode());
                    put("description_sale", product1.getDescription_sale());
                    put("list_price", product1.getList_price());
                    put("standard_price", product1.getStandard_price());
                }
                }

        );

        updateProductTaskId = odoo.update(listener, database, uid, password, "product.template", data);

    }



    public void onDeleteProduct(View view){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(productFormActivity.this);
        String msg = "deseas eliminar este producto";
        alertDialogBuilder.setMessage(msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
                dialog.dismiss();
            }
        }).create().show();

    }


    private void  deleteProduct(){
        List conditions = Arrays.asList(
                Arrays.asList( product1.getId() ));
        deleteProductTaskId = odoo.delete(listener, database, uid, password, "product.template", conditions);

    }
    /*
    public void onSaveProduct(View v){
        updateProductModel();
        if(product1.getId() != null)
            updateProductToOdoo();
        else
            createProductToOdoo();
    }
    */


    private void createProductToOdoo(){

        try {


            List data = Arrays.asList
                    (new HashMap() {
                         {
                             put("name", product1.getName());

                             put("type", product1.getType());
                             put("default_code", product1.getDefault_code());
                             put("barcode", product1.getBarcode());
                             put("description_sale", product1.getDescription_sale());
                             put("list_price", product1.getList_price());
                             put("standard_price", product1.getStandard_price());

                         }
                     }
                    );


            createProductTaskId = odoo.create(listener, database, uid, password, "product.template", data);


        }catch (Exception xc){

        }




    }







    @Override
    public void onValidationSucceeded() {
        updateProductModel();
        if(product1.getId() != null)
            updateProductToOdoo();
        else
            createProductToOdoo();


    }
/*
    @Override
    public void onValidationFailed(View view, Rule<?> rule) {
        final String failureMessage = rule.getFailureMessage();

        if (view instanceof EditText) {
            EditText failed = (EditText) view;
            failed.requestFocus();
            failed.setError(failureMessage);
        } else {
            Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_SHORT).show();
        }


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
            case R.id.action_settings2:
                Log.i("ActionBar", "Settings!");
                Intent myIntent = new Intent(productFormActivity.this, MenuActivity.class);
                productFormActivity.this.startActivity(myIntent);
                return true;

            case R.id.action_settings3:
                Log.i("ActionBar", "Settings!");
                Intent myInten = new Intent(productFormActivity.this, MainActivity.class);
                productFormActivity.this.startActivity(myInten);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
*/



}
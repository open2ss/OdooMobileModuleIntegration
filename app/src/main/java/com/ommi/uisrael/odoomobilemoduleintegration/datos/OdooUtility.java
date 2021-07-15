package com.ommi.uisrael.odoomobilemoduleintegration.datos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.ommi.uisrael.odoomobilemoduleintegration.datos.M2OField;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCClient;

public class OdooUtility {
    private URL url;
    private XMLRPCClient client;

//@param serverAddress
//@param path

    public   OdooUtility(String serverAddress, String path) {
        try {
            url = new URL( "http://" + serverAddress + "/xmlrpc/2/" + path);
            client = new XMLRPCClient(url);
        } catch (Exception ex) {
            Log.e("ODOO UTILITY: ", ex.getMessage());
        }

    }

    //    @param listener
    //    @param db
    //    @param username
    //    @param password
    //    @return long integer task id


    public long login(XMLRPCCallback listener,
                       String db,
                       String username,
                       String password) {
        Map<String, Object> emptyMap = new HashMap<String, Object>();
        Long id = client.callAsync(listener, "authenticate", db, username, password, emptyMap);
        return id;

    }

    // search_read ()datos de busqueda de openerp y leer regiistros
    //@param listener recibir respuestas del servidor
    //@param db nombre de la base de datos
    //@param uid numero entero del usuario obtenido del inicio de cesion
    //@param clave del usuario
    //@param objet nombre del modelo
    //@param conditions condicion para ir a buscar
    //@param field campos a buscar
    //@param  identiicador de tareas


    public long search_read(XMLRPCCallback listener,
                            String db,
                            String uid,
                            String password,
                            String Object,
                            List conditions,
                            Map<String, List> fields) {
        long id = client.callAsync(listener, "execute_kw", db, Integer.parseInt(uid), password, Object, "search_read", conditions, fields);
        return id;

    }


    //metodo par aenviar una peticion para crear un nuevo registro



    public long create (XMLRPCCallback listener,
                        String db,
                        String uid,
                        String password,
                        String Object,
                        List data)

    {
        long id=0;
        try {
            id = client.callAsync(listener, "execute_kw", db, Integer.parseInt(uid), password, Object, "create", data);

        }catch (Exception xc){

        }
        return id;
    }



    // añadir un metodo para enviar una peticion par ala ejecucion de un metodo
    // publico en un onjeto
    // esto seria muy util para confirmar una venta validacion de facturas

    public long exec(XMLRPCCallback listener,
                     String db,
                     String uid,
                     String password,
                     String Object,
                     String method,
                     List data) {
        long id = client.callAsync(listener, "execute_kw", db, Integer.parseInt(uid), password, Object, method, data);
        return id;

    }


    // metodo para actualizacion de registros

    public long update(XMLRPCCallback listener,
                       String db,
                       String uid,
                       String password,
                       String Object,
                       List data) {
        long id = client.callAsync(listener, "execute_kw", db, Integer.parseInt(uid), password, Object, "write", data);
        return id;
    }

    // metodo par ala eliminacion de registros


    public long delete(XMLRPCCallback listener,
                       String db,
                       String uid,
                       String password,
                       String Object,
                       List data) {
        long id = client.callAsync(listener, "execute_kw", db, Integer.parseInt(uid), password, Object, "unlink", data);
        return id;


    }


    //class obj
    // fiel name  codigo (10, nombre de compania)
    // aaron


    public static M2OField getMany2One(Map<String,Object> classObj, String fieldName) {
        Integer fieldId = 0;
        String fieldValue = "";
        M2OField res = new M2OField();
        if (classObj.get(fieldName) instanceof Object[]) {
            Object[] field = (Object[])classObj.get(fieldName);
            if (field.length > 0) {
                fieldId = (Integer)field[0];
                fieldValue = (String)field[1];

            }
        }
        res.id = fieldId;
        res.value = fieldValue;

        return res;

    }

    public static List getOne2Many(Map<String, Object> classObj, String fieldName) {
        List res = new ArrayList();
        Object[] field = (Object[]) classObj.get(fieldName);

        for (int i = 0; i < field.length; i++) {
            res.add(i, field[i]);
        }

        return res;

    }

    // si el valor esta vacio devuelve una cadena vacia


    public static String getString(Map<String, Object> classObj, String fieldName) {
        String res = "";
        if (classObj.get(fieldName) instanceof String) {
            res = (String) classObj.get(fieldName);
        }

        return res;
    }


    // esto ara que el valor de boolean

    public static Boolean getBoolean(Map<String, Object> classObj, String fieldName) {
        Boolean res = Boolean.FALSE;
        if (classObj.get(fieldName) instanceof Boolean) {
            res = (Boolean) classObj.get(fieldName);
        }

        return res;
    }


    // esto ara que el valor de numrico

    public static Number getNumber(Map<String, Object> classObj, String fieldName) {
        Number res = 0;
        if (classObj.get(fieldName) instanceof Boolean) {
            res = (Number) classObj.get(fieldName);
        }

        return res;
    }


    // esto ara que el valor de doble precicion se un campo esta vacio entonces devolvera 0.0

    public static Double getDouble(Map<String,Object> classObj, String fieldName) {
        Double res = 0.0;
        if (classObj.get(fieldName) instanceof Double) {
            res = (Double) classObj.get(fieldName);
        }
        return res;
    }





    // esto ara que un valor de doble precicion de un campo si el campo esta vacio enonces me devolvera o

    public static Integer getInteger(Map<String, Object> classObj, String fieldName)
    {
        Integer res = 0;
        if (classObj.get(fieldName) instanceof Double)
        {
            res = (Integer) classObj.get(fieldName);
        }

        return res;

    }


    // esto e para mostrar dialogo de mensaje emergentr de usosm multiples

    public void  MessageDialog(Context context, String msg)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
                dialog.cancel();
            }
        }).create().show();
    }


    public void  MessageDialogPro(Context context, String msg)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
                dialog.cancel();
            }
        }).create().show();
    }
































}

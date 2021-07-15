package com.ommi.uisrael.odoomobilemoduleintegration.manejador;


import com.ommi.uisrael.odoomobilemoduleintegration.datos.OdooUtility;
import java.util.Map;


public class Product1 {

    private Integer id;
    private String name;
    private String type;
    private String default_code;
    private String barcode;
    private String description_sale;
    private Double list_price;
    private Double standard_price;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefault_code() {
        return default_code;
    }

    public void setDefault_code(String default_code) {
        this.default_code = default_code;
    }

    public String getDescription_sale() {
        return description_sale;
    }

    public void setDescription_sale(String description_sale) {this.description_sale = description_sale;
    }

    public Double getList_price() {
        return list_price;
    }

    public void setList_price(Double list_price) {
        this.list_price = list_price;
    }

    public Double getStandard_price() {
        return standard_price;
    }

    public void setStandard_price(Double standard_price) {
        this.standard_price = standard_price;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public  void setData(Map<String,Object> classObj){


        setId((Integer) classObj.get("id"));
        setName(OdooUtility.getString(classObj,"name"));
        setType(OdooUtility.getString(classObj,"type"));
        setDefault_code(OdooUtility.getString(classObj,"default_code"));
        setBarcode(OdooUtility.getString(classObj,"barcode"));
        setDescription_sale(OdooUtility.getString(classObj,"description_sale"));
        setList_price(OdooUtility.getDouble(classObj,"list_price"));
        setStandard_price(OdooUtility.getDouble(classObj,"standard_price"));

    }

}

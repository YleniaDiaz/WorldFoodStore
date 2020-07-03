package com.example.worldfoodstore.pojos;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class Product implements Serializable {

    private String id;
    private String nombre;
    private String precio;
    private String precioOferta;
    private transient Bitmap imagen;
    private boolean novedad;
    private String tipo;
    private String urlDownload;

    public Product(String nombre, String precio, String precioOferta, Bitmap imagen, String urlDownload){
        this.nombre=nombre;
        this.precio=precio;
        this.precioOferta=precioOferta;
        this.imagen=imagen;
        this.urlDownload=urlDownload;
    }

    public Product(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(String precioOferta) {
        this.precioOferta = precioOferta;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public boolean isNovedad() {
        return novedad;
    }

    public void setNovedad(boolean novedad) {
        this.novedad = novedad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }
}

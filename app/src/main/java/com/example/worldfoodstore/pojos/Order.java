package com.example.worldfoodstore.pojos;


public class Order {

    private String idOrder;
    private boolean sended;

    public Order() {

    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }
}

package models;

public class DataShared {
    private double luasAlas;
    private double kelilingAlas;

    public synchronized void setLuasAlas(double luasAlas) {
        this.luasAlas = luasAlas;
    }

    public synchronized double getLuasAlas() {
        return luasAlas;
    }

    public synchronized void setKelilingAlas(double kelilingAlas) {
        this.kelilingAlas = kelilingAlas;
    }

    public synchronized double getKelilingAlas() {
        return kelilingAlas;
    }
}
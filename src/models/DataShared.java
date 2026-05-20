package models;

public class DataShared {
    private double[] luasAlas;
    private double[] kelilingAlas;

    public DataShared(int size) {
        luasAlas = new double[size];
        kelilingAlas = new double[size];
    }

    public void setLuasAlas(int index, double luas) {
        this.luasAlas[index] = luas;
    }

    public double getLuasAlas(int index) {
        return luasAlas[index];
    }

    public void setKelilingAlas(int index, double keliling) {
        this.kelilingAlas[index] = keliling;
    }

    public double getKelilingAlas(int index) {
        return kelilingAlas[index];
    }
}
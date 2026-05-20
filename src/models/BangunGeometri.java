/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public abstract class BangunGeometri {
    protected double panjang;
    protected double lebar;

    public BangunGeometri(double panjang, double lebar) {
        this.panjang = panjang;
        this.lebar = lebar;
    }

    public abstract String getHasilKalkulasi();
}

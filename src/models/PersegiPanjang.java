/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

// [PILAR OOP: PEWARISAN (INHERITANCE)]
// PersegiPanjang mewarisi sifat (panjang, lebar) dari BangunGeometri menggunakan keyword 'extends'
public class PersegiPanjang extends BangunGeometri {

    public PersegiPanjang(double panjang, double lebar) {
        super(panjang, lebar);
    }

    public double hitungLuas() {
        return panjang * lebar;
    }

    public double hitungKeliling() {
        return 2 * (panjang + lebar);
    }

    // [PILAR OOP: POLIMORFISME]
    // Method Overriding: Menimpa method dari superclass dengan bentuk/implementasi spesifik untuk Persegi Panjang.
    @Override
    public String getHasilKalkulasi() {
        return String.format("--- Persegi Panjang ---\nLuas: %.2f\nKeliling: %.2f\n", 
            hitungLuas(), hitungKeliling());
    }
}

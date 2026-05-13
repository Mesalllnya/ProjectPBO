/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

// [PILAR OOP: ABSTRAKSI] 
// Menggunakan 'abstract class' karena class ini hanya kerangka dasar, 
// tidak boleh diinstansiasi langsung menjadi objek.
public abstract class BangunGeometri {
    
    // [PILAR OOP: ENKAPSULASI]
    // Menggunakan access modifier 'protected' agar variabel ini disembunyikan dari class luar,
    // tetapi masih bisa diakses oleh class turunannya (subclass).
    protected double panjang;
    protected double lebar;

    public BangunGeometri(double panjang, double lebar) {
        this.panjang = panjang;
        this.lebar = lebar;
    }

    // [PILAR OOP: ABSTRAKSI]
    // Abstract method: memaksa semua subclass untuk membuat implementasi spesifik dari method ini,
    // menyembunyikan detail "bagaimana" cara menghitungnya di level superclass.
    public abstract String getHasilKalkulasi();
}

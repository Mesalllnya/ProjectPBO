package models;

// [PILAR OOP: PEWARISAN (INHERITANCE)]
// PrismaPersegiPanjang mewarisi PersegiPanjang agar bisa menggunakan ulang (reuse) method hitungLuas()
public class PrismaPersegiPanjang extends PersegiPanjang {
    
    // [PILAR OOP: ENKAPSULASI]
    // Variabel tinggi di-set 'private' karena hanya milik Prisma, tidak boleh diakses sembarangan dari luar.
    private double tinggi;

    public PrismaPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar); // Memanggil konstruktor PersegiPanjang
        this.tinggi = tinggi;
    }

    public double hitungVolume() {
        // Menggunakan kembali hitungLuas() dari superclass PersegiPanjang
        return hitungLuas() * tinggi; 
    }

    public double hitungLuasPermukaan() {
        // Menggunakan kembali hitungLuas() dari superclass PersegiPanjang
        return 2 * (hitungLuas() + (panjang * tinggi) + (lebar * tinggi));
    }

    // [PILAR OOP: POLIMORFISME]
    // Method Overriding spesifik untuk Prisma
    @Override
    public String getHasilKalkulasi() {
        return String.format("--- Prisma Persegi Panjang ---\nVolume: %.2f\nLuas Permukaan: %.2f\n", 
            hitungVolume(), hitungLuasPermukaan());
    }
}
package models;

// [PILAR OOP: PEWARISAN (INHERITANCE)]
// Mewarisi PersegiPanjang untuk menggunakan ulang method hitungLuas()
public class LimasPersegiPanjang extends PersegiPanjang {
    
    // [PILAR OOP: ENKAPSULASI]
    private double tinggi;

    public LimasPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar); // Memanggil konstruktor PersegiPanjang
        this.tinggi = tinggi;
    }

    public double hitungVolume() {
        // Menggunakan kembali hitungLuas() dari superclass PersegiPanjang
        return (1.0 / 3.0) * hitungLuas() * tinggi; 
    }

    public double hitungLuasPermukaan() {
        // Menggunakan kembali hitungLuas() dari superclass PersegiPanjang untuk luas alas
        double luasAlas = hitungLuas(); 
        
        // Perhitungan Pythagoras untuk tinggi segitiga di sisi tegak
        double tinggiSegitigaPanjang = Math.sqrt(Math.pow(tinggi, 2) + Math.pow(lebar / 2.0, 2));
        double tinggiSegitigaLebar = Math.sqrt(Math.pow(tinggi, 2) + Math.pow(panjang / 2.0, 2));
        
        double luasSisiTegakPanjang = 2 * (0.5 * panjang * tinggiSegitigaPanjang);
        double luasSisiTegakLebar = 2 * (0.5 * lebar * tinggiSegitigaLebar);
        
        return luasAlas + luasSisiTegakPanjang + luasSisiTegakLebar;
    }

    // [PILAR OOP: POLIMORFISME]
    // Method Overriding spesifik untuk Limas
    @Override
    public String getHasilKalkulasi() {
        return String.format("--- Limas Persegi Panjang ---\nVolume: %.2f\nLuas Permukaan: %.2f\n", 
            hitungVolume(), hitungLuasPermukaan());
    }
}
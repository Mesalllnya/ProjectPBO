package models;

public class LimasPersegiPanjang extends PersegiPanjang {
    private double tinggi;

    public LimasPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar);
        this.tinggi = tinggi;
    }

    public double hitungVolume() {
        return (1.0 / 3.0) * hitungLuas() * tinggi; 
    }

    public double hitungLuasPermukaan() {
        double luasAlas = hitungLuas(); 
        double tinggiSegitigaPanjang = Math.sqrt(Math.pow(tinggi, 2) + Math.pow(lebar / 2.0, 2));
        double tinggiSegitigaLebar = Math.sqrt(Math.pow(tinggi, 2) + Math.pow(panjang / 2.0, 2));
        
        double luasSisiTegakPanjang = 2 * (0.5 * panjang * tinggiSegitigaPanjang);
        double luasSisiTegakLebar = 2 * (0.5 * lebar * tinggiSegitigaLebar);
        
        return luasAlas + luasSisiTegakPanjang + luasSisiTegakLebar;
    }

    @Override
    public String getHasilKalkulasi() {
        return String.format("--- Limas Persegi Panjang ---\nVolume: %.2f\nLuas Permukaan: %.2f\n", 
            hitungVolume(), hitungLuasPermukaan());
    }
}
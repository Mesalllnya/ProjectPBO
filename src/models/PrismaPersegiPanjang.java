package models;

public class PrismaPersegiPanjang extends PersegiPanjang {
    private double tinggi;

    public PrismaPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar);
        this.tinggi = tinggi;
    }

    public double hitungVolume() {
        return hitungLuas() * tinggi; 
    }

    public double hitungLuasPermukaan() {
        return 2 * (hitungLuas() + (panjang * tinggi) + (lebar * tinggi));
    }

    @Override
    public String getHasilKalkulasi() {
        return String.format("--- Prisma Persegi Panjang ---\nVolume: %.2f\nLuas Permukaan: %.2f\n", 
            hitungVolume(), hitungLuasPermukaan());
    }
}
public class LimasPersegiPanjang extends PersegiPanjang {
    public double tinggi;
    public double luasPermukaanLimas;
    public double volumeLimas;

    // CONSTRUCTOR 1: TANPA PARAMETER (Random)
    public LimasPersegiPanjang() {
        super();
        this.tinggi = (Math.random() * 40) + 5;
    }

    // CONSTRUCTOR 2: DENGAN PARAMETER
    public LimasPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar);
        this.tinggi = tinggi;
    }

    // --- IMPLEMENTASI TANPA PARAMETER ---
    @Override
    public double menghitungLuasPermukaan() {
        double tSegiLebar = Math.sqrt(Math.pow(panjang / 2.0, 2) + Math.pow(tinggi, 2));
        double tSegiPanjang = Math.sqrt(Math.pow(lebar / 2.0, 2) + Math.pow(tinggi, 2));
        luasPermukaanLimas = super.menghitungLuas() + (panjang * tSegiPanjang) + (lebar * tSegiLebar);
        return luasPermukaanLimas;
    }

    @Override
    public double menghitungVolume() {
        volumeLimas = (super.menghitungLuas() * tinggi) / 3.0;
        return volumeLimas;
    }

    // --- IMPLEMENTASI DENGAN PARAMETER ---
    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        double tSegiLebar = Math.sqrt(Math.pow(p / 2.0, 2) + Math.pow(t, 2));
        double tSegiPanjang = Math.sqrt(Math.pow(l / 2.0, 2) + Math.pow(t, 2));
        luasPermukaanLimas = super.menghitungLuas(p, l) + (p * tSegiPanjang) + (l * tSegiLebar);
        return luasPermukaanLimas;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        volumeLimas = (super.menghitungLuas(p, l) * t) / 3.0;
        return volumeLimas;
    }
}
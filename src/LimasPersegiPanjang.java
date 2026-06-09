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
        double tSegiLebar = Math.sqrt(Math.pow(super.panjang / 2.0, 2) + Math.pow(this.tinggi, 2));
        double tSegiPanjang = Math.sqrt(Math.pow(super.lebar / 2.0, 2) + Math.pow(this.tinggi, 2));
        this.luasPermukaanLimas = super.menghitungLuas() + (super.panjang * tSegiPanjang) + (super.lebar * tSegiLebar);
        return this.luasPermukaanLimas;
    }

    @Override
    public double menghitungVolume() {
        this.volumeLimas = (super.menghitungLuas() * this.tinggi) / 3.0;
        return this.volumeLimas;
    }

    // --- IMPLEMENTASI DENGAN PARAMETER ---
    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        double tSegiLebar = Math.sqrt(Math.pow(p / 2.0, 2) + Math.pow(t, 2));
        double tSegiPanjang = Math.sqrt(Math.pow(l / 2.0, 2) + Math.pow(t, 2));
        this.luasPermukaanLimas = super.menghitungLuas(p, l) + (p * tSegiPanjang) + (l * tSegiLebar);
        return this.luasPermukaanLimas;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        this.volumeLimas = (super.menghitungLuas(p, l) * t) / 3.0;
        return this.volumeLimas;
    }
}
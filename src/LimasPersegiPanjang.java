public class LimasPersegiPanjang extends PersegiPanjang {
    public double tinggi;
    public double luasPermukaanLimas;
    public double volumeLimas;
    // tsegi = tinggi segitiga untuk setiap lebar dan panjang
    public double tSegiLebar;
    public double tSegiPanjang;

    // TANPA PARAMETER (Random)
    public LimasPersegiPanjang() {
        super();
        this.tinggi = (Math.random() * 40) + 5;
    }

    // DENGAN PARAMETER
    public LimasPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar);
        this.tinggi = tinggi;
    }

    @Override
    public double menghitungLuasPermukaan() {
         tSegiLebar = Math.sqrt(Math.pow(super.panjang / 2.0, 2) + Math.pow(this.tinggi, 2));
         tSegiPanjang = Math.sqrt(Math.pow(super.lebar / 2.0, 2) + Math.pow(this.tinggi, 2));
        this.luasPermukaanLimas = super.luas + (super.panjang * tSegiPanjang) + (super.lebar * tSegiLebar);
        return this.luasPermukaanLimas;
    }

    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        tSegiLebar = Math.sqrt(Math.pow(p / 2.0, 2) + Math.pow(t, 2));
        tSegiPanjang = Math.sqrt(Math.pow(l / 2.0, 2) + Math.pow(t, 2));
        this.luasPermukaanLimas = super.menghitungLuas(p, l) + (p * tSegiPanjang) + (l * tSegiLebar);
        return this.luasPermukaanLimas;
    }

    @Override
    public double menghitungVolume() {
        this.volumeLimas = (super.luas * this.tinggi) / 3.0;
        return this.volumeLimas;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        this.volumeLimas = (super.menghitungLuas(p, l) * t) / 3.0;
        return this.volumeLimas;
    }
    
    @Override
    public double menghitungKeliling() {
        return 0;
    }
    
    @Override
    public double menghitungKeliling(double p, double l) {
        return 0;
    }
}
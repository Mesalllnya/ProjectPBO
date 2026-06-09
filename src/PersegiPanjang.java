public class PersegiPanjang implements Geometri {
    public double panjang;
    public double lebar;
    public double luas;
    public double keliling;

    // CONSTRUCTOR 1: TANPA PARAMETER (Otomatis generate nilai random 5 - 45)
    public PersegiPanjang() {
        this.panjang = (Math.random() * 40) + 5;
        this.lebar = (Math.random() * 40) + 5;
    }

    // CONSTRUCTOR 2: DENGAN PARAMETER (Menerima input dinamis)
    public PersegiPanjang(double panjang, double lebar) {
        this.panjang = panjang;
        this.lebar = lebar;
    }

    // --- IMPLEMENTASI TANPA PARAMETER ---
    @Override
    public double menghitungLuas() {
        this.luas = panjang * lebar;
        return this.luas;
    }

    @Override
    public double menghitungKeliling() {
        this.keliling = 2 * (panjang + lebar);
        return this.keliling;
    }

    @Override
    public double menghitungLuasPermukaan() {
        return menghitungLuas();
    }

    @Override
    public double menghitungVolume() {
        return 0;
    }

    // --- IMPLEMENTASI DENGAN PARAMETER ---
    @Override
    public double menghitungLuas(double p, double l) {
        this.luas = p * l;
        return this.luas;
    }

    @Override
    public double menghitungKeliling(double p, double l) {
        this.keliling = 2 * (p + l);
        return this.keliling;
    }

    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        return menghitungLuas(p, l);
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        return 0;
    }
}
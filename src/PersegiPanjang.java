public class PersegiPanjang implements Geometri {
    public double panjang;
    public double lebar;
    public double luas;
    public double keliling;

    // TANPA PARAMETER (Otomatis generate nilai random 5 - 45)
    // Math.random() akan mengenerate angka dengan nilai dari 0.0 hingga kurang dari 1
    public PersegiPanjang() {
        this.panjang = (Math.random() * 40) + 5;
        this.lebar = (Math.random() * 40) + 5;
    }

    // DENGAN PARAMETER (Menerima input dinamis)
    public PersegiPanjang(double panjang, double lebar) {
        this.panjang = panjang;
        this.lebar = lebar;
    }

    @Override
    public double menghitungLuas() {
        this.luas = panjang * lebar;
        return this.luas;
    }

    @Override
    public double menghitungLuas(double p, double l) {
        this.luas = p * l;
        return this.luas;
    }

    @Override
    public double menghitungKeliling() {
        this.keliling = 2 * (this.panjang + this.lebar);
        return this.keliling;
    }

    @Override
    public double menghitungKeliling(double p, double l) {
        this.keliling = 2 * (p + l);
        return this.keliling;
    }

    @Override
    public double menghitungLuasPermukaan() {
        return menghitungLuas();
    }

    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        return 0;
    }
    
    @Override
    public double menghitungVolume() {
        return 0;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        return 0;
    }
}
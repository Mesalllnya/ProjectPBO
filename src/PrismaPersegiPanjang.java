public class PrismaPersegiPanjang extends PersegiPanjang {
    public double tinggi;
    public double luasPermukaanPrisma;
    public double volumePrisma;

    public PrismaPersegiPanjang() {
        super(); // Memanggil constructor yang mengenerate input random milik PersegiPanjang
        this.tinggi = (Math.random() * 40) + 5;
    }

    public PrismaPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar);
        this.tinggi = tinggi;
    }

    @Override
    public double menghitungLuasPermukaan() {
        this.luasPermukaanPrisma = 2 * ((super.panjang * super.lebar) + (super.panjang * tinggi) + (super.lebar * tinggi));
        return this.luasPermukaanPrisma;
    }

    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        this.luasPermukaanPrisma = 2 * ((super.luas) + (p * t) + (l * t));
        return this.luasPermukaanPrisma;
    }

    @Override
    public double menghitungVolume() {
        this.volumePrisma = super.menghitungLuas() * this.tinggi;
        return this.volumePrisma;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        this.volumePrisma = super.menghitungLuas(p, l) * t;
        return this.volumePrisma;
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
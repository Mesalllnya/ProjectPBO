public class PrismaPersegiPanjang extends PersegiPanjang {
    public double tinggi;
    public double luasPermukaanPrisma;
    public double volumePrisma;

    // CONSTRUCTOR 1: TANPA PARAMETER (Random)
    public PrismaPersegiPanjang() {
        super(); // Memanggil constructor random milik PersegiPanjang
        this.tinggi = (Math.random() * 40) + 5;
    }

    // CONSTRUCTOR 2: DENGAN PARAMETER
    public PrismaPersegiPanjang(double panjang, double lebar, double tinggi) {
        super(panjang, lebar);
        this.tinggi = tinggi;
    }

    // --- IMPLEMENTASI TANPA PARAMETER ---
    @Override
    public double menghitungLuasPermukaan() {
        luasPermukaanPrisma = 2 * ((panjang * lebar) + (panjang * tinggi) + (lebar * tinggi));
        return luasPermukaanPrisma;
    }

    @Override
    public double menghitungVolume() {
        volumePrisma = super.menghitungLuas() * tinggi;
        return volumePrisma;
    }

    // --- IMPLEMENTASI DENGAN PARAMETER ---
    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        luasPermukaanPrisma = 2 * ((p * l) + (p * t) + (l * t));
        return luasPermukaanPrisma;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        volumePrisma = super.menghitungLuas(p, l) * t;
        return volumePrisma;
    }
}
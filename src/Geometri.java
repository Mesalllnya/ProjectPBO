public interface Geometri {
    // Versi Tanpa Parameter (Akan menggunakan data internal/Random)
    double menghitungLuas();
    double menghitungKeliling();
    double menghitungLuasPermukaan();
    double menghitungVolume();

    // Versi Dengan Parameter (Akan menerima data angka/Statis)
    double menghitungLuas(double p, double l);
    double menghitungKeliling(double p, double l);
    double menghitungLuasPermukaan(double p, double l, double t);
    double menghitungVolume(double p, double l, double t);
}
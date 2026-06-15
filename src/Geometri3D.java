public interface Geometri3D {
    // Tanpa Parameter (Akan menggunakan data internal/Random)
    double menghitungLuasPermukaan();
    double menghitungVolume();

    // Dengan Parameter (Akan menerima data angka/Statis) dari input user
    double menghitungLuasPermukaan(double p, double l, double t);
    double menghitungVolume(double p, double l, double t);
}

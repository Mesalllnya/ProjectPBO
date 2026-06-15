public interface Geometri2D {
        // Tanpa Parameter (Akan menggunakan data internal/Random)
    double menghitungLuas();
    double menghitungKeliling();
    

    // Dengan Parameter (Akan menerima data angka/Statis) dari input user
    double menghitungLuas(double p, double l);
    double menghitungKeliling(double p, double l);
    
}


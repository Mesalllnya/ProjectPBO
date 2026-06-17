import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.util.Vector;

public class LimasPersegiPanjang extends PersegiPanjang implements Geometri3D {
    public double tinggi;
    public double luasPermukaanLimas;
    public double volumeLimas;
    // tsegi = tinggi segitiga untuk setiap lebar dan panjang
    public double tSegiLebar;
    public double tSegiPanjang;

    // --- CONSTRUCTOR 1 & 2 (Perhitungan Standar) ---
    public LimasPersegiPanjang() {
        super();
        this.tinggi = (Math.random() * 40) + 5;
    }

    public LimasPersegiPanjang(double panjang, double lebar, double tinggi, double luasAlas) {
        super(panjang, lebar);
        this.tinggi = tinggi;
        super.luas=luasAlas;
    }

    // --- CONSTRUCTOR 3 (Khusus untuk menjalankan Thread) ---
    public LimasPersegiPanjang(MainGUI gui, int batasData, String namaThread, JProgressBar pb, double[][] dataAlas) {
        // Memanggil constructor thread milik superclass (PersegiPanjang)
        super(gui, batasData, namaThread, pb, dataAlas);
    }

    @Override
    public void run() {
        Vector<Vector<Object>> batchData = new Vector<>();

        for (int i = 0; i < batasData; i++) {
            // Cukup perbarui atribut milik dirinya sendiri (objek Thread ini)
            super.panjang = dataAlas[i][0];
            super.lebar = dataAlas[i][1];
            super.luas = dataAlas[i][2]; // Luas alas langsung diisi
            this.tinggi = dataAlas[i][3];

            // Komputer akan otomatis menghitung menggunakan data yang baru saja di-update di atas
            double luasHitung = this.menghitungLuasPermukaan();
            double volumeHitung = this.menghitungVolume();
            double kelilingHitung = 0; 

            String paramStr = String.format("P=%.1f, L=%.1f, T=%.1f", super.panjang, super.lebar, this.tinggi);

            Vector<Object> baris = new Vector<>();
            baris.add(gui.totalBaris + i + 1); 
            baris.add("Limas Segi Empat");
            baris.add(paramStr);
            baris.add(String.format("%.2f", luasHitung));
            baris.add(String.format("%.2f", volumeHitung));
            baris.add(String.format("%.2f", kelilingHitung));
            baris.add(namaThread);
            batchData.add(baris);

            int persen = (int) (((double) (i + 1) / batasData) * 100);
            if ((i + 1) % 500 == 0 || (i + 1) == batasData) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(persen);
                    progressBar.setString(namaThread + " : " + persen + "%");
                });
            }
        }

        // Update Tabel dan Statistik di GUI
        SwingUtilities.invokeLater(() -> {
            for (Vector<Object> row : batchData) {
                gui.tableModel.addRow(row);
            }
            gui.countLimas += batasData;
            gui.totalBaris += batasData;
            gui.updateStatistik();
            gui.cekWaktuSelesai();
        });
    }

    // --- RUMUS MATEMATIKA ---
    @Override
    public double menghitungLuasPermukaan() {
         this.tSegiLebar = Math.sqrt(Math.pow(super.panjang / 2.0, 2) + Math.pow(this.tinggi, 2));
         this.tSegiPanjang = Math.sqrt(Math.pow(super.lebar / 2.0, 2) + Math.pow(this.tinggi, 2));
         
         // Memanggil method super.menghitungLuas() agar P x L dihitung saat ini juga
         this.luasPermukaanLimas = super.luas + (super.panjang * this.tSegiPanjang) + (super.lebar * this.tSegiLebar);
         return this.luasPermukaanLimas;
    }

    @Override
    public double menghitungLuasPermukaan(double p, double l, double t) {
        this.tSegiLebar = Math.sqrt(Math.pow(p / 2.0, 2) + Math.pow(t, 2));
        this.tSegiPanjang = Math.sqrt(Math.pow(l / 2.0, 2) + Math.pow(t, 2));
        this.luasPermukaanLimas = super.menghitungLuas(p, l) + (p * this.tSegiPanjang) + (l * this.tSegiLebar);
        return this.luasPermukaanLimas;
    }

    @Override
    public double menghitungVolume() {
        // Memanggil method super.menghitungLuas()
        this.volumeLimas = (super.luas * this.tinggi) / 3.0;
        return this.volumeLimas;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        this.volumeLimas = (super.menghitungLuas(p, l) * t) / 3.0;
        return this.volumeLimas;
    }
}
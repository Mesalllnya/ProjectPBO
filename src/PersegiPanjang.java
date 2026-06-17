import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.util.Vector;

public class PersegiPanjang implements Geometri, Runnable {
    public double panjang, lebar, luas, keliling;

    // --- VARIABEL UNTUK KEBUTUHAN THREAD ---
    public MainGUI gui;
    public int batasData;
    public String namaThread;
    public JProgressBar progressBar;
    public double[][] dataAlas; // Menerima data array dari GUI

    // Constructor 1 & 2 (Untuk hitungan biasa)
    public PersegiPanjang() {
        this.panjang = (Math.random() * 40) + 5;
        this.lebar = (Math.random() * 40) + 5;
    }
    public PersegiPanjang(double panjang, double lebar) {
        this.panjang = panjang;
        this.lebar = lebar;
    }

    // Constructor 3 (KHUSUS UNTUK MENJALANKAN THREAD)
    public PersegiPanjang(MainGUI gui, int batasData, String namaThread, JProgressBar pb, double[][] dataAlas) {
        this.gui = gui;
        this.batasData = batasData;
        this.namaThread = namaThread;
        this.progressBar = pb;
        this.dataAlas = dataAlas;
    }

    // --- LOGIKA MULTITHREADING ---
    @Override
    public void run() {
        Vector<Vector<Object>> batchData = new Vector<>();

        for (int i = 0; i < batasData; i++) {
            // 1. JANGAN BUAT OBJEK BARU! 
            // Cukup perbarui atribut milik dirinya sendiri (objek Thread ini)
            this.panjang = dataAlas[i][0];
            this.lebar = dataAlas[i][1];
            this.luas = dataAlas[i][2]; // Luas alas langsung diisi
//            this.tinggi = dataAlas[i][3];

            // 2. Langsung panggil method perhitungannya!
            // Komputer akan otomatis menghitung menggunakan data yang baru saja di-update di atas
            double luasHitung = this.menghitungLuas();
            double volumeHitung = 0;
            double kelilingHitung = this.menghitungKeliling(); 

            String paramStr = String.format("P=%.1f, L=%.1f", this.panjang, this.lebar);

            Vector<Object> baris = new Vector<>();
            baris.add(gui.totalBaris + i + 1); 
            baris.add("Persegi Panjang");
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

        SwingUtilities.invokeLater(() -> {
            for (Vector<Object> row : batchData) gui.tableModel.addRow(row);
            gui.countPersegi += batasData;
            gui.totalBaris += batasData;
            gui.updateStatistik();
            gui.cekWaktuSelesai();
        });
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
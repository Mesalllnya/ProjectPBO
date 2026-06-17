import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.util.Vector;

public class PrismaPersegiPanjang extends PersegiPanjang implements Geometri3D {
    public double tinggi, luasPermukaanPrisma, volumePrisma;

    public PrismaPersegiPanjang() {
        super();
        this.tinggi = (Math.random() * 40) + 5;
    }
    public PrismaPersegiPanjang(double panjang, double lebar, double tinggi, double luasAlas) {
        super(panjang, lebar);
        this.tinggi = tinggi;
        super.luas=luasAlas;
    }

    // Constructor Thread (Memanggil constructor thread milik bapaknya)
    public PrismaPersegiPanjang(MainGUI gui, int batasData, String namaThread, JProgressBar pb, double[][] dataAlas) {
        super(gui, batasData, namaThread, pb, dataAlas);
    }

    @Override
    public void run() {
        Vector<Vector<Object>> batchData = new Vector<>();

        for (int i = 0; i < batasData; i++) {
            // Cukup perbarui atribut milik dirinya sendiri
            super.panjang = dataAlas[i][0];
            super.lebar = dataAlas[i][1];
            super.luas = dataAlas[i][2]; // Luas alas langsung diisi
            this.tinggi = dataAlas[i][3];

            
            double luasHitung = this.menghitungLuasPermukaan();
            double volumeHitung = this.menghitungVolume();
            double kelilingHitung = 0; 

            String paramStr = String.format("P=%.1f, L=%.1f, T=%.1f", super.panjang, super.lebar, this.tinggi);

            Vector<Object> baris = new Vector<>();
            baris.add(gui.totalBaris + i + 1); 
            baris.add("Prisma Segi Empat");
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
            gui.countPrisma += batasData;
            gui.totalBaris += batasData;
            gui.updateStatistik();
            gui.cekWaktuSelesai();
        });
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
        this.volumePrisma = super.luas * this.tinggi;
        return this.volumePrisma;
    }

    @Override
    public double menghitungVolume(double p, double l, double t) {
        this.volumePrisma = super.menghitungLuas(p, l) * t;
        return this.volumePrisma;
    }
    
}
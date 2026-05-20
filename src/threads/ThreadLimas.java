package threads;

import models.DataShared;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ThreadLimas extends Thread {
    private double[] p, l, t;
    private ThreadPersegi threadUtama;
    private DataShared shared;
    private DefaultTableModel tableModel;
    private JProgressBar progressBar;

    public ThreadLimas(double[] p, double[] l, double[] t, ThreadPersegi threadUtama, DataShared shared, DefaultTableModel tableModel, JProgressBar progressBar) {
        this.p = p;
        this.l = l;
        this.t = t;
        this.threadUtama = threadUtama;
        this.shared = shared;
        this.tableModel = tableModel;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {
        try {
            threadUtama.join();
            
            List<Object[]> barisData = new ArrayList<>();
            
            for (int i = 0; i < t.length; i++) {
                double luasAlas = shared.getLuasAlas(i);
                double kelilingAlas = shared.getKelilingAlas(i); // Ambil data keliling alas
                
                double volume = (1.0 / 3.0) * luasAlas * t[i]; 
                double tinggiSegitigaPanjang = Math.sqrt(Math.pow(t[i], 2) + Math.pow(l[i] / 2.0, 2));
                double tinggiSegitigaLebar = Math.sqrt(Math.pow(t[i], 2) + Math.pow(p[i] / 2.0, 2));
                
                double luasSisiTegakPanjang = 2 * (0.5 * p[i] * tinggiSegitigaPanjang);
                double luasSisiTegakLebar = 2 * (0.5 * l[i] * tinggiSegitigaLebar);
                double luasPermukaan = luasAlas + luasSisiTegakPanjang + luasSisiTegakLebar;
                
                // Kolom Keliling diisi dengan Keliling Alas agar tidak kosong
                Object[] row = {
                    (i + 1), "Limas Segi Empat", 
                    String.format("P=%.1f, L=%.1f, T=%.1f", p[i], l[i], t[i]),
                    String.format("%.2f", luasPermukaan), 
                    String.format("%.2f", volume), 
                    String.format("%.2f", kelilingAlas), 
                    "Thread 3"
                };
                barisData.add(row);
            }
            
            SwingUtilities.invokeLater(() -> {
                for (Object[] row : barisData) {
                    tableModel.addRow(row);
                }
                if (progressBar != null) {
                    progressBar.setValue(100);
                }
            });
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package threads;

import models.DataShared;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ThreadPrisma extends Thread {
    private double[] p, l, t;
    private ThreadPersegi threadUtama;
    private DataShared shared;
    private DefaultTableModel tableModel;
    private JProgressBar progressBar;

    public ThreadPrisma(double[] p, double[] l, double[] t, ThreadPersegi threadUtama, DataShared shared, DefaultTableModel tableModel, JProgressBar progressBar) {
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
                double kelilingAlas = shared.getKelilingAlas(i);
                
                double volume = luasAlas * t[i]; 
                double luasPermukaan = (2 * luasAlas) + (kelilingAlas * t[i]);
                
                // Kolom Keliling diisi dengan Keliling Alas agar tidak kosong
                Object[] row = {
                    (i + 1), "Prisma Segi Empat", 
                    String.format("P=%.1f, L=%.1f, T=%.1f", p[i], l[i], t[i]),
                    String.format("%.2f", luasPermukaan), 
                    String.format("%.2f", volume), 
                    String.format("%.2f", kelilingAlas), 
                    "Thread 2"
                };
                barisData.add(row);
            }
            
            SwingUtilities.invokeLater(() -> {
                for (Object[] row : barisData) {
                    tableModel.addRow(row);
                }
                if (progressBar != null) {
                    progressBar.setValue(66);
                }
            });
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package threads;

import models.DataShared;
import models.PersegiPanjang;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ThreadPersegi extends Thread {
    private double[] p, l;
    private DataShared shared;
    private DefaultTableModel tableModel;
    private JProgressBar progressBar;

    public ThreadPersegi(double[] p, double[] l, DataShared shared, DefaultTableModel tableModel, JProgressBar progressBar) {
        this.p = p;
        this.l = l;
        this.shared = shared;
        this.tableModel = tableModel;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {
        List<Object[]> barisData = new ArrayList<>();
        
        for (int i = 0; i < p.length; i++) {
            PersegiPanjang persegi = new PersegiPanjang(p[i], l[i]);
            double luas = persegi.hitungLuas();
            double keliling = persegi.hitungKeliling();
            
            shared.setLuasAlas(i, luas);
            shared.setKelilingAlas(i, keliling);
            
            // Kolom Volume diisi "0.00" karena Persegi Panjang tidak memiliki volume
            Object[] row = {
                (i + 1), "Persegi Panjang", 
                String.format("P=%.1f, L=%.1f", p[i], l[i]),
                String.format("%.2f", luas), 
                "0.00", 
                String.format("%.2f", keliling), 
                "Thread 1"
            };
            barisData.add(row);
        }
        
        SwingUtilities.invokeLater(() -> {
            for (Object[] row : barisData) {
                tableModel.addRow(row);
            }
            if (progressBar != null) {
                progressBar.setValue(33);
            }
        });
    }
}
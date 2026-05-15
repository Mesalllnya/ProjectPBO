package threads;

import models.DataShared;
import models.PersegiPanjang;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ThreadPersegi extends Thread {
    private double p, l;
    private DataShared shared;
    private JTextArea output;

    public ThreadPersegi(double p, double l, DataShared shared, JTextArea output) {
        this.p = p;
        this.l = l;
        this.shared = shared;
        this.output = output;
    }

    @Override
    public void run() {
        PersegiPanjang persegi = new PersegiPanjang(p, l);
        double luas = persegi.hitungLuas();
        double keliling = persegi.hitungKeliling();
        
        shared.setLuasAlas(luas);
        shared.setKelilingAlas(keliling);
        
        SwingUtilities.invokeLater(() -> {
            output.append("[Thread 1 - Persegi Panjang]\n");
            output.append("Luas : " + luas + "\n");
            output.append("Keliling : " + keliling + "\n\n");
        });
    }
}
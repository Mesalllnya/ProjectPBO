package threads;

import models.DataShared;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ThreadPrisma extends Thread {
    private double t;
    private ThreadPersegi threadUtama;
    private DataShared shared;
    private JTextArea output;

    public ThreadPrisma(double t, ThreadPersegi threadUtama, DataShared shared, JTextArea output) {
        this.t = t;
        this.threadUtama = threadUtama;
        this.shared = shared;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            threadUtama.join(); // Menunggu ThreadPersegi selesai
            
            double luasAlas = shared.getLuasAlas();
            double kelilingAlas = shared.getKelilingAlas();
            
            double volume = luasAlas * t; 
            double luasPermukaan = (2 * luasAlas) + (kelilingAlas * t);
            
            SwingUtilities.invokeLater(() -> {
                output.append("[Thread 2 - Prisma Persegi Panjang]\n");
                output.append("Volume: " + volume + "\n");
                output.append("Luas Permukaan: " + luasPermukaan + "\n\n");
            });
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}   
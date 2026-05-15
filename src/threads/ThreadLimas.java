package threads;

import models.DataShared;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ThreadLimas extends Thread {
    private double p, l, t;
    private ThreadPersegi threadUtama;
    private DataShared shared;
    private JTextArea output;

    // Menambahkan p dan l pada konstruktor untuk perhitungan sisi miring limas
    public ThreadLimas(double p, double l, double t, ThreadPersegi threadUtama, DataShared shared, JTextArea output) {
        this.p = p;
        this.l = l;
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
            
            // Perhitungan Volume
            double volume = (1.0 / 3.0) * luasAlas * t; 
            
            // Perhitungan Luas Permukaan
            double tinggiSegitigaPanjang = Math.sqrt(Math.pow(t, 2) + Math.pow(l / 2.0, 2));
            double tinggiSegitigaLebar = Math.sqrt(Math.pow(t, 2) + Math.pow(p / 2.0, 2));
            
            double luasSisiTegakPanjang = 2 * (0.5 * p * tinggiSegitigaPanjang);
            double luasSisiTegakLebar = 2 * (0.5 * l * tinggiSegitigaLebar);
            
            double luasPermukaan = luasAlas + luasSisiTegakPanjang + luasSisiTegakLebar;
            
            SwingUtilities.invokeLater(() -> {
                output.append("[Thread 3 - Limas Persegi Panjang]\n");
                output.append("Volume: " + String.format("%.2f", volume) + "\n");
                output.append("Luas Permukaan: " + String.format("%.2f", luasPermukaan) + "\n");
                output.append("------------------------------------------------\n");
            });
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
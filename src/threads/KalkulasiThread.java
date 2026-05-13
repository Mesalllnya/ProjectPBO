package threads;

import models.BangunGeometri;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

// [MULTITHREADING: PENERAPAN]
// Class ini mewarisi class 'Thread' bawaan Java agar proses kalkulasi bisa berjalan 
// secara asinkron di background tanpa membuat GUI menjadi 'hang' atau 'freeze'.
public class KalkulasiThread extends Thread {
    private BangunGeometri bangun;
    private JTextArea outputArea;

    public KalkulasiThread(BangunGeometri bangun, JTextArea outputArea) {
        this.bangun = bangun;
        this.outputArea = outputArea;
    }

    // [MULTITHREADING: EKSEKUSI]
    // Method run() adalah method wajib dari Thread yang akan dijalankan di latar belakang.
    @Override
    public void run() {
        
        // [PILAR OOP: POLIMORFISME - DYNAMIC METHOD DISPATCH]
        // Di sinilah keajaiban Polimorfisme terjadi. Kita tidak peduli apakah objek 'bangun' 
        // itu persegi, prisma, atau limas. Program secara otomatis memanggil versi getHasilKalkulasi() 
        // yang tepat sesuai dengan objek yang dimasukkan oleh user di GUI.
        String hasil = bangun.getHasilKalkulasi();

        // Swing tidak thread-safe, update UI harus dipindahkan ke Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            outputArea.append(hasil + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }
}
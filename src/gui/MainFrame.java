package gui;

import threads.ThreadLimas;
import threads.ThreadPrisma;
import threads.ThreadPersegi;
import threads.*;
import models.DataShared;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextField txtPanjang, txtLebar, txtTinggi;
    private JTextArea txtOutput;

    public MainFrame() {
        setTitle("Kalkulator Geometri Otomatis (Real-time Multithreading)");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // Panel Input - Tanpa JComboBox dan Button
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelInput.add(new JLabel("Panjang:"));
        txtPanjang = new JTextField();
        panelInput.add(txtPanjang);

        panelInput.add(new JLabel("Lebar:"));
        txtLebar = new JTextField();
        panelInput.add(txtLebar);

        panelInput.add(new JLabel("Tinggi:"));
        txtTinggi = new JTextField();
        panelInput.add(txtTinggi);

        // Menambahkan listener untuk kalkulasi real-time
        addRealTimeListener(txtPanjang);
        addRealTimeListener(txtLebar);
        addRealTimeListener(txtTinggi);

        add(panelInput, BorderLayout.NORTH);

        // Panel Output
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtOutput.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(txtOutput);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Live Output (Multithreading Dependency)"));
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Menambahkan DocumentListener ke JTextField agar mendeteksi 
     * setiap perubahan teks (ketik/hapus).
     */
    private void addRealTimeListener(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { jalankanKalkulasi(); }
            @Override
            public void removeUpdate(DocumentEvent e) { jalankanKalkulasi(); }
            @Override
            public void changedUpdate(DocumentEvent e) { jalankanKalkulasi(); }
        });
    }

    private void jalankanKalkulasi() {
    // 1. Reset Output setiap kali ada perubahan input
    txtOutput.setText("--- STATUS KALKULASI ---\n\n");

    try {
        // Ambil string dari textfield
        String strP = txtPanjang.getText();
        String strL = txtLebar.getText();
        String strT = txtTinggi.getText();

        // 2. Validasi Dasar: Apakah Panjang & Lebar sudah diisi?
        if (strP.isEmpty() || strL.isEmpty()) {
            txtOutput.append("[Sistem] Menunggu input Panjang & Lebar...\n");
            return;
        }

        double p = Double.parseDouble(strP);
        double l = Double.parseDouble(strL);

        // Validasi Logika 2D: Tidak boleh 0 atau negatif
        if (p <= 0 || l <= 0) {
            txtOutput.append("[Error] Panjang & Lebar harus lebih dari 0!\n");
            return;
        }

        // Inisialisasi DataShared untuk menampung Luas Alas
        DataShared sharedData = new DataShared();
        
        // 3. JALANKAN THREAD PERSEGI (Selalu jalan jika p & l valid)
        ThreadPersegi thread1 = new ThreadPersegi(p, l, sharedData, txtOutput);
        thread1.start();

        // 4. VALIDASI TINGGI UNTUK PRISMA & LIMAS
        if (strT.isEmpty()) {
            txtOutput.append("\n[Notifikasi Prisma & Limas]:\n");
            txtOutput.append("-> Gagal diproses: Tinggi belum diinput!\n");
        } else {
            double t = Double.parseDouble(strT);

            if (t <= 0) {
                txtOutput.append("\n[Notifikasi Prisma & Limas]:\n");
                txtOutput.append("-> Gagal diproses: Tinggi harus lebih dari 0!\n");
            } else {
                // Jika tinggi valid (> 0), jalankan Thread Prisma & Limas
                ThreadPrisma thread2 = new ThreadPrisma(t, thread1, sharedData, txtOutput);
                ThreadLimas thread3 = new ThreadLimas(p, l, t, thread1, sharedData, txtOutput);

                thread2.start();
                thread3.start();
            }
        }

    } catch (NumberFormatException ex) {
        txtOutput.setText("[Error] Input harus berupa angka yang valid!");
    }
}
}
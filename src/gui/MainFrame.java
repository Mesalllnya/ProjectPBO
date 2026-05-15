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
        // Cek jika field kosong untuk menghindari spam error di console
        if (txtPanjang.getText().isEmpty() || txtLebar.getText().isEmpty() || txtTinggi.getText().isEmpty()) {
            txtOutput.setText("Menunggu input lengkap...");
            return;
        }

        try {
            double p = Double.parseDouble(txtPanjang.getText());
            double l = Double.parseDouble(txtLebar.getText());
            double t = Double.parseDouble(txtTinggi.getText());
            
            // Reset output setiap kali kalkulasi baru dimulai
            txtOutput.setText("--- PROSES KALKULASI BERANTAI ---\n\n");

            // Cari metode jalankanKalkulasi() di MainFrame.java dan ubah bagian inisialisasi thread menjadi:

            DataShared sharedData = new DataShared();

            ThreadPersegi thread1 = new ThreadPersegi(p, l, sharedData, txtOutput);
            ThreadPrisma thread2 = new ThreadPrisma(t, thread1, sharedData, txtOutput);
            // Tambahkan argumen p dan l ke dalam ThreadLimas
            ThreadLimas thread3 = new ThreadLimas(p, l, t, thread1, sharedData, txtOutput);

            thread1.start();
            thread2.start();
            thread3.start();

        } catch (NumberFormatException ex) {
            // Tidak menampilkan dialog error agar tidak mengganggu proses mengetik
            txtOutput.setText("Input harus berupa angka.");
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import models.*;
import threads.KalkulasiThread;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextField txtPanjang, txtLebar, txtTinggi;
    private JComboBox<String> cbPilihan;
    private JTextArea txtOutput;
    private JButton btnHitung;

    public MainFrame() {
        setTitle("Kalkulator Geometri OOP & Multithreading");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // Panel Input
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelInput.add(new JLabel("Pilih Bangun:"));
        String[] pilihan = {"Persegi Panjang", "Prisma Persegi Panjang", "Limas Persegi Panjang"};
        cbPilihan = new JComboBox<>(pilihan);
        cbPilihan.addActionListener(e -> sesuaikanInput());
        panelInput.add(cbPilihan);

        panelInput.add(new JLabel("Panjang:"));
        txtPanjang = new JTextField();
        panelInput.add(txtPanjang);

        panelInput.add(new JLabel("Lebar:"));
        txtLebar = new JTextField();
        panelInput.add(txtLebar);

        panelInput.add(new JLabel("Tinggi (Kosongkan utk 2D):"));
        txtTinggi = new JTextField();
        txtTinggi.setEnabled(false); // Default disable karena Persegi Panjang
        panelInput.add(txtTinggi);

        add(panelInput, BorderLayout.NORTH);

        // Panel Output
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtOutput);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hasil (Multithreading Output)"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel Tombol
        JPanel panelTombol = new JPanel();
        btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> jalankanKalkulasi());
        panelTombol.add(btnHitung);
        add(panelTombol, BorderLayout.SOUTH);
    }

    private void sesuaikanInput() {
        if (cbPilihan.getSelectedIndex() == 0) {
            txtTinggi.setEnabled(false);
            txtTinggi.setText("");
        } else {
            txtTinggi.setEnabled(true);
        }
    }

    // ... [kode GUI lainnya di atas sama persis] ...

    private void jalankanKalkulasi() {
        try {
            double p = Double.parseDouble(txtPanjang.getText());
            double l = Double.parseDouble(txtLebar.getText());
            int pilihan = cbPilihan.getSelectedIndex();
            
            // [PILAR OOP: POLIMORFISME]
            // Referensi superclass bisa menampung objek dari subclass-nya.
            BangunGeometri bangun = null;

            if (pilihan == 0) {
                bangun = new PersegiPanjang(p, l);
            } else {
                double t = Double.parseDouble(txtTinggi.getText());
                if (pilihan == 1) {
                    bangun = new PrismaPersegiPanjang(p, l, t);
                } else if (pilihan == 2) {
                    bangun = new LimasPersegiPanjang(p, l, t);
                }
            }

            if (bangun != null) {
                // [MULTITHREADING: INISIALISASI & START]
                // Membuat thread baru setiap kali tombol hitung ditekan, melempar objek bangun ke dalamnya,
                // lalu start() untuk memulai proses di background.
                KalkulasiThread thread = new KalkulasiThread(bangun, txtOutput);
                thread.start(); 
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid. Pastikan semua field diisi dengan angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

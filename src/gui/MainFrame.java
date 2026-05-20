package gui;

import threads.ThreadLimas;
import threads.ThreadPersegi;
import threads.ThreadPrisma;
import models.DataShared;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class MainFrame extends JFrame {

    // --- Deklarasi Komponen Kontrol ---
    private JCheckBox chkPersegiPanjang, chkPrisma, chkLimas;
    private JTextField txtJumlahData;
    private JSpinner spinThread;
    private JButton btnMulai, btnReset;
    private JProgressBar mainProgressBar;
    private JLabel lblWaktuTotal;
    private JPanel panelProgressThread;
    private JTextArea txtStatistik;

    // --- Deklarasi Komponen Tabel ---
    private JTable tabelHasil;
    private DefaultTableModel tableModel;

    // --- Deklarasi Komponen Bawah ---
    private JLabel lblStatus;

    public MainFrame() {
        setTitle("Perhitungan Geometri");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inisialisasiUI();
    }

    private void inisialisasiUI() {
        // 1. HEADER (Biru)
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(78, 52, 46));
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblJudul = new JLabel("Perhitungan Geometri");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(Color.WHITE);
        
        JLabel lblSubJudul = new JLabel("Generate & hitung bangun geometri menggunakan multi-threading");
        lblSubJudul.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubJudul.setForeground(Color.WHITE);
        
        panelHeader.add(lblJudul, BorderLayout.NORTH);
        panelHeader.add(lblSubJudul, BorderLayout.SOUTH);
        add(panelHeader, BorderLayout.NORTH);

        // --- BUNGKUSAN UTAMA UNTUK KONTROL (ATAS) & TABEL (BAWAH) ---
        JPanel panelUtama = new JPanel(new BorderLayout(0, 15)); // Jarak vertikal 15px
        panelUtama.setBorder(new EmptyBorder(15, 20, 15, 20)); // Margin sisi layar

        // 2. PANEL ATAS (Dashboard Menu, dibagi jadi 4 kolom rata)
        JPanel panelAtas = new JPanel(new GridLayout(1, 4, 15, 0)); // 1 Baris, 4 Kolom, Jarak antar kolom 15px
        panelAtas.setPreferredSize(new Dimension(0, 200)); // Membatasi tinggi panel atas agar tidak memakan layar tabel

        // 2a. Kolom 1: Pilih Bangun
        JPanel panelPilihBangun = new JPanel(new GridLayout(3, 1, 5, 5));
        panelPilihBangun.setBorder(BorderFactory.createTitledBorder("Pilih Bangun"));
        chkPersegiPanjang = new JCheckBox("Persegi Panjang", true);
        chkPrisma = new JCheckBox("Prisma Segi Empat", true);
        chkLimas = new JCheckBox("Limas Segi Empat", true);
        panelPilihBangun.add(chkPersegiPanjang);
        panelPilihBangun.add(chkPrisma);
        panelPilihBangun.add(chkLimas);
        panelAtas.add(panelPilihBangun);

        // 2b. Kolom 2: Konfigurasi Proses
        JPanel panelKonfig = new JPanel(new GridBagLayout());
        panelKonfig.setBorder(BorderFactory.createTitledBorder("Konfigurasi Proses"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panelKonfig.add(new JLabel("Jumlah Data:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtJumlahData = new JTextField("10000");
        panelKonfig.add(txtJumlahData, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelKonfig.add(new JLabel("Jumlah Thread:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        spinThread = new JSpinner(new SpinnerNumberModel(3, 1, 16, 1));
        panelKonfig.add(spinThread, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        mainProgressBar = new JProgressBar(0, 100);
        mainProgressBar.setStringPainted(true);
        mainProgressBar.setString("Belum dimulai");
        panelKonfig.add(mainProgressBar, gbc);

        JPanel panelTombol = new JPanel(new GridLayout(1, 2, 10, 0));
        btnMulai = new JButton("Mulai Proses");
        btnReset = new JButton("Reset");
        panelTombol.add(btnMulai);
        panelTombol.add(btnReset);
        
        gbc.gridy = 3;
        panelKonfig.add(panelTombol, gbc);

        gbc.gridy = 4;
        lblWaktuTotal = new JLabel("Waktu: -- ms", SwingConstants.CENTER);
        lblWaktuTotal.setForeground(new Color(230, 126, 34));
        panelKonfig.add(lblWaktuTotal, gbc);
        panelAtas.add(panelKonfig);

        // 2c. Kolom 3: Progress Tiap Thread
        panelProgressThread = new JPanel();
        panelProgressThread.setLayout(new BoxLayout(panelProgressThread, BoxLayout.Y_AXIS));
        JScrollPane scrollProgress = new JScrollPane(panelProgressThread);
        scrollProgress.setBorder(BorderFactory.createTitledBorder("Progress Tiap Thread (Not Supported)"));
        panelAtas.add(scrollProgress);

        // 2d. Kolom 4: Statistik Hasil
        JPanel panelStat = new JPanel(new BorderLayout());
        panelStat.setBorder(BorderFactory.createTitledBorder("Statistik Hasil"));
        txtStatistik = new JTextArea();
        txtStatistik.setEditable(false);
        txtStatistik.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStatistik.setBackground(new Color(245, 245, 245));
        txtStatistik.setMargin(new Insets(5, 5, 5, 5));
        txtStatistik.setText("Total baris\t: -\nPersegi Panjang\t: -\nPrisma\t\t: -\nLimas\t\t: -");
        panelStat.add(new JScrollPane(txtStatistik), BorderLayout.CENTER);
        panelAtas.add(panelStat);

        // Masukkan panel Atas ke Panel Utama bagian Utara
        panelUtama.add(panelAtas, BorderLayout.NORTH);

        // 3. PANEL TENGAH/BAWAH (Tabel Hasil - Sekarang Lebar Penuh)
        JPanel panelTabel = new JPanel(new BorderLayout());
        panelTabel.setBorder(BorderFactory.createTitledBorder("Hasil Generate & Perhitungan"));
        
        String[] kolomTabel = {"No", "Nama Bangun", "Parameter", "Luas/LP", "Volume", "Keliling", "Thread"};
        tableModel = new DefaultTableModel(kolomTabel, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelHasil = new JTable(tableModel);
        
        // Sedikit penyesuaian lebar karena tabel sekarang sangat lebar
        tabelHasil.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelHasil.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelHasil.getColumnModel().getColumn(2).setPreferredWidth(250);
        
        panelTabel.add(new JScrollPane(tabelHasil), BorderLayout.CENTER);
        
        // Masukkan tabel ke Panel Utama bagian Tengah
        panelUtama.add(panelTabel, BorderLayout.CENTER);

        // Masukkan keseluruhan Panel Utama ke Frame
        add(panelUtama, BorderLayout.CENTER);

        // 4. BOTTOM (Status Bar)
        lblStatus = new JLabel(" Siap. Pilih bangun, atur jumlah data, lalu klik Mulai.");
        lblStatus.setBorder(BorderFactory.createEtchedBorder());
        add(lblStatus, BorderLayout.SOUTH);

        // --- EVENT LISTENERS ---
        btnMulai.addActionListener((ActionEvent e) -> jalankanProses());
        btnReset.addActionListener((ActionEvent e) -> resetUI());
    }

    private void jalankanProses() {
        if (!chkPersegiPanjang.isSelected() && !chkPrisma.isSelected() && !chkLimas.isSelected()) {
            JOptionPane.showMessageDialog(this, "Pilih minimal 1 bangun ruang/datar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int jumlahData = Integer.parseInt(txtJumlahData.getText().trim());
            if (jumlahData <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah data harus > 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kunci UI
            btnMulai.setEnabled(false);
            tableModel.setRowCount(0);
            lblStatus.setText(" Memproses " + jumlahData + " data...");
            mainProgressBar.setValue(0);
            mainProgressBar.setString("Membangkitkan data acak...");

            // Jalankan di Background Thread agar GUI tidak freeze
            new Thread(() -> {
                long startTime = System.currentTimeMillis();

                // 1. Generate Data
                double[] p = new double[jumlahData];
                double[] l = new double[jumlahData];
                double[] t = new double[jumlahData];
                
                Random rand = new Random();
                for (int i = 0; i < jumlahData; i++) {
                    p[i] = 1 + (49 * rand.nextDouble());
                    l[i] = 1 + (49 * rand.nextDouble());
                    t[i] = 1 + (49 * rand.nextDouble());
                }

                DataShared sharedData = new DataShared(jumlahData);

                // 2. Inisialisasi Thread 
                ThreadPersegi thread1 = new ThreadPersegi(p, l, sharedData, tableModel, mainProgressBar);
                ThreadPrisma thread2 = new ThreadPrisma(p, l, t, thread1, sharedData, tableModel, mainProgressBar);
                ThreadLimas thread3 = new ThreadLimas(p, l, t, thread1, sharedData, tableModel, mainProgressBar);

                // 3. Menjalankan Thread 
                thread1.start();
                
                if (chkPrisma.isSelected()) thread2.start();
                if (chkLimas.isSelected()) thread3.start();

                // 4. Tunggu semua proses selesai
                try {
                    thread1.join();
                    if (chkPrisma.isSelected()) thread2.join();
                    if (chkLimas.isSelected()) thread3.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;

                // 5. Update UI setelah selesai
                SwingUtilities.invokeLater(() -> {
                    lblWaktuTotal.setText("Waktu: " + totalTime + " ms");
                    mainProgressBar.setValue(100);
                    mainProgressBar.setString("Selesai!");
                    lblStatus.setText(" Selesai memproses data.");
                    
                    int totalRow = tableModel.getRowCount();
                    int cPersegi = chkPersegiPanjang.isSelected() ? jumlahData : 0;
                    int cPrisma = chkPrisma.isSelected() ? jumlahData : 0;
                    int cLimas = chkLimas.isSelected() ? jumlahData : 0;
                    
                    txtStatistik.setText("Total baris\t: " + totalRow + 
                                         "\nPersegi Panjang\t: " + cPersegi + 
                                         "\nPrisma\t\t: " + cPrisma + 
                                         "\nLimas\t\t: " + cLimas);
                    
                    btnMulai.setEnabled(true);
                });

            }).start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah data harus berupa angka bulat!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetUI() {
        tableModel.setRowCount(0);
        mainProgressBar.setValue(0);
        mainProgressBar.setString("Belum dimulai");
        lblWaktuTotal.setText("Waktu: -- ms");
        lblStatus.setText(" Siap. Pilih bangun, atur jumlah data, lalu klik Mulai.");
        txtStatistik.setText("Total baris\t: -\nPersegi Panjang\t: -\nPrisma\t\t: -\nLimas\t\t: -");
    }
}
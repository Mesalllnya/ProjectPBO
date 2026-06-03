import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class MainGUI extends JFrame {

    public JCheckBox chkPersegi, chkPrisma, chkLimas;
    public JTextField txtJumlahData, txtPanjang, txtLebar, txtTinggi;
    public JButton btnMulai, btnReset;
    public JProgressBar progPersegi, progLimas, progPrisma;
    public JTextArea txtStatistik;
    public DefaultTableModel tableModel;
    public JTable tableHasil;
    public JLabel lblWaktu;

    public int totalBaris = 0;
    public int countPersegi = 0, countPrisma = 0, countLimas = 0;
    public long startTime;
    
    // Array sebagai "Keranjang" (Shared Memory) antar thread
    // Menggunakan volatile agar update dari Persegi langsung terbaca oleh Prisma/Limas
    public volatile double[][] sharedBaseData;

    public MainGUI() {
        setTitle("Perhitungan Geometri (Multithreading)");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(80, 50, 40));
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        JLabel title1 = new JLabel("Perhitungan Geometri");
        title1.setFont(new Font("Arial", Font.BOLD, 24));
        title1.setForeground(Color.WHITE);
        JLabel title2 = new JLabel("Generate & hitung bangun geometri menggunakan multi-threading");
        title2.setForeground(Color.WHITE);
        panelHeader.add(title1);
        panelHeader.add(title2);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new GridLayout(1, 4, 10, 10));
        panelTengah.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelPilih = new JPanel(new GridLayout(3, 1));
        panelPilih.setBorder(BorderFactory.createTitledBorder("Pilih Bangun"));
        chkPersegi = new JCheckBox("Persegi Panjang", true);
        chkPrisma = new JCheckBox("Prisma Segi Empat", true);
        chkLimas = new JCheckBox("Limas Segi Empat", true);
        panelPilih.add(chkPersegi);
        panelPilih.add(chkPrisma);
        panelPilih.add(chkLimas);

        JPanel panelKonfig = new JPanel(new GridLayout(6, 1, 2, 2));
        panelKonfig.setBorder(BorderFactory.createTitledBorder("Konfigurasi Proses"));
        JPanel pnlJumlah = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlJumlah.add(new JLabel("Jml Data: "));
        txtJumlahData = new JTextField("10000", 6);
        pnlJumlah.add(txtJumlahData);
        JPanel pnlDimensi = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pnlDimensi.add(new JLabel("P:")); txtPanjang = new JTextField(3); pnlDimensi.add(txtPanjang);
        pnlDimensi.add(new JLabel("L:")); txtLebar = new JTextField(3); pnlDimensi.add(txtLebar);
        pnlDimensi.add(new JLabel("T:")); txtTinggi = new JTextField(3); pnlDimensi.add(txtTinggi);
        JLabel lblInfoInput = new JLabel("(Kosongkan P,L,T utk Random)", SwingConstants.CENTER);
        lblInfoInput.setFont(new Font("Arial", Font.ITALIC, 10));
        btnMulai = new JButton("Mulai Proses");
        btnMulai.setBackground(new Color(0, 120, 215));
        btnMulai.setForeground(Color.WHITE);
        btnReset = new JButton("Reset");
        lblWaktu = new JLabel("Waktu: 0 ms", SwingConstants.CENTER);
        lblWaktu.setForeground(Color.RED);
        panelKonfig.add(pnlJumlah);
        panelKonfig.add(pnlDimensi);
        panelKonfig.add(lblInfoInput);
        panelKonfig.add(btnMulai);
        panelKonfig.add(btnReset);
        panelKonfig.add(lblWaktu);

        JPanel panelProgress = new JPanel(new GridLayout(3, 1, 5, 15));
        panelProgress.setBorder(BorderFactory.createTitledBorder("Status Komputasi (Live Race)"));
        progPersegi = buatProgressBar(new Color(200, 70, 60));
        progLimas = buatProgressBar(new Color(220, 180, 50));
        progPrisma = buatProgressBar(new Color(50, 130, 180));
        panelProgress.add(progPersegi);
        panelProgress.add(progLimas);
        panelProgress.add(progPrisma);

        JPanel panelStat = new JPanel(new BorderLayout());
        panelStat.setBorder(BorderFactory.createTitledBorder("Statistik Hasil"));
        txtStatistik = new JTextArea("Total baris : 0\nPersegi Panjang : 0\nPrisma : 0\nLimas : 0");
        txtStatistik.setEditable(false);
        txtStatistik.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelStat.add(txtStatistik, BorderLayout.CENTER);

        panelTengah.add(panelPilih);
        panelTengah.add(panelKonfig);
        panelTengah.add(panelProgress);
        panelTengah.add(panelStat);
        add(panelTengah, BorderLayout.CENTER);

        String[] kolom = {"No", "Nama Bangun", "Parameter", "Luas/LP", "Volume", "Keliling", "Thread"};
        tableModel = new DefaultTableModel(kolom, 0);
        tableHasil = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(tableHasil);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Hasil Generate & Perhitungan"));
        add(scrollTable, BorderLayout.SOUTH);

        btnMulai.addActionListener(e -> mulaiProses());
        btnReset.addActionListener(e -> resetGUI());
    }

    public JProgressBar buatProgressBar(Color warna) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        pb.setForeground(warna);
        pb.setString("Menunggu...");
        return pb;
    }

    public void resetGUI() {
        tableModel.setRowCount(0);
        progPersegi.setValue(0); progPersegi.setString("Menunggu...");
        progPrisma.setValue(0); progPrisma.setString("Menunggu...");
        progLimas.setValue(0); progLimas.setString("Menunggu...");
        totalBaris = 0; countPersegi = 0; countPrisma = 0; countLimas = 0;
        updateStatistik();
        lblWaktu.setText("Waktu: 0 ms");
    }

    public void mulaiProses() {
        try {
            int jumlahData = Integer.parseInt(txtJumlahData.getText());
            
            // Validasi: Persegi Panjang WAJIB dicentang karena dia adalah "Produsen" P dan L
            if (!chkPersegi.isSelected()) {
                if (chkPrisma.isSelected() || chkLimas.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Persegi Panjang WAJIB dicentang karena bangun 3D membutuhkan datanya!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }

            resetGUI();
            startTime = System.currentTimeMillis();

            
            // Hanya menyiapkan keranjang kosong. Array double di Java otomatis berisi 0.0
            sharedBaseData = new double[jumlahData][2];

            // 1. Thread Persegi
            Thread threadPersegi = new Thread(() -> {
                prosesKalkulasiBangun("Persegi Panjang", "Thread Segi4 (2D)", jumlahData, progPersegi);
            });

            // 2. Thread Prisma
            Thread threadPrisma = new Thread(() -> {
                prosesKalkulasiBangun("Prisma Segi Empat", "Thread Prisma (3D)", jumlahData, progPrisma);
            });

            // 3. Thread Limas
            Thread threadLimas = new Thread(() -> {
                prosesKalkulasiBangun("Limas Segi Empat", "Thread Limas (3D)", jumlahData, progLimas);
            });

            // Mulai jalankan thread secara paralel
            threadPersegi.start();
            if (chkPrisma.isSelected()) threadPrisma.start();
            if (chkLimas.isSelected()) threadLimas.start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid pada Jumlah Data!");
        }
    }

    public void prosesKalkulasiBangun(String namaBangun, String namaThread, int batasData, JProgressBar progressBar) {
        Vector<Vector<Object>> batchData = new Vector<>();
        
        String textP = txtPanjang.getText().trim();
        String textL = txtLebar.getText().trim();
        String textT = txtTinggi.getText().trim();
        boolean modeManual = !textP.isEmpty() && !textL.isEmpty();
        
        double pInput = 0, lInput = 0, tInput = 0;
        if (modeManual) {
            pInput = Double.parseDouble(textP);
            lInput = Double.parseDouble(textL);
            if (!textT.isEmpty()) tInput = Double.parseDouble(textT);
        }
        
        for (int i = 1; i <= batasData; i++) {
            double pRun = 0, lRun = 0, tRun = 0;

            // =======================================================
            // LOGIKA GENERATE DATA SAAT OPERASI BERJALAN (ON THE FLY)
            // =======================================================
            if (modeManual) {
                pRun = pInput; lRun = lInput; tRun = tInput;
            } else {
                if (namaBangun.equals("Persegi Panjang")) {
                    // 1. Persegi Panjang bertugas membuat data P dan L secara random
                    PersegiPanjang generator = new PersegiPanjang();
                    pRun = generator.panjang;
                    lRun = generator.lebar;
                    
                    // Memasukkan hasil random ke dalam keranjang (Shared Memory)
                    sharedBaseData[i - 1][0] = pRun;
                    sharedBaseData[i - 1][1] = lRun;
                    
                } else {
                    // 2. Prisma dan Limas bertugas mengambil data.
                    // Jika Persegi Panjang belum membuat data untuk baris ini (masih 0.0), TUNGGU!
                    while (sharedBaseData[i - 1][0] == 0.0) {
                        try {
                            Thread.sleep(1); // Menunggu 1 milidetik lalu cek keranjang lagi
                        } catch (InterruptedException e) {}
                    }
                    
                    // Setelah data tersedia, ambil!
                    pRun = sharedBaseData[i - 1][0];
                    lRun = sharedBaseData[i - 1][1];
                    
                    // Setelah dapat P dan L dari Persegi, baru bangun 3D me-random Tingginya sendiri
                    tRun = (Math.random() * 40) + 5; 
                }
            }
            // =======================================================

            double luas = 0, volume = 0, keliling = 0;
            String paramStr = "";

            if (namaBangun.equals("Persegi Panjang")) {
                PersegiPanjang persegi = new PersegiPanjang(pRun, lRun);
                if (modeManual) { 
                    luas = persegi.menghitungLuas(pRun, lRun);
                    keliling = persegi.menghitungKeliling(pRun, lRun);
                } else { 
                    luas = persegi.menghitungLuas();
                    keliling = persegi.menghitungKeliling();
                }
                paramStr = String.format("P=%.1f, L=%.1f", pRun, lRun);

            } else if (namaBangun.equals("Prisma Segi Empat")) {
                PrismaPersegiPanjang prisma = new PrismaPersegiPanjang(pRun, lRun, tRun);
                if (modeManual) {
                    luas = prisma.menghitungLuasPermukaan(pRun, lRun, tRun);
                    keliling = prisma.menghitungKeliling(pRun, lRun); 
                    volume = prisma.menghitungVolume(pRun, lRun, tRun);
                } else {
                    luas = prisma.menghitungLuasPermukaan();
                    keliling = prisma.menghitungKeliling();
                    volume = prisma.menghitungVolume();
                }
                paramStr = String.format("P=%.1f, L=%.1f, T=%.1f", pRun, lRun, tRun);

            } else if (namaBangun.equals("Limas Segi Empat")) {
                LimasPersegiPanjang limas = new LimasPersegiPanjang(pRun, lRun, tRun);
                if (modeManual) {
                    luas = limas.menghitungLuasPermukaan(pRun, lRun, tRun);
                    keliling = limas.menghitungKeliling(pRun, lRun);
                    volume = limas.menghitungVolume(pRun, lRun, tRun);
                } else {
                    luas = limas.menghitungLuasPermukaan();
                    keliling = limas.menghitungKeliling();
                    volume = limas.menghitungVolume();
                }
                paramStr = String.format("P=%.1f, L=%.1f, T=%.1f", pRun, lRun, tRun);
            }

            Vector<Object> baris = new Vector<>();
            baris.add(totalBaris + i); 
            baris.add(namaBangun);
            baris.add(paramStr);
            baris.add(String.format("%.2f", luas));
            baris.add(String.format("%.2f", volume));
            baris.add(String.format("%.2f", keliling));
            baris.add(namaThread);
            batchData.add(baris);

            int persen = (int) (((double) i / batasData) * 100);
            if (i % 500 == 0 || i == batasData) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(persen);
                    progressBar.setString(namaThread + " : " + persen + "%");
                });
            }
        }

        SwingUtilities.invokeLater(() -> {
            for (Vector<Object> row : batchData) {
                tableModel.addRow(row);
            }
            if (namaBangun.equals("Persegi Panjang")) countPersegi += batasData;
            if (namaBangun.equals("Prisma Segi Empat")) countPrisma += batasData;
            if (namaBangun.equals("Limas Segi Empat")) countLimas += batasData;
            totalBaris += batasData;
            
            updateStatistik();
            cekWaktuSelesai();
        });
    }

    public synchronized void updateStatistik() {
        txtStatistik.setText(String.format("Total baris : %d\nPersegi Panjang : %d\nPrisma : %d\nLimas : %d",
                totalBaris, countPersegi, countPrisma, countLimas));
    }

    public synchronized void cekWaktuSelesai() {
        long timeTaken = System.currentTimeMillis() - startTime;
        lblWaktu.setText("Waktu: " + timeTaken + " ms");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}
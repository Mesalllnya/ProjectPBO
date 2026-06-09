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
            // 1. Ambil input jumlah data
            int jumlahData = Integer.parseInt(txtJumlahData.getText());
            
            // Mengecek Jumlah data tidak boleh 0 atau kurang dari 0
            if (jumlahData <= 0) {
                throw new Exception("Jumlah data harus lebih besar dari 0!");
            }

            // Mengecek Minimal harus ada satu checkbox yang dicentang
            if (!chkPersegi.isSelected() && !chkPrisma.isSelected() && !chkLimas.isSelected()) {
                throw new Exception("Anda harus mencentang minimal satu pilihan bangun geometri!");
            }
            
            // Mengecek Persegi Panjang WAJIB dicentang jika 3D dicentang
            if (!chkPersegi.isSelected()) {
                if (chkPrisma.isSelected() || chkLimas.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Persegi Panjang WAJIB dicentang karena bangun 3D membutuhkan datanya!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }

            // Mengecek Input manual dimensi tidak boleh 0 atau negatif
            String textP = txtPanjang.getText().trim();
            String textL = txtLebar.getText().trim();
            String textT = txtTinggi.getText().trim();

            if (!textP.isEmpty()) {
                double p = Double.parseDouble(textP);
                if (p <= 0) throw new Exception("Nilai Panjang harus lebih besar dari 0!");
            }
            if (!textL.isEmpty()) {
                double l = Double.parseDouble(textL);
                if (l <= 0) throw new Exception("Nilai Lebar harus lebih besar dari 0!");
            }
            // Validasi tinggi HANYA berjalan jika kolom tidak kosong DAN user memilih Prisma atau Limas (Bangun 3D)
            if (!textT.isEmpty() && (chkPrisma.isSelected() || chkLimas.isSelected())) {
                double t = Double.parseDouble(textT);
                if (t <= 0) throw new Exception("Nilai Tinggi harus lebih besar dari 0!");
            }

            // Jika lolos semua validasi, mulai reset dan jalankan program
            resetGUI();
            startTime = System.currentTimeMillis();
            
            // Menyiapkan keranjang kosong
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
            // Error ini terpanggil jika text field diisi huruf atau karakter non-angka
            JOptionPane.showMessageDialog(this, "Pastikan Jumlah Data, Panjang, Lebar, dan Tinggi diisi dengan format angka yang valid!", "Error Format Angka", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Error ini terpanggil dari 'throw new Exception' yang kita buat di atas
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void prosesKalkulasiBangun(String namaBangun, String namaThread, int batasData, JProgressBar progressBar) {
        Vector<Vector<Object>> batchData = new Vector<>();
        
        String textP = txtPanjang.getText().trim();
        String textL = txtLebar.getText().trim();
        String textT = txtTinggi.getText().trim();
        
        // Cek input masing-masing secara MANDIRI (tidak lagi digabung menjadi satu mode)
        boolean isPManual = !textP.isEmpty();
        boolean isLManual = !textL.isEmpty();
        boolean isTManual = !textT.isEmpty();
        
        // Parsing nilai jika diisi, biarkan 0 jika kosong
        double pInput = isPManual ? Double.parseDouble(textP) : 0;
        double lInput = isLManual ? Double.parseDouble(textL) : 0;
        double tInput = isTManual ? Double.parseDouble(textT) : 0;
        
        for (int i = 1; i <= batasData; i++) {
            double pRun = 0, lRun = 0, tRun = 0;

            // Pakai inputan user JIKA ADA, generate otomatis JIKA KOSONG
            if (namaBangun.equals("Persegi Panjang")) {
                pRun = isPManual ? pInput : (Math.random() * 40) + 5;
                lRun = isLManual ? lInput : (Math.random() * 40) + 5;
                
                // Masukkan hasil ke keranjang (Shared Memory)
                sharedBaseData[i - 1][0] = pRun;
                sharedBaseData[i - 1][1] = lRun;
                
            } else {
                // Prisma dan Limas (3D) menunggu P dan L dari keranjang Persegi Panjang
                while (sharedBaseData[i - 1][0] == 0.0) {
                    try { Thread.sleep(1); } catch (InterruptedException e) {}
                }
                pRun = sharedBaseData[i - 1][0];
                lRun = sharedBaseData[i - 1][1];
                
                // Tinggi (T) ditentukan khusus untuk 3D. Pakai input jika ada, otomatis jika kosong.
                tRun = isTManual ? tInput : (Math.random() * 40) + 5; 
            }

            double luas = 0, volume = 0, keliling = 0;
            String paramStr = "";

            switch (namaBangun) {
                case "Persegi Panjang":
                    PersegiPanjang persegi = new PersegiPanjang(pRun, lRun);
                    luas = persegi.menghitungLuas(pRun, lRun);
                    keliling = persegi.menghitungKeliling(pRun, lRun);
                    paramStr = String.format("P=%.1f, L=%.1f", pRun, lRun);
                    break;
                case "Prisma Segi Empat":
                    PrismaPersegiPanjang prisma = new PrismaPersegiPanjang(pRun, lRun, tRun);
                    luas = prisma.menghitungLuasPermukaan(pRun, lRun, tRun);
                    keliling = prisma.menghitungKeliling(pRun, lRun);
                    volume = prisma.menghitungVolume(pRun, lRun, tRun);
                    paramStr = String.format("P=%.1f, L=%.1f, T=%.1f", pRun, lRun, tRun);
                    break;
                case "Limas Segi Empat":
                    LimasPersegiPanjang limas = new LimasPersegiPanjang(pRun, lRun, tRun);
                    luas = limas.menghitungLuasPermukaan(pRun, lRun, tRun);
                    keliling = limas.menghitungKeliling(pRun, lRun);
                    volume = limas.menghitungVolume(pRun, lRun, tRun);
                    paramStr = String.format("P=%.1f, L=%.1f, T=%.1f", pRun, lRun, tRun);
                    break;
                default:
                    break;
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
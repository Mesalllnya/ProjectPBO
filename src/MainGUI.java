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
//    public volatile double[][] sharedBaseData;

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
        pnlJumlah.add(new JLabel("Jumlah Data: "));
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

        btnMulai.addActionListener(e -> {
            try {
                mulaiProses();
            } catch (GeometriInvalidException ex) {
                System.getLogger(MainGUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        
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

    public void mulaiProses() throws GeometriInvalidException{
        try {
            int jumlahData = Integer.parseInt(txtJumlahData.getText());
            if (jumlahData <= 0) throw new GeometriInvalidException("Jumlah data harus lebih besar dari 0!");
            if (!chkPersegi.isSelected() && !chkPrisma.isSelected() && !chkLimas.isSelected()) {
                throw new GeometriInvalidException("Minimal centang satu bangun!");
            }
            if (!chkPersegi.isSelected() && (chkPrisma.isSelected() || chkLimas.isSelected())) {
                JOptionPane.showMessageDialog(this, "Persegi Panjang WAJIB dicentang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Ambil Input
            String textP = txtPanjang.getText().trim();
            String textL = txtLebar.getText().trim();
            String textT = txtTinggi.getText().trim();
            
            boolean isPManual = !textP.isEmpty();
            boolean isLManual = !textL.isEmpty();
            boolean isTManual = !textT.isEmpty();
            
            double pInput = isPManual ? Double.parseDouble(textP) : 0;
            double lInput = isLManual ? Double.parseDouble(textL) : 0;
            // Validasi Input
            if (isPManual && pInput <= 0) throw new GeometriInvalidException("P harus > 0");
            if (isLManual && lInput <= 0) throw new GeometriInvalidException("L harus > 0");
        
            double tInput = isTManual ? Double.parseDouble(textT) : 0;
            if (!textT.isEmpty() && (chkPrisma.isSelected() || chkLimas.isSelected())) {
                if (Double.parseDouble(textT) <= 0){ 
                    GeometriInvalidException invalid = new GeometriInvalidException("Tinggi harus > 0");
                    throw invalid;
                }
            }
            resetGUI();
            startTime = System.currentTimeMillis();

            double[][] dataAlas = new double[jumlahData][4]; 
            for (int i = 0; i < jumlahData; i++) {
                double p = isPManual ? pInput : (Math.random() * 40) + 5;
                double l = isLManual ? lInput : (Math.random() * 40) + 5;

                // Logika pembuatan T dilakukan di sini, SATU KALI SAJA di MainGUI
                double t = isTManual ? tInput : (Math.random() * 40) + 5; 

                dataAlas[i][0] = p; // Slot Panjang
                dataAlas[i][1] = l; // Slot Lebar
                dataAlas[i][2] = p * l; // Slot Luas Alas
                dataAlas[i][3] = t; // Slot Tinggi
            }
            
            // Menggunakan Polimorphism:
            PersegiPanjang persegi = new PersegiPanjang(this, jumlahData, "Thread Segi4 (2D)", progPersegi, dataAlas);
            PersegiPanjang prisma = new PrismaPersegiPanjang(this, jumlahData, "Thread Prisma (3D)", progPrisma, dataAlas);
            PersegiPanjang limas = new LimasPersegiPanjang(this, jumlahData, "Thread Limas (3D)", progLimas, dataAlas);
            
//            PersegiPanjang m = new PrismaPersegiPanjang();
//            double k = prisma.menghitungLuas();
            
            // Bisa langsung dimasukkan karena PersegiPanjang sudah 'implements Runnable'
            Thread threadPersegi = new Thread(persegi);
            Thread threadPrisma = new Thread(prisma);
            Thread threadLimas = new Thread(limas);

            // 3. Mulai balapan!
            threadPersegi.start();
            if (chkPrisma.isSelected()) threadPrisma.start();
            if (chkLimas.isSelected()) threadLimas.start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pastikan input angka valid!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
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
package models;

import java.util.concurrent.LinkedBlockingQueue;

public class DataShared {
    // Antrean khusus untuk mengirim data dari Thread 1 ke Thread 2 & 3
    public LinkedBlockingQueue<Double> antreanLuasUntukPrisma;
    public LinkedBlockingQueue<Double> antreanLuasUntukLimas;
    public LinkedBlockingQueue<Double> antreanKelilingUntukPrisma;
    public LinkedBlockingQueue<Double> antreanKelilingUntukLimas;

    public DataShared(int size) {
        // Kapasitas antrean disesuaikan dengan jumlah data
        antreanLuasUntukPrisma = new LinkedBlockingQueue<>(size);
        antreanLuasUntukLimas = new LinkedBlockingQueue<>(size);
        antreanKelilingUntukPrisma = new LinkedBlockingQueue<>(size);
        antreanKelilingUntukLimas = new LinkedBlockingQueue<>(size);
    }
}
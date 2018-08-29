package id.notation.kosanku.models.kamarkos;

public class Kamarkos {

    int id_kamar;
    int id_indekos;
    int no_kamar;
    int lantai_ke;
    int harga;
    String fasilitas;
    int kwh;
    int status;
    int ukuran;

    public Kamarkos(int id_indekos, int no_kamar, int lantai_ke, int harga, String fasilitas, int kwh, int status) {
        this.id_indekos = id_indekos;
        this.no_kamar = no_kamar;
        this.lantai_ke = lantai_ke;
        this.harga = harga;
        this.fasilitas = fasilitas;
        this.kwh = kwh;
        this.status = status;
    }

    public int getId_kamar() {
        return id_kamar;
    }

    public void setId_kamar(int id_kamar) {
        this.id_kamar = id_kamar;
    }

    public int getId_indekos() {
        return id_indekos;
    }

    public void setId_indekos(int id_indekos) {
        this.id_indekos = id_indekos;
    }

    public int getNo_kamar() {
        return no_kamar;
    }

    public void setNo_kamar(int no_kamar) {
        this.no_kamar = no_kamar;
    }

    public int getLantai_ke() {
        return lantai_ke;
    }

    public void setLantai_ke(int lantai_ke) {
        this.lantai_ke = lantai_ke;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public int getKwh() {
        return kwh;
    }

    public void setKwh(int kwh) {
        this.kwh = kwh;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUkuran() {
        return ukuran;
    }
}

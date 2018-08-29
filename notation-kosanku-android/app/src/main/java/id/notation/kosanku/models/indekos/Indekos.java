package id.notation.kosanku.models.indekos;

public class Indekos {

    private Integer id_indekos;
    private Integer id_user;
    private String nama;
    private String alamat;
    private String kota;
    private String fasilitas_umum;
    private String foto;
    private String gender;
    private String created_at;
    private String updated_at;

    public Indekos() {
    }

    public Indekos(String nama, String alamat, String kota, String fasilitas_umum, String gender) {
        this.nama = nama;
        this.alamat = alamat;
        this.kota = kota;
        this.fasilitas_umum = fasilitas_umum;
        this.gender = gender;
    }

    public Integer getId_indekos() {
        return id_indekos;
    }

    public void setId_indekos(Integer id_indekos) {
        this.id_indekos = id_indekos;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getFasilitas_umum() {
        return fasilitas_umum;
    }

    public void setFasilitas_umum(String fasilitas_umum) {
        this.fasilitas_umum = fasilitas_umum;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() { return id_indekos; }

    public String toString() { return nama; }
}

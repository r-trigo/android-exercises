package pt.ipg.seminarios;

/**
 * Created by RT on 11/11/2016.
 */

public class Seminario {
    private int id;
    private String titulo;
    private int idOrador;
    private String sumario;

    public Seminario(int id, String titulo, int idOrador, String sumario) {
        this.id = id;
        this.titulo = titulo;
        this.idOrador = idOrador;
        this.sumario = sumario;
    }

    public Seminario(String titulo, int idOrador, String sumario) {
        this(-1, titulo, idOrador, sumario);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdOrador() {
        return idOrador;
    }

    public void setIdOrador(int idOrador) {
        this.idOrador = idOrador;
    }

    public String getSumario() {
        return sumario;
    }

    public void setSumario(String sumario) {
        this.sumario = sumario;
    }

    public String getStringId() {
        return String.valueOf(id);
    }
}

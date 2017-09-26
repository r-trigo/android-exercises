package pt.ipg.seminarios;

/**
 * Created by RT on 11/11/2016.
 */

public class Orador {
    private int id;
    private String nome;

    public Orador(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Orador(String nome) {
        this(-1, nome);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStringId() {
        return Integer.toString(id);
        //return String.valueOf(id);
    }
}

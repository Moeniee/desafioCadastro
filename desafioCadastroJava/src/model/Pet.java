package model;

public class Pet {
    private String nomeCompleto;
    private TipoPet tipo;
    private SexoPet sexo;
    private String endereco;
    private Double idade;
    private Double peso;
    private String raca;

    public Pet(String nomeCompleto, TipoPet tipo, SexoPet sexo, String endereco, Double idade, Double peso, String raca) {
        this.nomeCompleto = nomeCompleto;
        this.tipo = tipo;
        this.sexo = sexo;
        this.endereco = endereco;
        this.idade = idade;
        this.peso = peso;
        this.raca = raca;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public TipoPet getTipo() {
        return tipo;
    }

    public SexoPet getSexo() {
        return sexo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getIdade() {
        return idade;
    }

    public void setIdade(Double idade) {
        this.idade = idade;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
}

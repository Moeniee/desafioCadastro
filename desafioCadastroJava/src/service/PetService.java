package service;

import exception.NomeInvalidoException;
import exception.SexoInvalidoException;
import exception.TipoInvalidoException;
import model.Constantes;
import model.Endereco;
import model.SexoPet;
import model.TipoPet;

import java.util.Scanner;

public class PetService {

    private Scanner sc = new Scanner(System.in);

    public String lerNomeCompleto(String pergunta){
        System.out.println(pergunta);
        String resposta = sc.nextLine().trim();

        if (resposta.isEmpty()){
            throw new NomeInvalidoException("O nome completo é obrigatório e não pode ficar em branco");
        }

        if (!resposta.matches("[a-zA-ZÀ-ÿ ]+")){
            throw new NomeInvalidoException("O nome deve conter apenas letras.");
        }

        return resposta;
    }

    public TipoPet lerTipo(String pergunta){
        System.out.println(pergunta);
        String resposta = sc.nextLine().trim();

        if(resposta.equalsIgnoreCase("Cachorro")){
            return TipoPet.CACHORRO;
        } else if (resposta.equalsIgnoreCase("Gato")) {
            return TipoPet.GATO;
        }else {
            throw new TipoInvalidoException("Tipo invalido! Digite 'Cachorro' ou 'Gato'.");
        }
    }
    
    public SexoPet lerSexo(String pergunta){
        System.out.println(pergunta);
        String resposta = sc.nextLine().trim();
        
        if(resposta.equalsIgnoreCase("Macho")){
            return SexoPet.MACHO;
        } else if (resposta.equalsIgnoreCase("Femea") || resposta.equalsIgnoreCase("Fêmea")) {
            return SexoPet.FEMEA;
        }else {
            throw new SexoInvalidoException("Sexo invalido! Digite 'Macho' ou 'Fêmea'.");
        }
    }

    public Endereco lerEndereco(){
        System.out.println("Qual o numero da casa?");
        String numero = sc.nextLine().trim();
        if (numero.isEmpty()){
            numero = Constantes.NAO_INFORMADO;
        }

        System.out.println("Qual a cidade?");
        String cidade = sc.nextLine().trim();

        System.out.println("Qual a rua?");
        String rua = sc.nextLine().trim();

        return new Endereco(numero, cidade, rua);
    }
}

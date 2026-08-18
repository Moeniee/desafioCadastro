package service;

import exception.NomeInvalidoException;

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
}

package service;

import exception.PerguntaInvalidaException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FormularioService {

    private static final Path ARQUIVO = Path.of("formulario.txt");
    private static final int QUANTIDADE_PERGUNTAS_ORIGINAIS = 7;

    private final Scanner sc;

    public FormularioService(Scanner sc) {
        this.sc = sc;
    }

    public List<String> listarPerguntas() {
        try {
            return new ArrayList<>(Files.readAllLines(ARQUIVO));
        }catch (IOException e){
            System.out.println("Erro ao ler formulario.txt: "  + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarPergunta(List<String> perguntas) {
        try {
            Files.write(ARQUIVO, perguntas);
        }catch (IOException e){
            System.out.println("Erro ao salvar formulario.txt: "  + e.getMessage());
        }
    }

    public void criarPergunta(){
        List<String> perguntas = listarPerguntas();

        System.out.print("Digite a nova pergunta: ");
        String textoPergunta = sc.nextLine().trim();

        if(textoPergunta.isEmpty()){
            throw new PerguntaInvalidaException("A pergunta nao pode ficar em branco.");
        }

        int proximoNumero = perguntas.size() + 1;
        perguntas.add(proximoNumero + " - " + textoPergunta);

        salvarPergunta(perguntas);
        System.out.print("Pergunta " + proximoNumero + " salvo com sucesso!");
    }
}

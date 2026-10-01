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

    private void salvarPerguntas(List<String> perguntas) {
        try {
            Files.write(ARQUIVO, perguntas);
        }catch (IOException e){
            System.out.println("Erro ao salvar formulario.txt: "  + e.getMessage());
        }
    }

    public void criarPergunta(){
        List<String> perguntas = listarPerguntas();

        System.out.println("Digite a nova pergunta: ");
        String textoPergunta = sc.nextLine().trim();

        if(textoPergunta.isEmpty()){
            throw new PerguntaInvalidaException("A pergunta nao pode ficar em branco.");
        }

        int proximoNumero = perguntas.size() + 1;
        perguntas.add(proximoNumero + " - " + textoPergunta);

        salvarPerguntas(perguntas);
        System.out.print("Pergunta " + proximoNumero + " salvo com sucesso!");
    }

    public void alterarPergunta(){
        List<String> perguntas = listarPerguntas();

        if(perguntas.size() <= QUANTIDADE_PERGUNTAS_ORIGINAIS){
            System.out.println("Nao ha perguntas extras cadastradas para alterar.");
            return;
        }

        exibirPerguntasExtras(perguntas);
        System.out.println("Digite o numero da pergunta que deseja alterar:");
        String entrada = sc.nextLine().trim();

        if(!entrada.matches("\\d+")){
            System.out.println("Numero invalido!");
            return;
        }

        int numero = Integer.parseInt(entrada);

        if (numero <= QUANTIDADE_PERGUNTAS_ORIGINAIS || numero > perguntas.size()){
            System.out.println("Voce so pode alterar perguntas extras (numero maior que " + QUANTIDADE_PERGUNTAS_ORIGINAIS);
            return;
        }

        System.out.print("Digite o novo texto da pergunta: ");
        String novoTexto = sc.nextLine().trim();

        if(novoTexto.isEmpty()){
            throw new PerguntaInvalidaException("A pergunta nao pode ficar em branco.");
        }

        perguntas.set(numero - 1, numero + " - " + novoTexto);
        salvarPerguntas(perguntas);
    }

    public void excluirPergunta(){
        List<String> perguntas = listarPerguntas();

        if(perguntas.size() <= QUANTIDADE_PERGUNTAS_ORIGINAIS){
            System.out.println("Nao ha perguntas extras cadastradas para excluir.");
            return;
        }

        exibirPerguntasExtras(perguntas);
        System.out.println("Digite o numero da pergunta que deseja excluir: ");
        String entrada = sc.nextLine().trim();

        if(!entrada.matches("\\d+")){
            System.out.println("Numero invalido!");
            return;
        }

        int numero = Integer.parseInt(entrada);

        if (numero <= QUANTIDADE_PERGUNTAS_ORIGINAIS || numero > perguntas.size()){
            System.out.println("Voce so pode excluir perguntas extras (numero maior que " + QUANTIDADE_PERGUNTAS_ORIGINAIS);
            return;
        }

        System.out.print("Confirma a exclusao da pergunta " + numero + "? (SIM/NAO)");
        String confirmacao = sc.nextLine().trim();

        if(confirmacao.equalsIgnoreCase("SIM")){
            System.out.println("Exclusao cancelada.");
            return;
        }

        perguntas.remove(numero - 1);
        renumerar(perguntas);
        salvarPerguntas(perguntas);
        System.out.println("Pergunta excluida com sucesso!");
    }

    private void exibirPerguntasExtras(List<String> perguntas){
        System.out.print("Perguntas extras cadastradas:");
        for (int i = QUANTIDADE_PERGUNTAS_ORIGINAIS; i < perguntas.size(); i++){
            System.out.println(perguntas.get(i));
        }
    }

    private void renumerar(List<String> perguntas){
        for (int i = 0; i < perguntas.size(); i++){
            String textoSemNumero = perguntas.get(i).substring(perguntas.get(i).indexOf(" - ") + 3);
            perguntas.set(i, (i + 1) + " - " + textoSemNumero);
        }
    }
}

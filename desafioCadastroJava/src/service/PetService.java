package service;

import exception.*;
import model.*;
import repository.PetRepository;
import util.FormatadorUtil;
import util.TextoUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PetService {

    private final Scanner sc;
    private final PetRepository repository;

    public PetService(Scanner sc,  PetRepository repository) {
        this.sc = sc;
        this.repository = repository;
    }

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

    public double lerPeso(String pergunta){
        System.out.println(pergunta);
        String resposta = sc.nextLine().trim();

        if(resposta.isEmpty()){
            return Constantes.PESO_NAO_INFORMADO;
        }

        resposta = resposta.replace(",", ".");

        if(!resposta.matches("\\d+(\\.\\d+)?")){
            throw new PesoInvalidoException("Peso invalido! Digite apenas numeros.");
        }

        double peso = Double.parseDouble(resposta);

        if(peso < 0.5 || peso > 60){
            throw new PesoInvalidoException("Peso deve estar entre 0.5kg e 60kg.");
        }

        return peso;
    }

    public double lerIdade(String pergunta){
        System.out.println(pergunta + " (se for menor de 1 ano, digite em meses, ex: '6 meses')");
        String resposta = sc.nextLine().trim();

        if (resposta.isEmpty()){
            return Constantes.IDADE_NAO_INFORMADA;
        }

        resposta = resposta.replace(",", ".");

        boolean emMeses = resposta.toLowerCase().endsWith("meses");

        String parteNumerica = emMeses
                ? resposta.toLowerCase().replace("meses", "").trim()
                : resposta;

        if (!parteNumerica.matches("\\d+(\\.\\d+)?")){
            throw new IdadeInvalidaException("Idade invalida! Digite apenas numeros (ou 'X meses').");
        }

        double valor = Double.parseDouble(parteNumerica);
        double idadeEmAnos = emMeses ? valor/12.0 : valor;

        if (idadeEmAnos > 20){
            throw new IdadeInvalidaException("Idade nao pode ser maior que 20 anos.");
        }

        return idadeEmAnos;
    }

    public String lerRaca(String pergunta){
        System.out.println(pergunta);
        String resposta = sc.nextLine().trim();

        if(resposta.isEmpty()){
            return Constantes.NAO_INFORMADO;
        }

        if(!resposta.matches("[a-zA-ZÀ-ÿ ]+")){
            throw new RacaInvalidaException("A raça não pode conter números ou caracteres especiais.");
        }
        return resposta;
    }

    public Pet cadastrarPet(){
        List<String> perguntas;
        try {
            perguntas = Files.readAllLines(Paths.get("formulario.txt"));
        }catch (IOException e){
            System.out.println("Erro ao ler formulario.txt: " + e.getMessage());
            return null;
        }

        String nome = tentarNovamente(() -> lerNomeCompleto(perguntas.get(0)));
        TipoPet tipo = tentarNovamente(() -> lerTipo(perguntas.get(1)));
        SexoPet sexo = tentarNovamente(() -> lerSexo(perguntas.get(2)));
        Endereco endereco = lerEndereco();
        double idade = tentarNovamente(() -> lerIdade(perguntas.get(4)));
        double peso = tentarNovamente(() -> lerPeso(perguntas.get(5)));
        String raca = tentarNovamente(() -> lerRaca(perguntas.get(6)));

        Pet pet = new Pet(nome, tipo, sexo, endereco, idade, peso, raca);
        repository.salvar(pet);
        System.out.println("Pet cadastrado com sucesso!");
        return pet;
    }

    private <T> T tentarNovamente(java.util.function.Supplier<T> leitor) {
        while (true) {
            try {
                return leitor.get();
            }catch (RuntimeException e){
                System.out.println(e.getMessage() + "Tente novamente.");
            }
        }
    }

    private String gerarNomeArquivo(Pet pet) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        String dataHora = agora.format(dtf);

        String nomeSemEscapacos = pet.getNomeCompleto()
                .toUpperCase()
                .replace(" ", "");

        return dataHora + "-" + nomeSemEscapacos + ".TXT";
    }

    public void salvarPet(Pet pet){
        try {
            Path pasta = Path.of("petsCadastrados");
            if(!Files.exists(pasta)){
                Files.createDirectory(pasta);
            }

            String nomeArquivo = gerarNomeArquivo(pet);
            Path caminhoArquivo = pasta.resolve(nomeArquivo);

            List<String> linhas = List.of(
                    "1 - " + pet.getNomeCompleto(),
                    "2 - " + formatarTipo(pet.getTipo()),
                    "3 - " + formatarSexo(pet.getSexo()),
                    "4 - " + pet.getEndereco().toString(),
                    "5 - " + formatarIdade(pet.getIdade()),
                    "6 - " + formatarPeso(pet.getPeso()),
                    "7 - " + pet.getRaca()
            );

            Files.write(caminhoArquivo, linhas, StandardOpenOption.CREATE);

            System.out.println("Pet salvo em: " + caminhoArquivo.toAbsolutePath());
        }catch (IOException e){
            System.out.println("Erro ao salvar pet: " + e.getMessage());
        }
    }

    private String formatarTipo(TipoPet tipo){
        return tipo == TipoPet.CACHORRO ? "Cachorro" : "Gato";
    }

    private String formatarSexo(SexoPet sexo){
        return sexo == SexoPet.MACHO ? "Macho" : "Femea";
    }

    private String formatarIdade(double idade){
        if (idade == Constantes.IDADE_NAO_INFORMADA){
            return Constantes.NAO_INFORMADO;
        }
        if (idade < 1){
            int meses = (int) Math.round(idade * 12);
            return meses + " meses";
        }
        return idade + " anos";
    }

    private String formatarPeso(double peso){
        if (peso == Constantes.PESO_NAO_INFORMADO){
            return Constantes.NAO_INFORMADO;
        }
        return peso + " kg";
    }

    public CriterioBusca montarCriterioBusca(){
        CriterioBusca criterio = new CriterioBusca();

        TipoPet tipo = tentarNovamente(() -> lerTipo("Qual o tipo de pet que voce procura?"));
        criterio.setTipo(tipo);

        exibirMenuCriteriosExtras(criterio);

        return criterio;
    }

    private void exibirMenuCriteriosExtras(CriterioBusca criterio){
        int escolhidos = 0;

        while (escolhidos < 2){
            System.out.println("\nDeseja filtrar por mais algum criterio? (0 - Nao, seguir com a busca)");
            System.out.println("1 - Nome");
            System.out.println("2 - Sexo");
            System.out.println("3 - Idade");
            System.out.println("4 - Peso");
            System.out.println("5 - Raca");
            System.out.println("6 - Endereco");
            System.out.println("Escolha uma opcao: ");

            String entrada = sc.nextLine().trim();

            if (entrada.equals("0")){
                break;
            }

            switch (entrada){
                case "1" -> {
                    criterio.setNome(tentarNovamente(() -> lerTextoLivre("Busca por qual nome (ou parte dele)?")));
                    escolhidos++;
                }
                case "2" -> {
                    criterio.setSexo(tentarNovamente(() -> lerSexo("Qual o sexo?")));
                    escolhidos++;
                }
                case "3" -> {
                    criterio.setIdade(tentarNovamente(() -> lerIdade("Qual a idade?")));
                    escolhidos++;
                }
                case "4" -> {
                    criterio.setPeso(tentarNovamente(() -> lerPeso("Qual o peso?")));
                    escolhidos++;
                }
                case "5" -> {
                    criterio.setRaca(tentarNovamente(() -> lerTextoLivre("Qual a raca?")));
                    escolhidos++;
                }
                case "6" -> {
                    criterio.setEndereco(tentarNovamente(() -> lerTextoLivre("Buscar por qual parte do endereco?")));
                    escolhidos++;
                }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private String lerTextoLivre(String pergunta){
        System.out.println(pergunta);
        return sc.nextLine().trim();
    }

    public List<PetArquivo> buscarPets(CriterioBusca criterio){
        List<PetArquivo> todos = repository.listarTodos();

        return todos.stream()
                .filter(pa -> pa.getPet().getTipo() == criterio.getTipo())
                .filter(pa -> criterio.getNome() == null ||
                        TextoUtil.normalizar(pa.getPet().getNomeCompleto()).contains(TextoUtil.normalizar(criterio.getNome())))
                .filter(pa -> criterio.getSexo() == null || pa.getPet().getSexo() == criterio.getSexo())
                .filter(pa -> criterio.getIdade() == null || pa.getPet().getIdade() == criterio.getIdade())
                .filter(pa -> criterio.getPeso() == null || pa.getPet().getPeso() == criterio.getPeso())
                .filter(pa -> criterio.getRaca() == null ||
                        TextoUtil.normalizar(pa.getPet().getRaca()).contains(TextoUtil.normalizar(criterio.getRaca())))
                .filter(pa -> criterio.getEndereco() == null ||
                        TextoUtil.normalizar(pa.getPet().getEndereco().toString()).contains(TextoUtil.normalizar(criterio.getEndereco())))
                .collect(Collectors.toList());
    }

    public void exibirResultados(List<PetArquivo> resultados){
        if (resultados.isEmpty()){
            System.out.println("Nenhum pet encontrado com esses criterios.");
            return;
        }

        for (int i = 0; i< resultados.size(); i++){
            Pet p = resultados.get(i).getPet();
            System.out.printf("%d. %s - %s - %s - %s - %s - %s%n",
                    i + 1,
                    p.getNomeCompleto(),
                    FormatadorUtil.formatarTipo(p.getTipo()),
                    FormatadorUtil.formatarSexo(p.getSexo()),
                    p.getEndereco().toString(),
                    FormatadorUtil.formatarIdade(p.getIdade()),
                    FormatadorUtil.formatarPeso(p.getPeso()),
                    p.getRaca()
            );
        }
    }

    private PetArquivo escolherPet(String acao){
        while (true){
            CriterioBusca criterio = montarCriterioBusca();
            List<PetArquivo> resultados = buscarPets(criterio);

            if (resultados.isEmpty()){
                System.out.println("Nenhum pet encontrado com esses criterios.");
                return null;
            }

            exibirResultados(resultados);
            System.out.println("Digite o numero do pet que deseja buscar: " + acao + (" (0 para cancelar)"));
            String entrada = sc.nextLine().trim();

            if (entrada.equals("0")){
                return null;
            }

            if (entrada.matches("\\d{1,9}")){
                int numero = Integer.parseInt(entrada);
                if (numero >= 1 && numero <= resultados.size()){
                    return resultados.get(numero - 1);
                }
            }

            System.out.println("Numero invalido! Refazendo busca...");
        }
    }

    public void alterarPet(){
        PetArquivo escolhido = escolherPet("alterar");
        if (escolhido == null){
            return;
        }

        Pet pet = escolhido.getPet();
        boolean alterando = true;

        while (alterando){
            System.out.println("\nO que deseja alterar? (tipo e sexo nao podem ser alterados)");
            System.out.println("1 - Nome");
            System.out.println("2 - Endereco");
            System.out.println("3 - Idade");
            System.out.println("4 - Peso");
            System.out.println("5 - Raca");
            System.out.println("0 - Salvar e voltar ao menu");
            System.out.println("Escolha uma opcao: ");

            String opcao = sc.nextLine().trim();

            switch (opcao) {
                case "1" -> pet.setNomeCompleto(tentarNovamente(() -> lerNomeCompleto("Qual o novo nome e sobrenome?")));
                case "2" -> pet.setEndereco(lerEndereco());
                case "3" -> pet.setIdade(tentarNovamente(() -> lerIdade("Qual a nova idade?")));
                case "4" -> pet.setPeso(tentarNovamente(() -> lerPeso("Qual o novo peso?")));
                case "5" -> pet.setRaca(tentarNovamente(() -> lerRaca("Qual a nova raca?")));
                case "0" -> alterando = false;
                default -> System.out.println("Opcao invalida!");
            }
        }
        repository.atualizar(escolhido, pet);
    }

    public void deletarPet(){
        PetArquivo escolhido = escolherPet("deletar");
        if (escolhido == null){
            return;
        }

        System.out.print("Tem certeza que deseja deletar " + escolhido.getPet().getNomeCompleto() + "? (SIM/NAO)");
        String confirmacao = sc.nextLine().trim();

        if (confirmacao.equalsIgnoreCase("SIM")) {
            repository.deletar(escolhido);
        }else {
            System.out.println("Exclusao cancelada!");
        }
    }
}

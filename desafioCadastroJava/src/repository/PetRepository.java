package repository;

import model.*;
import util.FormatadorUtil;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PetRepository {

    private static final Path PASTA = Path.of("petsCadastrados");

    public void salvar(Pet pet) {
        try {
            if (!Files.exists(PASTA)) {
                Files.createDirectory(PASTA);
            }

            String nomeArquivo = gerarNomeArquivo(pet);
            Path caminhoArquivo = PASTA.resolve(nomeArquivo);

            List<String> linhas = List.of(
                    "1 - " + pet.getNomeCompleto(),
                    "2 - " + FormatadorUtil.formatarTipo(pet.getTipo()),
                    "3 - " + FormatadorUtil.formatarSexo(pet.getSexo()),
                    "4 - " + pet.getEndereco().toString(),
                    "5 - " + FormatadorUtil.formatarIdade(pet.getIdade()),
                    "6 - " + FormatadorUtil.formatarPeso(pet.getPeso()),
                    "7 - " + pet.getRaca()
            );

            Files.write(caminhoArquivo, linhas, StandardOpenOption.CREATE);
            System.out.println("Pet salvo em: " + caminhoArquivo.toAbsolutePath());
        }catch (Exception e) {
            System.out.println("Erro ao salvar o pet: " + e.getMessage());
        }
    }

    public List<Pet> listarTodos() {
        List<Pet> pets = new ArrayList<>();

        if (!Files.exists(PASTA)) {
            return pets;
        }

        try(DirectoryStream<Path> arquivos = Files.newDirectoryStream(PASTA, "*.TXT")){
            for (Path arquivo : arquivos) {
                try {
                    Pet pet = lerPetDoArquivo(arquivo);
                    if (pet != null) {
                        pets.add(pet);
                    }
                }catch (RuntimeException e){
                    System.out.println("Arquivo ignorado (formato invalido): " + e.getMessage());
                }
            }
        }catch (Exception e) {
            System.out.println("Erro ao listar os pets: " + e.getMessage());
        }
        return pets;
    }

    public Pet lerPetDoArquivo(Path arquivo){
        try {
            List<String> linhas = Files.readAllLines(arquivo);

            String nome = extrairValor(linhas.get(0));
            TipoPet tipo = extrairValor(linhas.get(1)).equalsIgnoreCase("Cachorro")
                    ? TipoPet.CACHORRO : TipoPet.GATO;
            SexoPet sexo = extrairValor(linhas.get(2)).equalsIgnoreCase("Macho")
                    ? SexoPet.MACHO : SexoPet.FEMEA;
            String enderecoTexto = extrairValor(linhas.get(3));
            double idade = parseIdadeSalva(extrairValor(linhas.get(4)));
            double peso = parsePesoSalvo(extrairValor(linhas.get(5)));
            String raca = extrairValor(linhas.get(6));

            Endereco endereco = parseEndereco(enderecoTexto);

            return new Pet(nome, tipo, sexo,  endereco, idade, peso, raca);
        }catch (Exception e) {
            System.out.println("Erro ao ler arquivo " + arquivo.getFileName() + ": " + e.getMessage());
            return null;
        }
    }

    private String gerarNomeArquivo(Pet pet) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        String dataHora = agora.format(dtf);
        String nomeSemEspacos = pet.getNomeCompleto().toUpperCase().replace(" ", "");
        return dataHora + "-" + nomeSemEspacos + ".TXT";
    }

    private String extrairValor(String linha){
        int indiceHifen = linha.indexOf(" - ");
        return linha.substring(indiceHifen + 3).trim();
    }

    private Endereco parseEndereco(String texto){
        String[] partes = texto.split(",");
        String rua = partes[0].trim();
        String numero = partes.length > 1 ? partes[1].trim() : Constantes.NAO_INFORMADO;
        String cidade = partes.length > 2 ? partes[2].trim() : Constantes.NAO_INFORMADO;
        return new Endereco(rua, numero, cidade);
    }

    private double parseIdadeSalva(String texto){
        if(texto.equals(Constantes.NAO_INFORMADO)){
            return Constantes.IDADE_NAO_INFORMADA;
        }
        if (texto.contains("meses")){
            int meses = Integer.parseInt(texto.replace("meses", "").trim());
            return meses / 12;
        }
        return Double.parseDouble(texto.replace("anos", "").trim());
    }

    private double parsePesoSalvo(String texto){
        if(texto.equals(Constantes.NAO_INFORMADO)){
            return Constantes.PESO_NAO_INFORMADO;
        }
        return Double.parseDouble(texto.replace("kg", "").trim());
    }
}

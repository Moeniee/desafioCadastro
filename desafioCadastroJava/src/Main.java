void main() {
    Scanner sc = new Scanner(System.in);

    try{
        List<String> perguntas = Files.readAllLines(Path.of("formulario.txt"));
        for (String pergunta : perguntas){
            System.out.println(pergunta);
        }
    } catch (IOException e) {
        System.out.println("Error ao ler o arquivo formulario.txt: " + e.getMessage());
    }

    boolean continuar = true;
    while (continuar){
        exibirMenu();
        int opcao = lerOpcao(sc);

        switch (opcao){
            case 1 -> System.out.println("Cadastrar novo pet");
            case 2 -> System.out.println("Alterar pet cadastrado");
            case 3 -> System.out.println("Deletar pet cadastrado");
            case 4 -> System.out.println("Listar todos os pets");
            case 5 -> System.out.println("Listar pets por criterio");
            case 6 -> {
                System.out.println("Encerrando o sistema...");
                continuar = false;
            }
            default -> System.out.println("Opcao invalida! Tente novamente.");
        }
    }
    sc.close();
}
private static void exibirMenu(){
    System.out.println("\n===== MENU =====");
    System.out.println("1 - Cadastrar um novo pet");
    System.out.println("2 - Alterar os dados do pet cadastrado");
    System.out.println("3 - Deletar um pet cadastrado");
    System.out.println("4 - Listar todos os pets cadastrados");
    System.out.println("5 - Listar pets por algum criterio");
    System.out.println("6 - Sair");
    System.out.print("Escolha uma opcao: ");
}

private static int lerOpcao(Scanner sc){
    String entrada = sc.nextLine().trim();

    if(!entrada.matches("\\d+")){
        System.out.println("Entrada invalida! Digite apenas numeros.");
        return -1;
    }

    int opcao = Integer.parseInt(entrada);

    if (opcao <= 0){
        System.out.println("O numero deve ser positivo!");
        return -1;
    }
    return opcao;
}
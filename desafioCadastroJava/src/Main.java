void main() {
    try{
        List<String> perguntas = Files.readAllLines(Path.of("formulario.txt"));
        for (String pergunta : perguntas){
            System.out.println(pergunta);
        }
    } catch (IOException e) {
        System.out.println("Error ao ler o arquivo formulario.txt: " + e.getMessage());
    }
}

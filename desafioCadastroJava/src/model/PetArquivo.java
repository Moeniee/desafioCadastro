package model;

import java.nio.file.Path;

public class PetArquivo {
    private final Pet pet;
    private final Path arquivoOrigem;

    public PetArquivo(Pet pet, Path arquivoOrigem) {
        this.pet = pet;
        this.arquivoOrigem = arquivoOrigem;
    }

    public Pet getPet() {
        return pet;
    }

    public Path getArquivoOrigem() {
        return arquivoOrigem;
    }
}

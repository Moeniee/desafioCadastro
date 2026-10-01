# 🐾 Sistema de Cadastro de Pets

Sistema CLI (interface de linha de comando) desenvolvido em Java para gerenciamento de cadastro de pets em um abrigo de animais. O projeto foi desenvolvido como desafio prático de programação, aplicando conceitos de Orientação a Objetos, manipulação de arquivos e boas práticas de desenvolvimento.

## 📋 Funcionalidades

### Gerenciamento de Pets
- **Cadastro de pets** com validações completas (nome, tipo, sexo, endereço, idade, peso, raça)
- **Busca por múltiplos critérios**, combinando até 2 filtros além do tipo (nome parcial, sexo, idade, peso, raça, endereço)
- **Busca case-insensitive e sem distinção de acentos**
- **Alteração de pets cadastrados** (exceto tipo e sexo, que são imutáveis após o cadastro)
- **Exclusão de pets cadastrados** com confirmação do usuário
- **Listagem de todos os pets** cadastrados

### Formulário Dinâmico
- **Criação de novas perguntas** no formulário de cadastro
- **Alteração de perguntas** previamente criadas pelo usuário
- **Exclusão de perguntas** com renumeração automática
- Perguntas originais (1 a 7) protegidas contra edição/exclusão
- Respostas de perguntas extras salvas junto ao arquivo de cada pet

### Persistência
- Cada pet é salvo em um arquivo `.txt` individual, na pasta `petsCadastrados`
- Nome do arquivo gerado automaticamente com data, hora e nome do pet (ex: `20260926T0936-REX.TXT`)
- Leitura do arquivo `formulario.txt` para exibição dinâmica das perguntas de cadastro

## 🛠️ Tecnologias e Conceitos Aplicados

- **Java puro**, sem frameworks ou bibliotecas externas
- **Orientação a Objetos**: encapsulamento, enums, imutabilidade de atributos sensíveis
- **Exceções customizadas**: validações de nome, tipo, sexo, idade, peso, raça e perguntas do formulário
- **Java IO / NIO**: leitura, escrita, atualização e exclusão de arquivos
- **Stream API**: filtragem de pets por critérios de busca
- **Regex**: validação de campos de texto e campos numéricos
- **Normalização de texto**: busca ignorando acentuação e caixa (maiúsculas/minúsculas)
- **Separação em camadas**: `model`, `service`, `repository`, `exception` e `util`

## 📁 Estrutura do Projeto

```
src/
 ├── Main.java                      # ponto de entrada da aplicação
 ├── model/
 │    ├── Pet.java                  # entidade principal
 │    ├── PetArquivo.java           # associa um Pet ao arquivo de origem
 │    ├── Endereco.java             # endereço do pet (número, cidade, rua)
 │    ├── CriterioBusca.java        # critérios usados na busca de pets
 │    ├── TipoPet.java              # enum: CACHORRO, GATO
 │    ├── SexoPet.java              # enum: MACHO, FEMEA
 │    └── Constantes.java           # constantes do sistema (ex: NÃO INFORMADO)
 ├── service/
 │    ├── PetService.java           # regras de cadastro, busca, alteração e exclusão
 │    └── FormularioService.java    # gerenciamento das perguntas do formulário
 ├── repository/
 │    └── PetRepository.java        # leitura, escrita e exclusão dos arquivos de pets
 ├── exception/
 │    └── (exceções customizadas de validação)
 └── util/
      ├── TextoUtil.java            # normalização de texto (acentos/caixa)
      └── FormatadorUtil.java       # formatação de idade, peso, tipo e sexo

formulario.txt          # perguntas do formulário de cadastro
petsCadastrados/        # pasta onde os pets cadastrados são salvos
```

## ▶️ Como Executar

1. Clone o repositório:
   ```
   git clone <url-do-repositorio>
   ```
2. Abra o projeto no IntelliJ IDEA (ou outra IDE de sua preferência).
3. Confirme que o JDK 17 ou superior está configurado no projeto.
4. Execute a classe `Main`.

> **Importante:** o programa lê o arquivo `formulario.txt` a partir do diretório de trabalho (*working directory*) do projeto. Caso receba um erro de arquivo não encontrado, verifique se a working directory da sua configuração de execução está apontando para a raiz do projeto.

## 🗺️ Navegação do Sistema

```
Menu Inicial
├── 1 - Sistema de cadastro de PETS
│    ├── 1 - Cadastrar um novo pet
│    ├── 2 - Alterar os dados do pet cadastrado
│    ├── 3 - Deletar um pet cadastrado
│    ├── 4 - Listar todos os pets cadastrados
│    ├── 5 - Listar pets por algum critério
│    └── 6 - Voltar ao menu inicial
├── 2 - Sistema de alterar formulário
│    ├── 1 - Criar nova pergunta
│    ├── 2 - Alterar pergunta existente
│    ├── 3 - Excluir pergunta existente
│    ├── 4 - Voltar ao menu inicial
│    └── 5 - Sair
└── 3 - Sair
```

## 👤 Autor

Desenvolvido por Samuel Silva Dos Santos como projeto de portfólio.

- GitHub: https://github.com/Moeniee
- LinkedIn: https://www.linkedin.com/in/samuel-santos-989796229/
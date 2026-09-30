#language:pt

@LivroTeste
Funcionalidade: Comportamento de um livro isolado
  Um livro controla o proprio estado de disponibilidade.

  Cenario: Um livro recem criado esta disponivel
    Dado um livro de titulo "Clean Code" do autor "Robert C. Martin"
    Entao o livro deve estar disponivel

  Cenario: Emprestar deixa o livro indisponivel
    Dado um livro de titulo "Duna" do autor "Frank Herbert"
    Quando o livro e emprestado
    Entao o livro nao deve estar disponivel

  Cenario: Devolver deixa o livro disponivel de novo
    Dado um livro de titulo "Duna" do autor "Frank Herbert"
    Quando o livro e emprestado
    E o livro e devolvido
    Entao o livro deve estar disponivel

  Cenario: Nao se pode emprestar um livro ja emprestado
    Dado um livro de titulo "Duna" do autor "Frank Herbert"
    Quando o livro e emprestado
    E o livro e emprestado novamente
    Entao deve ocorrer o erro de livro "Livro indisponivel"

  Cenario: Varios livros do acervo comecam disponiveis
    Dado o cadastro dos livros
      | titulo             | autor              |
      | Clean Code         | Robert C. Martin   |
      | Duna               | Frank Herbert      |
      | O Senhor dos Aneis | J. R. R. Tolkien   |
    Entao todos os livros cadastrados devem estar disponiveis

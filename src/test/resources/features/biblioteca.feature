#language:pt

@BibliotecaTeste
Funcionalidade: Emprestimo e devolucao de livros na biblioteca
  Estes cenarios validam as regras de negocio da biblioteca:
  disponibilidade do livro, limite de emprestimos por membro e multa por atraso.

  Contexto:
    Dado uma biblioteca com limite de 2 emprestimos por membro
    E o livro "Clean Code" disponivel no acervo
    E o livro "O Senhor dos Aneis" disponivel no acervo
    E o livro "Duna" disponivel no acervo

  Cenario: Emprestar um livro disponivel
    Quando o membro "Bruno" pega emprestado o livro "Clean Code"
    Entao nao deve ocorrer nenhum erro
    E o livro "Clean Code" nao deve estar disponivel
    E o membro "Bruno" deve ter 1 emprestimos ativos

  Cenario: Nao pode emprestar um livro ja emprestado
    Dado o membro "Bruno" pega emprestado o livro "Clean Code"
    Quando o membro "Adrias" pega emprestado o livro "Clean Code"
    Entao deve ocorrer o erro "Livro indisponivel"

  Cenario: Membro nao pode ultrapassar o limite de emprestimos
    Dado o membro "Bruno" pega emprestado o livro "Clean Code"
    E o membro "Bruno" pega emprestado o livro "O Senhor dos Aneis"
    Quando o membro "Bruno" pega emprestado o livro "Duna"
    Entao deve ocorrer o erro "Membro atingiu o limite de emprestimos"
    E o membro "Bruno" deve ter 2 emprestimos ativos

  Cenario: Devolver um livro no prazo nao gera multa
    Dado o membro "Bruno" pega emprestado o livro "Duna"
    Quando o membro "Bruno" devolve o livro "Duna" no prazo
    Entao a multa cobrada deve ser "0.0"
    E o livro "Duna" deve estar disponivel

  Cenario: Devolver um livro em atraso gera multa proporcional
    Dado o membro "Bruno" pega emprestado o livro "Duna"
    Quando o membro "Bruno" devolve o livro "Duna" com 3 dias de atraso
    Entao a multa cobrada deve ser "6.0"
    E o livro "Duna" deve estar disponivel

  Cenario: Devolver um livro liberado permite novo emprestimo
    Dado o membro "Bruno" pega emprestado o livro "Duna"
    E o membro "Bruno" devolve o livro "Duna" no prazo
    Quando o membro "Adrias" pega emprestado o livro "Duna"
    Entao nao deve ocorrer nenhum erro
    E o membro "Adrias" deve ter 1 emprestimos ativos

  Esquema do Cenario: A multa cresce conforme os dias de atraso
    Dado o membro "Bruno" pega emprestado o livro "Duna"
    Quando o membro "Bruno" devolve o livro "Duna" com <dias> dias de atraso
    Entao a multa cobrada deve ser "<multa>"

    Exemplos:
      | dias | multa |
      | 0    | 0.0   |
      | 1    | 2.0   |
      | 5    | 10.0  |
      | 10   | 20.0  |

  Cenario: A multa e calculada a partir das datas de retirada e devolucao
    # Prazo padrao de 7 dias: retirada 01/01 => devolucao prevista 08/01.
    Dado o membro "Bruno" pegou emprestado o livro "Duna" em "2025-01-01"
    Quando o membro "Bruno" devolve o livro "Duna" na data "2025-01-11"
    Entao a multa cobrada deve ser "6.0"
    E o livro "Duna" deve estar disponivel

  Cenario: Devolver antes do prazo nao gera multa
    Dado o membro "Bruno" pegou emprestado o livro "Duna" em "2025-01-01"
    Quando o membro "Bruno" devolve o livro "Duna" na data "2025-01-05"
    Entao a multa cobrada deve ser "0.0"

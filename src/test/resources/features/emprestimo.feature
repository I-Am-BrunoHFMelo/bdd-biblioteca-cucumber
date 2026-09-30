#language:pt

@EmprestimoTeste
Funcionalidade: Calculo de multa de um emprestimo isolado
  O emprestimo guarda a data prevista de devolucao e calcula sozinho
  a multa a partir da data real de devolucao.

  Contexto:
    Dado um emprestimo com retirada em "2025-01-01" e prazo de 7 dias

  Cenario: Devolver na data prevista nao gera multa
    Quando o livro e devolvido em "2025-01-08"
    Entao a multa do emprestimo deve ser "0.0"

  Cenario: Devolver antes do prazo nao gera multa
    Quando o livro e devolvido em "2025-01-05"
    Entao a multa do emprestimo deve ser "0.0"

  Esquema do Cenario: A multa varia conforme a data de devolucao
    Quando o livro e devolvido em "<dataDevolucao>"
    Entao a multa do emprestimo deve ser "<multa>"

    Exemplos:
      | dataDevolucao | multa |
      | 2025-01-08    | 0.0   |
      | 2025-01-09    | 2.0   |
      | 2025-01-11    | 6.0   |
      | 2025-01-13    | 10.0  |
      | 2025-01-18    | 20.0  |

  Cenario: Varias devolucoes com atrasos diferentes
    Entao as devolucoes geram as multas esperadas
      | dataDevolucao | multa |
      | 2025-01-08    | 0.0   |
      | 2025-01-10    | 4.0   |
      | 2025-01-15    | 14.0  |

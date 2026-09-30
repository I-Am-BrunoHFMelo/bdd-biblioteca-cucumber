#language:pt

@MembroTeste
Funcionalidade: Comportamento de um membro isolado
  Um membro controla o proprio limite de emprestimos ativos.

  Cenario: Membro novo pode pegar emprestado
    Dado um membro chamado "Bruno" com limite de 2 emprestimos
    Entao o membro pode pegar emprestado
    E o membro tem 0 emprestimos ativos

  Cenario: Adicionar emprestimos contabiliza a quantidade ativa
    Dado um membro chamado "Bruno" com limite de 3 emprestimos
    Quando o membro recebe 2 emprestimos
    Entao o membro tem 2 emprestimos ativos
    E o membro pode pegar emprestado

  Cenario: Remover um emprestimo libera espaco
    Dado um membro chamado "Bruno" com limite de 1 emprestimos
    Quando o membro recebe 1 emprestimos
    E o membro devolve 1 emprestimos
    Entao o membro tem 0 emprestimos ativos
    E o membro pode pegar emprestado

  Esquema do Cenario: O limite decide se o membro ainda pode pegar emprestado
    Dado um membro chamado "Bruno" com limite de <limite> emprestimos
    Quando o membro recebe <ativos> emprestimos
    Entao o membro pode pegar emprestado deve ser <pode>

    Exemplos:
      | limite | ativos | pode  |
      | 3      | 0      | true  |
      | 3      | 2      | true  |
      | 3      | 3      | false |
      | 1      | 1      | false |

  Cenario: Estourar o limite gera erro
    Dado um membro chamado "Adrias" com limite de 2 emprestimos
    Quando o membro tenta receber os emprestimos
      | titulo             |
      | Clean Code         |
      | Duna               |
      | O Senhor dos Aneis |
    Entao deve ocorrer o erro de membro "Membro atingiu o limite de emprestimos"
    E o membro tem 2 emprestimos ativos

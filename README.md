# Biblioteca — Testes e Orientação a Objetos com Cucumber/BDD

Exercício de **Testes e Orientação a Objetos** em Java + Spring Boot (usado aqui
apenas para trazer o Maven e a infraestrutura de testes — não há API REST).

O domínio é uma **biblioteca de empréstimo de livros**, com regras de negócio
reais cobertas por testes unitários (JUnit 5) e testes de aceitação
(Cucumber/Gherkin, em português).

## Domínio

Quatro classes, cada uma com sua responsabilidade:

| Classe | Responsabilidade |
|--------|------------------|
| `Livro` | Controla o próprio estado (disponível / emprestado). |
| `Membro` | Controla o próprio limite de empréstimos ativos. |
| `Emprestimo` | Liga um membro a um livro e **calcula a multa a partir das datas** (prevista × real de devolução). |
| `Biblioteca` | Agregado que orquestra os três e aplica as regras. |

### Regras de negócio

- Um livro só pode ser emprestado se estiver disponível.
- Um membro respeita um limite máximo de empréstimos ativos simultâneos.
- Devolver um livro o torna disponível novamente.
- Devolução em atraso gera multa de **R$ 2,00 por dia** (calculada pelo próprio
  `Emprestimo` contando os dias entre a data prevista e a real).

## Estrutura

```
src/main/java/com/example/bdd/modelos/   → Livro, Membro, Emprestimo, Biblioteca
src/test/java/com/example/bdd/unit/      → testes unitários (JUnit 5)
src/test/java/com/example/bdd/runners/   → runners Cucumber (um por classe)
src/test/java/com/example/bdd/steps/     → step definitions (um por classe)
src/test/resources/features/             → features Gherkin (uma por classe)
```

Cada classe tem sua própria feature com uma tag dedicada
(`@LivroTeste`, `@MembroTeste`, `@EmprestimoTeste`, `@BibliotecaTeste`). Nas
classes mais simples, os cenários usam **DataTable** e **Esquema do Cenário**
para variar os dados.

## Como rodar

Com o Maven instalado no sistema, use `mvn`. Se não tiver Maven instalado, use
o wrapper que acompanha o projeto (`.\mvnw` no PowerShell, `./mvnw` no
Linux/macOS) — ele baixa a versão correta automaticamente.

```bash
mvn test             # roda os testes (Maven instalado)
.\mvnw test          # Windows / PowerShell (via wrapper)
./mvnw test          # Linux/macOS (via wrapper)
```

Cada runner Cucumber gera um relatório HTML individual em
`target/cucumber-*-report.html`.

### Relatório consolidado (todos os testes numa página)

```bash
mvn verify           # Maven instalado
.\mvnw verify        # Windows / PowerShell
./mvnw verify        # Linux/macOS
```

O `verify` roda os testes e agrega os resultados num único relatório HTML rico
(com gráficos e pass/fail por feature) em:

```
target/cucumber-html-reports/overview-features.html
```

## Nota de aprendizado — locale e `{double}` no Cucumber

Em features com `#language:pt`, o conversor de `{double}`/`{float}` do Cucumber
usa o locale pt-BR, onde a **vírgula** é o separador decimal e o **ponto** vira
separador de milhar — então `6.0` era lido como `60`. Solução adotada: receber
o valor como `{string}` e converter com `Double.parseDouble`, que sempre usa
ponto como decimal, independente do locale.

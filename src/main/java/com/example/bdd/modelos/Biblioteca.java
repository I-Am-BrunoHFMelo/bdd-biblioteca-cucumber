package com.example.bdd.modelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca {

    public static final int PRAZO_PADRAO_EM_DIAS = 7;

    private final int limiteDeEmprestimosPorMembro;
    private final Map<String, Livro> acervo;
    private final Map<String, Membro> membros;
    private final List<Emprestimo> emprestimosAtivos;

    public Biblioteca(int limiteDeEmprestimosPorMembro) {
        this.limiteDeEmprestimosPorMembro = limiteDeEmprestimosPorMembro;
        this.acervo = new HashMap<>();
        this.membros = new HashMap<>();
        this.emprestimosAtivos = new ArrayList<>();
    }

    public void adicionarLivro(String titulo) {
        adicionarLivro(titulo, "Desconhecido");
    }

    public void adicionarLivro(String titulo, String autor) {
        acervo.put(titulo, new Livro(titulo, autor));
    }

    public void emprestar(String nomeMembro, String titulo) {
        emprestar(nomeMembro, titulo, LocalDate.now());
    }

    public void emprestar(String nomeMembro, String titulo, LocalDate dataRetirada) {
        Livro livro = acervo.get(titulo);
        if (livro == null || !livro.estaDisponivel()) {
            throw new RuntimeException("Livro indisponivel");
        }

        Membro membro = obterOuCriarMembro(nomeMembro);
        LocalDate prevista = dataRetirada.plusDays(PRAZO_PADRAO_EM_DIAS);
        Emprestimo emprestimo = new Emprestimo(membro, livro, dataRetirada, prevista);

        membro.adicionarEmprestimo(emprestimo);
        livro.emprestar();
        emprestimosAtivos.add(emprestimo);
    }

    public double devolver(String titulo, int diasDeAtraso) {
        Emprestimo emprestimo = buscarEmprestimoAtivo(titulo);
        if (emprestimo == null) {
            throw new RuntimeException("Livro nao esta emprestado");
        }
        LocalDate dataDevolucao =
                emprestimo.getDataPrevistaDevolucao().plusDays(diasDeAtraso);
        return devolverNaData(emprestimo, dataDevolucao);
    }

    public double devolver(String titulo, LocalDate dataDevolucao) {
        Emprestimo emprestimo = buscarEmprestimoAtivo(titulo);
        if (emprestimo == null) {
            throw new RuntimeException("Livro nao esta emprestado");
        }
        return devolverNaData(emprestimo, dataDevolucao);
    }

    private double devolverNaData(Emprestimo emprestimo, LocalDate dataDevolucao) {
        emprestimo.getLivro().devolver();
        emprestimo.getMembro().removerEmprestimo(emprestimo);
        emprestimosAtivos.remove(emprestimo);
        return emprestimo.calcularMulta(dataDevolucao);
    }

    public boolean estaDisponivel(String titulo) {
        Livro livro = acervo.get(titulo);
        return livro != null && livro.estaDisponivel();
    }

    public int emprestimosAtivosDoMembro(String nomeMembro) {
        Membro membro = membros.get(nomeMembro);
        return membro == null ? 0 : membro.getQuantidadeDeEmprestimosAtivos();
    }

    public int getQuantidadeDeEmprestimosAtivos() {
        return emprestimosAtivos.size();
    }

    public int getLimiteDeEmprestimosPorMembro() {
        return limiteDeEmprestimosPorMembro;
    }

    private Membro obterOuCriarMembro(String nome) {
        return membros.computeIfAbsent(nome,
                n -> new Membro(n, limiteDeEmprestimosPorMembro));
    }

    private Emprestimo buscarEmprestimoAtivo(String titulo) {
        for (Emprestimo emprestimo : emprestimosAtivos) {
            if (emprestimo.getLivro().getTitulo().equals(titulo)) {
                return emprestimo;
            }
        }
        return null;
    }
}

package com.example.bdd.modelos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    public static final double MULTA_POR_DIA_DE_ATRASO = 2.0;

    private final Membro membro;
    private final Livro livro;
    private final LocalDate dataRetirada;
    private final LocalDate dataPrevistaDevolucao;

    public Emprestimo(Membro membro, Livro livro,
                      LocalDate dataRetirada, LocalDate dataPrevistaDevolucao) {
        this.membro = membro;
        this.livro = livro;
        this.dataRetirada = dataRetirada;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public double calcularMulta(LocalDate dataDevolucao) {
        long diasDeAtraso = ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucao);
        return calcularMulta(diasDeAtraso);
    }

    public double calcularMulta(long diasDeAtraso) {
        if (diasDeAtraso <= 0) {
            return 0.0;
        }
        return diasDeAtraso * MULTA_POR_DIA_DE_ATRASO;
    }

    public Membro getMembro() {
        return membro;
    }

    public Livro getLivro() {
        return livro;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }
}

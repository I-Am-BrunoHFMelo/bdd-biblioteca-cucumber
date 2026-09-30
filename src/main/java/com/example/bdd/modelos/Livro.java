package com.example.bdd.modelos;

public class Livro {

    private final String titulo;
    private final String autor;
    private boolean emprestado;

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.emprestado = false;
    }

    public void emprestar() {
        if (emprestado) {
            throw new RuntimeException("Livro indisponivel");
        }
        this.emprestado = true;
    }

    public void devolver() {
        this.emprestado = false;
    }

    public boolean estaDisponivel() {
        return !emprestado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isEmprestado() {
        return emprestado;
    }
}

"""Gera a apresentacao PPTX do projeto BDD Biblioteca.

Foco: como Cucumber/Gherkin foram usados para escrever os testes.
Estrutura: 1 slide de visao geral + 4 slides de classe + 1 slide por cenario Gherkin.
"""

from pptx import Presentation
from pptx.util import Inches, Pt, Emu
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR

# ---------------------------------------------------------------- paleta
ROXO = RGBColor(0x4C, 0x35, 0x75)
ROXO_CLARO = RGBColor(0x7A, 0x5C, 0xA8)
FUNDO = RGBColor(0xF6, 0xF4, 0xFA)
BRANCO = RGBColor(0xFF, 0xFF, 0xFF)
TEXTO = RGBColor(0x2B, 0x2B, 0x2B)
CINZA = RGBColor(0x6B, 0x6B, 0x6B)
VERDE = RGBColor(0x2E, 0x7D, 0x32)
CODE_BG = RGBColor(0x2B, 0x2B, 0x3A)
CODE_FG = RGBColor(0xE6, 0xE6, 0xF0)
KEYWORD = RGBColor(0xC5, 0x92, 0xF5)

LARGURA = Inches(13.333)
ALTURA = Inches(7.5)

prs = Presentation()
prs.slide_width = LARGURA
prs.slide_height = ALTURA
BLANK = prs.slide_layouts[6]


def fundo(slide, cor=FUNDO):
    slide.background.fill.solid()
    slide.background.fill.fore_color.rgb = cor


def caixa(slide, x, y, w, h):
    tb = slide.shapes.add_textbox(x, y, w, h)
    tf = tb.text_frame
    tf.word_wrap = True
    return tb, tf


def barra_topo(slide, texto, cor=ROXO):
    faixa = slide.shapes.add_shape(1, 0, 0, LARGURA, Inches(1.15))
    faixa.fill.solid()
    faixa.fill.fore_color.rgb = cor
    faixa.line.fill.background()
    faixa.shadow.inherit = False
    tf = faixa.text_frame
    tf.word_wrap = True
    tf.margin_left = Inches(0.5)
    tf.margin_top = Inches(0.1)
    p = tf.paragraphs[0]
    p.text = texto
    p.font.size = Pt(28)
    p.font.bold = True
    p.font.color.rgb = BRANCO
    return faixa


def run(p, texto, size=18, cor=TEXTO, bold=False, mono=False, italic=False):
    r = p.add_run()
    r.text = texto
    r.font.size = Pt(size)
    r.font.color.rgb = cor
    r.font.bold = bold
    r.font.italic = italic
    if mono:
        r.font.name = "Consolas"
    return r


# ---------------------------------------------------------------- capa
def slide_capa():
    s = prs.slides.add_slide(BLANK)
    fundo(s, ROXO)
    _, tf = caixa(s, Inches(1), Inches(2.4), Inches(11.3), Inches(2))
    p = tf.paragraphs[0]
    p.alignment = PP_ALIGN.CENTER
    run(p, "Sistema de Biblioteca", size=48, cor=BRANCO, bold=True)
    p2 = tf.add_paragraph()
    p2.alignment = PP_ALIGN.CENTER
    run(p2, "Testes de aceitacao com Cucumber e Gherkin", size=26, cor=RGBColor(0xD9, 0xCC, 0xF0))
    _, tf2 = caixa(s, Inches(1), Inches(5.2), Inches(11.3), Inches(1))
    p3 = tf2.paragraphs[0]
    p3.alignment = PP_ALIGN.CENTER
    run(p3, "Orientacao a Objetos + BDD  |  Spring Boot / Maven", size=18, cor=RGBColor(0xB8, 0xA6, 0xDC))


# ---------------------------------------------------------------- visao geral
def slide_visao_geral():
    s = prs.slides.add_slide(BLANK)
    fundo(s)
    barra_topo(s, "A Biblioteca e suas regras de negocio")

    _, tf = caixa(s, Inches(0.6), Inches(1.5), Inches(6.1), Inches(5.6))
    p = tf.paragraphs[0]
    run(p, "O dominio", size=22, cor=ROXO, bold=True)
    intro = tf.add_paragraph()
    run(intro, "Uma biblioteca empresta livros do seu acervo aos membros e cobra multa por atraso na devolucao.", size=16, cor=TEXTO)
    intro.space_after = Pt(10)

    regras = [
        "Um livro so pode ser emprestado se estiver disponivel",
        "Cada membro tem um limite de emprestimos ativos",
        "Devolver um livro o libera para um novo emprestimo",
        "Multa = R$ 2,00 por dia de atraso (prazo padrao: 7 dias)",
    ]
    tt = tf.add_paragraph()
    run(tt, "Regras que viraram teste", size=20, cor=ROXO, bold=True)
    tt.space_before = Pt(10)
    for r in regras:
        b = tf.add_paragraph()
        b.level = 1
        run(b, "•  ", size=16, cor=ROXO_CLARO, bold=True)
        run(b, r, size=16, cor=TEXTO)
        b.space_after = Pt(6)

    # cartao lateral: as 4 classes
    cartao = s.shapes.add_shape(1, Inches(7.1), Inches(1.5), Inches(5.6), Inches(5.4))
    cartao.fill.solid()
    cartao.fill.fore_color.rgb = BRANCO
    cartao.line.color.rgb = ROXO_CLARO
    cartao.line.width = Pt(1.5)
    cartao.shadow.inherit = False
    ctf = cartao.text_frame
    ctf.word_wrap = True
    ctf.margin_left = Inches(0.35)
    ctf.margin_top = Inches(0.3)
    cp = ctf.paragraphs[0]
    run(cp, "O modelo orientado a objetos", size=20, cor=ROXO, bold=True)
    classes = [
        ("Livro", "controla o proprio estado disponivel/emprestado"),
        ("Membro", "controla seu limite e seus emprestimos ativos"),
        ("Emprestimo", "guarda as datas e calcula a propria multa"),
        ("Biblioteca", "orquestra os tres e aplica as regras"),
    ]
    for nome, desc in classes:
        cp2 = ctf.add_paragraph()
        run(cp2, nome, size=17, cor=ROXO_CLARO, bold=True, mono=True)
        cp2.space_before = Pt(10)
        cp3 = ctf.add_paragraph()
        run(cp3, desc, size=14, cor=CINZA)


# ---------------------------------------------------------------- slide de classe
def slide_classe(nome, papel, atributos, metodos):
    s = prs.slides.add_slide(BLANK)
    fundo(s)
    barra_topo(s, f"Classe  {nome}")

    _, tf = caixa(s, Inches(0.6), Inches(1.35), Inches(12), Inches(0.9))
    p = tf.paragraphs[0]
    run(p, papel, size=18, cor=CINZA, italic=True)

    # atributos
    ca = s.shapes.add_shape(1, Inches(0.6), Inches(2.4), Inches(5.9), Inches(4.6))
    ca.fill.solid(); ca.fill.fore_color.rgb = BRANCO
    ca.line.color.rgb = ROXO_CLARO; ca.line.width = Pt(1.25)
    ca.shadow.inherit = False
    atf = ca.text_frame; atf.word_wrap = True
    atf.margin_left = Inches(0.3); atf.margin_top = Inches(0.25)
    ap = atf.paragraphs[0]
    run(ap, "Atributos", size=20, cor=ROXO, bold=True)
    for tipo, campo in atributos:
        r = atf.add_paragraph()
        run(r, f"{campo}", size=16, cor=TEXTO, mono=True, bold=True)
        run(r, f" : {tipo}", size=15, cor=CINZA, mono=True)
        r.space_after = Pt(7)

    # metodos
    cm = s.shapes.add_shape(1, Inches(6.85), Inches(2.4), Inches(5.85), Inches(4.6))
    cm.fill.solid(); cm.fill.fore_color.rgb = BRANCO
    cm.line.color.rgb = ROXO_CLARO; cm.line.width = Pt(1.25)
    cm.shadow.inherit = False
    mtf = cm.text_frame; mtf.word_wrap = True
    mtf.margin_left = Inches(0.3); mtf.margin_top = Inches(0.25)
    mp = mtf.paragraphs[0]
    run(mp, "Comportamento (metodos)", size=20, cor=ROXO, bold=True)
    for assinatura, desc in metodos:
        r = mtf.add_paragraph()
        run(r, assinatura, size=15, cor=ROXO_CLARO, mono=True, bold=True)
        if desc:
            r2 = mtf.add_paragraph()
            run(r2, desc, size=13, cor=CINZA)
        r.space_before = Pt(7)


# ---------------------------------------------------------------- slide de cenario
def slide_cenario(feature_titulo, tag, tipo, titulo, linhas, tabela=None):
    s = prs.slides.add_slide(BLANK)
    fundo(s)
    barra_topo(s, f"Cenario: {titulo}", cor=ROXO)

    # subtitulo: feature + tag
    _, tf = caixa(s, Inches(0.6), Inches(1.2), Inches(12), Inches(0.5))
    p = tf.paragraphs[0]
    run(p, f"{feature_titulo}   ", size=14, cor=CINZA, italic=True)
    run(p, tag, size=14, cor=VERDE, mono=True, bold=True)
    if tipo == "esquema":
        run(p, "   (Esquema do Cenario)", size=14, cor=ROXO_CLARO, bold=True)

    # bloco de codigo Gherkin
    altura_cod = Inches(4.9) if not tabela else Inches(3.0)
    cod = s.shapes.add_shape(1, Inches(0.6), Inches(1.85), Inches(12.1), altura_cod)
    cod.fill.solid(); cod.fill.fore_color.rgb = CODE_BG
    cod.line.fill.background(); cod.shadow.inherit = False
    ctf = cod.text_frame; ctf.word_wrap = True
    ctf.margin_left = Inches(0.35); ctf.margin_top = Inches(0.25)
    palavras = ("Dado", "Quando", "Entao", "E ", "Mas ", "Contexto", "Exemplos", "|")
    first = True
    for linha in linhas:
        p = ctf.paragraphs[0] if first else ctf.add_paragraph()
        first = False
        kw = next((k for k in palavras if linha.strip().startswith(k.strip())), None)
        if kw and kw != "|":
            partes = linha.strip().split(" ", 1)
            run(p, "   " + partes[0] + " ", size=16, cor=KEYWORD, mono=True, bold=True)
            if len(partes) > 1:
                run(p, partes[1], size=16, cor=CODE_FG, mono=True)
        else:
            run(p, "   " + linha.strip(), size=15, cor=RGBColor(0x9F, 0x9F, 0xC0), mono=True)
        p.space_after = Pt(2)

    if tabela:
        _tabela_exemplos(s, tabela, Inches(5.0))


def _tabela_exemplos(s, dados, y):
    linhas = len(dados)
    cols = len(dados[0])
    largura = Inches(min(12.0, 2.4 * cols))
    tab = s.shapes.add_table(linhas, cols, Inches(0.6), y, largura, Inches(0.4 * linhas)).table
    for j in range(cols):
        c = tab.cell(0, j)
        c.text = dados[0][j]
        c.fill.solid(); c.fill.fore_color.rgb = ROXO
        for pr in c.text_frame.paragraphs:
            pr.font.size = Pt(14); pr.font.bold = True; pr.font.color.rgb = BRANCO
            pr.alignment = PP_ALIGN.CENTER
    for i in range(1, linhas):
        for j in range(cols):
            c = tab.cell(i, j)
            c.text = dados[i][j]
            c.fill.solid()
            c.fill.fore_color.rgb = BRANCO if i % 2 else RGBColor(0xEE, 0xEA, 0xF6)
            for pr in c.text_frame.paragraphs:
                pr.font.size = Pt(13); pr.font.color.rgb = TEXTO
                pr.alignment = PP_ALIGN.CENTER


# ================================================================ BUILD
slide_capa()
slide_visao_geral()

# ---- 4 classes
slide_classe(
    "Livro",
    "Representa um livro do acervo e controla o proprio estado de disponibilidade.",
    [("String", "titulo"), ("String", "autor"), ("boolean", "emprestado")],
    [("emprestar()", "marca como emprestado; lanca erro se ja estiver"),
     ("devolver()", "volta a ficar disponivel"),
     ("estaDisponivel() : boolean", "informa se pode ser emprestado")],
)
slide_classe(
    "Membro",
    "Representa quem pega livros emprestados e controla o proprio limite.",
    [("String", "nome"), ("int", "limiteDeEmprestimos"),
     ("List<Emprestimo>", "emprestimosAtivos")],
    [("podePegarEmprestado() : boolean", "true enquanto abaixo do limite"),
     ("adicionarEmprestimo(e)", "registra; lanca erro no limite"),
     ("removerEmprestimo(e)", "libera espaco ao devolver"),
     ("getQuantidadeDeEmprestimosAtivos() : int", "")],
)
slide_classe(
    "Emprestimo",
    "Liga um Membro a um Livro, guarda as datas e calcula a propria multa.",
    [("Membro", "membro"), ("Livro", "livro"),
     ("LocalDate", "dataRetirada"), ("LocalDate", "dataPrevistaDevolucao"),
     ("double", "MULTA_POR_DIA_DE_ATRASO = 2.0")],
    [("calcularMulta(dataDevolucao) : double", "conta os dias de atraso e multiplica"),
     ("calcularMulta(dias) : double", "overload que recebe os dias direto"),
     ("getDataPrevistaDevolucao() : LocalDate", "")],
)
slide_classe(
    "Biblioteca",
    "Agregado que orquestra o acervo, os membros e os emprestimos, aplicando as regras.",
    [("int", "limiteDeEmprestimosPorMembro"),
     ("Map<String,Livro>", "acervo"),
     ("Map<String,Membro>", "membros"),
     ("List<Emprestimo>", "emprestimosAtivos"),
     ("int", "PRAZO_PADRAO_EM_DIAS = 7")],
    [("adicionarLivro(titulo, autor)", "cadastra no acervo"),
     ("emprestar(membro, titulo)", "valida disponibilidade e limite"),
     ("devolver(titulo, data) : double", "libera o livro e retorna a multa"),
     ("estaDisponivel(titulo) : boolean", "")],
)

# ---- CENARIOS: LIVRO
FL = "Comportamento de um livro isolado"
slide_cenario(FL, "@LivroTeste", "cenario", "Um livro recem criado esta disponivel",
    ['Dado um livro de titulo "Clean Code" do autor "Robert C. Martin"',
     'Entao o livro deve estar disponivel'])
slide_cenario(FL, "@LivroTeste", "cenario", "Emprestar deixa o livro indisponivel",
    ['Dado um livro de titulo "Duna" do autor "Frank Herbert"',
     'Quando o livro e emprestado',
     'Entao o livro nao deve estar disponivel'])
slide_cenario(FL, "@LivroTeste", "cenario", "Devolver deixa o livro disponivel de novo",
    ['Dado um livro de titulo "Duna" do autor "Frank Herbert"',
     'Quando o livro e emprestado',
     'E o livro e devolvido',
     'Entao o livro deve estar disponivel'])
slide_cenario(FL, "@LivroTeste", "cenario", "Nao se pode emprestar um livro ja emprestado",
    ['Dado um livro de titulo "Duna" do autor "Frank Herbert"',
     'Quando o livro e emprestado',
     'E o livro e emprestado novamente',
     'Entao deve ocorrer o erro de livro "Livro indisponivel"'])
slide_cenario(FL, "@LivroTeste", "cenario", "Varios livros do acervo comecam disponiveis",
    ['Dado o cadastro dos livros',
     'Entao todos os livros cadastrados devem estar disponiveis'],
    tabela=[["titulo", "autor"],
            ["Clean Code", "Robert C. Martin"],
            ["Duna", "Frank Herbert"],
            ["O Senhor dos Aneis", "J. R. R. Tolkien"]])

# ---- CENARIOS: MEMBRO
FM = "Comportamento de um membro isolado"
slide_cenario(FM, "@MembroTeste", "cenario", "Membro novo pode pegar emprestado",
    ['Dado um membro chamado "Bruno" com limite de 2 emprestimos',
     'Entao o membro pode pegar emprestado',
     'E o membro tem 0 emprestimos ativos'])
slide_cenario(FM, "@MembroTeste", "cenario", "Adicionar emprestimos contabiliza a quantidade",
    ['Dado um membro chamado "Bruno" com limite de 3 emprestimos',
     'Quando o membro recebe 2 emprestimos',
     'Entao o membro tem 2 emprestimos ativos',
     'E o membro pode pegar emprestado'])
slide_cenario(FM, "@MembroTeste", "cenario", "Remover um emprestimo libera espaco",
    ['Dado um membro chamado "Bruno" com limite de 1 emprestimos',
     'Quando o membro recebe 1 emprestimos',
     'E o membro devolve 1 emprestimos',
     'Entao o membro tem 0 emprestimos ativos',
     'E o membro pode pegar emprestado'])
slide_cenario(FM, "@MembroTeste", "esquema", "O limite decide se o membro ainda pode pegar",
    ['Dado um membro chamado "Bruno" com limite de <limite> emprestimos',
     'Quando o membro recebe <ativos> emprestimos',
     'Entao o membro pode pegar emprestado deve ser <pode>'],
    tabela=[["limite", "ativos", "pode"],
            ["3", "0", "true"], ["3", "2", "true"],
            ["3", "3", "false"], ["1", "1", "false"]])
slide_cenario(FM, "@MembroTeste", "cenario", "Estourar o limite gera erro",
    ['Dado um membro chamado "Ana" com limite de 2 emprestimos',
     'Quando o membro tenta receber os emprestimos (Clean Code, Duna, O Senhor dos Aneis)',
     'Entao deve ocorrer o erro de membro "Membro atingiu o limite de emprestimos"',
     'E o membro tem 2 emprestimos ativos'])

# ---- CENARIOS: EMPRESTIMO
FE = "Calculo de multa de um emprestimo isolado"
CTX_E = 'Contexto: emprestimo com retirada em "2025-01-01" e prazo de 7 dias'
slide_cenario(FE, "@EmprestimoTeste", "cenario", "Devolver na data prevista nao gera multa",
    [CTX_E,
     'Quando o livro e devolvido em "2025-01-08"',
     'Entao a multa do emprestimo deve ser "0.0"'])
slide_cenario(FE, "@EmprestimoTeste", "cenario", "Devolver antes do prazo nao gera multa",
    [CTX_E,
     'Quando o livro e devolvido em "2025-01-05"',
     'Entao a multa do emprestimo deve ser "0.0"'])
slide_cenario(FE, "@EmprestimoTeste", "esquema", "A multa varia conforme a data de devolucao",
    [CTX_E,
     'Quando o livro e devolvido em "<dataDevolucao>"',
     'Entao a multa do emprestimo deve ser "<multa>"'],
    tabela=[["dataDevolucao", "multa"],
            ["2025-01-08", "0.0"], ["2025-01-09", "2.0"],
            ["2025-01-11", "6.0"], ["2025-01-13", "10.0"],
            ["2025-01-18", "20.0"]])
slide_cenario(FE, "@EmprestimoTeste", "cenario", "Varias devolucoes com atrasos diferentes",
    ['Entao as devolucoes geram as multas esperadas'],
    tabela=[["dataDevolucao", "multa"],
            ["2025-01-08", "0.0"], ["2025-01-10", "4.0"],
            ["2025-01-15", "14.0"]])

# ---- CENARIOS: BIBLIOTECA
FB = "Emprestimo e devolucao de livros na biblioteca"
CTX_B = 'Contexto: biblioteca com limite 2; acervo: Clean Code, O Senhor dos Aneis, Duna'
slide_cenario(FB, "@BibliotecaTeste", "cenario", "Emprestar um livro disponivel",
    [CTX_B,
     'Quando o membro "Bruno" pega emprestado o livro "Clean Code"',
     'Entao nao deve ocorrer nenhum erro',
     'E o livro "Clean Code" nao deve estar disponivel',
     'E o membro "Bruno" deve ter 1 emprestimos ativos'])
slide_cenario(FB, "@BibliotecaTeste", "cenario", "Nao pode emprestar um livro ja emprestado",
    [CTX_B,
     'Dado o membro "Bruno" pega emprestado o livro "Clean Code"',
     'Quando o membro "Ana" pega emprestado o livro "Clean Code"',
     'Entao deve ocorrer o erro "Livro indisponivel"'])
slide_cenario(FB, "@BibliotecaTeste", "cenario", "Membro nao pode ultrapassar o limite",
    [CTX_B,
     'Dado o membro "Bruno" pega emprestado o livro "Clean Code"',
     'E o membro "Bruno" pega emprestado o livro "O Senhor dos Aneis"',
     'Quando o membro "Bruno" pega emprestado o livro "Duna"',
     'Entao deve ocorrer o erro "Membro atingiu o limite de emprestimos"',
     'E o membro "Bruno" deve ter 2 emprestimos ativos'])
slide_cenario(FB, "@BibliotecaTeste", "cenario", "Devolver um livro no prazo nao gera multa",
    [CTX_B,
     'Dado o membro "Bruno" pega emprestado o livro "Duna"',
     'Quando o membro "Bruno" devolve o livro "Duna" no prazo',
     'Entao a multa cobrada deve ser "0.0"',
     'E o livro "Duna" deve estar disponivel'])
slide_cenario(FB, "@BibliotecaTeste", "cenario", "Devolver em atraso gera multa proporcional",
    [CTX_B,
     'Dado o membro "Bruno" pega emprestado o livro "Duna"',
     'Quando o membro "Bruno" devolve o livro "Duna" com 3 dias de atraso',
     'Entao a multa cobrada deve ser "6.0"',
     'E o livro "Duna" deve estar disponivel'])
slide_cenario(FB, "@BibliotecaTeste", "cenario", "Devolver um livro liberado permite novo emprestimo",
    [CTX_B,
     'Dado o membro "Bruno" pega emprestado o livro "Duna"',
     'E o membro "Bruno" devolve o livro "Duna" no prazo',
     'Quando o membro "Ana" pega emprestado o livro "Duna"',
     'Entao nao deve ocorrer nenhum erro',
     'E o membro "Ana" deve ter 1 emprestimos ativos'])
slide_cenario(FB, "@BibliotecaTeste", "esquema", "A multa cresce conforme os dias de atraso",
    [CTX_B,
     'Dado o membro "Bruno" pega emprestado o livro "Duna"',
     'Quando o membro "Bruno" devolve o livro "Duna" com <dias> dias de atraso',
     'Entao a multa cobrada deve ser "<multa>"'],
    tabela=[["dias", "multa"],
            ["0", "0.0"], ["1", "2.0"], ["5", "10.0"], ["10", "20.0"]])
slide_cenario(FB, "@BibliotecaTeste", "cenario", "A multa e calculada a partir das datas",
    ['Dado o membro "Bruno" pegou emprestado o livro "Duna" em "2025-01-01"',
     'Quando o membro "Bruno" devolve o livro "Duna" na data "2025-01-11"',
     'Entao a multa cobrada deve ser "6.0"  (prazo previsto: 08/01)',
     'E o livro "Duna" deve estar disponivel'])
slide_cenario(FB, "@BibliotecaTeste", "cenario", "Devolver antes do prazo nao gera multa (por data)",
    ['Dado o membro "Bruno" pegou emprestado o livro "Duna" em "2025-01-01"',
     'Quando o membro "Bruno" devolve o livro "Duna" na data "2025-01-05"',
     'Entao a multa cobrada deve ser "0.0"'])

# ---- fecho
def slide_fecho():
    s = prs.slides.add_slide(BLANK)
    fundo(s, ROXO)
    _, tf = caixa(s, Inches(1), Inches(2.6), Inches(11.3), Inches(2.5))
    p = tf.paragraphs[0]; p.alignment = PP_ALIGN.CENTER
    run(p, "23 cenarios Gherkin  |  4 features  |  4 runners verdes", size=28, cor=BRANCO, bold=True)
    p2 = tf.add_paragraph(); p2.alignment = PP_ALIGN.CENTER
    run(p2, "As regras de negocio, escritas em linguagem de negocio, executando como teste.", size=18, cor=RGBColor(0xD9, 0xCC, 0xF0))
    p2.space_before = Pt(14)

slide_fecho()

SAIDA = r"C:\Repositorios\bdd-biblioteca-cucumber\Apresentacao-Biblioteca-BDD.pptx"
prs.save(SAIDA)
print("SLIDES:", len(prs.slides.__iter__.__self__._sldIdLst))
print("SALVO:", SAIDA)

package br.com.lhcortez.servicos;

import br.com.lhcortez.entidades.Filme;
import br.com.lhcortez.entidades.Locacao;
import br.com.lhcortez.entidades.Usuario;
import br.com.lhcortez.exceptions.FilmesSemEstoqueException;
import br.com.lhcortez.exceptions.LocadoraException;
import br.com.lhcortez.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.com.lhcortez.utils.DataUtils.isMesmaData;
import static br.com.lhcortez.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Luiz Cortez");
        List<Filme> filmes = Arrays.asList(new Filme("Senhor dos Aneis", 1, 5.0));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    //forma elegante
    @Test(expected = FilmesSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Luiz Cortez");
        List<Filme> filmes = Arrays.asList(new Filme("O Retorno do Rei", 0, 4.0));

        //acao
        service.alugarFilme(usuario, filmes);
    }

    //forma robusta
    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmesSemEstoqueException {
        //cenario
        List<Filme> filmes = Arrays.asList(new Filme("As Duas Torres", 1, 4.0));

        //acao
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    //forma nova
    @Test
    public void naoDevealugarFilmeSemFilme() throws FilmesSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Luiz Cortez");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);

    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmesSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Luiz Cortez");
        List<Filme> filmes = Arrays.asList
                (new Filme("Filme 1", 2, 4.0),
                        new Filme("Filme 2", 2, 4.0),
                        new Filme("Filme 3", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(11.0));

    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmesSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Luiz Cortez");
        List<Filme> filmes = Arrays.asList
                (new Filme("Filme 1", 2, 4.0),
                        new Filme("Filme 2", 2, 4.0),
                        new Filme("Filme 3", 2, 4.0),
                        new Filme("Filme 4", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(13.0));

    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmesSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Luiz Cortez");
        List<Filme> filmes = Arrays.asList
                (new Filme("Filme 1", 2, 4.0),
                        new Filme("Filme 2", 2, 4.0),
                        new Filme("Filme 3", 2, 4.0),
                        new Filme("Filme 4", 2, 4.0),
                        new Filme("Filme 5", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));

    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmesSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Luiz Cortez");
        List<Filme> filmes = Arrays.asList
                (new Filme("Filme 1", 2, 4.0),
                        new Filme("Filme 2", 2, 4.0),
                        new Filme("Filme 3", 2, 4.0),
                        new Filme("Filme 4", 2, 4.0),
                        new Filme("Filme 5", 2, 4.0),
                        new Filme("Filme 6", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));

    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmesSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);

    }


//    //forma robusta
//    @Test
//    public void testLocacao_filmeSemEstoque2() {
//        LocacaoService service = new LocacaoService();
//        Usuario usuario = new Usuario("Luiz Cortez");
//        Filme filme = new Filme("Senhor dos Aneis", 0, 5.0);
//
//        //acao
//        try {
//            service.alugarFilme(usuario, filme);
//            Assert.fail("Deveria ter lancado uma excecao");
//        } catch (Exception e) {
//            Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
//        }
//    }
//
//    //forma nova
//    @Test
//    public void testLocacao_filmeSemEstoque3() throws Exception {
//        LocacaoService service = new LocacaoService();
//        Usuario usuario = new Usuario("Luiz Cortez");
//        Filme filme = new Filme("Senhor dos Aneis", 0, 5.0);
//
//        exception.expect(Exception.class);
//        exception.expectMessage("Filme sem estoque");
//
//        //acao
//        service.alugarFilme(usuario, filme);
//
//
//    }
}

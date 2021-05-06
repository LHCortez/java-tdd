package br.com.lhcortez.servicos;

import br.com.lhcortez.entidades.Filme;
import br.com.lhcortez.entidades.Locacao;
import br.com.lhcortez.entidades.Usuario;
import br.com.lhcortez.exceptions.FilmesSemEstoqueException;
import br.com.lhcortez.exceptions.LocadoraException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.com.lhcortez.utils.DataUtils.isMesmaData;
import static br.com.lhcortez.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testeLocacao() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Luiz Cortez");
        Filme filme = new Filme("Senhor dos Aneis", 1, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    //forma elegante
    @Test(expected = FilmesSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Luiz Cortez");
        Filme filme = new Filme("Senhor dos Aneis", 0, 5.0);

        //acao
        service.alugarFilme(usuario, filme);
    }

    @Test
    public void testeLocacao_usuarioVazio() throws FilmesSemEstoqueException {
        //cenario
        LocacaoService service = new LocacaoService();
        Filme filme = new Filme("As Duas Torres", 1, 4.0);

        //acao
        try {
            service.alugarFilme(null, filme);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
        System.out.println("Forma robusta");
    }

    @Test
    public void testeLocacao_FilmeVazio() throws FilmesSemEstoqueException, LocadoraException {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Luiz Cortez");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario,null);

        System.out.println("Forma nova");
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

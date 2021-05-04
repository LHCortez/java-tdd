package br.com.lhcortez.servicos;

import br.com.lhcortez.entidades.Filme;
import br.com.lhcortez.entidades.Locacao;
import br.com.lhcortez.entidades.Usuario;
import br.com.lhcortez.servicos.LocacaoService;
import br.com.lhcortez.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LocacaoServiceTest {

    @Test
    public void teste() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Luiz Cortez");
        Filme filme = new Filme("Senhor dos Aneis", 2, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        //verificacao
        Assert.assertTrue(locacao.getValor() == 5.0);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

    }
}

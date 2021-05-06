package br.com.lhcortez.servicos;

import java.util.Date;

import br.com.lhcortez.entidades.Filme;
import br.com.lhcortez.entidades.Locacao;
import br.com.lhcortez.entidades.Usuario;
import br.com.lhcortez.exceptions.FilmesSemEstoqueException;
import br.com.lhcortez.exceptions.LocadoraException;
import br.com.lhcortez.utils.DataUtils;

public class LocacaoService {

    public Locacao alugarFilme(Usuario usuario, Filme filme) throws FilmesSemEstoqueException, LocadoraException {

        if (usuario == null) {
            throw new LocadoraException("Usuario vazio");
        }

        if (filme == null) {
            throw new LocadoraException("Filme vazio");
        }

        if (filme.getEstoque() == 0) {
            throw new FilmesSemEstoqueException();
        }

        Locacao locacao = new Locacao();
        locacao.setFilme(filme);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());
        locacao.setValor(filme.getPrecoLocacao());

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        //TODO adicionar método para salvar

        return locacao;
    }

}
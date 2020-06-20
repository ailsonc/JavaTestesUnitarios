package br.ba.acosta.servicos;

import br.ba.acosta.entidades.Usuario;

public interface EmailService {

    public void notificarAtraso(Usuario usuario);

}

package br.ba.acosta.builders;

import br.ba.acosta.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {}

    public static UsuarioBuilder umUsuario() {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Usuario");
        return  builder;
    }

    public UsuarioBuilder comNOme(String nome) {
        usuario.setNome(nome);
        return this;
    }

    public Usuario agora() {
        return usuario;
    }

}

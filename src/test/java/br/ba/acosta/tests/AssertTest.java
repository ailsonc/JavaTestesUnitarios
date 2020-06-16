package br.ba.acosta.test;

import br.ba.acosta.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {
    
    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);
        Assert.assertEquals(1,1);
        Assert.assertEquals("Erro de comparacao",1,1); //Message

        Assert.assertEquals(0.51,0.51, 0.01);
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        int i = 5; //Tipo primitivo
        Integer z = 5; //Objeto
        Assert.assertEquals(Integer.valueOf(i), z);
        Assert.assertEquals(i, z.intValue());

        //Colocar na ordem certa  <Expected>  <actual> facilita identicar o erro.
        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("bola", "casa");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola".startsWith("bol"));

        Usuario u1 = new Usuario("Usuario");
        Usuario u2 = new Usuario("Usuario");
        Usuario u3 = u2;
        Usuario u4 = null;

        Assert.assertEquals(u1,u2); //Precisa ter o metodo equals class Usuario
        Assert.assertSame(u2, u3); //Mesma instancia
        Assert.assertNotSame(u1, u2); //Garantir são instancias diferentes
        Assert.assertTrue(u4 == null);
        Assert.assertNull(u4);
        Assert.assertNotNull(u2);

    }
}

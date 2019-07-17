package com.clientes;

import com.clientes.domain.Cliente;
import com.clientes.domain.ClienteService;
import com.clientes.domain.dto.ClienteDTO;
import com.clientes.api.exception.ObjectNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientesServiceTest {

    @Autowired
    private ClienteService service;

    @Test
    public void testSave() {

        Cliente carro = new Cliente();
        carro.setNome("João");
        carro.setIdade(30);

        ClienteDTO c = service.insert(carro);

        assertNotNull(c);

        Long id = c.getId();
        assertNotNull(id);

        // Buscar o objeto
        c = service.getClienteById(id);
        assertNotNull(c);

        assertEquals("João",c.getNome());
        assertEquals(30.0 ,c.getIdade());

        // Deletar o objeto
        service.delete(id);

        // Verificar se deletou
        try {
            service.getClienteById(id);
            fail("O cliente não foi excluído");
        } catch (ObjectNotFoundException e) {
            // OK
        }
    }

    @Test
    public void testLista() {

        List<ClienteDTO> carros = service.getClientes();

        assertEquals(30, carros.size());
    }

    @Test
    public void testGet() {

        ClienteDTO c = service.getClienteById(1L);

        assertNotNull(c);


        assertEquals("João", c.getNome());
    }
}
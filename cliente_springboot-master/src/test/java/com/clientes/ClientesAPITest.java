package com.clientes;

import com.clientes.domain.Cliente;
import com.clientes.domain.ClienteService;
import com.clientes.domain.dto.ClienteDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientesAPITest {
    @Autowired
    protected TestRestTemplate rest;

    @Autowired
    private ClienteService service;

    private ResponseEntity<ClienteDTO> getCliente(String url) {
        return
                rest.getForEntity(url, ClienteDTO.class);
    }

    private ResponseEntity<List<ClienteDTO>> getClientes(String url) {
        return rest.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ClienteDTO>>() {
                });
    }

    @Test
    public void testSave() {

        Cliente carro = new Cliente();
        carro.setNome("João");
        carro.setIdade(30);

        // Insert
        ResponseEntity response = rest.postForEntity("/api/v1/clientes", carro, null);
        System.out.println(response);

        // Verifica se criou
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Buscar o objeto
        String location = response.getHeaders().get("location").get(0);
        ClienteDTO c = getCliente(location).getBody();

        assertNotNull(c);
        assertEquals("João", c.getNome());
        assertEquals(30D, c.getIdade());

        // Deletar o objeto
        rest.delete(location);

        // Verificar se deletou
        assertEquals(HttpStatus.NOT_FOUND, getCliente(location).getStatusCode());
    }

    @Test
    public void testLista() {
        List<ClienteDTO> clientes = getClientes("/api/v1/clientes").getBody();
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
    }

    @Test
    public void testGetOk() {

        ResponseEntity<ClienteDTO> response = getCliente("/api/v1/clientes/1");
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        ClienteDTO c = response.getBody();
        assertEquals("João", c.getNome());
    }

    @Test
    public void testGetNotFound() {

        ResponseEntity response = getCliente("/api/v1/clientes/1100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
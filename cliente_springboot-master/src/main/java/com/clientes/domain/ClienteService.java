package com.clientes.domain;

import com.clientes.domain.dto.ClienteDTO;
import com.clientes.api.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository rep;

    public ClienteDTO insert(Cliente cliente) {
        Assert.isNull(cliente.getId(),"Não foi possível inserir o registro");
        return ClienteDTO.create(rep.save(cliente));
    }

    public ClienteDTO update(Cliente cliente, Long id) {
        Assert.notNull(id,"Não foi possível atualizar o registro");

        // Busca o cliente no banco de dados
        Optional<Cliente> optional = rep.findById(id);
        if(optional.isPresent()) {
            Cliente db = optional.get();
            // Copiar as propriedades
            db.setNome(cliente.getNome());
            db.setIdade(cliente.getIdade());
            System.out.println("Cliente id " + db.getId());

            // Atualiza o cliente
            rep.save(db);

            return ClienteDTO.create(db);
        } else {
            return null;
            //throw new RuntimeException("Não foi possível atualizar o registro");
        }
    }

    public ClienteDTO getClienteById(Long id) {
        Optional<Cliente> cliente = rep.findById(id);
        return cliente.map(ClienteDTO::create).orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado"));
    }

    public List<ClienteDTO> getClientes() {
        List<ClienteDTO> list = rep.findAll().stream().map(ClienteDTO::create).collect(Collectors.toList());
        return list;
    }

    public void delete(Long id) {
        rep.deleteById(id);
    }
}

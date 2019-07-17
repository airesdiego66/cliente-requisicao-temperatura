package com.clientes.api.clientes;

import com.clientes.api.model.CoordernadasGeograficas;
import com.clientes.api.model.Localizacao;
import com.clientes.api.model.Temperatura;
import com.clientes.domain.Cliente;
import com.clientes.domain.ClienteService;
import com.clientes.domain.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClientesController {
    @Autowired
    private ClienteService service;
    @Autowired
    private HttpServletRequest request;

    @PostMapping
    public ResponseEntity post(@RequestBody ClienteDTO cliente)
    {
        Temperatura t = getTemperature();
        Cliente c = new Cliente();
        c.setNome(cliente.getNome());
        c.setIdade(cliente.getIdade());
        c.setTemperaturaMax(Double.parseDouble(t.getMax_temp()));
        c.setTemperaturaMin(Double.parseDouble(t.getMin_temp()));
        c.setDataCriacao(t.getData_criacao());

        service.insert(c);

        URI location = getUri(c.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody ClienteDTO cliente) {

        Temperatura t = getTemperature();
        Cliente c = new Cliente();
        c.setId(id);
        c.setNome(cliente.getNome());
        c.setIdade(cliente.getIdade());
        c.setTemperaturaMax(Double.parseDouble(t.getMax_temp()));
        c.setTemperaturaMin(Double.parseDouble(t.getMin_temp()));
        c.setDataCriacao(t.getData_criacao());

        ClienteDTO cdto = service.update(c, id);

        return cdto != null ?
                ResponseEntity.ok(cdto) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        ClienteDTO cliente = service.getClienteById(id);

        return ResponseEntity.ok(cliente);
    }

    @GetMapping()
    public ResponseEntity get() {
        List<ClienteDTO> clientes = service.getClientes();
        return ResponseEntity.ok(clientes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        service.delete(id);

        return ResponseEntity.ok().build();
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    private Temperatura getTemperature()
    {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");

        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        //##### Obtendo latitude e longitude #####
        final String uriLongLati = "https://ipvigilante.com/{ip}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("ip", ipAddress);

        RestTemplate restTemplate = new RestTemplate();
        CoordernadasGeograficas resultLatLong = restTemplate.getForObject(
                uriLongLati,
                CoordernadasGeograficas.class,
                params);

        //###### Obtendo woeid ######
        final String uriWoeid = "https://www.metaweather.com/api/location/search/?lattlong=";
        String  paramLatLong = resultLatLong.getCorpo().getLatitude() + ","+ resultLatLong.getCorpo().getLongitude();
        restTemplate = new RestTemplate();

        ResponseEntity<List<Localizacao>> responseLocal = restTemplate.exchange(
                uriWoeid + paramLatLong,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Localizacao>>(){});
        List<Localizacao> localizacoes = responseLocal.getBody();
        Localizacao localizacaoSelecionada = new Localizacao();
        if (!localizacoes.isEmpty() && localizacoes.size() > 0) {
            localizacaoSelecionada = localizacoes.get(0);
        }

        //###### Obtendo temperatura #######
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        String dataAtual = year + "/"+ month + "/"+ day + "/";
        final String uriTemperature = "https://www.metaweather.com/api/location/"
                + localizacaoSelecionada.getWoeid() + "/" +  dataAtual;

        restTemplate = new RestTemplate();
        ResponseEntity<List<Temperatura>> responseTemp = restTemplate.exchange(
                uriTemperature,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Temperatura>>(){});

        List<Temperatura> temperaturas = responseTemp.getBody();
        Temperatura temperaturaSelecionada = new Temperatura();
        if (!temperaturas.isEmpty() && temperaturas.size() > 0) {
            temperaturaSelecionada = temperaturas.get(0);
        }
        temperaturaSelecionada.setData_criacao(date);
        return temperaturaSelecionada;
    }
}

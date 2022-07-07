/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cadastroapp.resource;

import br.com.cadastroapp.model.ValidadorConnection;
import br.com.cadastroapp.repository.validadorconnectionRepository;
import br.com.cadastroapp.services.ClienteService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Everton Molina <everton.molina@herasistemas.com.br>
 */
@Transactional
@Controller
@RequestMapping(value = "/cliente", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteResource {

    @Autowired
    validadorconnectionRepository cr;

    @Autowired
    ClienteService cs;

    @GetMapping("/retornar/{cnpj}")
    public @ResponseBody
    ValidadorConnection retornar(@PathVariable("cnpj") String cnpj) {
        ValidadorConnection cliente = cr.findBycnpj(cnpj);
        return cliente;
    }

    @PostMapping("/cadastrar/")
    public @ResponseBody
    ResponseEntity<?> cadastrar(@RequestBody ValidadorConnection cliente) throws SQLException, IOException, InterruptedException {
        ValidadorConnection cli = new ValidadorConnection();
        ValidadorConnection clienteNovo = new ValidadorConnection();
        //TenantContext.setCurrentTenant("login");
        cli = cr.findBycnpj(cliente.getCnpj());
        if (cli == null) {
            cli = cliente;
            cli.setUsername(cs.gerarUsuarioAleatoria());
            cli.setDatawarehouse(cliente.getCnpj() + "app");
            cli.setSenhaDescriptografada(cs.gerarSenhaAleatoria());
            cli.setPassword(cs.criptografarSenha(cli.getSenhaDescriptografada()));
            cs.salvarCliente(cli);
            clienteNovo = cr.findByCnpj(cli.getCnpj());
            clienteNovo.setSenhaDescriptografada(cli.getSenhaDescriptografada());
            if (clienteNovo.getId() != 0) {
                cs.gerarbaseDadosClienete(cli.getDatawarehouse());
                cs.cadastraGrupoLojaUsuarioDaEmpresa(cliente);
                return ResponseEntity.status(HttpStatus.CREATED).body(clienteNovo);
            } else {
                return ResponseEntity.status(HttpStatus.FOUND).body(clienteNovo);
            }
        } else {
            if (cliente != null && !Objects.equals(cli.getCancelado(), cliente.getCancelado())) {
                cli.setCancelado(cliente.getCancelado());
                cr.save(cli);
                clienteNovo = cr.findByCnpj(cli.getCnpj());
                return ResponseEntity.status(HttpStatus.OK).body(clienteNovo);
            }
        }
        return null;
    }

    @PostMapping("/cancelar/{cnpj}")
    public @ResponseBody
    ResponseEntity<?> cancelar(@PathVariable("cnpj") String cnpj) {
        if (cnpj != null) {
            ValidadorConnection valConnection = cr.findByCnpj(cnpj);
            if (valConnection != null) {
                valConnection.setCancelado(Boolean.TRUE);
                cr.save(valConnection);
                return ResponseEntity.status(HttpStatus.OK).body("Utilização de APP cancelada com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
            }
        }
        return null;
    }
}

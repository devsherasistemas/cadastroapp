/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cadastroapp.repository;

import br.com.cadastroapp.model.ValidadorConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Everton Molina <everton.molina@herasistemas.com.br>
 */
@Repository
public interface validadorconnectionRepository extends JpaRepository<ValidadorConnection, Integer> {

    public ValidadorConnection findBycnpj(String cnpj);
    
    @Modifying
    @Query(value = "insert into grupo value(?1,?2,?3)", nativeQuery = true)
    public void cadastraGrupo(String nome, String cnpj, String dtwh);
    
    @Modifying
    @Query(value = "insert into loja value(?1,?2,?3,?4)", nativeQuery = true)
    public void cadastrarLoja(String numLoja, String cnpj, String grupoId, String nome);
    
    @Modifying
    @Query(value = "insert into usuario (?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
    public void cadastrarUsuario(String cnpj_cpf, String login, String senha, String email,
            int grupo_id, String role, String password, String username, Boolean admin);
    
    /**
     *
     * @param username
     * @return
     */
    public ValidadorConnection findByUsername(String username);

    /**
     *
     * @param cliente
     * @return 
     */
    @Override
    public ValidadorConnection save(ValidadorConnection cliente);
    
    public ValidadorConnection findByCnpj(String cnpj);
    
}

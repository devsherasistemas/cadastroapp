/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cadastroapp.services;

import br.com.cadastroapp.model.ValidadorConnection;
import br.com.cadastroapp.repository.validadorconnectionRepository;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Everton Molina <everton.molina@herasistemas.com.br>
 */
@Service
@Repository
public class ClienteService {

    @Autowired
    EntityManager em;
    @Autowired
    EntityManagerFactory emf;
    @Autowired
    validadorconnectionRepository cr;

    public String caminhoDatabase = "localhost";
    public String senhaDatabase = "240190";

    public String gerarSenhaAleatoria() {
        Random gerador = new Random();
        String senha = "";
        //imprime sequência de 10 números inteiros aleatórios entre 0 e 25
        for (int i = 0; i < 6; i++) {
            System.out.println(senha = senha + gerador.nextInt(9));
        }
        //senha = this.criptografarSenha(senha);
        return senha;
    }

    public String gerarUsuarioAleatoria() {
        ValidadorConnection valConn = new ValidadorConnection();
        String usuario = this.gerarSenhaAleatoria();
        valConn = cr.findByUsername(usuario);
        while (valConn != null) {
            usuario = this.gerarSenhaAleatoria();
            valConn = cr.findByUsername(usuario);
        }
        return usuario;
    }

    public String criptografarSenha(String senha) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(senha);
        return hashedPassword;
    }

    @Transactional
    public int gerarbaseDadosClienete(String datawarehouse) throws SQLException, IOException, InterruptedException {
        int baseCriada = 0;
        System.out.println("Criando datawarehouse " + datawarehouse);
        try {
            Connection connection
                    = DriverManager.getConnection("jdbc:mysql://" + caminhoDatabase + ":3306", "root", senhaDatabase);
            try (Statement stmt = connection.createStatement()) {
                baseCriada = stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + datawarehouse);
                connection.close();
            }
            System.out.println("Datawarehouse construido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //  this.importarSQLDatawarehouse(datawarehouse);
        return 0;
    }

    public void importarSQLDatawarehouse(String datawarehouse) throws IOException, InterruptedException {
        System.out.println("Importando SQL ");
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("cmd /c mysql -h" + caminhoDatabase + " -uroot -p" + senhaDatabase + " -D "
                    + datawarehouse + "< c:\\baseapp.sql");
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
            System.out.println("SQL importado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void cadastraGrupoLojaUsuarioDaEmpresa(ValidadorConnection cliente) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + caminhoDatabase + ":3306/" + cliente.getDatawarehouse(), "root", senhaDatabase);
        } catch (SQLException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("CREATE TABLE `cliente`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `cpf` varchar(255) DEFAULT NULL,"
                    + "  `id_origem` int(11) DEFAULT NULL,"
                    + "  `nome` varchar(255) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `data`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `data_hora` varchar(255) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `finalizadora`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `valor` varchar(255) DEFAULT NULL,"
                    + "  `venda_origem` int(11) DEFAULT NULL,"
                    + "  `venda_id` int(11) DEFAULT NULL,"
                    + "  `nome` varchar(255) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `grupo`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `nome` varchar(255) DEFAULT NULL,"
                    + "  `cnpj` varchar(255) DEFAULT NULL,"
                    + "  `dtwh` varchar(255) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `loja`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `num_loja` varchar(11) DEFAULT NULL,"
                    + "  `cnpj` varchar(255) DEFAULT NULL,"
                    + "  `grupo_id` int(11) DEFAULT NULL,"
                    + "  `nome` varchar(255) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`),"
                    + "  KEY `FKktgpjunltvyee4sx27meh9lab` (`grupo_id`)"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `produto`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `gtin` varchar(255) DEFAULT NULL,"
                    + "  `id_origem` int(11) DEFAULT NULL,"
                    + "  `nome` varchar(255) DEFAULT NULL,"
                    + "  `quantidade_vendida` double DEFAULT NULL,"
                    + "  `valor_venda` double DEFAULT NULL,"
                    + "  `valor_custo` double DEFAULT NULL,"
                    + "  `venda_id` int(11) DEFAULT NULL,"
                    + "  `setor_id` int(11) DEFAULT NULL,"
                    + "  `desconto` double DEFAULT NULL,"
                    + "  `cancelado` varchar(24) DEFAULT NULL,"
                    + "  `devolucao` double DEFAULT NULL,"
                    + "  `acrescimo` double DEFAULT NULL,"
                    + "  `valor_devolvido` double DEFAULT NULL,"
                    + "  `unidade` varchar(255) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`),"
                    + "  KEY `FK2f9gvx04m1l617l107m9ywp9l` (`setor_id`),"
                    + "  KEY `FKpsnko2qbu2dvgp1khucv9my4w` (`venda_id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `setor`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `id_origem` int(11) DEFAULT NULL,"
                    + "  `nome` varchar(255) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `usuario`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `cnpj_cpf` varchar(255) DEFAULT NULL,"
                    + "  `login` varchar(255) NOT NULL,"
                    + "  `senha` varchar(255) NOT NULL,"
                    + "  `email` varchar(255) DEFAULT NULL,"
                    + "  `grupo_id` int(11) DEFAULT NULL,"
                    + "  `role` varchar(255) DEFAULT NULL,"
                    + "  `password` varchar(255) DEFAULT NULL,"
                    + "  `username` varchar(255) DEFAULT NULL,"
                    + "  `admin` bit(1) DEFAULT b'1',"
                    + "  PRIMARY KEY (`id`),"
                    + "  KEY `FKfd6jj7rbshxuteolau6gmukon` (`grupo_id`),"
                    + "  CONSTRAINT `FKfd6jj7rbshxuteolau6gmukon` FOREIGN KEY (`grupo_id`) REFERENCES `grupo` (`id`),"
                    + "  CONSTRAINT `id_fk_grupo` FOREIGN KEY (`grupo_id`) REFERENCES `grupo` (`id`)"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            stmt.executeUpdate("CREATE TABLE `venda`("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `id_origem` int(11) DEFAULT NULL,"
                    + "  `data_id` int(11) DEFAULT NULL,"
                    + "  `cliente_id` int(11) DEFAULT '0',"
                    + "  `loja_id` int(11) DEFAULT '0',"
                    + "  `status_venda` varchar(255) DEFAULT NULL,"
                    + "  `num_loja` int(11) DEFAULT NULL,"
                    + "  `grupo_nome` varchar(255) DEFAULT NULL,"
                    + "  `valor_final` double DEFAULT '0',"
                    + "  `valor_cancelado` double DEFAULT '0',"
                    + "  `valor_venda` double DEFAULT '0',"
                    + "  `data_hora` datetime DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`),"
                    + "  KEY `FK50murhuotq9h2dnxej317jjiy` (`cliente_id`),"
                    + "  KEY `FKhj5bb6c9d8blw324ms6wk5wjf` (`data_id`),"
                    + "  KEY `FK6lg5cbp146t512l70buxjggto` (`loja_id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 ;");
            stmt.executeUpdate("insert into grupo() value('0','" + cliente.getRazaoSocial() + "','" + cliente.getCnpj() + "','" + cliente.getDatawarehouse() + "')");
            stmt.executeUpdate("insert into loja() value('0',0001,'" + cliente.getCnpj() + "','1','" + cliente.getRazaoSocial() + "')");
            stmt.executeUpdate("insert into usuario() value('0','" + cliente.getCnpj() + "',\"admin\",123,'','1',\"ADMIN\",'" + cliente.getPassword() + "','" + cliente.getUsername() + "',true)");
            connection.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public ValidadorConnection salvarCliente(ValidadorConnection cliente) {
        ValidadorConnection clienteNovo = new ValidadorConnection();
        ValidadorConnection cli = new ValidadorConnection();
        if (cliente != null) {
            cli = cr.findByCnpj(cliente.getCnpj());
            if (cli == null) {
                clienteNovo = cr.save(cliente);
            }
        }
        return clienteNovo;
    }
}

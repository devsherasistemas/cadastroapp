/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cadastroapp.services;

import java.io.*;

/**
 * 
 * @author Everton Molina <everton.molina@herasistemas.com.br>
 */
public class teste {
     public static void importarSQLDatawarehouse(String datawarehouse) throws IOException{
         String SLQ = "mysql -hdatabase-1.cfbok3bgeb5r.us-west-2.rds.amazonaws.com -uroot "
                + "-p265969sm -D " + datawarehouse + "  < C:/baseapp/baseapp.sql";
        Runtime run = Runtime.getRuntime();
        run.exec(SLQ);
    }
    public static  void main(String args[]) throws IOException{
 
        importarSQLDatawarehouse("475084110156");
    
    
    }

}

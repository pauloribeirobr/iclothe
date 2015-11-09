package uam.grupoalemanha;

import java.sql.SQLException;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre - 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe ControleEstoque - Gestão dos Estoques
 */

public class ControleEstoque {
    
    public void menuEstoque() throws SQLException {
        
        int opc = 1;
		
	while (opc != 0) {

            System.out.println("================================================");
            System.out.println(" iClothe - Controle de Estoque                  ");
            System.out.println("================================================");
            System.out.println("1 - Saldo em estoque");
            System.out.println("2 - Inserir quantidade em estoque");
            System.out.println("3 - Retirar quantidade em estoque");
            System.out.println("0 - Voltar");
            System.out.print("Qual a sua opção: ");
	
            Scanner scanner = new Scanner(System.in);

            String valorTela = scanner.nextLine();
            opc = Integer.parseInt(valorTela);
            Produto produto = new Produto();

            if (opc == 1) {
		/*
                Lista quantidade em estoque
                */
                System.out.println("==========================================");
                System.out.println("Saldo em estoque                          ");
                System.out.println("------------------------------------------");
                System.out.print("Qual é o ID do produto?: ");
                produto.setId(Integer.parseInt(scanner.nextLine()));
                
                /*
                Verifica se o produto está cadastrado
                */
                if(produto.isProduto(produto.getId())) {
                    System.out.println("++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("Saldo em estoque: " + produto.getEstoqueProduto(produto.getId()) + " " + produto.getUnidadeProduto(produto.getId()));
                    System.out.println("++++++++++++++++++++++++++++++++++++++++");
                }else{
                    System.out.println("Produto " + produto.getId() + " não encontrado!");
                }
            }

            if (opc == 2) {
		System.out.println("==========================================");
                System.out.println("Inserir estoque                           ");
                System.out.println("------------------------------------------");
                System.out.print("Qual é o ID do produto?: ");
                produto.setId(Integer.parseInt(scanner.nextLine()));
                
                /*
                Verifica se o produto está cadastrado
                */
                if(produto.isProduto(produto.getId())) {
                    System.out.println("Saldo atual: " + produto.getEstoqueProduto(produto.getId()) + " " + produto.getUnidadeProduto(produto.getId()));
                    System.out.print("Quantidade a inserir: ");
                    if(produto.inserirEstoque(produto.getId(),Integer.parseInt(scanner.nextLine()))) {
                        System.out.println("++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("Saldo atual " + produto.getEstoqueProduto(produto.getId()) + " " + produto.getUnidadeProduto(produto.getId()));
                        System.out.println("++++++++++++++++++++++++++++++++++++++++");  
                    }else{
                        System.out.println("Problema na inclusão de estoque!");
                    }
        
                }else{
                    System.out.println("Produto " + produto.getId() + " não encontrado!");
                }
            }
            
            if (opc == 3) {
                System.out.println("==========================================");
                System.out.println("Retirar estoque                           ");
                System.out.println("------------------------------------------");
                System.out.print("Qual é o ID do produto?: ");
                produto.setId(Integer.parseInt(scanner.nextLine()));
                /*
                Verifica se o produto está cadastrado
                */
                if(produto.isProduto(produto.getId())) {
                    System.out.println("Saldo atual: " + produto.getEstoqueProduto(produto.getId()) + " " + produto.getUnidadeProduto(produto.getId()));
                    System.out.print("Quantidade a retirar: ");
                    if(produto.retirarEstoque(produto.getId(),Integer.parseInt(scanner.nextLine()))) {
                        System.out.println("++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("Saldo atual " + produto.getEstoqueProduto(produto.getId()) + " " + produto.getUnidadeProduto(produto.getId()));
                        System.out.println("++++++++++++++++++++++++++++++++++++++++");  
                    }else{
                        System.out.println("++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("Saldo insuficiente!");
                        System.out.println("++++++++++++++++++++++++++++++++++++++++");
                    }
        
                }else{
                    System.out.println("Produto " + produto.getId() + " não encontrado!");
                }
                
                
            }
	}
        
    }
    
}

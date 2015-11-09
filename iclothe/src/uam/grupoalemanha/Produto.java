package uam.grupoalemanha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre - 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe Produto - Produtos do iClothe
 */
public class Produto {
    /*
    Atributos da Classe Produto
    */
    private int id;
    private String descricao;
    private String unidade;
    private String ncm;
    private double valorUnitario;
    private int qtde;
    
    /*
    Gets e Sets de cada atributo
    */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }
    
    /*
    Atributos de uso nas Operações do Banco
     */
    boolean resultadoOperacao = false;
    String stringDeConexao = "jdbc:sqlite:db/iclothe.db";
    
    /*
    Método menuProduto
    */
    public void menuProduto() throws SQLException {
        int opc = 1;
		
	while (opc != 0) {

            System.out.println("================================================");
            System.out.println(" iClothe - Cadastro de Produtos                 ");
            System.out.println("================================================");
            System.out.println("1 - Listar Produtos");
            System.out.println("2 - Cadastrar Produto");
            System.out.println("3 - Editar Produto");
            System.out.println("0 - Voltar");
            System.out.print("Qual a sua opção: ");
	
            Scanner scanner = new Scanner(System.in);

            String valorTela = scanner.nextLine();
            opc = Integer.parseInt(valorTela);

            if (opc == 1) {
		/*
                Método listarProdutos()
                */
                listarProdutos();
             }

            if (opc == 2) {
		/*
                Método inserirProduto
                */
                inserirProduto();
            }
            
            if (opc == 3) {
		/*
                Método editarProduto
                */
                editarProduto();
            }
	}
        
    }
    
    /*
    Método inserirProduto() - Cadastra o Produto na Base de Dados
    */
    public void inserirProduto() throws SQLException  {
        
        boolean ncmEncontrado   = false;
		
        Scanner scanner         = new Scanner(System.in);
        Produto produto         = new Produto();
        Imposto imposto         = new Imposto();
        
        System.out.println("==========================================");
        System.out.println("Cadastrar Novo Produto                    ");
        System.out.println("------------------------------------------");
        
        /*
        Começa pela digitação do NCM, assim verifica se o mesmo existe
        */
        while(!ncmEncontrado) {
            System.out.print("Digite o NCM: ");
            produto.setNcm(scanner.nextLine());
            if(!imposto.verificaNCM(produto.getNcm())) {
                System.out.println("O NCM " + produto.getNcm() + " não foi encontrado! Tente novamente.");
            }else{
                ncmEncontrado = true;
            }
        }
        System.out.print("Descrição do Produto: ");
        produto.setDescricao(scanner.nextLine());
        System.out.print("Unidade: PC , M , CX: (max: 2 caracteres)");
        produto.setUnidade(scanner.nextLine());
        System.out.print("Quantidade em Estoque (0) se não souber: ");
        produto.setQtde(Integer.parseInt(scanner.nextLine()));
        System.out.print("Valor Unitário (0 se não souber) - Formato 00.00:");
        produto.setValorUnitario(Double.parseDouble(scanner.nextLine()));
        
        /*
        Insere o produto no Banco de Dados
        */
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("INSERT INTO PRODUTOS (NCM, DESCRICAO, UNIDADE, "
                + "QTDE, VALOR_UNITARIO) VALUES (?, ?, ?, ?, ?)");
            comando.setString(1, produto.getNcm());
            comando.setString(2, produto.getDescricao());
            comando.setString(3, produto.getUnidade());
            comando.setInt(4, produto.getQtde()); 
            comando.setDouble(5, produto.getValorUnitario());
            comando.execute();
                
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexao.close();
        }
        
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println("Produto Cadastrado com Sucesso!");
        System.out.println("+++++++++++++++++++++++++++++++");
        
    }
    
     /*
     * Metodo listarProdutos() - Lista todos os Produtos cadastrados
     */
    public void listarProdutos() throws SQLException {

        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT ID, NCM, DESCRICAO, UNIDADE, "
                + "QTDE, VALOR_UNITARIO FROM PRODUTOS ORDER BY DESCRICAO");
            ResultSet resultado = comando.executeQuery();
            System.out.println("===========================================================================================");
            System.out.println("Relação de Produtos                                                                        ");
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println("ID  NCM      DESCRICAO                                             QTDE    UNIDADE UNITARIO");
            System.out.println("-------------------------------------------------------------------------------------------");

            while (resultado.next()) {
                /*
                 * Caracteres de Formatação - Ex: %-15s - 15 posições, alinhado a esquerda
                 */
                System.out.printf("%-4s"	, resultado.getInt("ID"));
                System.out.printf("%-9s"	, resultado.getString("NCM"));
                System.out.printf("%-55s"	, resultado.getString("DESCRICAO"));
                System.out.printf("%-8s"	, resultado.getInt("QTDE"));     
                System.out.printf("%-10s"	, resultado.getString("UNIDADE"));
                System.out.printf("%-10s"	, resultado.getDouble("VALOR_UNITARIO"));
                System.out.println("");
            }
            System.out.println("-------------------------------------------------------------------------------------------");

        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            conexao.close();
        }

    }
    
    /*
    Método editarProduto()
    */
    public void editarProduto() throws SQLException {
    
        /*
        Atributos do Produto selecionado
        */
        String descricaoAntiga      = null;
        String unidadeAntiga        = null;
        String ncmAntigo            = null;
        Double valorUnitarioAntigo  = null;
        int qtdeAntiga              = 0;
        
        boolean produtoEncontrado   = false;
        boolean ncmEncontrado       = false;
        
        Scanner scanner = new Scanner(System.in);
        Imposto imposto = new Imposto();
        Produto produto = new Produto();
        
        System.out.println("==========================================");
        System.out.println("Editar Produto                            ");
        System.out.println("------------------------------------------");
        System.out.print("Qual é o ID do produto?: ");
        produto.setId(Integer.parseInt(scanner.nextLine()));
        
        /*
        Pesquisa se o produto existe
        */
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {

            PreparedStatement comando = conexao.prepareStatement("SELECT ID, NCM, DESCRICAO,"
                    + "QTDE, UNIDADE, VALOR_UNITARIO FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, produto.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                ncmAntigo           = linhas.getString("NCM");
                descricaoAntiga     = linhas.getString("DESCRICAO");
                qtdeAntiga          = linhas.getInt("QTDE");
                unidadeAntiga       = linhas.getString("UNIDADE");
                valorUnitarioAntigo = linhas.getDouble("VALOR_UNITARIO");
                produtoEncontrado           = true;

            }else{
                System.out.println("Produto " + produto.getId() + " nao foi encontrado!");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        /*
        Se o produto foi encontrado, parte para a edição
        */
        if(produtoEncontrado) {
            while(!ncmEncontrado) {
                System.out.println("NCM antigo: " + ncmAntigo);
                System.out.println("Qual o novo NCM? ");
                produto.setNcm(scanner.nextLine());
                if(!imposto.verificaNCM(produto.getNcm())) {
                    System.out.println("O NCM " + produto.getNcm() + " não foi encontrado! Tente novamente.");
                }else{
                    ncmEncontrado = true;
                }
            }
            
            System.out.println("Descrição antiga: " + descricaoAntiga);
            System.out.print("NOVA descricao (ENTER - mantem a mesma):");
            /*
            Se a digitacao for em branco, a nova variavel mantem o valor da antiga
            */
            produto.setDescricao(scanner.nextLine());
            if(produto.getDescricao().isEmpty()) {
                produto.setDescricao(descricaoAntiga);
            }
            
            System.out.println("Unidade antiga: " + unidadeAntiga);
            System.out.print("Nova Unidade: PC , M , CX: (max: 2 caracteres)");
            produto.setUnidade(scanner.nextLine());
            if(produto.getUnidade().isEmpty()) {
                produto.setUnidade(unidadeAntiga);
            }           
                        
            System.out.println("Quantidade antiga: " + qtdeAntiga);
            System.out.print("Quantidade em Estoque (0) se não souber: "); 
            produto.setQtde(Integer.parseInt(scanner.nextLine()));  
            
            System.out.println("Valor Unitário antigo:" + valorUnitarioAntigo);
            System.out.print("Valor Unitário (0 se não souber) - Formato 00.00:");
            produto.setValorUnitario(Double.parseDouble(scanner.nextLine()));
            
            /*
            E agora faz a atualização do produto
            */
            try {
                PreparedStatement comando = conexao.prepareStatement("UPDATE PRODUTOS SET NCM = ?,"
                        + "DESCRICAO = ?, QTDE = ?, UNIDADE = ?, VALOR_UNITARIO = ? WHERE ID = ?");
                comando.setString(1, produto.getNcm());
                comando.setString(2, produto.getDescricao());
                comando.setInt(3, produto.getQtde());
                comando.setString(4, produto.getUnidade()); 
                comando.setDouble(5, produto.getValorUnitario());
                comando.setInt(6, produto.getId());

                comando.execute();
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexao.close();
            }
            
            System.out.println("+++++++++++++++++++++++++++++");            
            System.out.println("Produto alterado com SUCESSO!");
            System.out.println("+++++++++++++++++++++++++++++"); 
        }
         
    }
    
    /*
    Método isProduto() -Verifica se o produto está cadastrado
    */
    public boolean isProduto(int id) throws SQLException {
        
        this.setId(id);
        boolean produtoEncontrado = false;
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT ID FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                
                produtoEncontrado = true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return produtoEncontrado;
    }
    
    /*
    Método getEstoqueProduto() - Retorna o estoque do produto
    */
    public int getEstoqueProduto(int id) throws SQLException {
        int qtde = 0; 
        this.setId(id);
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT QTDE FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                qtde    = linhas.getInt("QTDE");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return qtde;
         
    }
    
    /*
    Método inserirEstoque() - Insere quantidade em estoque
    */
    public boolean inserirEstoque(int id, int qtde) throws SQLException {
        int qtdeAtual   = 0; 
        this.setId(id);
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        /*
        Primeiro pega o saldo atual
        */
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT QTDE FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                qtdeAtual = linhas.getInt("QTDE");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        /*
        Determina o novo estoque
        */
        qtde = qtdeAtual + qtde;
        
        /*
        Atualiza o estoque na Base de Dados
        */
         try {
            PreparedStatement comando = conexao.prepareStatement("UPDATE PRODUTOS SET QTDE = ? WHERE ID = ?");
            comando.setInt(1, qtde);
            comando.setInt(2, this.getId());
            comando.execute();

        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return true;
    }
    
    /*
    Método retirarEstoque() - Retira quantidade em estoque
    */
    public boolean retirarEstoque(int id, int qtde) throws SQLException {
        int qtdeAtual   = 0; 
        this.setId(id);
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        /*
        Primeiro pega o saldo atual
        */
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT QTDE FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                qtdeAtual = linhas.getInt("QTDE");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        /*
        Se a quantidade a ser retirada for MAIOR que a atual, não permite
        */
        if (qtde > qtdeAtual) {
            return false;
        } 
        /*
        Determina o novo estoque
        */
        qtde = qtdeAtual - qtde;
        
        /*
        Atualiza o estoque na Base de Dados
        */
         try {
            PreparedStatement comando = conexao.prepareStatement("UPDATE PRODUTOS SET QTDE = ? WHERE ID = ?");
            comando.setInt(1, qtde);
            comando.setInt(2, this.getId());
            comando.execute();

        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return true;
    }
    
    
    
    
    /*
    Método getUnidadeProduto() - Retorna a unidade do produto
    */
    public String getUnidadeProduto(int id) throws SQLException {
        String unidade = null; 
        this.setId(id);
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT UNIDADE FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                unidade    = linhas.getString("UNIDADE");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return unidade;
    }
    
    /*
    Método getNCMProduto() - Retorna o NCM do Produto
    */
    public String getNCMProduto(int id) throws SQLException {
        String ncm = null; 
        this.setId(id);
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT NCM FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                ncm    = linhas.getString("NCM");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return ncm;
    }
    
    /*
    Método getDescricaoProduto() - Retorna a Descricao do Produto
    */
    public String getDescricaoProduto(int id) throws SQLException {
        String descricao = null; 
        this.setId(id);
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT DESCRICAO FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                descricao    = linhas.getString("DESCRICAO");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return descricao;
    }
    
    /*
    Método getValorUnitarioProduto() - Retorna o Valor Unitário do Produto
    */
    public double getValorUnitarioProduto(int id) throws SQLException {
        double valorUnitario = 0;
        this.setId(id);
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT VALOR_UNITARIO FROM PRODUTOS WHERE ID = ?");
            comando.setInt(1, this.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                valorUnitario    = linhas.getDouble("VALOR_UNITARIO");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        return valorUnitario;
    }

}

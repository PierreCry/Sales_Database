package Modele;

import Object.Customer;
import Object.Order;
import Object.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DAO {

    private final DataSource myDataSource;

    public DAO(DataSource dataSource) {
        this.myDataSource = dataSource;
    }

    //CLIENT
    //Partie Login Client
    //Requête permettant de retrouver les infos d'un client via son email
    public Customer Customer(String email) throws SQLException {
        Customer c = null;
        String sql = "SELECT * FROM Customer WHERE email = ?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setString(1, email); // On fixe le 1° paramètre de la requête
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("CUSTOMER_ID");
                    String dc = rs.getString("DISCOUNT_CODE");
                    int zip = rs.getInt("ZIP");
                    String name = rs.getString("NAME");
                    String adress = rs.getString("ADDRESSLINE1");
                    String adress2 = rs.getString("ADDRESSLINE2");
                    String city = rs.getString("CITY");
                    String state = rs.getString("STATE");
                    String phone = rs.getString("PHONE");
                    String fax = rs.getString("FAX");
                    int credit = rs.getInt("CREDIT_LIMIT");
                    c = new Customer(id, dc, zip, name, adress, adress2, city, state, phone, fax, email, credit);
                }
            }
        }
        return c;
    }

    //Requête permettant de modifier un Client
    public int updateCustomer(int id, String name, String address1, String city, String State, String Phone, String Email, int credit) throws SQLException {
        String sql = "UPDATE CUSTOMER SET NAME=?, ADDRESSLINE1 = ?, CITY = ?, STATE = ?, PHONE=?,CREDIT_LIMIT=? WHERE EMAIL=?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Définir la valeur du paramètre
            stmt.setString(1, name);
            stmt.setString(2, address1);
            stmt.setString(3, city);
            stmt.setString(4, State);
            stmt.setString(5, Phone);
            stmt.setInt(6, credit);
            stmt.setString(7, Email);

            return stmt.executeUpdate();
        }
    }

    //Requête renvoyant une liste de tous les états
    public List<String> existingStates() throws Exception {
        List<String> result = new LinkedList<>();
        String sql = "SELECT DISTINCT STATE FROM CUSTOMER";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String state = rs.getString("STATE");
                result.add(state);
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
        return result;
    }

    //Partie Commande
    //Requête qui insère une commande
    //c_id le customer ID doit déjà existé ainsi que le p_id qui est le product_ID
    public void insertOrder(int ordernum, int c_id, int p_id, int quantity, float shipping, String sale_d, String shipping_d, String Company) throws SQLException {
        String sql = "INSERT INTO PURCHASE_ORDER VALUES(?, ?, ?,?,?,?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ordernum);
            stmt.setInt(2, c_id);
            stmt.setInt(3, p_id);
            stmt.setInt(4, quantity);
            stmt.setFloat(5, shipping);
            stmt.setString(6, sale_d);
            stmt.setString(7, shipping_d);
            stmt.setString(8, Company);
            stmt.executeUpdate();
        }
    }

    //Requête qui supprimer une commande
    public int deleteOrder(int ordernum) throws Exception {
        String sql = "DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM=?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ordernum);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }
    }

    //Requête qui met à jour une commande
    public int updateOrder(int ordernum, int quantity, double price) throws SQLException {
        String sql = "UPDATE PURCHASE_ORDER SET QUANTITY=?, SHIPPING_COST = ? WHERE ORDER_NUM=?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setDouble(2, price);
            stmt.setInt(3, ordernum);
            return stmt.executeUpdate();

        }
    }

    //Requête qui récupère le prix d'une commande
    public float Po_Price(int ordernum) throws SQLException {
        float result = 0;
        String sql = "SELECT SHIPPING_COST AS COST FROM PURCHASE_ORDER WHERE ORDER_NUM=?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, ordernum);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = rs.getFloat("COST");
                }
            }

        }
        return result;
    }

    //Requête qui renvoie les commandes par clients
    public List<Order> OrderByClient(int id) throws SQLException {
        List<Order> list = new LinkedList();
        String sql = "SELECT * FROM Purchase_Order WHERE Customer_ID=?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int ordernum = rs.getInt("ORDER_NUM");
                    int prod_id = rs.getInt("PRODUCT_ID");
                    int quantity = rs.getInt("Quantity");
                    float cost = rs.getFloat("SHIPPING_COST");
                    String sale_d = rs.getString("SALES_DATE");
                    String shipping_d = rs.getString("SHIPPING_DATE");
                    Order o = new Order(ordernum, id, prod_id, quantity, cost, sale_d, shipping_d);
                    list.add(o);
                }
            }
        }
        return list;
    }

    //ADMINISTRATEUR
    //Partie Produit
    //Requête qui permet d'insérer un produit
    //Faut que le man_id soit dans MANUFACTURER et que le code soit dans PRODUCT_CODE
    public void insertProduct(int id, int man_id, String code, double Purchase, int quantity, double markup, boolean available, String Description) throws SQLException {
        String sql = "INSERT INTO PRODUCT VALUES(?, ?, ?,?,?,?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, man_id);
            stmt.setString(3, code);
            stmt.setDouble(4, Purchase);
            stmt.setInt(5, quantity);
            stmt.setDouble(6, markup);
            stmt.setBoolean(7, available);
            stmt.setString(8, Description);
            stmt.executeUpdate();
        }
    }

    //Requête qui modifie le produit
    public int updateProduct(int id, double purchase, int quantity, double markup, String desc) throws SQLException {
        String sql = "UPDATE PRODUCT SET PURCHASE_COST=?,QUANTITY_ON_HAND =?,MARKUP=?,DESCRIPTION=? WHERE PRODUCT_ID=?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, purchase);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, markup);
            stmt.setString(4, desc);
            stmt.setInt(5, id);
            return stmt.executeUpdate();
        }
    }

    //Requêtes qui supprime un produit
    public int deleteProduct(int id) throws Exception {
        String sql = "DELETE FROM PRODUCT WHERE PRODUCT_ID= ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }
    }

    //Requête qui récupère tout les Manufacturer_ID
    public List<Integer> allMan_ID() throws SQLException {
        List<Integer> result = new LinkedList<>();
        String sql = "SELECT MANUFACTURER_ID FROM MANUFACTURER";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("MANUFACTURER_ID");
                result.add(id);
            }
        }
        return result;
    }

    //Requête qui récupère tout les Product_Code
    public List<String> allProd_Code() {
        List<String> result = new LinkedList<>();
        String sql = "SELECT PROD_CODE FROM PRODUCT_CODE";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString("PROD_CODE");
                result.add(code);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    //Requête qui renvoie le manufacturer pour un produit donnée
    public String ManbyProduct(int product) throws SQLException {
        String result = null;
        String sql = "SELECT NAME AS NOM \n"
                + "    FROM MANUFACTURER \n"
                + "        INNER JOIN PRODUCT \n"
                + "            ON PRODUCT.MANUFACTURER_ID=MANUFACTURER.MANUFACTURER_ID and PRODUCT.PRODUCT_ID=?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, product);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString("NOM");
                }
            }
        }
        return result;
    }

    //Requete qui renvoie le prix d'un produit donnée
    public int ProductPrice(int product) throws SQLException {
        int result = 0;
        String sql = "SELECT PURCHASE_COST AS COST FROM PRODUCT WHERE PRODUCT_ID = ?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, product);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("COST");
                }
            }
        }
        return result;
    }

    //Requete qui donne le numéro maximale de l'OrderNum d'une commande
    public int maxOrderNum() throws SQLException {
        int result = 0;
        String sql = "SELECT MAX(ORDER_NUM) AS MAXI FROM PURCHASE_ORDER";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                result = rs.getInt("MAXI");
            }
        }
        return result;
    }

    //PARTIE CHARTS 
    //Requête qui renvoie le chiffre d'affaire par clients
    //Classer par ordre alphabétique des noms des clients
    public Map<String, Double> CustomerCA(String beg, String end) throws SQLException {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT CUSTOMER.\"NAME\" AS NAME,SUM(PRODUCT.PURCHASE_COST*PURCHASE_ORDER.QUANTITY) AS COUT \n"
                + "                    FROM PURCHASE_ORDER \n"
                + "                       INNER JOIN PRODUCT ON PURCHASE_ORDER.PRODUCT_ID=PRODUCT.PRODUCT_ID \n"
                + "                           INNER JOIN CUSTOMER ON PURCHASE_ORDER.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID and PURCHASE_ORDER.SALES_DATE BETWEEN ? and ?\n"
                + "                                GROUP BY CUSTOMER.\"NAME\"";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setString(1, beg);
            statement.setString(2, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("NAME"), rs.getDouble("COUT"));
                }
            }
            return result;
        }
    }

    //Requête qui renvoie le chiffre d'affaire par états
    //Classer par ordre alphabétique des etats
    public Map<String, Double> StateCA(String beg, String end) throws SQLException {
        Map<String, Double> result = new HashMap();
        String sql = "SELECT CUSTOMER.STATE AS STATE,SUM(PRODUCT.PURCHASE_COST*PURCHASE_ORDER.QUANTITY) AS COUT\n"
                + "                    FROM CUSTOMER INNER JOIN PURCHASE_ORDER ON CUSTOMER.CUSTOMER_ID=PURCHASE_ORDER.CUSTOMER_ID\n"
                + "                      INNER JOIN PRODUCT ON PURCHASE_ORDER.PRODUCT_ID=PRODUCT.PRODUCT_ID and PURCHASE_ORDER.SALES_DATE BETWEEN ? and ?\n"
                + "                          GROUP BY CUSTOMER.STATE";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setString(1, beg);
            statement.setString(2, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("STATE"), rs.getDouble("COUT"));
                }
            }
            return result;
        }
    }

    //Requête qui renvoie le chiffre d'affaire par catégorie de produits
    //Classer par ordre alphabétique des description.
    public Map<String, Double> ProductCA(String beg, String end) throws SQLException {
        Map<String, Double> result = new HashMap();
        String sql = "SELECT PRODUCT_CODE.DESCRIPTION AS DESCRIPTION,SUM(PRODUCT.PURCHASE_COST*PURCHASE_ORDER.QUANTITY) AS COUT  \n"
                + "    FROM PRODUCT_CODE INNER JOIN PRODUCT ON PRODUCT.PRODUCT_CODE=PRODUCT_CODE.PROD_CODE\n"
                + "    INNER JOIN PURCHASE_ORDER ON PURCHASE_ORDER.PRODUCT_ID=PRODUCT.PRODUCT_ID and PURCHASE_ORDER.SALES_DATE BETWEEN ? and ?\n"
                + "    GROUP BY PRODUCT_CODE.DESCRIPTION";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setString(1, beg);
            statement.setString(2, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("DESCRIPTION"), rs.getDouble("COUT"));
                }
            }
            return result;
        }
    }

    //REQUETES PAS UTILISE DANS L'APPLICATION MAIS LAISSE ICI POUR 
    //MONTRER LES RECHERCHES...
    //Requête permettant d'envoyer les informations sur une commande
    public Order findOrder(int ordernum) throws SQLException {
        Order result = null;
        String sql = "SELECT * FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, ordernum);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int c_id = rs.getInt("CUSTOMER_ID");
                    int p_id = rs.getInt("PRODUCT_ID");
                    int q = rs.getInt("QUANTITY");
                    float sp = rs.getFloat("SHIPPING_COST");
                    String sd = rs.getString("SALES_DATE");
                    String sh_d = rs.getString("SHIPPING_DATE");
                    result = new Order(ordernum, c_id, p_id, q, sp, sd, sh_d);
                }
            }
        }
        return result;
    }

    //Requête qui renvoie le nombre de commandes
    public int allOrder() throws SQLException {
        int result = 0;
        String sql = "SELECT COUNT(*) AS NUMBER FROM PURCHASE_ORDER";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) { // Tant qu'il y a des enregistrements
                result = rs.getInt("NUMBER");
            }
        }
        return result;
    }

    //Requête récupérant tout les ID des Customer
    public List<Integer> listCustomerID() throws SQLException {
        List<Integer> list = new LinkedList();
        String sql = "SELECT Customer_ID FROM Customer AS Liste";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("Customer_ID"));
            }
        }
        return list;
    }

    //Requête calculant la somme payé par un client
    public double costByClient(int id) throws SQLException {
        double res = 0;
        String sql = "SELECT SUM(PRODUCT.PURCHASE_COST*PURCHASE_ORDER.QUANTITY) AS COUT FROM PURCHASE_ORDER INNER JOIN PRODUCT ON PURCHASE_ORDER.PRODUCT_ID=PRODUCT.PRODUCT_ID and PURCHASE_ORDER.CUSTOMER_ID=?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, id);
            // On fixe le 1° paramètre de la requête
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    res = rs.getDouble("COUT");
                }
            }
        }
        return res;
    }

    //Requête donnant la quantité acheté par un client
    public int quantityByClient(int id) throws SQLException {
        int res = 0;
        String sql = "SELECT SUM(Quantity) AS NUMBER FROM Purchase_Order WHERE Customer_ID=?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, id); // On fixe le 1° paramètre de la requête
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    res = rs.getInt("NUMBER");
                }
            }
        }
        return res;
    }

    //Requête récupérant toutes les adresses mails des clients
    public List<String> allEmails() throws SQLException {
        List<String> result = new LinkedList<>();
        String sql = "SELECT EMAIL FROM Customer";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String email = rs.getString("EMAIL");

                result.add(email);
            }
        }
        return result;
    }

    //Requête qui compte le nombre de clients
    public int numberCustomer() throws SQLException {
        int result = 0;
        String sql = "SELECT COUNT(*) AS Number FROM Customer";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                result = rs.getInt("NUMBER");
            }

        }
        return result;
    }

    //Requête qui renvoie tous les produits dans une liste
    public List<Product> AllProduct() throws SQLException {
        List<Product> result = new LinkedList();
        String sql = "SELECT * FROM Product";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product(rs.getInt("Product_ID"),
                        rs.getInt("MANUFACTURER_ID"),
                        rs.getString("PRODUCT_CODE"),
                        rs.getFloat("PURCHASE_COST"),
                        rs.getInt("QUANTITY_ON_HAND"),
                        rs.getFloat("MARKUP"),
                        rs.getBoolean("AVAILABLE"),
                        rs.getString("Description"));
                result.add(p);
            }
        }
        return result;
    }

    //Requête qui compte le nombre de produits
    public int numberProduct() throws SQLException {
        int result = 0;
        String sql = "SELECT COUNT(*) AS Number FROM Product";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                result = rs.getInt("NUMBER");
            }
        }
        return result;
    }

    //Requête qui renvoie toutes les informations d'un produit
    public Product findProduct(int productId) throws SQLException {
        Product result = null;
        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_ID = ?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = new Product(productId,
                            rs.getInt("MANUFACTURER_ID"),
                            rs.getString("PRODUCT_CODE"),
                            rs.getFloat("PURCHASE_COST"),
                            rs.getInt("QUANTITY_ON_HAND"),
                            rs.getFloat("MARKUP"),
                            rs.getBoolean("AVAILABLE"),
                            rs.getString("Description"));
                }
            }
        }
        return result;
    }
}

package Modele;


import Object.Customer;
import Object.Order;
import Object.Product;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DAOTest {
    
    private static DataSource myDataSource; // La source de données à utiliser
    private static Connection myConnection ;	
    private DAO myDAO;
    
    @Before
    public void setUp() throws SQLException, IOException, SqlToolError {
        
        // On crée la connection vers la base de test "in memory"
        myDataSource = getDataSource();
	myConnection = myDataSource.getConnection();
        
        // On initialise la base avec le contenu d'un fichier de test
        String sqlFilePath = DAOTest.class.getResource("TestData.sql").getFile();
	SqlFile sqlFile = new SqlFile(new File(sqlFilePath));
	sqlFile.setConnection(myConnection);
	sqlFile.execute();
	sqlFile.closeReader();
        
        // On crée l'objet à tester
	myDAO = new DAO(myDataSource);
        
    }
    
    @After
    public void tearDown() throws SQLException {
        myConnection.close();
    }

    private DataSource getDataSource() {
        org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
	ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
	ds.setUser("sa");
	ds.setPassword("sa");
	return ds;
    }
    //CLIENT
    
    //Partie Client
    
    @Test
    public void CustomerTest() throws SQLException {
        Customer c = myDAO.Customer("apple@example.com");
        assertEquals("Apple",c.getName());
    }
    
    @Test
    public void updateCustomerTest() throws Exception{
        Customer c = myDAO.Customer("apple@example.com");
        String name = c.getName();
        String address1 = c.getAddressLine1();
        assertEquals(name,"Apple");
        assertEquals(address1,"1. Apple Park Way");
        myDAO.updateCustomer(10, "Girafe","adress1",  "City", "FR", "558-956-5854", "apple@example.com", 500);
        Customer c2 = myDAO.Customer("apple@example.com");
        String nameT = c2.getName();
        String address1T = c2.getAddressLine1();
        assertEquals(nameT,"Girafe");
        assertEquals(address1T,"adress1");
    }
    
    @Test
    public void existingStatesTest() throws Exception{
        List<String> States= myDAO.existingStates();
        String e = States.get(0);
        assertEquals(e,"CA");
        assertEquals(States.size(),2);
    }
    
    //Partie Commande
    
    @Test
    public void insertOrderTest() throws Exception{
        int c = myDAO.allOrder();
        assertEquals(c,2);
        myDAO.insertOrder(5485, 10, 1, 10, (float) 25.00, "2018-11-29","2018-12-01","UPS");
        int c2 = myDAO.allOrder();
        assertEquals(c2,c+1);
    }
    
    @Test
    public void deleteOrderTest() throws Exception{
        int c = myDAO.allOrder();
        assertEquals(c,2);
        myDAO.insertOrder(5485, 10, 1, 10, (float) 25.00, "2018-11-29","2018-12-01","UPS");
        int c2 = myDAO.allOrder();
        assertEquals(c2,c+1);
        myDAO.deleteOrder(5485);
        int c3 = myDAO.allOrder();
        assertEquals(c3,c);
    }
    
    @Test
    public void updateOrderTest() throws Exception{
        Order o = myDAO.findOrder(22112018);
        assertEquals(o.getQuantity(),1);
        myDAO.updateOrder(22112018, 10,25.0);
        Order o2 = myDAO.findOrder(22112018);
        assertEquals(o2.getQuantity(),10);
        assertEquals(o2.getShippingCost(),25.0,0.0);
    }

    @Test
    public void Po_Price() throws SQLException{
        float p = myDAO.Po_Price(15092018);
        assertEquals(p,1500.0,0.0);
    }
    
    @Test
    public void OrderByClientTest() throws Exception{
        List<Order> l = myDAO.OrderByClient(10);
        int p = l.get(0).getProductID();
        assertEquals(1,p);
        assertEquals(1,l.size());
    }
    
    
    //ADMINISTRATEUR
    
    //Partie Produit
    
    @Test
    public void insertProductTest() throws SQLException{
        myDAO.insertProduct(45, 666, "SW", 5.5, 20, 5.5, true, "Description en cours");
        
        assertEquals(myDAO.numberProduct(),3);
   }
   
    @Test
    public void updateProductTest() throws SQLException{
        Product p = myDAO.findProduct(1);
        assertEquals(p.getDescription(),"Accounting Application");
        myDAO.updateProduct(1, 45.0, 10, 20.0, "Computer");
        Product p2 = myDAO.findProduct(1);
        assertEquals(p2.getDescription(),"Computer");
    }
    
    @Test
    public void deleteProductTest() throws SQLException, Exception{
        myDAO.insertProduct(45, 666, "SW", 5.5, 20, 5.5, true, "Description en cours");
        myDAO.deleteProduct(45);
        assertEquals(myDAO.numberProduct(),2);
    }
    
    @Test
    public void allMan_IDTest() throws Exception{
        List<Integer> ManID=myDAO.allMan_ID();
        assertEquals(ManID.size(),2);
        int m =ManID.get(0);
        assertEquals(m,666);
    }
    
    @Test
    public void allProd_CodeTest() throws Exception{
        List<String> Pc=myDAO.allProd_Code();
        assertEquals(Pc.size(),2);
        String code =Pc.get(0);
        assertEquals(code,"CB");
    }
    
    @Test
    public void ManByProductTest() throws Exception{
        assertEquals(myDAO.ManbyProduct(1),"Lennox International Inc.");
    }
    
    @Test
    public void PriceProductTest() throws Exception{
        int p = myDAO.ProductPrice(2);
        assertEquals(p,13600);
    }
    
    @Test 
    public void maxOrdernumTest() throws Exception{
        int m = myDAO.maxOrderNum();
        assertEquals(m,22112018);
}
    
    
    //Partie Charts
    
    @Test
    public void CustomerCATest() throws Exception{
        Map<String, Double>  res=myDAO.CustomerCA("2018-09-14","2018-11-23");
        assertEquals(res.size(),2);
        assertEquals(res.get("IBM"),5*13600,0.0);
    }
    
    @Test
    public void StateCATest() throws Exception{
        Map<String, Double> res=myDAO.StateCA("2018-09-14","2018-11-23");
        assertEquals(res.size(),2);
        assertEquals(res.get("NY"),5*13600,0.0);
    }
    
    @Test
    public void ProductCATest() throws Exception{
        Map<String, Double>  res=myDAO.ProductCA("2018-09-14","2018-11-23");
        assertEquals(res.size(),2);
        assertEquals(res.get("Cables"),5*13600,0.0);
    }
    
    
    
    
    //RECHERCHE FAITES MAIS REQUETES NON UTILISER DANS APPLICATION
    
    @Test
    public void findOrderTest() throws Exception{
        Order c = myDAO.findOrder(22112018);
        assertEquals(c.getCustomerID(),10);
    }
    
    @Test 
    public void allOrderTest() throws Exception{
        int c = myDAO.allOrder();
        assertEquals(c,2);
    }
    
    @Test
    public void ListCustomerIDTest() throws SQLException {
        List<Integer> id = myDAO.listCustomerID();
        assertEquals(myDAO.numberCustomer(),id.size());
    }
    
    @Test
    public void costByClient() throws SQLException{
        double c = myDAO.costByClient(10);
        assertEquals(c,175.0,0.0);
    }
    
    
    @Test 
    public void quantityByClientTest() throws Exception{
        int c = myDAO.quantityByClient(30);
        assertEquals(c,5);
    }
    
    @Test
    public void AllEmailsTest() throws SQLException {
        List<String> email = myDAO.allEmails();
        assertEquals(myDAO.numberCustomer(),email.size());
    }
    
    
    @Test
    public void NumberCustomerTest() throws SQLException {
        int n = myDAO.numberCustomer();
        assertEquals(3,n);
    }
    
    @Test
    public void allProductTest() throws SQLException{
        List<Product> p = myDAO.AllProduct();
        assertEquals(p.size(),2);
        assertEquals(p.get(0).getDescription(),"Accounting Application");
    }
    
    @Test
    public void NumberProductTest() throws SQLException {
        int n = myDAO.numberProduct();
        assertEquals(2,n);
    }
   
    
    @Test
    public void findProductTest() throws SQLException{
        Product p = myDAO.findProduct(1);
        assertEquals(p.getDescription(),"Accounting Application");
    }
    
   
    
   
    
    
    
    
   
    
   
   
}
    





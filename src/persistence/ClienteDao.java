package persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import entity.Cliente;

public class ClienteDao {
	
	static EntityManagerFactory factory = Persistence.createEntityManagerFactory("PUjpa");
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
		
	}

	EntityManager manager;
	
	TypedQuery<Cliente>query;
	
	public void create(Cliente c)throws Exception{
		manager=getEntityManager();
		manager.getTransaction().begin();
		manager.persist(c);
		
		manager.getTransaction().commit();
	}
	
	public void update(Cliente c)throws Exception{
		manager=getEntityManager();
		manager.getTransaction().begin();
		manager.merge(c);
		
		manager.getTransaction().commit();
	}
	
	public void delete(Cliente c)throws Exception{
		manager=getEntityManager();
		manager.getTransaction().begin();
		manager.remove(c);
		
		manager.getTransaction().commit();
	}
	
	public List<Cliente>findAll()throws Exception{
	manager=getEntityManager();
	List<Cliente>lista=manager.createQuery("select obj from Cliente as obj",Cliente.class).getResultList();
		return lista;
	}
	
	public Cliente FindByCodel(Long cod)throws Exception{
		manager=getEntityManager();
		Cliente cliente = manager.find(Cliente.class,cod);
			return cliente;
		}
	public static void main(String[] args) {
		try {
			
			Cliente c1 = new Cliente(null,"soneca","soneca@gmail.com");
			Cliente c2 = new Cliente(null,"paqueta","paqueta@gmail.com");
			Cliente c3 = new Cliente(null,"everton","everton@gmail.com");
			
			ClienteDao dao = new ClienteDao();
			
			dao.create(c1);
			dao.create(c2);
			dao.create(c3);
			
			System.out.println("Dados Gerados");
			
			dao.findAll().stream().forEach(x->System.out.println(x.getNome()+","+x.getEmail()));
			
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			
		}
	}
}

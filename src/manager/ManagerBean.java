package manager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.spi.SessionImplementor;

import entity.Cliente;
import net.sf.jasperreports.engine.JasperRunManager;
import persistence.ClienteDao;
import persistence.HibernateUtil;

@ManagedBean(name="mb")
@RequestScoped
public class ManagerBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	private List<Cliente> clientes;
	
	public ManagerBean() {
		
		this.cliente = new Cliente();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public void reportCliente() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		try {
			
			InputStream arquivo = fc.getExternalContext().getResourceAsStream("/ReportCliente.jasper");
			SessionImplementor sim = (SessionImplementor) HibernateUtil.getSessionFactory().openSession();
			
			Connection con = (Connection) sim.connection();
			byte []report = JasperRunManager.runReportToPdf(arquivo, null, con);
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			
			response.setHeader("context-Disposition", "inline;filename=relatorio.pdf");
			response.setContentLength(report.length);
			OutputStream out = response.getOutputStream();
			out.write(report);
			out.close();
			
			FacesContext.getCurrentInstance().responseComplete();
				
		}catch(Exception ex) {
			
			ex.printStackTrace();
		}
	}
	
	public void cadastrar() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		try {
			
			new ClienteDao().create(this.cliente);
			
			fc.addMessage(null, new FacesMessage("Dados Gravados"));
			
			this.cliente = new Cliente();
			
		}catch(Exception ex) {
			
			fc.addMessage(null, new FacesMessage("Error: " + ex.getMessage()));
		}
	}
	
	public void alterar() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		try {
			
			new ClienteDao().update(this.cliente);
			
			this.cliente = new Cliente();
			
			fc.addMessage(null, new FacesMessage("Dados Alterados"));
			
		}catch(Exception ex) {
			
			fc.addMessage(null, new FacesMessage("Error: " + ex.getMessage()));
		}
	}
	
	public void excluir() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		try {
			
			new ClienteDao().delete(this.cliente);
			
			this.cliente = new Cliente();
			
			fc.addMessage(null, new FacesMessage("Dados Excluidos"));
			
		}catch(Exception ex) {
			
			fc.addMessage(null, new FacesMessage("Error: " + ex.getMessage()));
		}
	}
	
	public List<Cliente> getClientes() {
		
		try {
		
			this.clientes = new ClienteDao().findAll();
		
		}catch(Exception ex) {
			
			ex.printStackTrace();
		}
		
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		
		this.clientes = clientes;
	}
}

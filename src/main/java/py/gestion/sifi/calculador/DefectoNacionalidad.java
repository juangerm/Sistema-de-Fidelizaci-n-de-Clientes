package py.gestion.sifi.calculador;

import javax.persistence.*;

import org.openxava.calculators.*;
import org.openxava.jpa.*;

public class DefectoNacionalidad implements ICalculator {

	private static final long serialVersionUID = 1L;

	public Object calculate() throws Exception {
		EntityManager em = XPersistence.getManager();
		try {		
			Query query=em.createQuery("SELECT n.codNacionalidad from Nacionalidad n where UPPER(n.descripcion)= 'PARAGUAYA'");
			System.out.println(query.getSingleResult());
			return query.getSingleResult();
			
		}catch(Exception e) {
			return null;
		}
		
	}

}

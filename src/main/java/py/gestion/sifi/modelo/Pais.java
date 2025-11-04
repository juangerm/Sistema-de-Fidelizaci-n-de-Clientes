package py.gestion.sifi.modelo;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;

@Entity
@Table(name = "paises")
@View(members = "descripcion; abreviatura;")
@View(name = "simple", members = "codPais, descripcion;")
@Getter@Setter
public class Pais {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id@Hidden
	@Column(name = "cod_pais", columnDefinition = "integer")
	private Integer codPais;
	
	private String descripcion;

	@Column(name = "abreviatura", length = 2)
	@Required
	private String abreviatura;
	
//	public static Pais buscarPorDescripcion(String descripcion) {
//		 try {
//			 Pais pais = (Pais) XPersistence.getManager()
//						.createQuery("Select o from Pais o where upper(o.descripcion) = upper(:descripcion)")
//						.setParameter("descripcion", descripcion).getResultList().get(0);
//				return pais;
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			return null;
//	}
}
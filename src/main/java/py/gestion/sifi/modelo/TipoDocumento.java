package py.gestion.sifi.modelo;
import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;

@Entity
@Table(name = "tipos_documentos")
@View(members = "descripcion;")
@Getter@Setter
public class TipoDocumento {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id@Hidden
	@Column(name = "cod_tipo_documento", columnDefinition = "integer")
	private Integer codTipoDocumento;
	
	private String descripcion;

//	public static TipoDocumento buscarPorDescripcion(String descripcion) {
//		try {
//			TipoDocumento tipoDocumento = (TipoDocumento) XPersistence.getManager()
//					.createQuery("Select o from TipoDocumento o where upper(o.descripcion) = upper(:descripcion)")
//					.setParameter("descripcion", descripcion).getResultList().get(0);
//			return tipoDocumento;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return null;
//	}
	
}

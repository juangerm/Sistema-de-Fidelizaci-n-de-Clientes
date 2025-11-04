package py.gestion.sifi.modelo;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;

@Entity
@Table(name = "nacionalidades")
@View(members = "nacionalidad; pais;")
@View(name="basico",members="nacionalidad")
@Getter@Setter
public class Nacionalidad {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id@Hidden
	@Column(name = "cod_nacionalidad", columnDefinition = "integer")
	private Integer codNacionalidad;
	
	private String nacionalidad;

	@NoModify@NoCreate
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "cod_pais", columnDefinition = "integer", foreignKey = @ForeignKey(name = "Fk_nacionalidad_pais"))
	@DescriptionsList
	private Pais pais;

//	public static Nacionalidad buscarPorDescripcion(String descripcion) {
//		try {
//			Nacionalidad nacionalidad = (Nacionalidad) XPersistence.getManager()
//					.createQuery("Select o from Nacionalidad o where upper(o.descripcion) = upper(:descripcion)")
//					.setParameter("descripcion", descripcion).getResultList().get(0);
//			return nacionalidad;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return null;
//	}
	
}

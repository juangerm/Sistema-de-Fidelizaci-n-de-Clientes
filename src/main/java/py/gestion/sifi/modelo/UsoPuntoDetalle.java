package py.gestion.sifi.modelo;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;

@View(members = "bolsaPunto;")
@Entity
@Getter@Setter
public class UsoPuntoDetalle {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id @Hidden
	@ReadOnly
	private Integer id;
	
	@ReferenceView("simple")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idUsoPuntoCabecera", columnDefinition = "integer",
	foreignKey = @ForeignKey(name = "Fk_usoPuntoDetalle_idUsoPuntoCabecera"))
	private UsoPuntoCabecera usoPuntoCabecera;
	
	@ReferenceView("simple")
	@SearchListTab("simple")
	//@OrderBy("vencimientoPunto.fechaFin")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idBolsaPunto", columnDefinition = "integer",
	foreignKey = @ForeignKey(name = "Fk_usoPuntoDetalle_idBolsaPunto"))
	private BolsaPunto bolsaPunto;


}

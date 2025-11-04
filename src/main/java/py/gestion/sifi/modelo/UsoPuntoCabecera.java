package py.gestion.sifi.modelo;

import java.time.*;
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import lombok.*;


@View(name = "simple", members = "id")
@View(members = "cliente; datos[#puntajeUtilizado, fecha, conceptoPunto;]; detalle")
@Entity
@Getter@Setter
public class UsoPuntoCabecera {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id @Hidden
	@ReadOnly
	private Integer id;
	
	@ReferenceView("simple")
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "idCliente", columnDefinition = "integer", foreignKey = @ForeignKey(name = "Fk_bolsaPunto_cliente"))
	//@DescriptionsList
	//@Required
	@NoModify@NoCreate@NoFrame
	private Cliente cliente;
	
	@Column(name = "puntaje_Utilizado")
	private Integer puntajeUtilizado;
	
	@Column(name = "fecha", columnDefinition = "date")
	@DefaultValueCalculator(CurrentLocalDateCalculator.class)
	private LocalDate fecha;
	
	@ReferenceView("simple")
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "idConceptoPunto", columnDefinition = "integer", foreignKey = @ForeignKey(name = "Fk_usoPuntoCabecera_conceptoPunto"))
	@DescriptionsList(descriptionProperties = "concepto")
	//@Required
	@NoModify@NoCreate@NoFrame
	private ConceptoPunto conceptoPunto;
	
	@OneToMany(mappedBy = "usoPuntoCabecera",cascade = CascadeType.ALL)
	@ListProperties("id, bolsaPunto.vencimientoPunto.fechaFin, bolsaPunto.puntajeUtilizado, bolsaPunto.saldoPunto")
	private List<UsoPuntoDetalle> detalle;

}

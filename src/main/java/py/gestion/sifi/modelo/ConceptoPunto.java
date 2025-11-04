package py.gestion.sifi.modelo;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;
import py.gestion.sifi.calculador.*;

@View(name="simple", members = "concepto")
@Entity
@Getter@Setter
public class ConceptoPunto {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id @Hidden
	@ReadOnly
	private Integer id;
	
	@Column(name = "concepto")
	private String concepto;
	
	@DefaultValueCalculator(DefectoPunto.class)
	@Column(name = "punto_Requerido")
	private Integer puntoRequerido;
}

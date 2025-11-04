package py.gestion.sifi.modelo;

import java.time.*;

import lombok.*;

@Getter @Setter
public class UsoPuntoDetalleDTO {
    private Integer id;
    private LocalDate fechaFin;
    private Integer saldoPunto;
	private Integer idBolsaPunto;
	private Integer puntajeUtilizado;
}

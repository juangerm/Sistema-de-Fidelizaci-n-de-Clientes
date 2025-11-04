package py.gestion.sifi.alCambiar;

import org.openxava.actions.*;

public class CalcularSaldoPuntoAlCambiarPuntajeUtilizado extends OnChangePropertyBaseAction{
	@Override
	public void execute() throws Exception {
		
		if(getNewValue() == null) {
			return ;
		}
		try {
			Integer puntajeUtilizado = getView().getValueInt("puntajeUtilizado");
			Integer puntajeAsignado= getView().getValueInt("puntajeAsignado");
			Integer saldoPunto = puntajeAsignado - puntajeUtilizado;
			getView().setValue("saldoPunto", saldoPunto);
		} catch (Exception e) {
			addError("no_se pudo calcular el salso");
		}
	}

}

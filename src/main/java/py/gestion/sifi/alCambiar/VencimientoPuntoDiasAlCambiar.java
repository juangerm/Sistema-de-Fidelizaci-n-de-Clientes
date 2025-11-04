package py.gestion.sifi.alCambiar;

import java.time.*;

import org.openxava.actions.*;

public class VencimientoPuntoDiasAlCambiar extends OnChangePropertyBaseAction{

	@Override
	public void execute() throws Exception {
		
		if(getNewValue() == null) {
			return ;
		}
		
		try {
			Integer diasValidez = getView().getValueInt("diasValidez");
			LocalDate fechaInicio = (LocalDate)getView().getValue("fechaInicio");
			getView().setValue("fechaFin", fechaInicio.plusDays(diasValidez));;
		}catch (Exception e) {
			addError("no_tiene_dias_validez");
		}
		
	}

}

package py.gestion.sifi.validador;

import org.openxava.util.*;
import org.openxava.validators.*;

import lombok.*;
@Setter@Getter
public class ValidadorNumeroDocumento implements IPropertyValidator{
	private static final long serialVersionUID = -1739917067935108627L;
	String numeroDocumento ="";
	@Override
	public void validate(Messages errores,  
	        Object objeto,  
	        String nombreObjeto,  
	        String nombrePropiedad)   
	{
		if(objeto == null) return;
		if(!(objeto instanceof String)) {
			errores.add("tipo_esperado",nombrePropiedad,nombreObjeto,"String");
			return;
		}
		 numeroDocumento = (String) objeto;
		 if(!esAlfaNumerico()) {
			 errores.add("tipo_esperado",nombrePropiedad,nombreObjeto,"String");
		 }
		 if(numeroDocumento.length() <= 5) {
			 errores.add("error_cantidad_caracteres_minimo_5");
		 }
		 
	}
private Boolean esAlfaNumerico( ) {
		
		for(char c : numeroDocumento.toCharArray()) {
			 if(!Character.isLetterOrDigit(c)) {
				 return false;
			 } 
		 }
		return true;
	}
 
	
}
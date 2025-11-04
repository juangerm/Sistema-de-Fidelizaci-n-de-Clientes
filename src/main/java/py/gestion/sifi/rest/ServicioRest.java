package py.gestion.sifi.rest;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.openxava.jpa.*;

import py.gestion.sifi.modelo.*;



@Path("/servicio")  // URL base del servicio
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioRest {

	/*
	 * Metodos get, post, put y delete para la entidad cliente
	 */

	@GET
	@Path("/listarClientes")
	public List<ClienteDTO> listarClientes() {
	    EntityManager em = XPersistence.getManager();
	    List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();

	    List<ClienteDTO> resultado = new ArrayList<>();

	    for (Cliente c : clientes) {
	        ClienteDTO dto = new ClienteDTO();
	        dto.setId(c.getId());
	        dto.setNombre(c.getNombre());
	        dto.setApellido(c.getApellido());
	        dto.setNumeroDocumento(c.getNumeroDocumento());
	        dto.setFechaNacimiento(c.getFechaNacimiento() != null ? c.getFechaNacimiento().toString() : null);
	        dto.setCelular(c.getCelular());
	        dto.setEmail(c.getEmail());
	        dto.setCodNacionalidad(c.getNacionalidad() != null ? c.getNacionalidad().getCodNacionalidad() : null);
	        dto.setCodTipoDocumento(c.getTipoDocumento() != null ? c.getTipoDocumento().getCodTipoDocumento() : null);
	        resultado.add(dto);
	    }
	    return resultado;
	}

	
	@GET
	@Path("/cliente/{id}")
	public ClienteDTO obtener(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();
	    Cliente cliente = em.find(Cliente.class, id);
	    if (cliente == null) {
	        throw new WebApplicationException("Cliente no encontrado", 404);
	    }
	    ClienteDTO dto = new ClienteDTO();	        
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setNumeroDocumento(cliente.getNumeroDocumento());
        dto.setFechaNacimiento(cliente.getFechaNacimiento() != null ? cliente.getFechaNacimiento().toString() : null);
        dto.setCelular(cliente.getCelular());
        dto.setEmail(cliente.getEmail());
        dto.setCodNacionalidad(cliente.getNacionalidad() != null ? cliente.getNacionalidad().getCodNacionalidad() : null);
        dto.setCodTipoDocumento(cliente.getTipoDocumento() != null ? cliente.getTipoDocumento().getCodTipoDocumento() : null);
        return dto;
	}
	
	@POST
	@Path("/grabarCliente")
	public Cliente grabar(Cliente persona) {
		try {
			EntityManager em = XPersistence.getManager();
			
			if (persona.getId() == null) {
				em.persist(persona);
			} else {
				em.merge(persona);
			}
			
			System.out.println("Grabado correctamente: ID=" + persona.getId() + ", Nombre=" + persona.getNombre());
			return persona;
			
		} catch (Exception e) {
			XPersistence.rollback();
			System.out.println("Error al grabar PersonaFisica: " + e.getMessage());
			throw new RuntimeException("Error al grabar PersonaFisica: " + e.getMessage(), e);
		}
	}
	
	@PUT
	@Path("/actualizarCliente/{id}")
	public ClienteDTO actualizar(@PathParam("id") Integer id, Cliente clienteActualizado) {
	    EntityManager em = XPersistence.getManager();
	    try {
	        Cliente clienteExistente = em.find(Cliente.class, id);
	        if (clienteExistente == null) {
	            throw new WebApplicationException("Cliente no encontrado con ID " + id, 404);
	        }

	        clienteExistente.setNombre(clienteActualizado.getNombre());
	        clienteExistente.setApellido(clienteActualizado.getApellido());
	        clienteExistente.setNumeroDocumento(clienteActualizado.getNumeroDocumento());
	        clienteExistente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
	        clienteExistente.setCelular(clienteActualizado.getCelular());
	        clienteExistente.setEmail(clienteActualizado.getEmail());

	        if (clienteActualizado.getNacionalidad() != null) {
	            Nacionalidad nacionalidad = em.find(Nacionalidad.class, clienteActualizado.getNacionalidad().getCodNacionalidad());
	            clienteExistente.setNacionalidad(nacionalidad);
	        }

	        if (clienteActualizado.getTipoDocumento() != null) {
	            TipoDocumento tipoDocumento = em.find(TipoDocumento.class, clienteActualizado.getTipoDocumento().getCodTipoDocumento());
	            clienteExistente.setTipoDocumento(tipoDocumento);
	        }

	        em.merge(clienteExistente);

	        // Convertimos el cliente a DTO antes de devolverlo
	        ClienteDTO dto = new ClienteDTO();	        
	        dto.setId(clienteExistente.getId());
	        dto.setNombre(clienteExistente.getNombre());
	        dto.setApellido(clienteExistente.getApellido());
	        dto.setNumeroDocumento(clienteExistente.getNumeroDocumento());
	        dto.setFechaNacimiento(clienteExistente.getFechaNacimiento() != null ? clienteExistente.getFechaNacimiento().toString() : null);
	        dto.setCelular(clienteExistente.getCelular());
	        dto.setEmail(clienteExistente.getEmail());
	        dto.setCodNacionalidad(clienteExistente.getNacionalidad() != null ? clienteExistente.getNacionalidad().getCodNacionalidad() : null);
	        dto.setCodTipoDocumento(clienteExistente.getTipoDocumento() != null ? clienteExistente.getTipoDocumento().getCodTipoDocumento() : null);
	        return dto;
	    } catch (Exception e) {
	        XPersistence.rollback();
	        throw new RuntimeException("Error al actualizar cliente: " + e.getMessage(), e);
	    }
	}


	@DELETE
	@Path("/eliminarCliente/{id}")
	public Response eliminarCliente(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();
	    try {
	        Cliente clienteExistente = em.find(Cliente.class, id);
	        if (clienteExistente == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("Cliente no encontrado con ID " + id)
	                           .build();
	        }

	        em.remove(clienteExistente);

	        System.out.println("Cliente eliminado: ID=" + id);
	        return Response.ok("Cliente eliminado correctamente").build();
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al eliminar cliente: " + e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity("Error al eliminar cliente: " + e.getMessage())
	                       .build();
	    }
	}
	
	/*
	 *Metodos get, post, put y delete para la entidad concepto punto
	 */
	
	@GET
	@Path("/listarConceptoPunto")
	public List<ConceptoPunto> listarConceptoPunto() {
	    EntityManager em = XPersistence.getManager();
	    return em.createQuery("SELECT c FROM ConceptoPunto c", ConceptoPunto.class).getResultList();
	}
	
	@POST
	@Path("/grabarConceptoPunto")
	public ConceptoPunto grabar(ConceptoPunto conceptoPunto) {
		try {
			EntityManager em = XPersistence.getManager();
			
			if (conceptoPunto.getId() == null) {
				em.persist(conceptoPunto);
			} else {
				em.merge(conceptoPunto);
			}
			
			System.out.println("Grabado correctamente: ID=" + conceptoPunto.getId() + ", Nombre=" + conceptoPunto.getConcepto());
			return conceptoPunto;
			
		} catch (Exception e) {
			XPersistence.rollback();
			System.out.println("Error al grabar: " + e.getMessage());
			throw new RuntimeException("Error al grabar: " + e.getMessage(), e);
		}
	}
	
	@PUT
	@Path("/actualizarConceptoPunto/{id}")
	public ConceptoPunto actualizar(@PathParam("id") Integer id, ConceptoPunto entidadActualizada) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	ConceptoPunto entidad = em.find(ConceptoPunto.class, id);
	        if (entidadActualizada == null) {
	            throw new WebApplicationException("No encontrado con ID " + id, 404);
	        }

	        // Actualizamos los campos
	        entidad.setConcepto(entidadActualizada.getConcepto());
	        entidad.setPuntoRequerido(entidadActualizada.getPuntoRequerido());

	        em.merge(entidad);

	        System.out.println("Actualizado: ID=" + entidadActualizada.getId() + ", Nombre=" + entidadActualizada.getConcepto());
	        return entidadActualizada;
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al actualizar: " + e.getMessage());
	        throw new RuntimeException("Error al actualizar: " + e.getMessage(), e);
	    }
	}

	@DELETE
	@Path("/eliminarConceptoPunto/{id}")
	public Response eliminarConceptoPunto(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	ConceptoPunto entidad = em.find(ConceptoPunto.class, id);
	        if (entidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("No encontrado con ID " + id)
	                           .build();
	        }

	        em.remove(entidad);

	        System.out.println("Eliminado: ID=" + id);
	        return Response.ok("Eliminado correctamente").build();
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al eliminar: " + e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity("Error al eliminar: " + e.getMessage())
	                       .build();
	    }
	}
	
	/*
	 *Metodos get, post, put y delete para la entidad regla asignacion punto
	 */
	@GET
	@Path("/listarRegla")
	public List<ReglaAsignacionPunto> listarReglaAsignacionPunto() {
	    EntityManager em = XPersistence.getManager();
	    return em.createQuery("SELECT c FROM ReglaAsignacionPunto c", ReglaAsignacionPunto.class).getResultList();
	}
	
	@POST
	@Path("/grabarRegla")
	public ReglaAsignacionPunto grabar(ReglaAsignacionPunto reglaAsignacionPunto) {
		try {
			EntityManager em = XPersistence.getManager();
			
			if (reglaAsignacionPunto.getId() == null) {
				em.persist(reglaAsignacionPunto);
			} else {
				em.merge(reglaAsignacionPunto);
			}
			
			System.out.println("Grabado correctamente: ID=" + reglaAsignacionPunto.getId() + ", Nombre=" + reglaAsignacionPunto.getPuntoEquivalente());
			return reglaAsignacionPunto;
			
		} catch (Exception e) {
			XPersistence.rollback();
			System.out.println("Error al grabar: " + e.getMessage());
			throw new RuntimeException("Error al grabar: " + e.getMessage(), e);
		}
	}
	
	@PUT
	@Path("/actualizarRegla/{id}")
	public ReglaAsignacionPunto actualizar(@PathParam("id") Integer id, ReglaAsignacionPunto entidadActualizada) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	ReglaAsignacionPunto entidad = em.find(ReglaAsignacionPunto.class, id);
	        if (entidadActualizada == null) {
	            throw new WebApplicationException("No encontrado con ID " + id, 404);
	        }

	        // Actualizamos los campos
	        entidad.setLimiteInferior(entidadActualizada.getLimiteInferior());
	        entidad.setLimiteSuperior(entidadActualizada.getLimiteSuperior());
	        entidad.setPuntoEquivalente(entidadActualizada.getPuntoEquivalente());
	        entidad.setMontoEquivalente(entidadActualizada.getMontoEquivalente());

	        em.merge(entidad);

	        System.out.println("Actualizado: ID=" + entidadActualizada.getId() + ", Nombre=" + entidadActualizada.getMontoEquivalente());
	        return entidadActualizada;
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al actualizar: " + e.getMessage());
	        throw new RuntimeException("Error al actualizar: " + e.getMessage(), e);
	    }
	}

	@DELETE
	@Path("/eliminarRegla/{id}")
	public Response eliminarRegla(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	ReglaAsignacionPunto entidad = em.find(ReglaAsignacionPunto.class, id);
	        if (entidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("No encontrado con ID " + id)
	                           .build();
	        }

	        em.remove(entidad);

	        System.out.println("Eliminado: ID=" + id);
	        return Response.ok("Eliminado correctamente").build();
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al eliminar: " + e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity("Error al eliminar: " + e.getMessage())
	                       .build();
	    }
	}
	
	/*
	 *Metodos get, post, put y delete para la entidad vencimiento punto
	 */
	@GET
	@Path("/listarVencimientos")
	public List<VencimientoPunto> listarVencimientos() {
	    EntityManager em = XPersistence.getManager();
	    return em.createQuery("SELECT c FROM VencimientoPunto c", VencimientoPunto.class).getResultList();
	}
	
	@POST
	@Path("/grabarVencimiento")
	public VencimientoPunto grabar(VencimientoPunto vencimientoPunto) {
		try {
			EntityManager em = XPersistence.getManager();
			
			if (vencimientoPunto.getId() == null) {
				em.persist(vencimientoPunto);
			} else {
				em.merge(vencimientoPunto);
			}
			
			System.out.println("Grabado correctamente: ID=" + vencimientoPunto.getId() + ", Nombre=" + vencimientoPunto.getFechaInicio());
			return vencimientoPunto;
			
		} catch (Exception e) {
			XPersistence.rollback();
			System.out.println("Error al grabar: " + e.getMessage());
			throw new RuntimeException("Error al grabar: " + e.getMessage(), e);
		}
	}
	
	@PUT
	@Path("/actualizarVencimiento/{id}")
	public VencimientoPunto actualizar(@PathParam("id") Integer id, VencimientoPunto entidadActualizada) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	VencimientoPunto entidad = em.find(VencimientoPunto.class, id);
	        if (entidadActualizada == null) {
	            throw new WebApplicationException("No encontrado con ID " + id, 404);
	        }

	        // Actualizamos los campos
	        entidad.setFechaInicio(entidadActualizada.getFechaInicio());
	        entidad.setDiasValidez(entidadActualizada.getDiasValidez());
	        entidad.setFechaFin(entidadActualizada.getFechaFin());

	        em.merge(entidad);

	        System.out.println("Actualizado: ID=" + entidadActualizada.getId() + ", Nombre=" + entidadActualizada.getFechaFin());
	        return entidadActualizada;
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al actualizar: " + e.getMessage());
	        throw new RuntimeException("Error al actualizar: " + e.getMessage(), e);
	    }
	}

	@DELETE
	@Path("/eliminarVencimiento/{id}")
	public Response eliminarVencimiento(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	VencimientoPunto entidad = em.find(VencimientoPunto.class, id);
	        if (entidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("No encontrado con ID " + id)
	                           .build();
	        }

	        em.remove(entidad);

	        System.out.println("Eliminado: ID=" + id);
	        return Response.ok("Eliminado correctamente").build();
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al eliminar: " + e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity("Error al eliminar: " + e.getMessage())
	                       .build();
	    }
	}
	
	/*
	 *Metodos get, post, put y delete para la entidad bolsa punto
	 */
	@GET
	@Path("/listarBolsaPunto")
	public List<BolsaPuntoDTO> listarBolsaPunto() {
	    EntityManager em = XPersistence.getManager();
	    List<BolsaPunto> bolsaPunto = em.createQuery("SELECT c FROM BolsaPunto c", BolsaPunto.class).getResultList();

	    List<BolsaPuntoDTO> resultado = new ArrayList<>();

	    for (BolsaPunto c : bolsaPunto) {
	    	BolsaPuntoDTO dto = new BolsaPuntoDTO();
	        dto.setId(c.getId());
		    dto.setIdCliente(c.getCliente() != null ? c.getCliente().getId() : null);
		    dto.setIdVencimientoPunto(c.getVencimientoPunto() != null ? c.getVencimientoPunto().getId() : null);
		    dto.setPuntajeAsignado(c.getPuntajeAsignado());
		    dto.setPuntajeUtilizado(c.getPuntajeUtilizado());
		    dto.setSaldoPunto(c.getSaldoPunto());
		    dto.setMontoOperacion(c.getMontoOperacion());

		    // Campos opcionales de descripción
		    dto.setNombreCliente(c.getCliente() != null ? c.getCliente().getNombre() + " " + c.getCliente().getApellido() : null);
		    dto.setVencimientoPunto(c.getVencimientoPunto() != null ? c.getVencimientoPunto().getFechaFin().toString() : null);
	        resultado.add(dto);
	    }
	    return resultado;
	}
	
	@GET
	@Path("/bolsaPunto/{id}")
	public BolsaPuntoDTO obtenerBolsaPunto(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();
	    BolsaPunto entidad = em.find(BolsaPunto.class, id);

	    if (entidad == null) {
	        throw new WebApplicationException("BolsaPunto no encontrada con ID " + id, 404);
	    }

	    BolsaPuntoDTO dto = new BolsaPuntoDTO();
	    dto.setId(entidad.getId());
	    dto.setIdCliente(entidad.getCliente() != null ? entidad.getCliente().getId() : null);
	    dto.setIdVencimientoPunto(entidad.getVencimientoPunto() != null ? entidad.getVencimientoPunto().getId() : null);
	    dto.setPuntajeAsignado(entidad.getPuntajeAsignado());
	    dto.setPuntajeUtilizado(entidad.getPuntajeUtilizado());
	    dto.setSaldoPunto(entidad.getSaldoPunto());
	    dto.setMontoOperacion(entidad.getMontoOperacion());

	    // Campos opcionales de descripción
	    dto.setNombreCliente(entidad.getCliente() != null ? entidad.getCliente().getNombre() + " " + entidad.getCliente().getApellido() : null);
	    dto.setVencimientoPunto(entidad.getVencimientoPunto() != null ? entidad.getVencimientoPunto().getFechaFin().toString() : null);

	    return dto;
	}
	
	@POST
	@Path("/grabarBolsaPunto")
	public BolsaPunto grabar(BolsaPunto bolsaPunto) {
		try {
			EntityManager em = XPersistence.getManager();
			
			if (bolsaPunto.getId() == null) {
				em.persist(bolsaPunto);
			} else {
				em.merge(bolsaPunto);
			}
			
			System.out.println("Grabado correctamente: ID=" + bolsaPunto.getId() + ", Nombre=" + bolsaPunto.getCliente().getNombre());
			return bolsaPunto;
			
		} catch (Exception e) {
			XPersistence.rollback();
			System.out.println("Error al grabar: " + e.getMessage());
			throw new RuntimeException("Error al grabar: " + e.getMessage(), e);
		}
	}
	
	@PUT
	@Path("/actualizarBolsaPunto/{id}")
	public BolsaPuntoDTO actualizar(@PathParam("id") Integer id, BolsaPuntoDTO actualizado) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	 BolsaPunto entidad = em.find(BolsaPunto.class, id);
	        if (entidad == null) {
	            throw new WebApplicationException("Cliente no encontrado con ID " + id, 404);
	        }

	        entidad.setPuntajeAsignado(actualizado.getPuntajeAsignado());
	        entidad.setPuntajeUtilizado(actualizado.getPuntajeUtilizado());
	        entidad.setSaldoPunto(actualizado.getSaldoPunto());
	        entidad.setMontoOperacion(actualizado.getMontoOperacion());
	        if (actualizado.getIdCliente() != null) {
	            Cliente cliente = em.find(Cliente.class, actualizado.getIdCliente());
	            entidad.setCliente(cliente);
	        }
	        if (actualizado.getIdVencimientoPunto() != null) {
	            VencimientoPunto vp = em.find(VencimientoPunto.class, actualizado.getIdVencimientoPunto());
	            entidad.setVencimientoPunto(vp);
	        }

	        em.merge(entidad);

	        // Convertimos el bolsa punto a DTO antes de devolverlo
	        BolsaPuntoDTO dto = new BolsaPuntoDTO();       
	        dto.setId(entidad.getId());
	        dto.setIdCliente(entidad.getCliente() != null ? entidad.getCliente().getId() : null);
	        dto.setIdVencimientoPunto(entidad.getVencimientoPunto() != null ? entidad.getVencimientoPunto().getId() : null);
	        dto.setPuntajeAsignado(entidad.getPuntajeAsignado());
	        dto.setPuntajeUtilizado(entidad.getPuntajeUtilizado());
	        dto.setSaldoPunto(entidad.getSaldoPunto());
	        dto.setMontoOperacion(entidad.getMontoOperacion());
	        return dto;
	    } catch (Exception e) {
	        XPersistence.rollback();
	        throw new RuntimeException("Error al actualizar: " + e.getMessage(), e);
	    }
	}
	
	@DELETE
	@Path("/eliminarBolsaPunto/{id}")
	public Response eliminarBolsaPunto(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();
	    try {
	    	BolsaPunto entidad = em.find(BolsaPunto.class, id);
	        if (entidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("No encontrado con ID " + id)
	                           .build();
	        }

	        em.remove(entidad);

	        System.out.println("Eliminado: ID=" + id);
	        return Response.ok("Eliminado correctamente").build();
	    } catch (Exception e) {
	        XPersistence.rollback();
	        System.out.println("Error al eliminar: " + e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity("Error al eliminar: " + e.getMessage())
	                       .build();
	    }
	}
	
	/*
	 *Metodos get, post, put y delete para la entidad uso punto
	 */
	@GET
	@Path("/listarUsoPunto")
	public List<UsoPuntoCabeceraDTO> listarUsoPuntoCabecera() {
	    EntityManager em = XPersistence.getManager();
	    List<UsoPuntoCabecera> usoPuntoCabecera = em.createQuery("SELECT u FROM UsoPuntoCabecera u", UsoPuntoCabecera.class).getResultList();

	    List<UsoPuntoCabeceraDTO> resultado = new ArrayList<>();

	    for (UsoPuntoCabecera c : usoPuntoCabecera) {
	        UsoPuntoCabeceraDTO dto = new UsoPuntoCabeceraDTO();
	        dto.setId(c.getId());
	        dto.setPuntajeUtilizado(c.getPuntajeUtilizado());
	        dto.setFecha(c.getFecha());

	        if (c.getCliente() != null) {
	            dto.setIdCliente(c.getCliente().getId());
	            dto.setNombreCliente(c.getCliente().getNombre() + " " + c.getCliente().getApellido());
	        }

	        if (c.getConceptoPunto() != null) {
	            dto.setIdConceptoPunto(c.getConceptoPunto().getId());
	            dto.setConcepto(c.getConceptoPunto().getConcepto());
	        }

	        // Convertir los detalles
	        List<UsoPuntoDetalleDTO> detallesDTO = new ArrayList<>();
	        if (c.getDetalle() != null && !c.getDetalle().isEmpty()) {
	            for (UsoPuntoDetalle det : c.getDetalle()) {
	                UsoPuntoDetalleDTO detDTO = new UsoPuntoDetalleDTO();
	                detDTO.setId(det.getId());
	                if (det.getBolsaPunto() != null) {
	                    detDTO.setFechaFin(det.getBolsaPunto().getVencimientoPunto() != null
	                        ? det.getBolsaPunto().getVencimientoPunto().getFechaFin()
	                        : null);
	                    detDTO.setSaldoPunto(det.getBolsaPunto().getSaldoPunto());
	                }
	                detallesDTO.add(detDTO);
	            }
	        }
	        dto.setDetalles(detallesDTO);

	        resultado.add(dto);
	    }

	    return resultado;
	}

	@POST
	@Path("/grabarUsoPuntoCabecera")
	public Response grabarUsoPuntoCabecera(UsoPuntoCabeceraDTO dto) {
	    EntityManager em = XPersistence.getManager();
	    try {
	        // Crear la entidad principal
	        UsoPuntoCabecera entidad = new UsoPuntoCabecera();
	        entidad.setPuntajeUtilizado(dto.getPuntajeUtilizado());
	        entidad.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());

	        // Relación con cliente
	        if (dto.getIdCliente() != null) {
	            Cliente cliente = em.find(Cliente.class, dto.getIdCliente());
	            if (cliente != null) entidad.setCliente(cliente);
	        }

	        // Relación con concepto punto
	        if (dto.getIdConceptoPunto() != null) {
	            ConceptoPunto concepto = em.find(ConceptoPunto.class, dto.getIdConceptoPunto());
	            if (concepto != null) entidad.setConceptoPunto(concepto);
	        }

	        // Guardar primero la cabecera (para tener ID generado)
	        em.persist(entidad);
	        em.flush();

	        // Procesar los detalles (si existen)
	        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
	            for (UsoPuntoDetalleDTO detDTO : dto.getDetalles()) {
	                UsoPuntoDetalle det = new UsoPuntoDetalle();
	                det.setUsoPuntoCabecera(entidad); // vincular cabecera

	                // Si se pasa un id de bolsaPunto o hay datos del saldo, cargamos
	                if (detDTO.getId() != null) {
	                    BolsaPunto bolsa = em.find(BolsaPunto.class, detDTO.getId());
	                    if (bolsa != null) det.setBolsaPunto(bolsa);
	                }

	                em.persist(det);
	            }
	        }
	        XPersistence.commit();
	        // Devolver la cabecera creada
	        return Response.status(Response.Status.CREATED).entity(dto).build();

	    } catch (Exception e) {
	        XPersistence.rollback();
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Error al crear: " + e.getMessage())
	                .build();
	    }
	}

	@PUT
	@Path("/actualizarUsoPunto/{id}")
	public Response actualizarUsoPuntoCabecera(@PathParam("id") Integer id, UsoPuntoCabeceraDTO dto) {
	    EntityManager em = XPersistence.getManager();

	    try {
	        UsoPuntoCabecera entidad = em.find(UsoPuntoCabecera.class, id);
	        if (entidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                    .entity("UsoPuntoCabecera no encontrada con ID " + id)
	                    .build();
	        }

	        // Actualizar los campos simples
	        entidad.setPuntajeUtilizado(dto.getPuntajeUtilizado());
	        entidad.setFecha(dto.getFecha() != null ? dto.getFecha() : entidad.getFecha());

	        // Actualizar cliente
	        if (dto.getIdCliente() != null) {
	            Cliente cliente = em.find(Cliente.class, dto.getIdCliente());
	            entidad.setCliente(cliente);
	        }

	        // Actualizar concepto punto
	        if (dto.getIdConceptoPunto() != null) {
	            ConceptoPunto concepto = em.find(ConceptoPunto.class, dto.getIdConceptoPunto());
	            entidad.setConceptoPunto(concepto);
	        }

	        // Actualizar detalles si se envían
	        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
	        	for (UsoPuntoDetalle d : new ArrayList<>(entidad.getDetalle())) {
	                em.remove(d); // elimina físicamente
	            }
	            entidad.getDetalle().clear();

	            for (UsoPuntoDetalleDTO detDTO : dto.getDetalles()) {
	                UsoPuntoDetalle det = new UsoPuntoDetalle();
	                det.setUsoPuntoCabecera(entidad);

	                if (detDTO.getIdBolsaPunto() != null) {
	                    BolsaPunto bolsa = em.find(BolsaPunto.class, detDTO.getIdBolsaPunto());
	                    det.setBolsaPunto(bolsa);
	                }

	                em.persist(det);
	            }
	        }

	        // Guardar los cambios
	        em.merge(entidad);
	        XPersistence.commit();

	        return Response.ok(dto).build();

	    } catch (Exception e) {
	        XPersistence.rollback();
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Error al actualizar: " + e.getMessage())
	                .build();
	    }
	}

	@DELETE
	@Path("/eliminarUsoPunto/{id}")
	public Response eliminarUsoPunto(@PathParam("id") Integer id) {
	    EntityManager em = XPersistence.getManager();

	    try {
	    	XPersistence.getManager().joinTransaction();

	        UsoPuntoCabecera entidad = em.find(UsoPuntoCabecera.class, id);
	        if (entidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                    .entity("No encontrada con ID " + id)
	                    .build();
	        }

	        // --- Eliminar los detalles asociados ---
	        if (entidad.getDetalle() != null && !entidad.getDetalle().isEmpty()) {
	            for (UsoPuntoDetalle detalle : new ArrayList<>(entidad.getDetalle())) {
	                em.remove(detalle);
	            }
	        }

	        // --- Eliminar la cabecera ---
	        em.remove(entidad);

	        XPersistence.commit();

	        return Response.ok()
	                .entity("ID " + id + " eliminada correctamente.")
	                .build();

	    } catch (Exception e) {
	    	e.printStackTrace();
	        XPersistence.rollback();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Error al eliminar: " + e.getMessage())
	                .build();
	    }
	}

	
	

}

package com.mycompany.electivapro.resources;

import ControladorJPA.UsuarioJpaController;
import Entidades.Usuarios;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("javaee8")
public class JavaEE8Resource {
    
    @GET
    public Response ping(){
        UsuarioJpaController udercon = new UsuarioJpaController();
        Object o[] = null;
        List<Usuarios> ListU = udercon.findUsuarioEntities();
        for (int i = 0; i < ListU.size(); i++) {
            System.out.println(" " + ListU.get(i).getUserNombres());
        }
        return Response
                .ok("ping")
                .build();
    }
}

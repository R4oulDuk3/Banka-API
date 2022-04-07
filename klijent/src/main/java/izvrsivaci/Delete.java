/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package izvrsivaci;

import javax.ws.rs.client.Invocation;

/**
 *
 * @author Gavrilo
 */
public class Delete implements Izvrsilac{

    @Override
    public String izvrsi(Invocation.Builder request) {
       return request.delete().readEntity(String.class);
    }
    
}

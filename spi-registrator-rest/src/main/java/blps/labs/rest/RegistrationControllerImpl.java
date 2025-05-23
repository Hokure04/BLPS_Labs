package blps.labs.rest;

import blps.labs.service.RegisterService;
import blps.labs.service.User;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Map;


public class RegistrationControllerImpl implements RegistrationController{

    private final RegisterService service;

    public RegistrationControllerImpl(RegisterService service) {
        this.service = service;
    }

    @POST
    @Path("/register")
    @Override
    public Response register(User user) {
        if (user == null) {
            throw new BadRequestException("request must contain user data");
        }
        if (service.register(user)) {
            return Response
                    .ok()
                    .entity(Map.of("success", true,"message","user successfully registered"))
                    .build();
        }
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("success", false,"message","fail to save user in keycloak system"))
                .build();
    }
}

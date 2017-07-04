package pinchuk.org.rest.webService;

import org.json.JSONObject;
import pinchuk.org.rest.dao.CategoryDAO;
import pinchuk.org.rest.dao.UserDAO;
import pinchuk.org.rest.dto.CategoryDTO;
import pinchuk.org.rest.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

/**
 * Created by Andrii Pinchuk on 27.02.2017.
 */
@Path("/users")
public class UserService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        List<UserDTO> users = UserDAO.get();
        if (users.size() > 0) {
            return Response.status(OK).entity(users).build();
        } else {
            String response = getResponse("Status", "Error. Collection of user is empty.");
            return Response.status(NOT_FOUND).entity(response).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        UserDTO userDTO = UserDAO.get(id);
        if (userDTO.getId() > 0) {
            return Response.status(OK).entity(userDTO).build();
        } else {
            String response = getResponse("Status", "Error. User with ID " + id + " Not found!");
            return Response.status(BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(UserDTO userDTO) {
        int categoryId = userDTO.getCategoryId();
        if (ifCategoryNotExist(categoryId)) {
            String response = getResponse("Status", "Error. Category with ID - " + categoryId + " doesn't exist!");
            return Response.status(BAD_REQUEST).entity(response).build();
        }

        int last_inserted_id = UserDAO.create(userDTO);
        if (last_inserted_id > 0) {
            String response = getResponse("Status",  "Success. User ID - " + last_inserted_id);
            return Response.status(CREATED).entity(response).build();
        }
        return Response.status(INTERNAL_SERVER_ERROR).entity("").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(UserDTO userDTO) {
        int rows = UserDAO.update(userDTO);
        if (rows > 0) {
            String response = getResponse("Status",  "Success. User was updated");
            return Response.status(OK).entity(response).build();
        } else {
            String response = getResponse("Status",  "Error. User not modified");
            return Response.status(NOT_MODIFIED).entity(response).build();
        }
    }

    private boolean ifCategoryNotExist(int id) {
        CategoryDTO categoryDTO = CategoryDAO.get(id);
        return categoryDTO.getId() == 0;
    }

    private String getResponse(String key, String value) {
        JSONObject json = new JSONObject();
        json.append(key, value);
        return json.toString();
    }
}
package org.m4nu;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.MediaType;

@Path("/tasks")
public class TaskResource {
	@Inject
	JsonWebToken jwt;

	@GET
	@RolesAllowed("user")
	public List<Task> getTasks() {
		return Task.getUserTasks(jwt.getClaim("id"));
	}

	@GET
	@Path("/{id}")
	@Authenticated
	public Task getTaskById(UUID id) {
		if (Task.findById(id) == null) {
			throw new NotFoundException();
		}
		return Task.findById(id);
	}

	@POST
	@Authenticated
	@Transactional
	public Task createTask(Task task) {
		task.userId = jwt.getClaim("id");
		task.persist();
		return task;
	}

	@PUT
	@Path("/{id}")
	@Authenticated
	@Transactional
	public Task updateTask(UUID id, Task task) {
		Task existingTask = Task.findById(id);

		if (existingTask == null) {
			throw new NotFoundException();
		}

		existingTask.name = task.name;
		existingTask.status = task.status;
		existingTask.description = task.description;
		existingTask.lastUpdate = task.lastUpdate;
		existingTask.userId = task.userId;

		return existingTask;
	}

	@DELETE
	@Path("/{id}")
	@Authenticated
	@Transactional
	public void deleteTask(UUID id) {
		if (Task.findById(id) == null) {
			throw new NotFoundException();
		}

		Task.deleteById(id);
	}

	@GET
	@Path("permit-all")
	@PermitAll
	public String hello(@Context SecurityContext ctx) {
		return getResponseString(ctx);
	}

	private String getResponseString(SecurityContext ctx) {
		String name;
		if (ctx.getUserPrincipal() == null) {
			name = "anonymous";
		} else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
			throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
		} else {
			name = ctx.getUserPrincipal().getName();
		}
		return String.format("hello %s,"
				+ " isHttps: %s,"
				+ " authScheme: %s,"
				+ " hasJWT: %s",
				name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
	}

	private boolean hasJwt() {
		return jwt.getClaimNames() != null;
	}
}

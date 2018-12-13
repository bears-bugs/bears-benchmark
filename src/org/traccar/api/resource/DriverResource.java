/*
 * Copyright 2017 Anton Tananaev (anton@traccar.org)
 * Copyright 2017 Andrey Kunitsyn (andrey@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.api.resource;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.traccar.Context;
import org.traccar.api.BaseResource;
import org.traccar.database.DriversManager;
import org.traccar.model.Driver;

@Path("drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverResource extends BaseResource {

    @GET
    public Collection<Driver> get(
            @QueryParam("all") boolean all, @QueryParam("userId") long userId, @QueryParam("groupId") long groupId,
            @QueryParam("deviceId") long deviceId, @QueryParam("refresh") boolean refresh) throws SQLException {

        DriversManager driversManager = Context.getDriversManager();
        if (refresh) {
            driversManager.refreshDrivers();
        }

        Set<Long> result = new HashSet<>();
        if (all) {
            if (Context.getPermissionsManager().isAdmin(getUserId())) {
                result.addAll(driversManager.getAllDrivers());
            } else {
                Context.getPermissionsManager().checkManager(getUserId());
                result.addAll(driversManager.getManagedDrivers(getUserId()));
            }
        } else {
            if (userId == 0) {
                userId = getUserId();
            }
            Context.getPermissionsManager().checkUser(getUserId(), userId);
            result.addAll(driversManager.getUserDrivers(userId));
        }

        if (groupId != 0) {
            Context.getPermissionsManager().checkGroup(getUserId(), groupId);
            result.retainAll(driversManager.getGroupDrivers(groupId));
        }

        if (deviceId != 0) {
            Context.getPermissionsManager().checkDevice(getUserId(), deviceId);
            result.retainAll(driversManager.getDeviceDrivers(deviceId));
        }
        return driversManager.getDrivers(result);

    }

    @POST
    public Response add(Driver entity) throws SQLException {
        Context.getPermissionsManager().checkReadonly(getUserId());
        Context.getDriversManager().addDriver(entity);
        Context.getDataManager().linkDriver(getUserId(), entity.getId());
        Context.getDriversManager().refreshUserDrivers();
        return Response.ok(entity).build();
    }

    @Path("{id}")
    @PUT
    public Response update(Driver entity) throws SQLException {
        Context.getPermissionsManager().checkReadonly(getUserId());
        Context.getPermissionsManager().checkDriver(getUserId(), entity.getId());
        Context.getDriversManager().updateDriver(entity);
        return Response.ok(entity).build();
    }

    @Path("{id}")
    @DELETE
    public Response remove(@PathParam("id") long id) throws SQLException {
        Context.getPermissionsManager().checkReadonly(getUserId());
        Context.getPermissionsManager().checkDriver(getUserId(), id);
        Context.getDriversManager().removeDriver(id);
        return Response.noContent().build();
    }

}

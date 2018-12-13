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
package org.traccar.database;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.traccar.Context;
import org.traccar.helper.Log;
import org.traccar.model.Group;

public class GroupsManager extends BaseObjectManager<Group> implements ManagableObjects {

    private AtomicLong groupsLastUpdate = new AtomicLong();
    private final long dataRefreshDelay;

    public GroupsManager(DataManager dataManager) {
        super(dataManager, Group.class);
        dataRefreshDelay = Context.getConfig().getLong("database.refreshDelay",
                DeviceManager.DEFAULT_REFRESH_DELAY) * 1000;
    }

    private void checkGroupCycles(Group group) {
        Set<Long> groups = new HashSet<>();
        while (group != null) {
            if (groups.contains(group.getId())) {
                throw new IllegalArgumentException("Cycle in group hierarchy");
            }
            groups.add(group.getId());
            group = getById(group.getGroupId());
        }
    }

    private void updateGroupCache(boolean force) throws SQLException {
        long lastUpdate = groupsLastUpdate.get();
        if ((force || System.currentTimeMillis() - lastUpdate > dataRefreshDelay)
                && groupsLastUpdate.compareAndSet(lastUpdate, System.currentTimeMillis())) {
            refreshItems();
        }
    }

    @Override
    public Set<Long> getAllItems() {
        Set<Long> result = super.getAllItems();
        if (result.isEmpty()) {
            try {
                updateGroupCache(true);
            } catch (SQLException e) {
                Log.warning(e);
            }
            result = super.getAllItems();
        }
        return result;
    }

    @Override
    protected void addNewItem(Group group) {
        checkGroupCycles(group);
        super.addNewItem(group);
    }

    @Override
    protected void updateCachedItem(Group group) {
        checkGroupCycles(group);
        super.updateCachedItem(group);
    }

    @Override
    public Set<Long> getUserItems(long userId) {
        if (Context.getPermissionsManager() != null) {
            return Context.getPermissionsManager().getGroupPermissions(userId);
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public Set<Long> getManagedItems(long userId) {
        Set<Long> result = new HashSet<>();
        result.addAll(getUserItems(userId));
        for (long managedUserId : Context.getUsersManager().getUserItems(userId)) {
            result.addAll(getUserItems(managedUserId));
        }
        return result;
    }

}

package com.simpolab.server_main.group.services;

import com.simpolab.server_main.elector.domain.NewElector;
import com.simpolab.server_main.group.domain.Group;
import java.util.List;

public interface GroupService {
  /**
   * Creates a new group with the given name
   *
   * @param groupName the name of the group
   * @return
   */
  Group newGroup(String groupName);

  /**
   * Returns the name of the group with the given id
   * @param id the id of the group
   * @return the name of the group or null if no group with the given id doesn't exist
   */
  Group getGroup(Long id);

  /**
   * Returns the names and id of all the groups
   * @return a list of the names and id of all the groups
   */
  List<Group> getGroups();

  /**
   * Returns the list of electors in the group with the given id
   *
   * @param id the id of the group
   * @return the list of electors in the given group
   */
  List<NewElector> getElectorsInGroup(Long id);

  /**
   * Deletes the group with the given id
   * @param id the id of the group
   */
  void deleteGroup(Long id);

  /**
   * Adds the elector with the given id to the group with the given id
   * @param groupId the id of the group
   * @param electorId the id of the elector
   */
  void addElector(Long groupId, Long electorId);

  /**
   * Removes the elector with the given id from the group with the given id
   * @param groupId the id of the group
   * @param electorId the id of the elector
   */
  void removeElector(Long groupId, Long electorId);
}

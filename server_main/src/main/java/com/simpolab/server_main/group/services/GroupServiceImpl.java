package com.simpolab.server_main.group.services;

import com.simpolab.server_main.db.ElectorDAO;
import com.simpolab.server_main.db.GroupDAO;
import com.simpolab.server_main.elector.domain.NewElector;
import com.simpolab.server_main.group.domain.Group;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

  private final GroupDAO groupDAO;
  private final ElectorDAO electorDAO;

  @Override
  public Group newGroup(String groupName) {
    try {
      long groupId = groupDAO.create(groupName);
      return getGroup(groupId);
    } catch (DuplicateKeyException e) {
      throw e;
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Group getGroup(Long id) {
    return groupDAO.get(id);
  }

  @Override
  public List<Group> getGroups() {
    return groupDAO.getAll();
  }

  @Override
  public List<NewElector> getElectorsInGroup(Long id) {
    return electorDAO.getAllInGroup(id);
  }

  @Override
  public void deleteGroup(Long id) {}

  @Override
  public void addElector(Long groupId, Long electorId) {
    try {
      groupDAO.addElector(groupId, electorId);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void removeElector(Long groupId, Long electorId) {
    groupDAO.removeElector(groupId, electorId);
  }
}

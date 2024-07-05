package com.ufund.api.ufundapi.viewmodel;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.CupboardDAO;
import com.ufund.api.ufundapi.persistence.UserFileDAO;

//connects User service to model
@Service
public class UserService {
  private UserFileDAO userFileDAO;
  /**
   * Service Class that acts as middle man for CRUD operations
   * @Autowired
   * @param cupboardDAO The {@link CupboardDAO User Data Access Object} to perform CRUD operations
   */
  public UserService(UserFileDAO userFileDAO){ // we need to implement a service in the 
    this.userFileDAO = userFileDAO;
  }

  /**
   * Gets User using ID from DAO
   * @param id the id used to locate {@link User user}
   * @return a {@link User user}
   */
  public User getUserByID(int id){
    return userFileDAO.getUser(id);
  }

  /**
   * Gets all toys in the cupboard
   * @return array of {@link User toys}
   * @throws IOException
   */
  public User[] getUsers() throws IOException {
    return userFileDAO.getUsers();
  }

  /**
   * Creates a user and adds it to storage using DAO call
   * @param user {@link User new user}
   * @return {@link User new user}
   * @throws IOException
   */
  public User addUser(User user) throws IOException {

    return userFileDAO.addUser(user);
  }

  public User updateUser(User user) throws IOException{
    return userFileDAO.updateUser(user);
  }

  /**
   * Removes a user from the cupboard by calling DAO
   * @param the id used to locate {@link User user}
   * @return boolean true if succesful, false otherwise
   * @throws IOException
   */
  public boolean deleteUser(int id) throws IOException {
    return userFileDAO.deleteUser(id); 
  } 

  /**
   * Gets list of all toys with name containing given string
   * @param name the name to be searched for
   * @return Array of all toys with "name" in name
   * @throws IOException
   */
  public User[] findUsers(String name) throws IOException {
    return userFileDAO.findUsers(name);
  }

  /**
   * Gets the ID of a user by their name
   * 
   * @param name The name of the user
   * @return The ID of the user if found, or -1 if not found
   */
  public int getUserIdByName(String name) {
    return userFileDAO.getUserIdByName(name);
  }

  public boolean makeUserPartner(int id) throws IOException {
    return userFileDAO.addPartner(id);
  }

  public boolean applyForPartnership(int id) throws IOException {
    return userFileDAO.applyForPartnership(id);
  }

  public List<Integer> getApplications() throws IOException {
    return userFileDAO.getApplications();
  }

  public boolean addPartner(int id) throws IOException {
    return userFileDAO.addPartner(id);
  }

  public List<Integer> getPartners() throws IOException {
    return userFileDAO.getPartners();
}

  public boolean removeApplication(int userId) throws IOException {
      return userFileDAO.removeApplication(userId);
  }

}

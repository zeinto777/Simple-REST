package pinchuk.org.rest.dao;

import org.apache.log4j.Logger;
import pinchuk.org.rest.db.Database;
import pinchuk.org.rest.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrii Pinchuk on 27.02.2017.
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    private static final String SELECT_USERS = "SELECT * FROM user u INNER JOIN user_to_category uc ON u.user_id=uc.user_id";

    private static final String SELECT_USER = SELECT_USERS + " WHERE u.user_id= ?";

    private static final String CREATE_USER = "INSERT INTO user (username, email, password, create_time) VALUES (?,?,?,?);" +
            "INSERT INTO user_to_category (user_id,category_id) VALUES (last_insert_id(),?)";

    private static final String UPDATE_USER = "UPDATE user u INNER JOIN user_to_category uc ON u.user_id=uc.user_id SET u.username = ?, u.email = ?, u.password = ?,uc.category_id=?"
            + " WHERE u.user_id = ?";

    public static List<UserDTO> get() {
        List<UserDTO> users = new ArrayList<>();
        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet resultSet = statement.executeQuery(SELECT_USERS)) {
                while (resultSet.next()) {
                    UserDTO userDTO = mapToDTO(resultSet);
                    users.add(userDTO);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return users;
    }

    public static UserDTO get(int id) {
        UserDTO userDTO = new UserDTO();
        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER)
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userDTO = mapToDTO(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return userDTO;
    }

    public static int create(UserDTO user) {
        int last_inserted_id = -1;
        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setTimestamp(4, getCurrentTimeStamp());
            preparedStatement.setInt(5, user.getCategoryId());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    last_inserted_id = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return last_inserted_id;
    }

    public static int update(UserDTO user) {
        int number_of_updated_rows = -1;
        System.out.println(user);
        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)
        ) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getCategoryId());
            preparedStatement.setInt(5, user.getId());
            number_of_updated_rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return number_of_updated_rows;
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }

    private static UserDTO mapToDTO(ResultSet resultSet) throws SQLException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(resultSet.getInt("user_id"));
        userDTO.setUsername(resultSet.getString("username"));
        userDTO.setEmail(resultSet.getString("email"));
        userDTO.setPassword(resultSet.getString("password"));
        userDTO.setDateTime(resultSet.getDate("create_time"));
        userDTO.setCategoryId(resultSet.getInt("category_id"));
        return userDTO;
    }
}

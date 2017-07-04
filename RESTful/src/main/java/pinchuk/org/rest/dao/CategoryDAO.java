package pinchuk.org.rest.dao;

import org.apache.log4j.Logger;
import pinchuk.org.rest.db.Database;
import pinchuk.org.rest.dto.CategoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrii Pinchuk on 27.02.2017.
 */
public class CategoryDAO {
    private static final Logger LOGGER = Logger.getLogger(CategoryDAO.class.getName());
    private static final String SELECT_CATEGORY = "SELECT * FROM category"
            + " WHERE category_id = ?";

    public static CategoryDTO get(int id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        try (
                Connection connection = Database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY);
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    categoryDTO.setId(resultSet.getInt("category_id"));
                    categoryDTO.setName(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return categoryDTO;
    }

}

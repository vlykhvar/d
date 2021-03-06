package mate.hwdao.dao.impl.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.hwdao.dao.DriverDao;
import mate.hwdao.dao.exception.DataProcessingException;
import mate.hwdao.lib.Dao;
import mate.hwdao.model.Driver;
import mate.hwdao.util.ConnectionUtil;

@Dao
public class DriverDaoJdbcImpl implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        String query = "INSERT INTO drivers (name, license_number, login, password) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement
                         = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, driver.getName());
            preparedStatement.setString(2, driver.getLicenseNumber());
            preparedStatement.setString(3, driver.getLogin());
            preparedStatement.setString(4, driver.getPassword());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                driver.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't saving driver " + driver
                    + " failed", ex);
        }
        return driver;
    }

    @Override
    public Optional<Driver> get(Long id) {
        Driver driver = null;
        String query = "SELECT * FROM drivers WHERE id = ? AND deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement
                             = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                driver = getDriver(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get driver with " + id, ex);
        }
        return Optional.ofNullable(driver);
    }

    @Override
    public Optional<Driver> findByLogin(String login) {
        Driver driver = null;
        String query = "SELECT * FROM drivers WHERE deleted = FALSE AND login = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement
                         = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                driver = getDriver(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get driver with " + login, ex);
        }
        return Optional.ofNullable(driver);
    }

    @Override
    public List<Driver> getAll() {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT * FROM drivers WHERE deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement
                         = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                drivers.add(getDriver(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get list of drivers ", ex);
        }
        return drivers;
    }

    @Override
    public Driver update(Driver driver) {
        String query = "UPDATE drivers SET name = ?, license_number = ?, login = ? , password = ?"
                + " WHERE id = ? AND deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement
                         = connection.prepareStatement(query)) {
            preparedStatement.setString(1, driver.getName());
            preparedStatement.setString(2, driver.getLicenseNumber());
            preparedStatement.setLong(3, driver.getId());
            preparedStatement.setString(4, driver.getLogin());
            preparedStatement.setString(5, driver.getPassword());
            if (preparedStatement.executeUpdate() > 0) {
                return driver;
            }
        } catch (SQLException ex) {
            throw new DataProcessingException(driver.toString()
                    + "was not updated, because of ", ex);
        }
        throw new RuntimeException("Can't find driver with id " + driver.getId());
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE drivers SET deleted = true WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement
                         = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("drivers with " + id
                    + "was not deleted", ex);
        }
    }

    private Driver getDriver(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getObject("id", Long.class));
        driver.setName(resultSet.getObject("name", String.class));
        driver.setLicenseNumber(resultSet.getObject("license_number", String.class));
        driver.setLogin(resultSet.getObject("login", String.class));
        driver.setPassword(resultSet.getObject("password", String.class));
        return driver;
    }
}

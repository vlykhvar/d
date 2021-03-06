package mate.hwdao.dao.impl.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.hwdao.dao.ManufacturerDao;
import mate.hwdao.dao.exception.DataProcessingException;
import mate.hwdao.lib.Dao;
import mate.hwdao.model.Manufacturer;
import mate.hwdao.util.ConnectionUtil;

@Dao
public class ManufactureDaoJdbcImpl implements ManufacturerDao {
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        String query = "INSERT INTO manufactures (name, country) VALUES (?, ?)";
        try (PreparedStatement preparedStatement
                    = ConnectionUtil.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setString(2, manufacturer.getCountry());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                manufacturer.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Saving manufacturer " + manufacturer
                    + " failed", ex);
        }
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        Manufacturer manufacturer = null;
        String query = "SELECT * FROM manufactures WHERE id = ? AND deleted = false ";
        try (PreparedStatement preparedStatement
                     = ConnectionUtil.getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                manufacturer = getManufacturer(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get manufacture with " + id, ex);
        }
        return Optional.ofNullable(manufacturer);
    }

    @Override
    public List<Manufacturer> getAll() {
        List<Manufacturer> manufacturers = new ArrayList<>();
        String query = "SELECT * FROM manufactures WHERE deleted = false";
        try (PreparedStatement preparedStatement
                     = ConnectionUtil.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                manufacturers.add(getManufacturer(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get list of manufacturers", ex);
        }
        return manufacturers;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String query = "UPDATE manufactures SET name = ?, country = ?"
                + " WHERE id = ? AND deleted = false";
        try (PreparedStatement preparedStatement
                     = ConnectionUtil.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setString(2, manufacturer.getCountry());
            preparedStatement.setLong(3, manufacturer.getId());
            if (preparedStatement.executeUpdate() > 0) {
                return manufacturer;
            }
        } catch (SQLException ex) {
            throw new DataProcessingException(manufacturer.toString()
                    + "was not updated, because of ", ex);
        }
        throw new RuntimeException("Can't find manufacturer with id " + manufacturer.getId());
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE manufactures SET deleted = true WHERE id = ?";
        try (PreparedStatement preparedStatement
                     = ConnectionUtil.getConnection()
                .prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("manufacturer with " + id
                    + "was not deleted because of", ex);
        }
    }

    private Manufacturer getManufacturer(ResultSet resultSet) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getObject(1, Long.class));
        manufacturer.setName(resultSet.getObject(2, String.class));
        manufacturer.setCountry(resultSet.getObject(3, String.class));
        return manufacturer;
    }
}

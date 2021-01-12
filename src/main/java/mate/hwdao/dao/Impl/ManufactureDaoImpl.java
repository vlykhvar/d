package mate.hwdao.dao.Impl;

import java.util.List;
import java.util.Optional;
import mate.hwdao.dao.ManufacturerDao;
import mate.hwdao.db.Storage;
import mate.hwdao.lib.Dao;
import mate.hwdao.model.Manufacturer;

@Dao
public class ManufactureDaoImpl implements ManufacturerDao {

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        Storage.listManufacturer.add(manufacturer);
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        return Optional.of(Storage.listManufacturer.stream().filter(x -> x.getId().equals(id)).findFirst().get());
    }

    @Override
    public List<Manufacturer> getAll() {
        return Storage.listManufacturer;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.listManufacturer.remove(Storage.listManufacturer.stream().filter(x -> x.getId().equals(id)).findFirst().get());
    }

}

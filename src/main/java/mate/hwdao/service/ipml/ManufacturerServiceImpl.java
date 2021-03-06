package mate.hwdao.service.ipml;

import java.util.List;
import mate.hwdao.dao.ManufacturerDao;
import mate.hwdao.lib.Inject;
import mate.hwdao.lib.Service;
import mate.hwdao.model.Manufacturer;
import mate.hwdao.service.ManufacturerService;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Inject
    private ManufacturerDao manufacturerDao;

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        manufacturerDao.create(manufacturer);
        return manufacturer;
    }

    @Override
    public Manufacturer get(Long id) {
        return manufacturerDao.get(id)
               .orElseThrow(() -> new RuntimeException("Can't find manufacturer with id " + id));
    }

    @Override
    public List<Manufacturer> getAll() {
        return manufacturerDao.getAll();
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        return manufacturerDao.update(manufacturer);
    }

    @Override
    public boolean delete(Long id) {
        return manufacturerDao.delete(id);
    }

}

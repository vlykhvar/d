package mate.hwdao.model;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private Long id;
    private String model;
    private Manufacturer manufacturer;
    private List<Driver> drivers;

    public Car(String model, Manufacturer manufacturer) {
        this.model = model;
        this.manufacturer = manufacturer;
        drivers = new ArrayList<>();
    }

    public Car() {
        drivers = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Car{"
                + "id=" + id
                + ", model='" + model + '\''
                + ", manufacturer=" + manufacturer
                + ", drivers=" + drivers.toString()
                + '}';
    }
}

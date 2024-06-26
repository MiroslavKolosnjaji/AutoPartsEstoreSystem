package com.myproject.autopartsestoresystem.bootstrap;

import com.myproject.autopartsestoresystem.model.*;
import com.myproject.autopartsestoresystem.repository.BrandRepository;
import com.myproject.autopartsestoresystem.repository.CityRepository;
import com.myproject.autopartsestoresystem.repository.CustomerRepository;
import com.myproject.autopartsestoresystem.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Miroslav Kološnjaji
 */
@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBrandData();
        loadModelData();
        loadCityData();
        loadCustomerData();
    }

    private void loadModelData() {


        Brand bmw = brandRepository.findByName("BMW").orElseThrow(() -> new RuntimeException("Brand BMW not found!"));

        Model model1 = Model.builder().id(new ModelId(bmw.getId(), "316")).brand(bmw).build();
        Model model2 = Model.builder().id(new ModelId(bmw.getId(), "318")).brand(bmw).build();
        Model model3 = Model.builder().id(new ModelId(bmw.getId(), "320")).brand(bmw).build();

        modelRepository.save(model1);
        modelRepository.save(model2);
        modelRepository.save(model3);

    }

    private void loadBrandData() {
        Brand brand1 = Brand.builder().name("BMW").build();
        Brand brand2 = Brand.builder().name("Pagani").build();
        Brand brand3 = Brand.builder().name("Lamborghini").build();

        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.save(brand3);
    }

    private void loadCityData() {
        City city1 = City.builder().name("Avon").zipCode("44011").build();
        City city2 = City.builder().name("Pittsburgh").zipCode("15226").build();
        City city3 = City.builder().name("Centennial").zipCode("80112").build();

        cityRepository.save(city1);
        cityRepository.save(city2);
        cityRepository.save(city3);
    }

    private void loadCustomerData() {
        Customer customer1 = getCustomer("John", "Smith", "1256 Harley Vincent Drive", "+4406539302", "john.smith@test.com", new City(1L,"Avon", "44011"));
        Customer customer2 = getCustomer("Sarah", "Connor", "1190 Stiles Street", "+4125718361", "sarahconnor@test.com", new City(2L,"Pittsburgh", "15226"));
        Customer customer3 = getCustomer("Anna", "Thompson", "1237 River Road", "+7192105599", "anna.thompson@example.com", new City(3L,"Centennial", "80112"));

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
    }

    private Customer getCustomer(String firstName, String lastName, String address, String phone, String email, City city) {
        return Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .phone(phone)
                .email(email)
                .city(city)
                .build();
    }
}

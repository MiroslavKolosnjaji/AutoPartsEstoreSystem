package com.myproject.autopartsestoresystem.bootstrap;

import com.myproject.autopartsestoresystem.model.*;
import com.myproject.autopartsestoresystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private final PartGroupRepository partGroupRepository;
    private final PartRepository partRepository;
    private final PriceRepository priceRepository;

    @Override
    public void run(String... args) throws Exception {
        loadPartGroupData();
        loadBrandData();
        loadModelData();
        loadPartData();
        loadCityData();
        loadCustomerData();
    }

    private void loadPartData() {

        PartGroup brakingSystem = partGroupRepository.findByName(PartGroupType.BRAKING_SYSTEM)
                .orElseThrow(() -> new RuntimeException("Part Group BRAKING_SYSTEM not found"));

        Part part1 = Part.builder()
                .partNumber("BP12345")
                .partName("Front Brake Pad Set")
                .description("High-Performance brake pads for superior stopping power. Suitable for both everyday driving and high-performance use.")
                .partGroup(brakingSystem)
                .vehicles(new ArrayList<>())
                .build();

        Part part2 = Part.builder()
                .partNumber("BR67890")
                .partName("Rear Brake Rotor")
                .description("Durable and heat-resistant brake rotor for improved braking efficiency. Designed to reduce brake noise and vibration.")
                .partGroup(brakingSystem)
                .vehicles(new ArrayList<>())
                .build();

        Part part3 = Part.builder()
                .partNumber("BC245680")
                .partName("Brake Caliper Assembly")
                .description("High-quality brake caliper for precise braking control. Comes pre-assembled with brake pads for easy installation")
                .partGroup(brakingSystem)
                .vehicles(new ArrayList<>())
                .build();

      List<Part> saved = partRepository.saveAll(List.of(part1, part2, part3));

      List<Price> prices = List.of(Price.builder().price(new BigDecimal("59.99")).currency(Currency.USD).build(),
              Price.builder().price(new BigDecimal("89.99")).currency(Currency.USD).build(),
              Price.builder().price(new BigDecimal("129.99")).currency(Currency.USD).build());

        for (int i = 0; i < saved.size(); i++) {
            prices.get(i).setId(new PriceId(saved.get(i).getId(), saved.get(i).getPartName()));
            saved.get(i).setPrices(List.of(prices.get(i)));
        }

        priceRepository.saveAll(prices);

    }

    private void loadPartGroupData() {
        PartGroup partGroup1 = PartGroup.builder()
                .name(PartGroupType.EXHAUST_SYSTEM)
                .parts(new ArrayList<>())
                .build();

        PartGroup partGroup2 = PartGroup.builder()
                .name(PartGroupType.BRAKING_SYSTEM)
                .parts(new ArrayList<>())
                .build();

        PartGroup partGroup3 = PartGroup.builder()
                .name(PartGroupType.STEERING_SYSTEM)
                .parts(new ArrayList<>())
                .build();

        partGroupRepository.saveAll(List.of(partGroup1, partGroup2, partGroup3));
    }

    private void loadModelData() {


        Brand bmw = brandRepository.findByName("BMW").orElseThrow(() -> new RuntimeException("Brand BMW not found!"));

        Model model1 = Model.builder()
                .id(new ModelId(bmw.getId(), "316"))
                .brand(bmw)
                .build();

        Model model2 = Model.builder()
                .id(new ModelId(bmw.getId(), "318"))
                .brand(bmw)
                .build();

        Model model3 = Model.builder()
                .id(new ModelId(bmw.getId(), "320"))
                .brand(bmw)
                .build();

        modelRepository.saveAll(List.of(model1, model2, model3));
    }

    private void loadBrandData() {
        Brand brand1 = Brand.builder()
                .name("BMW")
                .build();

        Brand brand2 = Brand.builder()
                .name("Pagani")
                .build();

        Brand brand3 = Brand.builder()
                .name("Lamborghini")
                .build();

        brandRepository.saveAll(List.of(brand1, brand2, brand3));
    }

    private void loadCityData() {
        City city1 = City.builder()
                .name("Avon")
                .zipCode("44011")
                .build();

        City city2 = City.builder()
                .name("Pittsburgh")
                .zipCode("15226")
                .build();

        City city3 = City.builder()
                .name("Centennial")
                .zipCode("80112")
                .build();

        cityRepository.saveAll(List.of(city1, city2, city3));
    }

    private void loadCustomerData() {

        City city1 = cityRepository.findByName("Avon").orElseThrow(() -> new RuntimeException("City Avon not found"));
        City city2 = cityRepository.findByName("Pittsburgh").orElseThrow(() -> new RuntimeException("City Pittsburgh not found"));
        City city3 = cityRepository.findByName("Centennial").orElseThrow(() -> new RuntimeException("City Centennial not found"));

        Customer customer1 = getCustomer("John", "Smith", "1256 Harley Vincent Drive", "+4406539302", "john.smith@test.com", city1);
        Customer customer2 = getCustomer("Sarah", "Connor", "1190 Stiles Street", "+4125718361", "sarahconnor@test.com", city2);
        Customer customer3 = getCustomer("Anna", "Thompson", "1237 River Road", "+7192105599", "anna.thompson@example.com", city3);


        customerRepository.saveAll(List.of(customer1, customer2, customer3));
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

package com.myproject.autopartsestoresystem.bootstrap;

import com.myproject.autopartsestoresystem.dto.*;
import com.myproject.autopartsestoresystem.mapper.BrandMapper;
import com.myproject.autopartsestoresystem.mapper.CityMapper;
import com.myproject.autopartsestoresystem.mapper.ModelMapper;
import com.myproject.autopartsestoresystem.mapper.PartMapper;
import com.myproject.autopartsestoresystem.model.*;
import com.myproject.autopartsestoresystem.model.Currency;
import com.myproject.autopartsestoresystem.repository.*;
import com.myproject.autopartsestoresystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Miroslav Kološnjaji
 */
@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final CustomerService customerService;

    private final CityService cityService;
    private final CityMapper cityMapper;

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    private final ModelService modelService;
    private final ModelMapper modelMapper;

    private final PartGroupRepository partGroupRepository;

    private final PartService partService;
    private final PartMapper partMapper;

    private final VehicleService vehicleService;
    private final CardService cardService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PurchaseOrderService purchaseOrderService;
    private final UserService userService;

    private final RoleRepository roleRepository;
    private final RoleService roleService;

    private final TextEncryptor textEncryptor;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadRoleData();
//        loadUserData();
        loadPaymentMethodData();
        loadPartGroupData();
        loadBrandData();
        loadModelData();
        loadPartData();
        loadVehicleData();
        loadCityData();
        loadCustomerData();
        loadCardData();
//        loadPurchaseOrderData();
    }

    private void loadUserData() {

        UserDTO user1 = UserDTO.builder().
                username("test@example.com")
                .password("12345678")
                .roles(Set.of(RoleDTO.builder().name(RoleName.ROLE_ADMIN).build()))
                .enabled(true)
                .build();

        userService.save(user1);
    }

    private void loadRoleData() {

        Role role1 = Role.builder().name(RoleName.ROLE_ADMIN).build();
        Role role2 = Role.builder().name(RoleName.ROLE_USER).build();
        Role role3 = Role.builder().name(RoleName.ROLE_MANAGER).build();
        Role role4 = Role.builder().name(RoleName.ROLE_STAFF).build();

        roleRepository.saveAll(List.of(role1, role2, role3, role4));
    }

    private void loadPurchaseOrderData() {

        CustomerDTO customerDTO = customerService.getById(1L);
        PartDTO partDTO = partService.getById(1L);

        if(partDTO.getPrices() == null || partDTO.getPrices().isEmpty())
            throw new IllegalArgumentException("No part prices found");

        Part part = partMapper.partDTOToPart(partDTO);



        PurchaseOrderItem purchaseOrderItem = PurchaseOrderItem.builder()
                .part(part)
                .quantity(2)
                .build();


        List<PurchaseOrderItem> purchaseOrderItems = Collections.singletonList(purchaseOrderItem);

        PurchaseOrderDTO purchaseOrderDTO = PurchaseOrderDTO.builder()
                .customer(customerDTO)
                .items(purchaseOrderItems)
                .build();

        purchaseOrderService.save(purchaseOrderDTO);
    }

    private void loadPaymentMethodData() {
        PaymentMethod paymentMethod1 = PaymentMethod.builder().paymentType(PaymentType.DEBIT_CARD).build();
        PaymentMethod paymentMethod2 = PaymentMethod.builder().paymentType(PaymentType.CREDIT_CARD).build();

        paymentMethodRepository.saveAll(List.of(paymentMethod1, paymentMethod2));
    }

    private void loadCardData() {

        CustomerDTO customer = customerService.getById(1L);


        CardDTO card = CardDTO.builder()
                .id(1L)
                .cardHolder("John Smith")
                .cardNumber(textEncryptor.encrypt("5169562420690104"))
                .expiryDate(LocalDate.of(2025,12, 1))
                .cvv("123")
                .customerId(customer.getId())
                .build();

        cardService.save(card);
    }

    private void loadVehicleData() {

        ModelDTO modelDTO = modelService.getById(new ModelId(1L, "316"));
        Model model = modelMapper.modelDtoToModel(modelDTO);

        List<PartDTO> partDTOList = partService.getAll();
        List<Part> parts = new ArrayList<>();

        for (PartDTO partDTO : partDTOList) {
            parts.add(partMapper.partDTOToPart(partDTO));
        }


        VehicleDTO vehicle1 = VehicleDTO.builder()
                .parts(parts)
                .model(model)
                .engineType("1.8i Injection")
                .series("Series 3")
                .build();

        vehicleService.save(vehicle1);
    }

    private void loadPartData() {

        List<Price> prices = Arrays.asList(Price.builder().id(new PriceId(1L, 0L)).price(new BigDecimal("122.99")).dateModified(LocalDateTime.now()).currency(Currency.USD).build(),
                Price.builder().id(new PriceId(2L, 0L)).price(new BigDecimal("131.99")).dateModified(LocalDateTime.now()).currency(Currency.USD).build(),
                Price.builder().id(new PriceId(3L, 0L)).price(new BigDecimal("213.99")).dateModified(LocalDateTime.now()).currency(Currency.USD).build());

        PartGroup brakingSystem = partGroupRepository.findByName(PartGroupType.BRAKING_SYSTEM)
                .orElseThrow(() -> new RuntimeException("Part Group BRAKING_SYSTEM not found"));

        PartDTO part1 = PartDTO.builder()
                .partNumber("BP12345")
                .partName("Front Brake Pad Set")
                .description("High-Performance brake pads for superior stopping power. Suitable for both everyday driving and high-performance use.")
                .partGroup(brakingSystem)
                .prices(List.of(prices.get(0)))
                .vehicles(new ArrayList<>())
                .build();

        PartDTO part2 = PartDTO.builder()
                .partNumber("BR67890")
                .partName("Rear Brake Rotor")
                .description("Durable and heat-resistant brake rotor for improved braking efficiency. Designed to reduce brake noise and vibration.")
                .partGroup(brakingSystem)
                .prices(List.of(prices.get(1)))
                .vehicles(new ArrayList<>())
                .build();

        PartDTO part3 = PartDTO.builder()
                .partNumber("BC245680")
                .partName("Brake Caliper Assembly")
                .description("High-quality brake caliper for precise braking control. Comes pre-assembled with brake pads for easy installation")
                .partGroup(brakingSystem)
                .prices(List.of(prices.get(2)))
                .vehicles(new ArrayList<>())
                .build();

        partService.save(part1);
        partService.save(part2);
        partService.save(part3);

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


        BrandDTO bmwDTO = brandService.getById(1L);
        Brand bmw = brandMapper.brandDTOToBrand(bmwDTO);

        ModelDTO model1 = ModelDTO.builder()
                .id(new ModelId(bmw.getId(), "316"))
                .brand(bmw)
                .build();

        ModelDTO model2 = ModelDTO.builder()
                .id(new ModelId(bmw.getId(), "318"))
                .brand(bmw)
                .build();

        ModelDTO model3 = ModelDTO.builder()
                .id(new ModelId(bmw.getId(), "320"))
                .brand(bmw)
                .build();


        modelService.save(model1);
        modelService.save(model2);
        modelService.save(model3);
    }

    private void loadBrandData() {
        BrandDTO brand1 = BrandDTO.builder()
                .name("BMW")
                .build();

        BrandDTO brand2 = BrandDTO.builder()
                .name("Pagani")
                .build();

        BrandDTO brand3 = BrandDTO.builder()
                .name("Lamborghini")
                .build();

        brandService.save(brand1);
        brandService.save(brand2);
        brandService.save(brand3);
    }

    private void loadCityData() {
        CityDTO city1 = CityDTO.builder()
                .name("Avon")
                .zipCode("44011")
                .build();

        CityDTO city2 = CityDTO.builder()
                .name("Pittsburgh")
                .zipCode("15226")
                .build();

        CityDTO city3 = CityDTO.builder()
                .name("Centennial")
                .zipCode("80112")
                .build();

        cityService.save(city1);
        cityService.save(city2);
        cityService.save(city3);
    }

    private void loadCustomerData() {

        CityDTO city1 = cityService.getById(1L);
        CityDTO city2 = cityService.getById(2L);
        CityDTO city3 = cityService.getById(3L);

        CustomerDTO customer1 = getCustomer("John", "Smith", "1256 Harley Vincent Drive", "+4406539302", "john.smith@test.com",cityMapper.cityDTOToCity(city1));
        CustomerDTO customer2 = getCustomer("Sarah", "Connor", "1190 Stiles Street", "+4125718361", "sarahconnor@test.com", cityMapper.cityDTOToCity(city2));
        CustomerDTO customer3 = getCustomer("Anna", "Thompson", "1237 River Road", "+7192105599", "anna.thompson@example.com", cityMapper.cityDTOToCity(city3));


        customerService.save(customer1);
        customerService.save(customer2);
        customerService.save(customer3);
    }

    private CustomerDTO getCustomer(String firstName, String lastName, String address, String phone, String email, City city) {
        return CustomerDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .phone(phone)
                .email(email)
                .city(city)
                .build();
    }
}

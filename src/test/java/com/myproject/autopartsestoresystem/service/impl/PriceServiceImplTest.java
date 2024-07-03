package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PriceDTO;
import com.myproject.autopartsestoresystem.mapper.PriceMapper;
import com.myproject.autopartsestoresystem.model.Currency;
import com.myproject.autopartsestoresystem.model.Price;
import com.myproject.autopartsestoresystem.model.PriceId;
import com.myproject.autopartsestoresystem.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private PriceMapper priceMapper;

    @InjectMocks
    private PriceServiceImpl priceService;

    private PriceDTO priceDTO;

    private Price price;

    @BeforeEach
    void setUp() {
        priceDTO = PriceDTO.builder()
                .id(new PriceId(1L, 1L))
                .price(new BigDecimal("1299.99"))
                .currency(Currency.USD)
                .dateModified(LocalDateTime.now())
                .build();

        price = Price.builder()
                .id(new PriceId(1L, 1L))
                .price(new BigDecimal("1299.99"))
                .currency(Currency.USD)
                .dateModified(priceDTO.getDateModified())
                .build();
    }


    @Test
    void testSavePrice_whenValidDetailsProvided_returnsPriceDTO() {

        //given

        priceDTO.setDateModified(null);
        price.setDateModified(null);

        when(priceRepository.findMaxPriceId()).thenReturn(1L);
        when(priceMapper.priceToPriceDTO(price)).thenReturn(priceDTO);
        when(priceMapper.priceDTOToPrice(priceDTO)).thenReturn(price);
        when(priceRepository.save(price)).thenReturn(price);

        //when
        PriceDTO savedDTO = priceService.save(priceDTO);

        //then
        assertNotNull(savedDTO, "Saved Price should not be null");
        assertEquals(priceDTO, savedDTO, "Saved Price should be equal to priceDTO");

        verify(priceRepository).findMaxPriceId();
        verify(priceMapper).priceToPriceDTO(price);
        verify(priceMapper).priceDTOToPrice(priceDTO);
        verify(priceRepository).save(price);

    }

    @DisplayName("Update Price - Update existing price")
    @Test
    void testUpdatePrice_whenUpdateExistingPrice_returnsPriceDTO() {

        //given
        when(priceRepository.getPriceByIdAndDateModified(price.getId(), price.getDateModified())).thenReturn(Optional.of(price));
        when(priceMapper.priceToPriceDTO(price)).thenReturn(priceDTO);
        when(priceRepository.save(price)).thenReturn(price);

        //when
        PriceDTO updatedDTO = priceService.update(price.getId(), priceDTO);

        assertNotNull(updatedDTO, "Updated Price should not be null");
        assertEquals(priceDTO, updatedDTO, "Updated Price should be equal to priceDTO");

        verify(priceRepository, times(2)).getPriceByIdAndDateModified(any(PriceId.class), any(LocalDateTime.class));
        verify(priceMapper, times(2)).priceToPriceDTO(price);
        verify(priceRepository).save(price);
    }

    @DisplayName("Update Price - Adding new price")
    @Test
    void testUpdatePrice_whenAddingNewPrice_returnsPriceDTO() {
        //given
        when(priceRepository.getPriceByIdAndDateModified(new PriceId(1L, 1L), price.getDateModified())).thenReturn(Optional.empty());
        when(priceRepository.findMaxPriceId()).thenReturn(1L);
        when(priceMapper.priceToPriceDTO(price)).thenReturn(priceDTO);
        when(priceMapper.priceDTOToPrice(priceDTO)).thenReturn(price);
        when(priceRepository.save(price)).thenReturn(price);

        //when
        PriceDTO updatedDTO = priceService.update(price.getId(), priceDTO);

        assertNotNull(updatedDTO, "Updated Price should not be null");
        assertEquals(priceDTO, updatedDTO, "Updated Price should be equal to priceDTO");

        verify(priceRepository).findMaxPriceId();
        verify(priceMapper).priceToPriceDTO(price);
        verify(priceMapper).priceDTOToPrice(priceDTO);
        verify(priceRepository).save(price);
    }

    @DisplayName("Get All Prices By Price ID")
    @Test
    void testGetAllPricesByPriceId_whenListIsPopulated_returnsListOfPriceDTO() {

        //given
        List<Price> prices = List.of(mock(Price.class), mock(Price.class), mock(Price.class));

        when(priceRepository.getPricesById(any(PriceId.class))).thenReturn(prices);

        //when
        List<PriceDTO> allPrices = priceService.getAllPricesByPriceId(price.getId());

        //then
        assertNotNull(allPrices);
        assertFalse(allPrices.isEmpty(), "List of prices should not be empty");
        assertEquals(3, allPrices.size(),"List should contain 3 elements");

        verify(priceRepository).getPricesById(any(PriceId.class));
    }

    @DisplayName("Get Price By Price ID and Last Modified Date - Returns Founded Price")
    @Test
    void testGetPriceByPriceIDAndLastModifiedDate_whenPriceExists_returnsOptionalOfPriceDTO() {

        //given
        PriceId priceId = new PriceId(1L, 0L);
        LocalDateTime modifiedDate = LocalDateTime.now();
        when(priceRepository.getPriceByIdAndDateModified(priceId, modifiedDate)).thenReturn(Optional.of(price));
        when(priceMapper.priceToPriceDTO(price)).thenReturn(priceDTO);

        //when
        Optional<PriceDTO> foundPrice = priceService.getPriceByPriceIdAndLastModifiedDate(priceId, modifiedDate);

        //then
        assertTrue(foundPrice.isPresent(), "Price should be present");
        assertEquals(priceDTO, foundPrice.get(), "Price should be equal to priceDTO");

        verify(priceRepository).getPriceByIdAndDateModified(priceId, modifiedDate);
        verify(priceMapper).priceToPriceDTO(price);
    }

    @DisplayName("Get Price By Price ID And Last Modified Date - Price Doesn't Exists")
    @Test
    void testGetPriceByPriceIDAndLastModifiedDate_whenPriceNotFound_returnsEmptyOptional() {

        //when
        Optional<PriceDTO> foundPrice = priceService.getPriceByPriceIdAndLastModifiedDate(new PriceId(1L, 0L), null);

        //then
        assertTrue(foundPrice.isEmpty(), "Price should be empty");
    }
}
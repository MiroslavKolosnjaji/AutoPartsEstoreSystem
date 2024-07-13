package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    public static final String PURCHASE_ORDER_URI = "/api/order";
    public static final String PURCHASE_ORDER_ID = "/{purchaseOrderId}";
    public static final String PURCHASE_ORDER_URI_WITH_ID = PURCHASE_ORDER_URI + PURCHASE_ORDER_ID;

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping()
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@Validated @RequestBody PurchaseOrderDTO purchaseOrderDTO) {

      try{
          PurchaseOrderDTO saved = purchaseOrderService.save(purchaseOrderDTO);

          HttpHeaders headers = new HttpHeaders();
          headers.add("Location", PURCHASE_ORDER_URI + "/" + saved.getId());

          return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
      }catch(PurchaseOrderItemNotFoundException e){
          throw new EntityNotFoundException(e.getMessage());
      }

    }

    @PutMapping(PURCHASE_ORDER_ID)
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder(@PathVariable("purchaseOrderId") Long purchaseOrderId, @Validated @RequestBody PurchaseOrderDTO purchaseOrderDTO) {

        try {

            purchaseOrderService.update(purchaseOrderId, purchaseOrderDTO);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (PurchaseOrderNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }


    @GetMapping()
    public ResponseEntity<List<PurchaseOrderDTO>> getPurchaseOrders() {

        List<PurchaseOrderDTO> purchaseOrderDTOList = purchaseOrderService.getAll();

        if (purchaseOrderDTOList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(purchaseOrderDTOList, HttpStatus.OK);
    }

    @GetMapping(PURCHASE_ORDER_ID)
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrder(@PathVariable("purchaseOrderId") Long purchaseOrderId) {

        try {

            PurchaseOrderDTO purchaseOrderDTO = purchaseOrderService.getById(purchaseOrderId);
            return new ResponseEntity<>(purchaseOrderDTO, HttpStatus.OK);
        } catch (PurchaseOrderNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(PURCHASE_ORDER_ID)
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable("purchaseOrderId") Long purchaseOrderId) {

        try {
            purchaseOrderService.delete(purchaseOrderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (PurchaseOrderNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }


}

package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.CardNotFoundException;
import com.myproject.autopartsestoresystem.model.Card;
import com.myproject.autopartsestoresystem.service.CardService;
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
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {

    public static final String CARD_URI = "/api/card";
    public static final String CARD_ID = "/{cardId}";
    public static final String CARD_URI_WITH_ID = CARD_URI + CARD_ID;

    private final CardService cardService;

    @PostMapping()
    public ResponseEntity<CardDTO> createCard(@Validated @RequestBody CardDTO cardDTO) {

        CardDTO saved = cardService.save(cardDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CARD_URI+ "/" + saved.getId());

        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
    }

    @PutMapping(CARD_ID)
    public ResponseEntity<CardDTO> updateCard(@PathVariable("cardId") Long cardId,  @Validated @RequestBody CardDTO cardDTO) {

        try{

            CardDTO updated = cardService.update(cardId, cardDTO);
            return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);

        }catch (CardNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping(CARD_ID)
    public ResponseEntity<CardDTO> getCard(@PathVariable("cardId") Long cardId) {

        try{

            CardDTO foundCard = cardService.getById(cardId);
            return new ResponseEntity<>(foundCard, HttpStatus.OK);

        }catch (CardNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }


    @GetMapping( "/holderName")
    public ResponseEntity<List<CardDTO>> getAllCardsByHolderName(@RequestParam("holderName") String holderName) {

        List<CardDTO> cards = cardService.getCardsByHolderName(holderName);

        if(cards.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @DeleteMapping(CARD_ID)
    public ResponseEntity<Void> deleteCard(@PathVariable("cardId") Long cardId) {

        try{
            cardService.delete(cardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (CardNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}

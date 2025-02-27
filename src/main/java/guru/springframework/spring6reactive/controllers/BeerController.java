package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String LOCALHOST = "http://localhost:8080";
    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeer(@PathVariable Integer beerId) {
        return beerService.deleteBeerById(beerId).thenReturn(ResponseEntity.noContent().build());
    }

    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchBeer(@PathVariable Integer beerId, @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.patchBeer(beerId, beerDTO).map(patchedBeer -> ResponseEntity.noContent().build());
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(beerId, beerDTO).map(updatedBeer -> ResponseEntity.noContent().build()); // Converti lo stream in una Response Entity
    }

    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
        return beerService.saveNewBeer(beerDTO).map(savedDto -> ResponseEntity.created(UriComponentsBuilder.fromUriString(LOCALHOST + BEER_PATH_ID).build().toUri()).build());
    }

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable Integer beerId) {
        return beerService.getBeerById(beerId);
    }

    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

}

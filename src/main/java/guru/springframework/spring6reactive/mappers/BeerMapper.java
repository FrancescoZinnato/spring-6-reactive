package guru.springframework.spring6reactive.mappers;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDTO(Beer beer);

    List<BeerDTO> beersToBeerDTOs(List<Beer> beers);

    List<Beer> beerDTOsToBeers(List<BeerDTO> dtos);

}

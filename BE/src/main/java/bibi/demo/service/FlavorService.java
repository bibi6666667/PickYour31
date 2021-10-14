package bibi.demo.service;

import bibi.demo.domain.Allergen;
import bibi.demo.domain.Base;
import bibi.demo.domain.Syrup;
import bibi.demo.domain.Topping;
import bibi.demo.domain.flavor.*;
import bibi.demo.domain.type.BaseTypeValue;
import bibi.demo.repository.*;
import bibi.demo.response.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FlavorService {

    private final FlavorRepository flavorRepository;
    private final BaseRepository baseRepository;
    private final ToppingRepository toppingRepository;
    private final SyrupRepository syrupRepository;
    private final AllergenRepository allergenRepository;
    private final FlavorBaseRepository flavorBaseRepository;

    public FlavorService(FlavorRepository flavorRepository,
                         BaseRepository baseRepository,
                         ToppingRepository toppingRepository,
                         SyrupRepository syrupRepository,
                         AllergenRepository allergenRepository,
                         FlavorBaseRepository flavorBaseRepository) {
        this.flavorRepository = flavorRepository;
        this.baseRepository = baseRepository;
        this.toppingRepository = toppingRepository;
        this.syrupRepository = syrupRepository;
        this.allergenRepository = allergenRepository;
        this.flavorBaseRepository = flavorBaseRepository;
    }

    public List<FlavorResponse> getAllFlavors() {
        List<Flavor> flavors = flavorRepository.findAll();
        return flavorsToFlavorResponses(flavors);
    }

    public List<FlavorResponse> getFlavorsByKeywordKR(String keywordKR) {
        List<Flavor> flavors = flavorRepository.findByNameKRContaining(keywordKR);
        return flavorsToFlavorResponses(flavors);
    }

    public List<FlavorResponse> getFlavorsByKeywordEN(String keywordEN) {
        List<Flavor> flavors = flavorRepository.findByNameENContainingIgnoreCase(keywordEN);
        return flavorsToFlavorResponses(flavors);
    }

    public List<FlavorResponse> getFlavorsOrderByNameKR() {
        List<Flavor> flavors = flavorRepository.findAll(Sort.by("nameKR"));
        return flavorsToFlavorResponses(flavors);
    }

    public List<FlavorResponse> getFlavorsOrderByNameEN() {
        List<Flavor> flavors = flavorRepository.findAll(Sort.by("nameEN"));
        return flavorsToFlavorResponses(flavors);
    }

    public List<FlavorResponse> getFlavorsOrderByKcal() {
        List<Flavor> flavors = flavorRepository.findAll(Sort.by("kcal"));
        return flavorsToFlavorResponses(flavors);
    }

    public List<FlavorResponse> getFlavorsFilteredBy(String baseType, String toppingType, String syrupType, String allergenType) {

        //System.out.println(baseType.equals("")); - 아무 파라미터 없이 검색할 때
        System.out.println("baseType : " + baseType);
        List<Flavor> flavors = new ArrayList<>();
        List<FlavorBase> flavorBases = flavorBaseRepository.findByBaseId(BaseTypeValue.getBaseTypeIdByNameEN(baseType));
        for (FlavorBase flavorBase : flavorBases) {
            Flavor flavor = flavorRepository.findById(flavorBase.getFlavor().getId()).orElseThrow(NoSuchElementException::new);
            System.out.println(flavor.getNameEN());
            flavors.add(flavor);
            System.out.println("필터링 결과 플레이버 : " + flavor);
        }
        return flavorsToFlavorResponses(flavors);
    }


    private List<FlavorResponse> flavorsToFlavorResponses(List<Flavor> flavors) {
        List<FlavorResponse> result = new ArrayList<>();
        for (Flavor flavor : flavors) {
            result.add(flavorToFlavorResponse(flavor));
        }
        return result;
    }

    private FlavorResponse flavorToFlavorResponse(Flavor flavor) {
        return new FlavorResponse(flavor.getId(), flavor.getNameKR(), flavor.getNameEN(), flavor.getKcal(),
                flavor.isSignature(), flavor.getInfo(), flavor.getImage(), flavor.getOnSale(),
                flavorBasesToBaseResponses(flavor.getFlavorBases()),
                flavorToppingsToToppingResponses(flavor.getFlavorToppings()),
                flavorSyrupsToSyrupResponses(flavor.getFlavorSyrups()),
                flavorAllergensToAllergenResponses(flavor.getFlavorAllergens()));
    }

    private List<BaseResponse> flavorBasesToBaseResponses(List<FlavorBase> flavorBases) {
        List<BaseResponse> result = new ArrayList<>();
        for (FlavorBase flavorBase : flavorBases) {
            Base base = baseRepository.findById(flavorBase.getBase().getId()).orElseThrow(NoSuchElementException::new);
            result.add(BaseResponse.toBaseResponse(base));
        }
        return result;
    }

    private List<ToppingResponse> flavorToppingsToToppingResponses(List<FlavorTopping> flavorToppings) {
        List<ToppingResponse> result = new ArrayList<>();
        for (FlavorTopping flavorTopping : flavorToppings) {
            Topping topping = toppingRepository.findById(flavorTopping.getTopping().getId()).orElseThrow(NoSuchElementException::new);
            result.add(ToppingResponse.toToppingResponse(topping));
        }
        return result;
    }

    private List<SyrupResponse> flavorSyrupsToSyrupResponses(List<FlavorSyrup> flavorSyrups) {
        List<SyrupResponse> result = new ArrayList<>();
        for (FlavorSyrup flavorSyrup : flavorSyrups) {
            Syrup syrup = syrupRepository.findById(flavorSyrup.getSyrup().getId()).orElseThrow(NoSuchElementException::new);
            result.add(SyrupResponse.toSyrupResponse(syrup));
        }
        return result;
    }

    private List<AllergenResponse> flavorAllergensToAllergenResponses(List<FlavorAllergen> flavorAllergens) {
        List<AllergenResponse> result = new ArrayList<>();
        for (FlavorAllergen flavorAllergen : flavorAllergens) {
            Allergen allergen = allergenRepository.findById(flavorAllergen.getAllergen().getId()).orElseThrow(NoSuchElementException::new);
            result.add(AllergenResponse.toAllergenResponse(allergen));
        }
        return result;
    }
}

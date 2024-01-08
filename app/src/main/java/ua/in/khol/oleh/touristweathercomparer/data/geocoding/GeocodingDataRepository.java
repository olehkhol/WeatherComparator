package ua.in.khol.oleh.touristweathercomparer.data.geocoding;

import static ua.in.khol.oleh.touristweathercomparer.Secrets.GEOCODING_API_KEY;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.Retrofit;
import ua.in.khol.oleh.touristweathercomparer.data.geocoding.pojo.AddressComponent;
import ua.in.khol.oleh.touristweathercomparer.data.geocoding.pojo.Result;

public class GeocodingDataRepository implements GeocodingRepository {

    private static final String STATUS_OK = "OK";
    private static final String SUBLOCALITY = "sublocality";
    private static final String LOCALITY = "locality";
    private static final String AREA_LEVEL_TWO = "administrative_area_level_2";
    private static final String AREA_LEVEL_ONE = "administrative_area_level_1";
    private static final String COUNTRY = "country";
    private static final String COMMA = ",";
    private static final String COMMA_SEPARATOR = ", ";

    private final GeocodingService geocodingService;

    public GeocodingDataRepository(Retrofit retrofit) {
        geocodingService = retrofit.create(GeocodingService.class);
    }

    @Override
    public Maybe<String> tryLocationName(double latitude, double longitude, String language) {
        return geocodingService
                .tryLocationModel(latitude + COMMA + longitude, language, GEOCODING_API_KEY)
                .filter(geocodingModel -> STATUS_OK.equals(geocodingModel.getStatus()))
                .map(geocodingModel -> {
                    List<Result> results = geocodingModel.getResults();
                    String sublocality = parseAddressComponents(results, SUBLOCALITY);
                    String locality = parseAddressComponents(results, LOCALITY);
                    String areaLevelTwo = parseAddressComponents(results, AREA_LEVEL_TWO);
                    String areaLevelOne = parseAddressComponents(results, AREA_LEVEL_ONE);
                    String country = parseAddressComponents(results, COUNTRY);

                    StringBuilder name = new StringBuilder();
                    if (isValuable(sublocality))
                        name.append(sublocality);
                    if (isValuable(locality) && notContains(locality, sublocality)) {
                        if (name.length() > 0)
                            name.append(COMMA_SEPARATOR);
                        name.append(locality);
                    }
                    if (isValuable(areaLevelTwo) && notContains(areaLevelTwo, locality)) {
                        if (name.length() > 0)
                            name.append(COMMA_SEPARATOR);
                        name.append(areaLevelTwo);
                    }
                    if (isValuable(areaLevelOne) && notContains(areaLevelOne, areaLevelTwo)) {
                        if (name.length() > 0)
                            name.append(COMMA_SEPARATOR);
                        name.append(areaLevelOne);
                    }
                    if (isValuable(country)) {
                        if (name.length() > 0)
                            name.append(COMMA_SEPARATOR);
                        name.append(country);
                    }
                    if (name.length() == 0) {
                        name.append(parseFormattedAddress(results));
                    }

                    return name.toString();
                })
                .filter(this::isValuable);
    }

    private boolean notContains(String locality, String sublocality) {
        if (!isValuable(sublocality))
            return true;

        return !locality.contains(sublocality);
    }

    private boolean isValuable(String locality) {
        return locality != null && !locality.isEmpty();
    }

    private String parseAddressComponents(List<Result> results, String type) {
        for (Result result : results)
            for (AddressComponent addressComponent : result.addressComponents)
                if (addressComponent.types.contains(type))
                    return addressComponent.longName;

        return null;
    }

    private String parseFormattedAddress(List<Result> results) {
        for (Result result : results) {
            String address = result.formattedAddress;
            if (address != null && !address.isEmpty()) {
                return address;
            }
        }

        return null;
    }
}
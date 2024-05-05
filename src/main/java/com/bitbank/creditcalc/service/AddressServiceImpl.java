package com.bitbank.creditcalc.service;

import com.bitbank.creditcalc.domain.AddressEntity;
import com.bitbank.creditcalc.utils.AddressUtils;
import com.bitbank.notificator.core.sender.EmailSender;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;

@Service
public class AddressServiceImpl implements AddressService {

    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String STREET = "addr";
    private static Logger log = Logger.getLogger(AddressServiceImpl.class);

    @Autowired
    private PropertiesConfiguration config;

    @Resource
    private JdbcTemplate siebelJdbcTemplate;

    @Autowired
    EmailSender emailSender;

    private List<String> regions;

    private List<String> cities;
      //region      //city   //streetFull  //street splitted
    Map<String, Map<String, Map<String, List<String>>>> addresses = new HashMap<>();

    @PostConstruct
    private void loadData() {
        try {
            regions = siebelJdbcTemplate.queryForList(config.getString("address.service.region.query"), String.class);
            cities = siebelJdbcTemplate.queryForList(config.getString("address.service.city.query"), String.class);

            FileInputStream fin = new FileInputStream(config.getString("address.service.datafile.path"));
            ObjectInputStream oos = new ObjectInputStream(fin);
            addresses = (Map<String, Map<String, Map<String, List<String>>>>) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Cant read addresses from file, exception: "+e.getMessage());
            log.debug("Reloading data...");
            try {
                reloadAddressesFromSiebel();
            } catch (Exception ex) {
                emailSender.sendErrorMail(ex);
            }
        }
    }

    @Override
    public synchronized void reloadAddressesFromSiebel() {
        //region      //city   //streetFull  //street splitted
        Map<String, Map<String, Map<String, List<String>>>> newAddresses = new HashMap<>();

        List<Map<String, Object>> result = siebelJdbcTemplate.queryForList(config.getString("address.service.load.query"));

        for (Map<String, Object> row: result) {
            Map<String, Map<String, List<String>>> region = newAddresses.get(row.get(COUNTRY));
            Map<String, List<String>> city;
            if (region == null) {
                region = new HashMap<>();
                newAddresses.put((String) row.get(COUNTRY), region);

                city = new HashMap<>();
                region.put((String) row.get(CITY), city);
            } else {
                city = region.get(row.get(CITY));
                if (city == null) {
                    city = new HashMap<>();
                    region.put((String) row.get(CITY), city);
                }
            }

            //putting street
            if (StringUtils.isNotEmpty((String) row.get(STREET))) {
                city.put((String) row.get(STREET), AddressUtils.splitAndLowercaseString((String) row.get(STREET)));
            }
        }

        try {
            FileOutputStream fout = new FileOutputStream(config.getString("address.service.datafile.path"));
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(newAddresses);
        } catch (IOException e) {
            log.error("Cant save addresses to file", e);
        }

        addresses = newAddresses;
    }

    @Override
    public Collection<String> getStreets(String region, String city, String searchString) {
        List<String> words = AddressUtils.splitAndLowercaseString(searchString);
        Map<Integer, List<String>> resultMap = new TreeMap<>();

        List<Map<String, List<String>>> streetsListMaps = new ArrayList<>();

        //searching for regions
        Map<String, Map<String, Map<String, List<String>>>> regions;
        if (StringUtils.isNotEmpty(region)) {
            regions = Collections.singletonMap(region, addresses.get(region));
        } else {
            regions = addresses;
        }

        //searching for cities
        for (Map<String, Map<String, List<String>>> cites4region: regions.values()) {
            if (StringUtils.isEmpty(city)) {
                streetsListMaps.addAll(cites4region.values());
            } else {
                Map<String, List<String>> street = cites4region.get(city);
                if (street != null && street.size()>0) {
                    streetsListMaps.add(street);
                }
            }
        }

        Map<String, List<String>> streetsMap = new HashMap<>();
        for (Map<String, List<String>> strFromCity: streetsListMaps) {
            streetsMap.putAll(strFromCity);
        }

        //searching for streets
        for (String street: streetsMap.keySet()) {
            List<String> streetWords = streetsMap.get(street);
            int dist = returnAddressIfSimilar(words, streetWords, street.length());
            if (dist >= 0) {
                if (resultMap.get(dist) == null) {
                    List<String> list = new ArrayList<>();
                    list.add(street);
                    resultMap.put(dist, list);
                } else {
                    resultMap.get(dist).add(street);
                }
            }
        }

        return getFirstElements(resultMap.values(), 20);//todo test + cnt to config
    }

    @Override
    public Collection<String> getCities(String region, String searchString) {
        List<String> words = AddressUtils.splitAndLowercaseString(searchString);
        Map<Integer, List<String>> resultMap = new TreeMap<>();

        if (StringUtils.isEmpty(region)) {
            for (String addr: cities) {
                int dist = returnAddressIfSimilar(words, Collections.singletonList(addr.toLowerCase()), addr.length());
                if (dist >= 0) {
                    if (resultMap.get(dist) == null) {
                        List<String> list = new ArrayList<>();
                        list.add(addr);
                        resultMap.put(dist, list);
                    } else {
                        resultMap.get(dist).add(addr);
                    }
                }
            }
        } else {
            Map<String, Map<String, List<String>>> citiesMap = addresses.get(region);
            for (String city: citiesMap.keySet()) {
                int dist = returnAddressIfSimilar(words, Collections.singletonList(city.toLowerCase()), city.length());
                if (dist >= 0) {
                    if (resultMap.get(dist) == null) {
                        List<String> list = new ArrayList<>();
                        list.add(city);
                        resultMap.put(dist, list);
                    } else {
                        resultMap.get(dist).add(city);
                    }
                }
            }
        }

        return getFirstElements(resultMap.values(), 20);//todo test + cnt to config
    }

    @Override
    public List<String> getRegions() {
        return regions;
    }

    @Override
    public Collection<AddressEntity> getAddressesForRegionCityStreet(String region, String city, String street) {
        List<AddressEntity> addresses = new ArrayList<>();
        List<Map<String, Object>> result = siebelJdbcTemplate.queryForList(config.getString("address.service.find.query"), "%"+street+"%", "%"+city+"%", "%"+region+"%");

        for (Map<String, Object> row: result) {
            addresses.add(new AddressEntity(row));
        }

        return addresses;
    }

    private Collection<String> getFirstElements(Collection<List<String>> collection, int elemsCnt) {
        Collection<String> resultCol = new ArrayList<>(elemsCnt);
        Iterator<List<String>> iter = collection.iterator();
        int i=0;
        while (i<elemsCnt && iter.hasNext()) {
            resultCol.addAll(iter.next());
        }
        return resultCol;
    }

    private <T> Collection<T> getFirstElementsGeneric(Collection<T> collection, int elemsCnt) {
        Collection<T> resultCol = new ArrayList<>(elemsCnt);
        Iterator<T> iter = collection.iterator();
        int i=0;
        while (i<elemsCnt && iter.hasNext()) {
            resultCol.add(iter.next());
        }
        return resultCol;
    }

    private int returnAddressIfSimilar(List<String> words, AddressEntity addr) {
        return 0;//returnAddressIfSimilar(words, addr.getAll(), addr.getAggrLength());
    }

    public static void main(String[] args) {
        String addr = "strname";
        String searched = "st";

        addr.contains(searched);
    }

    protected int returnAddressIfSimilar(List<String> enteredWords, List<String> addr, int addrLength) {
        //todo add substring
        int aggrDiff = 0;
        int similarCnt = 0;
        int wordsFound = 0;
        for (String word: enteredWords) {
            boolean found = false;
            for(String addrPart : addr) {
                if (word.length()<3) {//todo uncomment
                    if (addrPart.startsWith(word)) {
                        found = true;
                        similarCnt++;
                        aggrDiff += addrPart.length()-word.length();
                    }
                } else {
                    int lengthDiff = addrPart.length()-word.length();
                    if (lengthDiff<-1) {
                        continue;
                    }

                    int currDiff = AddressUtils.findDistance(word, addrPart);
                    if (currDiff < 2) {
                        similarCnt++;
                    }
                    aggrDiff += currDiff;
                    if (currDiff-lengthDiff < 2) {
                        similarCnt++;
                        found = true;
                    }
                    aggrDiff += (currDiff - lengthDiff)*3;

                    int curShortDiff = 0;
                    if (lengthDiff >= 0) {
                        curShortDiff = AddressUtils.findDistance(word, addrPart.substring(0, word.length()));
                    } else {
                        curShortDiff = currDiff;
                    }

                    if (curShortDiff < 2) {
                        similarCnt++;
                    }
                    aggrDiff += curShortDiff*2;
                }
            }
            if (found) {
                wordsFound++;
            }
        }
           /*long words*/                            /*short words*/
        if ((similarCnt >= 2* enteredWords.size() || similarCnt >= enteredWords.size()) && wordsFound>= enteredWords.size()) {
            return 100*aggrDiff/addrLength;
        } else {
            return -1;
        }
    }

}

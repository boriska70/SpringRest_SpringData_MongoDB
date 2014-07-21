package com.bb.dal.dao;/**
 * Created with IntelliJ IDEA.
 * User: belozovs
 * Date: 7/14/14
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */

import com.bb.dal.entity.Address;
import com.bb.dal.entity.Place;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:META-INF/spring/test-context.xml"})
public class PlaceWithAddressRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    private Place place;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        place = new Place();
        place.setAlias("myalias");
        place.setName("myplace");
        place.setNotes("mynotes");

    }

    @After
    public void tearDown() throws Exception {
        placeRepository.deleteAll();
    }

    @Test
    public void saveTest() throws Exception {

        List<Address> addressList = new ArrayList<Address>();

        Address address = new Address();
        address.setAlias("MainBuilding");
        address.setAddress("Street, house number");
        address.setComment("Get by metro is convenient");
        address.setMetro("Deep Station");
        addressList.add(address);
        place.setAddresses(addressList);

        address = new Address();
        address.setAlias("Building 2");
        address.setAddress("Same street, same house number");
        address.setComment("Get by taxi is convenient");
        address.setMetro("");
        addressList.add(address);

        placeRepository.save(place);

        Place myPlace = placeRepository.findByAlias(place.getAlias());
        assertNotNull(myPlace);
        System.out.println(myPlace);

        assertEquals(2, myPlace.getAddresses().size());

        Place myPlace2 = placeRepository.findByAlias(place.getAlias());

        List<Address> myAddresses2 = myPlace2.getAddresses();
        for(Address address2 : myAddresses2){
            if(address2.getAlias().indexOf("MainBuilding")==-1){
                address2.setAlias("UPDATED:" + address2.getAlias());
            }
        }
        placeRepository.save(myPlace2);

        List<Place> allPlaces = (List<Place>) placeRepository.findAll();
        assertNotNull(allPlaces);
        assertEquals(1, allPlaces.size());
        System.out.println("After update:");
        System.out.println(allPlaces.get(0));


    }


}

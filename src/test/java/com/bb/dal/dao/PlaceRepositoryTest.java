package com.bb.dal.dao;/**
 * Created with IntelliJ IDEA.
 * User: belozovs
 * Date: 7/14/14
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */

import com.bb.dal.entity.Place;
import org.junit.*;

import static junit.framework.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:META-INF/spring/test-context.xml"})
public class PlaceRepositoryTest {

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
    public void readFirstPageCorrectlyTest() throws Exception {
        Page<Place> allPlaces = placeRepository.findAll(new PageRequest(0, 10));
        assertTrue(allPlaces.isFirst());
    }

    @Test
    public void savePlaceTest() throws Exception {
        long initCount = placeRepository.count();
        Place qqq = placeRepository.save(place);
        long count = placeRepository.count();
        assertEquals(initCount+1,count);
    }

    @Test
    public void findByNameTest() throws Exception {
        placeRepository.save(place);
        Place myPlace = placeRepository.findByAlias("myalias");
        assertNotNull(myPlace);
        assertEquals("myplace", myPlace.getName());
    }

    @Test
    public void findByNameLikeTest() throws Exception {
        placeRepository.save(place);
        List<Place> myPlaces = placeRepository.findByNameLike("place");
        assertNotNull(myPlaces);
        assertEquals(1, myPlaces.size());
        assertEquals("myalias", myPlaces.get(0).getAlias());
    }

    @Test
    public void findWithQueryOnRepositoryTest() throws Exception {
        placeRepository.save(place);
        Place myPlace = placeRepository.findNameAndNotesByAlias("myalias");
        assertNotNull(myPlace);
        assertEquals("myplace", myPlace.getName());
    }

    @Test
    public void findWithQueryWithLikeOnRepositoryTest() throws Exception {
        placeRepository.save(place);
        List<Place> myPlaces = placeRepository.findNameAndNotesByAliasLike("alias");
        assertNotNull(myPlaces);
        assertEquals(1, myPlaces.size());
        assertEquals("myplace", myPlaces.get(0).getName());
    }

    @Test
    public void findWithQueryWithLikeCaseInsensitiveOnRepositoryTest() throws Exception {
        placeRepository.save(place);
        List<Place> myPlaces = placeRepository.findNameAndNotesByAliasLikeCaseInsensitive("aLIAs");
        assertNotNull(myPlaces);
        assertEquals(1, myPlaces.size());
        assertEquals("myplace", myPlaces.get(0).getName());
    }

    @Test(expected = org.springframework.dao.DuplicateKeyException.class)
    public void saveDuplicateNameTest() throws Exception {
        placeRepository.save(place);
        Place nextPlace = new Place();
        nextPlace.setName(place.getName());
        nextPlace.setAlias("nextalias");
        placeRepository.save(nextPlace);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void savePlaceWithEmptyAliasTest() throws Exception {
        Place nextPlace = new Place();
        nextPlace.setName(place.getName());
        nextPlace.setNotes("nextnotes");
        placeRepository.save(nextPlace);
    }

}

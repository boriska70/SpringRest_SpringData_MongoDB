package com.bb.dal.dao;/**
 * Created with IntelliJ IDEA.
 * User: belozovs
 * Date: 7/18/14
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.*;

import static junit.framework.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class MyTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMy() throws Exception {
        String s = "yo-dude: lRRRRRRRike, ... []{}this is a string";
        s = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        System.out.println(s);
    }

}

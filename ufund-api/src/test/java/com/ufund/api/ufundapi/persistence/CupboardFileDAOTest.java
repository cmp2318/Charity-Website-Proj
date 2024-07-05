package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;

import com.ufund.api.ufundapi.model.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CupboardFileDAOTest {

  CupboardFileDAO CupboardFileDAO;
  Toy[] testToys;
  ObjectMapper mockObjectMapper;

  /**
   * Before each test, we will create and inject a Mock Object Mapper to
   * isolate the tests from the underlying file
   * @throws IOException
   */
  // Augmented Author Matthew Tosi
  @BeforeEach
  public void setupCupboardFileDAO() throws IOException {
      mockObjectMapper = mock(ObjectMapper.class);
      testToys = new Toy[3];
      //"quantity": 10,
    //"cost": 15,
    //"type": "Plush"
      testToys[0] = new Toy(99,"Wi-Fire",2,15,"Figure");
      testToys[1] = new Toy(100,"Galactic Agent",3,16,"Figur");
      testToys[2] = new Toy(101,"Ice Gladiator",4,17,"Figu");

      // When the object mapper is supposed to read from the file
      // the mock object mapper will return the Toy array above
      when(mockObjectMapper
          .readValue(new File("doesnt_matter.txt"),Toy[].class))
              .thenReturn(testToys);
      CupboardFileDAO = new CupboardFileDAO("doesnt_matter.txt",mockObjectMapper);
  }

  // Augmented Author Matthew Tosi
  @Test
  public void testSaveException() throws IOException{
      doThrow(new IOException())
          .when(mockObjectMapper)
              .writeValue(any(File.class),any(Toy[].class));

      Toy toy = new Toy(102,"Wi-Fire",2,15,"Figure");

      assertThrows(IOException.class,
                      () -> CupboardFileDAO.createToy(toy),
                      "IOException not thrown");
  }

  // Augmented Author Matthew Tosi
  @Test
  public void testConstructorException() throws IOException {
      // Setup
      ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
      // We want to simulate with a Mock Object Mapper that an
      // exception was raised during JSON object deseerialization
      // into Java objects
      // When the Mock Object Mapper readValue method is called
      // from the HeroFileDAO load method, an IOException is
      // raised
      doThrow(new IOException())
          .when(mockObjectMapper)
              .readValue(new File("doesnt_matter.txt"),Toy[].class);

      // Invoke & Analyze
      assertThrows(IOException.class,
                      () -> new CupboardFileDAO("doesnt_matter.txt",mockObjectMapper),
                      "IOException not thrown");
  }


  //----------------------------------------------------------------------------
  @Test
  public void testGetToys() {
      // Invoke
      Toy[] toys = CupboardFileDAO.getToys();

      // Analyze
      assertEquals(toys.length,testToys.length);
      for (int i = 0; i < testToys.length;++i)
          assertEquals(toys[i],testToys[i]);
  }


  @Test
  public void testFindToys() {
      // Invoke
      Toy[] toys = CupboardFileDAO.findToys("la");

      // Analyze
      assertEquals(toys.length,2);
      assertEquals(toys[0],testToys[1]);
      assertEquals(toys[1],testToys[2]);
  }

  
  @Test
  public void testGetToy() {
      // Invoke
      Toy toy = CupboardFileDAO.getToy(99);

      // Analzye
      assertEquals(toy,testToys[0]);
  }

  @Test
  public void testGetHeroNotFound() {
      // Invoke
      Toy toy = CupboardFileDAO.getToy(98);

      // Analyze
      assertEquals(toy,null);
  }
  
  
  //Delete - 
  @Test
  public void testDeleteToy() {
      // Invoke
      boolean result = assertDoesNotThrow(() -> CupboardFileDAO.deleteToy(99),
                          "Unexpected exception thrown");

      // Analzye
      
      assertEquals(result,true);
      // We check the internal tree map size against the length
      // of the test heroes array - 1 (because of the delete)
      // Because heroes attribute of CupboardFileDAO is package private
      // we can access it directly
      assertEquals(CupboardFileDAO.getToys().length,testToys.length-1);
  }


  @Test
  public void testDeleteToyNotFound() {
      // Invoke
      boolean result = assertDoesNotThrow(() -> CupboardFileDAO.deleteToy(98),
                                              "Unexpected exception thrown");

      // Analyze
      assertEquals(result,false);
      assertEquals(CupboardFileDAO.getToys().length,testToys.length);
  }


  //---------------------------------------------------------------------------
  //Create - 

  @Test
  public void testCreateHero() {
      // Setup
      Toy toy = new Toy(102,"Wonder-Person",21,34,"Test");

      // Invoke
      Toy result = assertDoesNotThrow(() -> CupboardFileDAO.createToy(toy),
                              "Unexpected exception thrown");

      // Analyze
      assertNotNull(result);
      Toy actual = CupboardFileDAO.getToy(toy.getId());
      assertEquals(actual.getId(),toy.getId());
      assertEquals(actual.getName(),toy.getName());
  }
  
  //----------------------------------------------------------------------------
  
  //Update - 
  @Test
  public void testUpdateHero() {
      // Setup
      Toy toy = new Toy(99,"Galactic Agent",12,34,"agent");

      // Invoke
      Toy result = assertDoesNotThrow(() -> CupboardFileDAO.updateToy(toy),
                              "Unexpected exception thrown");

      // Analyze
      assertNotNull(result);
      Toy actual = CupboardFileDAO.getToy(toy.getId());
      assertEquals(actual,toy);
  }
  @Test
  public void testUpdateHeroNotFound() {
      // Setup
      Toy toy = new Toy(98,"Bolt",12,45,"Metal");

      // Invoke
      Toy result = assertDoesNotThrow(() -> CupboardFileDAO.updateToy(toy),
                                              "Unexpected exception thrown");

      // Analyze
      assertNull(result);
  }
  
  //----------------------------------------------------------------------------
  
}

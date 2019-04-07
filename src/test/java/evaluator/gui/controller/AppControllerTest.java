package evaluator.gui.controller;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.model.Intrebare;
import evaluator.repository.IntrebariRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class AppControllerTest {

    @Mock
    private IntrebariRepository repo;

    private AppController appController;

    //TC1_ECP
    @Test
    public void addNewIntrebare_ValidInput_IntrebareIsReturned() {
        //Arrange
        appController = new AppController(repo);
        Intrebare intrebare = createValidIntrebare();
        //Act
        try {
            Intrebare result = appController.addNewIntrebare(intrebare, "valid");
            //Assert
            assertEquals(intrebare, result);
        } catch (DuplicateIntrebareException | IOException ex) {
            fail();
        }
    }

    //TC2_ECP
    @Test(expected = IllegalArgumentException.class)
    public void addNewIntrebare_IntrebareIsNull_IllegalArgumentExceptionIsThrown() {
        //Arrange
        appController = new AppController(repo);
        //Act
        try {
            appController.addNewIntrebare(null, "valid");
        } catch (DuplicateIntrebareException | IOException ex) {
            fail();
        }
    }

    //TC3_ECP / TC1_BVA
    @Test(expected = IllegalArgumentException.class)
    public void addNewIntrebare_FilePathIsNull_IllegalArgumentExceptionIsThrown(){
        //Arrange
        appController = new AppController(repo);
        Intrebare intrebare = createValidIntrebare();
        //Act
        try {
            appController.addNewIntrebare(intrebare, null);
        } catch (DuplicateIntrebareException | IOException ex) {
            fail();
        }
    }

    //TC2_BVA
    @Test
    public void addNewIntrebare_FilePathHasLength0_IntrebareIsReturned() {
        //Arrange
        appController = new AppController(repo);
        Intrebare intrebare = createValidIntrebare();
        //Act
        try {
            Intrebare result = appController.addNewIntrebare(intrebare, "");
            //Assert
            assertEquals(intrebare, result);
        } catch (DuplicateIntrebareException | IOException ex) {
            fail();
        }
    }

    //TC3_BVA
    @Test
    public void addNewIntrebare_FilePathHasLength1_IntrebareIsReturned(){
        //Arrange
        appController = new AppController(repo);
        Intrebare intrebare = createValidIntrebare();
        //Act
        try {
            Intrebare result = appController.addNewIntrebare(intrebare, "1");
            //Assert
            assertEquals(intrebare, result);
        } catch (DuplicateIntrebareException | IOException ex) {
            fail();
        }
    }


    //TC6_BVA
    @Test(expected = IllegalArgumentException.class)
    public void addNewIntrebare_FilePathHasLength256_IllegalArgumentExceptionIsThrown() {
        //Arrange
        appController = new AppController(repo);
        Intrebare intrebare = createValidIntrebare();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= 256; i++) {
            stringBuilder.append('a');
        }
        String filePath = stringBuilder.toString();
        //Act
        try {
            Intrebare result = appController.addNewIntrebare(intrebare, filePath);
        } catch (DuplicateIntrebareException | IOException ex) {
            fail();
        }
    }


    //F02_TC1
    @Test(expected = NotAbleToCreateTestException.class)
    public void createNewTest_4Intrebari4Domenii_NotAbleToCreateTestExceptionIsThrown() throws NotAbleToCreateTestException {
        //Arrange
        appController = new AppController(repo);
        Mockito.when(repo.getIntrebari()).thenReturn(createValidIntrebari(4));
        Mockito.when(repo.getNumberOfDistinctDomains()).thenReturn(4);
        //Act
        appController.createNewTest();
    }

    //F02_TC2
    @Test(expected = NotAbleToCreateTestException.class)
    public void createNewTest_5Intrebari1Domeniu_NotAbleToCreateTestExceptionIsThrown() throws NotAbleToCreateTestException {
        //Arrange
        appController = new AppController(repo);
        Mockito.when(repo.getIntrebari()).thenReturn(createValidIntrebari(5));
        Mockito.when(repo.getNumberOfDistinctDomains()).thenReturn(1);
        //Act
        appController.createNewTest();
    }

    //F02_TC3
    @Test
    public void createNewTest_7Intrebari7DomeniiTC03_TestOf5IntrebariReturned(){
        //Arrange
        appController = new AppController(repo);
        List<Intrebare> intrebari = createValidIntrebari(7);
        Mockito.when(repo.getIntrebari()).thenReturn(intrebari);
        Mockito.when(repo.getNumberOfDistinctDomains()).thenReturn(7);
        Mockito.when(repo.pickRandomIntrebare()).thenReturn(intrebari.get(0),intrebari.get(1),intrebari.get(2),intrebari.get(3),intrebari.get(4));
        //Act
        try{
           appController.createNewTest();
        }catch (NotAbleToCreateTestException ex){
            fail();
        }
    }

    //F02_TC4
    @Test
    public void createNewTest_7Intrebari7DomeniiTC04_TestOf5IntrebariReturned(){
        //Arrange
        appController = new AppController(repo);
        List<Intrebare> intrebari = createValidIntrebari(7);
        Mockito.when(repo.getIntrebari()).thenReturn(intrebari);
        Mockito.when(repo.getNumberOfDistinctDomains()).thenReturn(7);
        Mockito.when(repo.pickRandomIntrebare()).thenReturn(intrebari.get(0),intrebari.get(0),intrebari.get(2),intrebari.get(3),intrebari.get(4), intrebari.get(5));
        //Act
        try{
            appController.createNewTest();
        }catch (NotAbleToCreateTestException ex){
            fail();
        }
    }


    //F02_TC5
    @Test
    public void createNewTest_7Intrebari7DomeniiTC05_TestOf5IntrebariReturned(){
        //Arrange
        appController = new AppController(repo);
        List<Intrebare> intrebari = createValidIntrebari(7);
        Mockito.when(repo.getIntrebari()).thenReturn(intrebari);
        Mockito.when(repo.getNumberOfDistinctDomains()).thenReturn(7);
        intrebari.get(1).setDomeniu(intrebari.get(0).getDomeniu());
        Mockito.when(repo.pickRandomIntrebare()).thenReturn(intrebari.get(0),intrebari.get(1),intrebari.get(2),intrebari.get(3),intrebari.get(4), intrebari.get(5));
        //Act
        try{
            appController.createNewTest();
        }catch (NotAbleToCreateTestException ex){
            fail();
        }
    }

    private Intrebare createValidIntrebare() {
        Intrebare intrebare = new Intrebare();
        intrebare.setEnunt("Enunt?");
        intrebare.setVarianta1("1)varianta1");
        intrebare.setVarianta2("2)varianta2");
        intrebare.setVarianta3("3)varianta3");
        intrebare.setVariantaCorecta("2");
        intrebare.setDomeniu("Domeniu");
        return intrebare;
    }

    private List<Intrebare> createValidIntrebari(int n){
        List<Intrebare> intrebari = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Intrebare intrebare = createValidIntrebare();
            intrebare.setDomeniu(intrebare.getDomeniu() + i);
            intrebari.add(intrebare);
        }
        return intrebari;
    }
}
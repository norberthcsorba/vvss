package evaluator.gui.controller;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.model.Intrebare;
import evaluator.repository.IntrebariRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.*;

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
        try{
            Intrebare result = appController.addNewIntrebare(intrebare, "valid");
            //Assert
            assertEquals(intrebare, result);
        }catch (DuplicateIntrebareException | IOException ex){
            fail();
        }
    }

    //TC2_ECP
    @Test(expected = IllegalArgumentException.class)
    public void addNewIntrebare_IntrebareIsNull_IllegalArgumentExceptionIsThrown(){
        //Arrange
        appController = new AppController(repo);
        //Act
        try {
            appController.addNewIntrebare(null, "valid");
        }catch (DuplicateIntrebareException | IOException ex){
            fail();
        }
    }

    //TC2_BVA
    @Test
    public void addNewIntrebare_FilePathHasLength0_IntrebareIsReturned(){
        //Arrange
        appController = new AppController(repo);
        Intrebare intrebare = createValidIntrebare();
        //Act
        try{
            Intrebare result = appController.addNewIntrebare(intrebare, "");
            //Assert
            assertEquals(intrebare, result);
        }catch (DuplicateIntrebareException | IOException ex){
            fail();
        }
    }

    //TC6_BVA
    @Test(expected = IllegalArgumentException.class)
    public void addNewIntrebare_FilePathHasLength256_IllegalArgumentExceptionIsThrown(){
        //Arrange
        appController = new AppController(repo);
        Intrebare intrebare = createValidIntrebare();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 1; i <= 256; i++){
            stringBuilder.append('a');
        }
        String filePath = stringBuilder.toString();
        //Act
        try{
            Intrebare result = appController.addNewIntrebare(intrebare, filePath);
        }catch (DuplicateIntrebareException | IOException ex){
            fail();
        }
    }


    private Intrebare createValidIntrebare(){
        Intrebare intrebare = new Intrebare();
        intrebare.setEnunt("Enunt?");
        intrebare.setVarianta1("1)varianta1");
        intrebare.setVarianta2("2)varianta2");
        intrebare.setVarianta3("3)varianta3");
        intrebare.setVariantaCorecta("2");
        intrebare.setDomeniu("Domeniu");
        return intrebare;
    }
}
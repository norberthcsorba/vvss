package integration;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.NotAbleToCreateStatisticsException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.gui.controller.AppController;
import evaluator.main.StartApp;
import evaluator.model.Intrebare;
import evaluator.model.Statistica;
import evaluator.repository.IntrebariRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TopDown {

    private static final String testFile = "src/test/resources/intrebari.txt";

    @Mock
    private PrintStream out;

    @Mock
    private BufferedReader console;

    @Before
    public void SetUp() throws NoSuchFieldException, IllegalAccessException, IOException{
        System.setOut(out);
        Field fileField = StartApp.class.getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(null, testFile);
        Field consoleField = StartApp.class.getDeclaredField("console");
        consoleField.setAccessible(true);
        consoleField.set(null, console);

        PrintWriter writer = new PrintWriter(testFile);
        writer.print("");
        writer.close();
    }

    //TC1_ECP
    @Test
    public void addNewIntrebare_ValidInput_IntrebareIsReturned() {
        //Arrange
        IntrebariRepository repo = Mockito.mock(IntrebariRepository.class);
        AppController appController = new AppController(repo);
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

    //F02_TC3
    @Test
    public void createNewTest_7Intrebari7DomeniiTC03_TestOf5IntrebariReturned() {
        //Arrange
        IntrebariRepository repo = Mockito.mock(IntrebariRepository.class);
        AppController appController = new AppController(repo);
        List<Intrebare> intrebari = createValidIntrebari(7);
        Mockito.when(repo.getIntrebari()).thenReturn(intrebari);
        Mockito.when(repo.getNumberOfDistinctDomains()).thenReturn(7);
        Mockito.when(repo.pickRandomIntrebare()).thenReturn(intrebari.get(0), intrebari.get(1), intrebari.get(2), intrebari.get(3), intrebari.get(4));
        //Act
        try {
            evaluator.model.Test test = appController.createNewTest();
            assertEquals(test.getIntrebari().size(), 5);
        } catch (NotAbleToCreateTestException ex) {
            fail();
        }
    }

    //F03_Valid
    @Test
    public void getStatistica_NrOfIntrebariGreaterThan0_ValidResult() {
        //Arrange
        IntrebariRepository repo = Mockito.mock(IntrebariRepository.class);
        AppController appController = new AppController(repo);
        String domain = "d1";
        Mockito.when(repo.getIntrebari()).thenReturn(Arrays.asList((Intrebare) null));
        Mockito.when(repo.getDistinctDomains()).thenReturn(new HashSet<>(Arrays.asList(domain)));
        Mockito.when(repo.getIntrebariByDomain("d1")).thenReturn(Arrays.asList((Intrebare) null));
        //Act
        try {
            Statistica statistica = appController.getStatistica();
            //Assert
            assertEquals(1, statistica.getIntrebariDomenii().keySet().size());
            assertTrue(statistica.getIntrebariDomenii().keySet().contains(domain));
            assertEquals(1, (long) statistica.getIntrebariDomenii().get(domain));
        } catch (NotAbleToCreateStatisticsException ex) {
            fail();
        }
    }


    //TopDown P-A
    @Test
    public void main_PA() throws IOException {
        //Arrange
        Mockito.when(console.readLine()).thenReturn("1", "4");
        //Act
        StartApp.main(null);
        //Assert
        Mockito.verify(out, Mockito.times(1)).println(Mockito.isA(Intrebare.class));
    }

    //TopDown P-A-B
    @Test
    public void main_PAB() throws IOException {
        //Arrange
        Mockito.when(console.readLine()).thenReturn("1","1","1","1","1", "2","4");
        //Act
        StartApp.main(null);
        //Assert
        Mockito.verify(out, Mockito.times(5)).println(Mockito.isA(Intrebare.class));
        Mockito.verify(out, Mockito.times(1)).println(Mockito.isA(evaluator.model.Test.class));
    }

    //TopDown P-A-B-C
    @Test
    public void main_PABC() throws IOException {
        //Arrange
        Mockito.when(console.readLine()).thenReturn("1","1","1","1","1", "2","3","4");
        //Act
        StartApp.main(null);
        //Assert
        Mockito.verify(out, Mockito.times(5)).println(Mockito.isA(Intrebare.class));
        Mockito.verify(out, Mockito.times(1)).println(Mockito.isA(evaluator.model.Test.class));
        Mockito.verify(out, Mockito.times(1)).println(Mockito.isA(Statistica.class));
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

    private List<Intrebare> createValidIntrebari(int n) {
        List<Intrebare> intrebari = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Intrebare intrebare = createValidIntrebare();
            intrebare.setDomeniu(intrebare.getDomeniu() + i);
            intrebari.add(intrebare);
        }
        return intrebari;
    }


}

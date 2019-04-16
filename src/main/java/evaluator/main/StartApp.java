package evaluator.main;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.InputValidationFailedException;
import evaluator.exception.NotAbleToCreateStatisticsException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.gui.controller.AppController;
import evaluator.model.Intrebare;
import evaluator.model.Statistica;
import evaluator.model.Test;
import evaluator.repository.IntrebariRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

//functionalitati
//F01.	 adaugarea unei noi intrebari pentru un anumit domeniu (enunt intrebare, raspuns 1, raspuns 2, raspuns 3, raspunsul corect, domeniul) in setul de intrebari disponibile;
//F02.	 crearea unui nou test (testul va contine 5 intrebari alese aleator din cele disponibile, din domenii diferite);
//F03.	 afisarea unei statistici cu numarul de intrebari organizate pe domenii.

public class StartApp {

    private static String file = "src/main/resources/intrebari.txt";

    private static BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {


        IntrebariRepository intrebariRepository = new IntrebariRepository();
        intrebariRepository.setIntrebari(intrebariRepository.loadIntrebariFromFile(file));
        AppController appController = new AppController(intrebariRepository);

        boolean activ = true;
        String optiune = null;

        while (activ) {

            System.out.println("");
            System.out.println("1.Adauga intrebare");
            System.out.println("2.Creeaza test");
            System.out.println("3.Statistica");
            System.out.println("4.Exit");
            System.out.println("");

            optiune = console.readLine();

            switch (optiune) {
                case "1":
                    //Aici ar veni cod pentru interactiunea cu utilizatorul in ce priveste adaugarea unei noi intrebari
                    String enunt = "A" + UUID.randomUUID().toString().substring(0,20) + "?";
                    String varianta1 = "1)" + UUID.randomUUID().toString().substring(0, 20);
                    String varianta2 = "2)" + UUID.randomUUID().toString().substring(0, 20);
                    String varianta3 = "3)" + UUID.randomUUID().toString().substring(0, 20);
                    String variantaCorecta = "1";
                    String domeniu = "A" + UUID.randomUUID().toString().substring(0, 20);
                    try {
                        Intrebare intrebare = new Intrebare(enunt, varianta1, varianta2, varianta3, variantaCorecta, domeniu);
                        appController.addNewIntrebare(intrebare, file);
                        System.out.println(intrebare);
                    } catch (InputValidationFailedException | DuplicateIntrebareException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        Test test = appController.createNewTest();
                        System.out.println(test);
                    } catch (NotAbleToCreateTestException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case "3":
                    try {
                        appController.loadIntrebariFromFile(file);
                        Statistica statistica = appController.getStatistica();
                        System.out.println(statistica);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    } catch (NotAbleToCreateStatisticsException e) {
                        System.out.println("Statisticile nu au putut fi create");
                    }

                    break;
                case "4":
                    activ = false;
                    break;
                default:
                    System.out.println("Comanda invalida");
                    break;
            }
        }

    }

}

package evaluator.gui.controller;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.NotAbleToCreateStatisticsException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.model.Intrebare;
import evaluator.model.Statistica;
import evaluator.model.Test;
import evaluator.repository.IntrebariRepository;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AppController {

    private IntrebariRepository intrebariRepository;

    public AppController(IntrebariRepository repo) {
        intrebariRepository = repo;
    }

    public Intrebare addNewIntrebare(Intrebare intrebare, String filePath) throws DuplicateIntrebareException, IOException {
        if(intrebare == null || filePath == null || filePath.length() > 255){
            throw new IllegalArgumentException();
        }
        intrebariRepository.addIntrebare(intrebare, filePath);
        return intrebare;
    }

    public boolean exists(Intrebare intrebare) {
        return intrebariRepository.exists(intrebare);
    }

    public Test createNewTest() throws NotAbleToCreateTestException {

        if (intrebariRepository.getIntrebari().size() < 5)
            throw new NotAbleToCreateTestException("Nu exista suficiente intrebari pentru crearea unui test!(5)");

        if (intrebariRepository.getNumberOfDistinctDomains() < 4)
            throw new NotAbleToCreateTestException("Nu exista suficiente domenii pentru crearea unui test!(5)");

        List<Intrebare> testIntrebari = new LinkedList<Intrebare>();
        List<String> domenii = new LinkedList<String>();
        Intrebare intrebare;
        Test test = new Test();

        while (testIntrebari.size() < 5) {
            intrebare = intrebariRepository.pickRandomIntrebare();

            if (!testIntrebari.contains(intrebare) && !domenii.contains(intrebare.getDomeniu())) {
                testIntrebari.add(intrebare);
                domenii.add(intrebare.getDomeniu());
            }

        }

        test.setIntrebari(testIntrebari);
        return test;

    }

    public void loadIntrebariFromFile(String f) throws IOException {
        intrebariRepository.setIntrebari(intrebariRepository.loadIntrebariFromFile(f));
    }

    public Statistica getStatistica() throws NotAbleToCreateStatisticsException {

        if (intrebariRepository.getIntrebari().isEmpty())
            throw new NotAbleToCreateStatisticsException("Repository-ul nu contine nicio intrebare!");

        Statistica statistica = new Statistica();
        for (String domeniu : intrebariRepository.getDistinctDomains()) {
            statistica.add(domeniu, intrebariRepository.getIntrebari().size());
        }

        return statistica;
    }

}

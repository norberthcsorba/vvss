package evaluator.repository;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.InputFileFormatException;
import evaluator.model.Intrebare;

import java.io.*;
import java.util.*;

public class IntrebariRepository {

    private List<Intrebare> intrebari;

    public IntrebariRepository() {
        setIntrebari(new LinkedList<Intrebare>());
    }

    public void addIntrebare(Intrebare i, String filePath) throws DuplicateIntrebareException, IOException {
        if (exists(i))
            throw new DuplicateIntrebareException("Intrebarea deja exista!");
        intrebari.add(i);
        try(PrintWriter writer = new PrintWriter(new FileOutputStream(filePath, true))){
            writer.println(i.getEnunt());
            writer.println(i.getVarianta1());
            writer.println(i.getVarianta2());
            writer.println(i.getVarianta3());
            writer.println(i.getVariantaCorecta());
            writer.println(i.getDomeniu());
            writer.println("##");
        }catch (IOException ex){
            throw ex;
        }
    }

    public boolean exists(Intrebare i) {
        for (Intrebare intrebare : intrebari)
            if (intrebare.equals(i))
                return true;
        return false;
    }

    public Intrebare pickRandomIntrebare() {
        Random random = new Random();
        return intrebari.get(random.nextInt(intrebari.size()));
    }

    public int getNumberOfDistinctDomains() {
        return getDistinctDomains().size();

    }

    public Set<String> getDistinctDomains() {
        Set<String> domains = new TreeSet<String>();
        for (Intrebare intrebre : intrebari)
            domains.add(intrebre.getDomeniu());
        return domains;
    }

    public List<Intrebare> getIntrebariByDomain(String domain) {
        List<Intrebare> intrebariByDomain = new LinkedList<Intrebare>();
        for (Intrebare intrebare : intrebari) {
            if (intrebare.getDomeniu().equals(domain)) {
                intrebariByDomain.add(intrebare);
            }
        }

        return intrebariByDomain;
    }

    public int getNumberOfIntrebariByDomain(String domain) {
        return getIntrebariByDomain(domain).size();
    }

    public List<Intrebare> loadIntrebariFromFile(String f) throws IOException {

        List<Intrebare> intrebari = new LinkedList<Intrebare>();
        String line = null;
        List<String> intrebareAux = new LinkedList<String>();
        Intrebare intrebare;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            while ((line = br.readLine()) != null) {
                if(!line.equals("##")) {
                    intrebareAux.add(line);
                } else {
                    if(intrebareAux.size() != 6){
                        throw new InputFileFormatException();
                    }
                    intrebare = new Intrebare();
                    intrebare.setEnunt(intrebareAux.get(0));
                    intrebare.setVarianta1(intrebareAux.get(1));
                    intrebare.setVarianta2(intrebareAux.get(2));
                    intrebare.setVarianta3(intrebareAux.get(3));
                    intrebare.setVariantaCorecta(intrebareAux.get(4));
                    intrebare.setDomeniu(intrebareAux.get(5));
                    intrebari.add(intrebare);
                    intrebareAux = new LinkedList<String>();
                }
            }

        } catch (IOException e) {
            throw e;
        }

        return intrebari;
    }

    public List<Intrebare> getIntrebari() {
        return intrebari;
    }

    public void setIntrebari(List<Intrebare> intrebari) {
        this.intrebari = intrebari;
    }

}

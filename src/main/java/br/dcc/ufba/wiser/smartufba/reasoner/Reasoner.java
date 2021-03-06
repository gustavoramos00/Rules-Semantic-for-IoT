package br.dcc.ufba.wiser.smartufba.reasoner;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;

import java.io.PrintWriter;
import java.util.Iterator;

;

public class Reasoner {

//    private static String fname = "http://192.168.0.13:3030/sistemasweb";
    private static String fname = "teste.ttl";
    private static String NS = "http://www.loa-cnr.it/ontologies/DUL.owl#/";

    public void reasoner() {

        Model data = FileManager.get().loadModel(fname);

//		    Regra sendo elaborada pelo Gustavo

        GenericRuleReasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL("rules.txt"));

        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, data);

        PrintWriter out = new PrintWriter(System.out);

//        for (StmtIterator i = inf.listStatements(inf.getResource(NS + "A"), inf.getProperty(NS + "p"), inf.getResource(NS + "D")); i.hasNext(); ) {
        for (StmtIterator i = inf.listStatements(); i.hasNext(); ) {
            Statement s = i.nextStatement();
            System.out.println("Statement is " + s);
            for (Iterator id = inf.getDerivation(s); id.hasNext(); ) {
                Derivation deriv = (Derivation) id.next();
                deriv.printTrace(out, true);
            }
        }
        out.flush();

        UpdateModel updatemodel = new UpdateModel();

        //Caso ocorra o Match na inferência o modelo é atualizado  ()

        String tripleStoreURI = "" +
            "PREFIX  j.1: <http://purl.oclc.org/NET/ssnx/ssn#>\n" +
            "PREFIX  j.0: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
            "PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#>\n" +
            "DELETE { <http://wiser.dcc.ufba.br/smartUFBA/devices/ufbaino#obsValue_14915308050001491530865086>\n" +
            "						       a                 j.1:ObservationValue ;\n" +
            "                            j.0:hasDataValue  \"37\"^^xsd:double ;\n" +
            "                            j.0:isSettingFor  false .}\n" +

            "INSERT { <http://wiser.dcc.ufba.br/smartUFBA/devices/ufbaino#obsValue_14915308050001491530865086>\n" +
            "                  a                 j.1:ObservationValue ;\n" +
            "                  j.0:hasDataValue  \"37\"^^xsd:double ;\n" +
            "                  j.0:isSettingFor  true .}\n " +

            "  WHERE { <http://wiser.dcc.ufba.br/smartUFBA/devices/ufbaino#obsValue_14915308050001491530865086>" +
            "                   a                 j.1:ObservationValue ;\n" +
            "                   j.0:hasDataValue  \"37\"^^xsd:double ;\n" +
            "                   j.0:isSettingFor  false . }";


        updatemodel.updateTripleStore(tripleStoreURI, data, fname);

    }

    public static void main(String[] args) {
        Reasoner reasoner = new Reasoner();
        reasoner.reasoner();
    }


}

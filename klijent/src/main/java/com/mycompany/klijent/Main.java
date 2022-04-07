/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.klijent;




import izvrsivaci.Get;
import izvrsivaci.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author Gavrilo
 */
public class Main {
    
    
    
    static Izvrsilac[] izvrsioci={new Get(),new Post(),new Delete(),new Put()};
    static Izvrsilac izvrsilac=izvrsioci[0];
    
    public static JPanel comboBox(){
        JPanel panel = new JPanel();
        final JComboBox combobox = new JComboBox();
      final JList list = new JList(new DefaultListModel());
      panel.add(BorderLayout.NORTH, combobox);
      panel.add(BorderLayout.CENTER, list);
      combobox.setEditable(true);
      combobox.setPrototypeDisplayValue("http://localhost:8080/Server/resources/podsistem2/transakcije/dohvatiTransakcije/{idR}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem1/mesto/svaMesta");
      
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem1/filijala/sveFilijale");
      
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem1/komitent/sviKomitenti");
      
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem2/racun/dohvatiRacune/{idK}");
      
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem2/transakcije/dohvatiTransakcije/{idR}");
      
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem3/razlike");
      
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem3/rezervnaBaza");
      
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem1/komitent/{naziv}/{adresa}/{idMesta}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem1/filijala/{naziv}/{adresa}/{idMesta}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem1/mesto/{naziv}/{postanskiBroj}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem1/komitent/promeniMesto/{idK}/{idMesta}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem2/racun/otvori/{idK}/{dozvoljenMinus}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem2/racun/zatvori/{idR}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem2/transakcije/prenos/{idRSa}/{idRNa}/{iznos}/{svrha}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem2/transakcije/uplata/{idR}/{idFil}/{iznos}/{svrha}");
      ((DefaultListModel) list.getModel()).addElement("http://localhost:8080/Server/resources/podsistem2/transakcije/isplata/{idR}/{idFil}/{iznos}/{svrha}");
      
      
      combobox.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent ie) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
               ((DefaultListModel) list.getModel()).addElement(combobox.getSelectedItem());
               combobox.insertItemAt(combobox.getSelectedItem(), 0);
            }
         }
      });
      return panel;
    }
    
    
    public static void main(String[] args){
        JPanel inputPanel = new JPanel();
        
        
        
        JTextField urlUnos = new JTextField(50);
        
        Font bigFont = urlUnos.getFont().deriveFont(Font.PLAIN, 15f);
        urlUnos.setFont(bigFont);
        
        
        String[] tipovi = { "GET", "POST", "DELETE", "PUT"};
        
        JComboBox izborTipa = new JComboBox(tipovi);
        izborTipa.setSelectedIndex(0);
        izborTipa.addActionListener((event)->{
            izvrsilac=izvrsioci[izborTipa.getSelectedIndex()];
        });
        
        JTextArea prikaz = new JTextArea(10, 10);
        
        JButton izvrsi = new JButton("Izvrsi");
        izvrsi.addActionListener(event->{
            try {
                String unos=urlUnos.getText();
                Client client = ClientBuilder.newClient();
                String odgovor = izvrsilac.izvrsi(client.target(unos).request());
                prikaz.setText(odgovor);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        inputPanel.add(izborTipa);
        inputPanel.add(urlUnos);
        inputPanel.add(izvrsi);
        
        
        prikaz.setEditable(false);
        JScrollPane scroll = new JScrollPane(prikaz);
        scroll.setSize(700, 500);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel,BorderLayout.NORTH);
        JPanel centerPanel=new JPanel(new GridLayout(0,2));
        centerPanel.add(comboBox());
        centerPanel.add(scroll);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        //mainPanel.add(comboBox(),BorderLayout.WEST);
        
        
        JFrame frame=new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(1000,1000);
      
       frame.getContentPane().add(mainPanel); 
       frame.pack();
       frame.setVisible(true);
       
       /*Client client = ClientBuilder.newClient();
           List<Mesto> mesta = client.target("http://localhost:8080/Server/resources/podsistem1/mesto/svaMesta")
        .request(MediaType.APPLICATION_XML).get(new GenericType<List<Mesto>>(){});
        mesta.forEach(m->{
        System.out.println(m.toString());
        });
        Invocation.Builder request = client.target("http://localhost:8080/Server/resources/podsistem1/mesto/svaMesta")
                .request();*/

    }
    
    /*
            Client client = ClientBuilder.newClient();
           List<Mesto> mesta = client.target("http://localhost:8080/Server/resources/podsistem1/mesto/svaMesta")
        .request(MediaType.APPLICATION_XML).get(new GenericType<List<Mesto>>(){});
        mesta.forEach(m->{
        System.out.println(m.toString());
        });
        /*String ans = client.target("http://localhost:8080/Server/resources/podsistem1/mesto/svaMesta")
        .request()
        .get(String.class);
        System.out.println("Odgovor "+ans.toString());*/
        /*String ans = client.target("http://localhost:8080/Server/resources/podsistem1/mesto/svaMesta")
                .request(MediaType.APPLICATION_XML)
                .get(String.class);
        
        /* try {
        
        
        JAXBContext jaxbContext = JAXBContext.newInstance(Mesto.class);
        
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        List<Mesto> m= (List<Mesto>) jaxbUnmarshaller.unmarshal(ans);
        
        System.out.println(que.getId()+" "+que.getQuestionname());
        System.out.println("Answers:");
        List<Answer> list=que.getAnswers();
        for(Answer ans:list)
        System.out.println(ans.getId()+" "+ans.getAnswername()+"  "+ans.getPostedby());
        
        } catch (JAXBException e) {
        e.printStackTrace();
        }
        //System.out.println("Odgovor "+ans.toString());
        //get.readEntity(new GenericType<ArrayList<Mesto>>(){});
        /*mesta.forEach(m->{
        System.out.println(m.toString());
        });*/
    
    
    
    
}

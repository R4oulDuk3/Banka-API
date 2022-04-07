/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

/**
 *
 * @author Gavrilo
 */
public class Flush {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    
    
    @Resource(lookup = "QueueServer")
    private static Queue queueServer;
    
    @Resource(lookup = "QueuePodsistem1")
    private static Queue queuePodsistem1;
    
    @Resource(lookup = "QueuePodsistem2")
    private static Queue queuePodsistem2;
    
    @Resource(lookup = "QueuePodsistem3")
    private static Queue queuePodsistem3;
        public static void main(String[] args) throws JMSException {
            JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer1 = context.createConsumer(queuePodsistem1);
        JMSConsumer consumer2 = context.createConsumer(queuePodsistem2);
        JMSConsumer consumer3 = context.createConsumer(queuePodsistem3);
        JMSConsumer consumer4 = context.createConsumer(queueServer);
        Message msg = null;
        while((msg=consumer1.receiveNoWait())!=null)System.out.println("1 check ID:" + msg.getStringProperty("idOperacije")+" "+msg.getStringProperty("izvor"));
        while((msg=consumer2.receiveNoWait())!=null)System.out.println("2 check ID:" + msg.getStringProperty("idOperacije")+" "+msg.getStringProperty("izvor"));
        while((msg=consumer3.receiveNoWait())!=null)System.out.println("3 check ID:" + msg.getStringProperty("idOperacije")+" "+msg.getStringProperty("izvor"));
        while((msg=consumer4.receiveNoWait())!=null)System.out.println("4 check ID:" + msg.getStringProperty("idOperacije")+" "+msg.getStringProperty("izvor"));
        }
}

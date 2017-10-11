package liqueurplant.osgi.silo.controller.state.machine;

import liqueurplant.osgi.silo.controller.api.SMReception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;


public class MessageQueue<T extends SMReception> extends LinkedBlockingDeque<T> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<MessageQueue> childList;

    public MessageQueue(){
        childList = new ArrayList<>(10);
    }

    @Override
    public boolean add(T e) {
        forwardToChilds(e);
        return super.add(e);
    }


    public T getNext() {
        // TODO Auto-generated method stub
        T ev=null;
        try {
            ev = this.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ev;
    }

    public T getNext(Collection<T> ignoreSet){
        Stack<T> ignored = new Stack<>();
        T ev = null;
        //Get events until one is not in the ignoreSet
        while (ev == null){
            ev = getNext();
            if (ignoreSet.contains(ev)){
                ignored.push(ev);
                ev = null;
            }
        }
        //Add ignored events to the queue again
        while (!ignored.isEmpty()){
            this.addFirst(ignored.pop());
        }
        return ev;
    }

    public void addChildQueue(MessageQueue queue){
        childList.add(queue);
    }

    public void removeChildQueue(MessageQueue queue){
        childList.remove(queue);
    }

    private void forwardToChilds(T message){
        for (MessageQueue child : childList) {
            child.add(message);
        }
    }

}
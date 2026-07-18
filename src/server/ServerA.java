package server;

import service.*;
import queue.*;
import java.util.*;

public class ServerA extends Thread
{
    public static PriorityQueue<Transaction> Apq=new PriorityQueue<>();
    Transaction t=new Transaction();
    public static int i=0;
    boolean flag;

    public ServerA() {

    }

    public ServerA(Transaction t) {
        this.t = t;
    }

    public static void serA() {
        try {
            synchronized (Transaction.qu) {

                if (Apq.isEmpty()) {

                    ServerA[] sb = new ServerA[100];

                    for (int i = 0; i < 100 && !Transaction.qu.isEmpty(); i++) {

                        Map.Entry<String, Transaction> first =
                                Transaction.qu.entrySet().iterator().next();

                        sb[i] = new ServerA(first.getValue());

                        Apq.add(first.getValue());

                        Transaction.qu.remove(first.getKey());

                        PendingTransaction.pending(Transaction.qu);


                        sb[i].start();
                    }
                }
                else if (!Transaction.qu.isEmpty()) {

                    Map.Entry<String, Transaction> first =
                            Transaction.qu.entrySet().iterator().next();

                    ServerA sb = new ServerA(first.getValue());

                    Apq.add(first.getValue());

                    Transaction.qu.remove(first.getKey());

                    PendingTransaction.pending(Transaction.qu);

                    sb.start();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void transfer(Transaction t) throws Throwable
    {
        try
        {

                if (!t.getStatus().equalsIgnoreCase("success")) {
                    switch (t.getTx_type().toUpperCase()) {
                        case "INTRA_BANK" -> new Transaction_type(t).intra_bank();
                        case "DEPOSIT" -> new Transaction_type(t).deposit();
                        case "WITHDRAWAL" -> new Transaction_type(t).withdrawal();
                        case "INTER_BANK" -> new Transaction_type(t).inter_bank();
                    }
                }
            //}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {

        while (true) {

            Transaction tx;

            synchronized (Apq) {

                while (Apq.isEmpty()) {
                    try {
                        Apq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                tx = Apq.poll();
            }

            if (tx != null) {
                try {
                    transfer(tx);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

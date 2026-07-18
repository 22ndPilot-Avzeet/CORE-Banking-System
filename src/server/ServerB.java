package server;

import service.*;
import queue.*;
import java.util.*;

public class ServerB extends Thread
{
    public static PriorityQueue<Transaction> Bpq=new PriorityQueue<>();
    Transaction t=new Transaction();
    public static int i=0;
    boolean flag;

    public ServerB() {

    }

    public ServerB(Transaction t) {
        this.t = t;
    }

    public static void serB() {
        try {

            synchronized (Transaction.qu) {

                if (Bpq.isEmpty()) {

                    ServerB[] sb = new ServerB[100];

                    for (int i = 0; i < 100 && !Transaction.qu.isEmpty(); i++) {

                        Map.Entry<String, Transaction> first =
                                Transaction.qu.entrySet().iterator().next();

                        sb[i] = new ServerB(first.getValue());

                        Bpq.add(first.getValue());

                        Transaction.qu.remove(first.getKey());

                        PendingTransaction.pending(Transaction.qu);

                        sb[i].start();
                    }
                }
                else if (!Transaction.qu.isEmpty()) {

                    Map.Entry<String, Transaction> first =
                            Transaction.qu.entrySet().iterator().next();

                    ServerB sb = new ServerB(first.getValue());

                    Bpq.add(first.getValue());

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

            synchronized (Bpq) {

                while (Bpq.isEmpty()) {
                    try {
                        Bpq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                tx = Bpq.poll();
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

import cuckoo.Cuckoo;
import cuckoo.NeedExpansionException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by goutham on 21/11/16.
 */
public class CuckooTest {
    @Test
    public void insertSerial() {
        HashMap<Integer, Integer> tm = new HashMap<Integer, Integer>();
        Cuckoo c = new Cuckoo(1024 * 64, 16);
        Random rand = new Random();

        for (int i = 0; i < 15000; i++) {
            KVStruct kv = new KVStruct(rand.nextInt(), rand.nextInt());
            try {
                c.put(new Integer(kv.key), new Integer(kv.value));
            } catch (NeedExpansionException e) {
                continue;
            }

            tm.put(new Integer(kv.key), new Integer(kv.value));
        }

        System.out.println(tm.size());

        for (HashMap.Entry<Integer, Integer> e: tm.entrySet()) {
            assertEquals(e.getValue().intValue(), c.get(e.getKey().intValue()));
        }
    }

    @Test
    public void deleteSerial() {
        HashMap<Integer, Integer> tm = new HashMap<Integer, Integer>();
        Cuckoo c = new Cuckoo(1024 * 64, 16);
        Random rand = new Random();

        for (int i = 0; i < 15000; i++) {
            KVStruct kv = new KVStruct(rand.nextInt(), rand.nextInt());
            try {
                c.put(new Integer(kv.key), new Integer(kv.value));
            } catch (NeedExpansionException e) {
                continue;
            }

            tm.put(new Integer(kv.key), new Integer(kv.value));
        }

        Iterator<HashMap.Entry<Integer, Integer>> iter = tm.entrySet().iterator();
        while (iter.hasNext()) {
            HashMap.Entry<Integer, Integer> e = iter.next();
            if (rand.nextFloat() > 0.5) {
                iter.remove();
                c.delete(e.getKey());
                assertEquals(null, c.get(e.getKey()));
            }
        }

        for (HashMap.Entry<Integer, Integer> e: tm.entrySet()) {
            assertEquals(e.getValue().intValue(), c.get(e.getKey().intValue()));
        }
    }
}

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class HshSetTester {

    @Test
    public void testConstructorsAndInitialization() {
        HshSet<Object> hs= new HshSet<Object>();
        assertEquals(0, hs.size());
        assertEquals(0, hs.load());
        assertEquals(hs.initialCapacity(), hs.capacity());
        assertEquals(0, hs.getRehashes());
    }
    
    @Test
    public void testSize1and2Set() {
        HshSet<Object> hs= new HshSet<Object>();
        Object ss= new Object();
        hs.add(ss); 
        assertEquals(1, hs.size());
        assertEquals(1, hs.load());
        assertEquals(hs.initialCapacity(), hs.capacity());
        assertEquals(0, hs.getRehashes());
        assertEquals(true, hs.contains(ss));
        assertEquals(false, hs.contains(new Object()));
        
        hs.add(ss);
        assertEquals(1, hs.size());
        assertEquals(hs.initialCapacity(), hs.capacity());
        assertEquals(0, hs.getRehashes());
        assertEquals(true, hs.contains(ss));
        assertEquals(false, hs.contains(new Object()));
    }
    
    @Test
    public void testPtRemove() {
        @SuppressWarnings("unused")
        HshSet<Pt> s= new HshSet();
        for (int k= 0; k < 6; k= k+1) {
            s.add(new Pt(3, k));
            }
        int size= s.size();
        int load= s.load();
        for (int k= 5; k >= 0; k= k-1) {
            assertEquals(size, s.size());
            assertEquals(true, s.contains(new Pt(3, k)));
            s.remove(new Pt(3, k));
            size= size-1;
            assertEquals(size, s.size());
            assertEquals(load, s.load());
            assertEquals(false, s.contains(new Pt(3, k)));
            };
        assertEquals(0, s.size());
    }
    
    @Test
    public void testPtAdd() {
        @SuppressWarnings("unused")
        HshSet<Pt> s= new HshSet();
        int initCapacity= s.capacity();
        s.add(new Pt(3, 5));
        s.add(new Pt(3, 5));
        s.add(new Pt(3, 6));
        assertEquals(true, s.contains(new Pt(3, 5)));
        assertEquals(false, s.contains(new Pt(5, 5)));
        assertEquals(true, s.contains(new Pt(3, 6)));
        assertEquals(2, s.size());
        assertEquals(initCapacity, s.capacity());
        assertEquals(0, s.getRehashes());
        
        for (int k= 0; k < 100; k= k+1) {
            s.add(new Pt(3, k));
            s.add(new Pt(3, k));  // This should not add an elements
        }
        assertEquals(100, s.size());
        assertEquals(100, s.load());
    }
    
    @Test
    public void testPtIterator() {
        @SuppressWarnings("unused")
        HshSet<Pt> s= new HshSet();
        int initCapacity= s.capacity();
        s.add(new Pt(3, 5));
        
        for (Pt p : s) {
            assertEquals(true, p.equals(new Pt(3, 5)));
        }
        s.add(new Pt(3, 5));
        s.add(new Pt(3, 6));
        
    }
    
    
    
    @Test
    public void testAddCapacity() {
        @SuppressWarnings("unused")
        HshSet<Pt> s= new HshSet();
        int initCapacity= s.capacity();
        int k= 0;
        while (s.load() <= .75 * initCapacity) {
            k= k+1;
            s.add(new Pt(k, k));
        }
        assertEquals(3*k, s.capacity());
        
        while (s.load() <= .75 * initCapacity) {
            k= k+1;
            s.add(new Pt(k, k+1));
        }
        assertEquals(3*k, s.capacity());
    }
   
}

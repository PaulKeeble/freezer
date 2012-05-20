package freezer.collection
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OrderedConcurrentSetSpec extends FunSpec with ShouldMatchers with BeforeAndAfter{
  val set = new OrderedConcurrentSet[Object]
  
  before {
    set.clear
  }
  
  describe("An OrderedConcurrentSet") {
    it("should index an object") {
      val obj = new Object
      set+=obj
      
      set.indexOf(obj) should equal(Some(0))
    }
    
    it("should not add the same object again") {
      val obj = new Object
      set+=obj
      set+=obj
      
      set.indexOf(obj) should equal(Some(0))
      set.size should equal (1)
    }

    it("should index multiple objects") {
      val o1 = new Object
      val o2 = new Object
      set+=o1
      set+=o2
      
      set.indexOf(o1) should equal(Some(0))
      set.indexOf(o2) should equal(Some(1))
    }
    
    it("#indexOf should return None when no object found") {
      val notFound = new Object
      set.indexOf(notFound) should equal (None)
    }
    
    it("should iterate through in order") {
      val o1 = new Object
      val o2 = new Object
      set+=o1
      set+=o2
      
      set.toArray should equal (Array(o1,o2))
    }
    
    it("should support multiple threads on addition") {
      val objs = Range(0,1000).par.foreach { i =>
        set += new Object
      }
      set.size should equal (1000)
    }
    
    it("should not find an object not in the index") {
      set.atIndex(0) should equal (None)
    }
    
    it("should convert indexes to objects") {
      val o1 = new Object
      set+=o1
      
      set.atIndex(0) should equal (Some(o1))
    }
    
    it("should find multiple indexes") {
      val o1 = new Object
      val o2 = new Object
      set+=o1
      set+=o2
      
      set.atIndex(0) should equal (Some(o1))
      set.atIndex(1) should equal (Some(o2))
      set.atIndex(2) should equal (None)
    }
    
    it("should not support negative indexes") {
      set.atIndex(-1) should equal (None)
    }
    
    it("should be equal when empty") {
      val i1 = new OrderedConcurrentSet[Object]
      val i2 = new OrderedConcurrentSet[Object]
      
      i1 should equal(i2)
    }
    
    it("should not be equal when one index contains a different entry") {
      val i1 = new OrderedConcurrentSet[Object]
      i1 += new Object
      val i2 = new OrderedConcurrentSet[Object]
      
      i1 should not equal(i2)
    }
    
    it("should be equal when containing multiple entries in the same order") {
      val o1 = new Object
      val o2 = new Object
      val i1 = new OrderedConcurrentSet[Object]
      val i2 = new OrderedConcurrentSet[Object]
      i1+=o1
      i1+=o2
      i2+=o1
      i2+=o2
      
      i1 should equal(i2)
    }
    
    it("should not be equal when the objects are in a different order") {
      val o1 = new Object
      val o2 = new Object
      val i1 = new OrderedConcurrentSet[Object]
      val i2 = new OrderedConcurrentSet[Object]
      i1+=o1
      i1+=o2
      i2+=o2
      i2+=o1
      
      i1 should not equal(i2)
    }
    
    it("should have consistent not equal hashcode") {
      val o1 = new Object
      val o2 = new Object
      val i1 = new OrderedConcurrentSet[Object]
      val i2 = new OrderedConcurrentSet[Object]
      
      i1+=o1
      i1+=o2
      i2+=o2
      i2+=o1
      
      i1.hashCode() should not equal (i2.hashCode())
    }
    
    it("should have consistent equal hashcode") {
      val o1 = new Object
      val i1 = new OrderedConcurrentSet[Object]
      val i2 = new OrderedConcurrentSet[Object]
      i1+=o1
      i2+=o1
      
      i1.hashCode() should equal (i2.hashCode())
    }
    
    /**
     * A test to ensure the cache clears and updates again via repeated updates
     */
    it("should find objects in the index before and after updates") {
      val o1 = new Object
      set += o1
      
      set.atIndex(0) should equal(Some(o1))
      set.indexOf(o1) should equal(Some(0))
      
      val o2 = new Object
      set += o2
      
      set.atIndex(1) should equal(Some(o2))
      set.indexOf(o2) should equal(Some(1))
    }
  }
}
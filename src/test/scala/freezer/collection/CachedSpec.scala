package freezer.collection
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CachedSpec extends FunSpec with ShouldMatchers {
  describe("A Cached") {
    it("should return value of function on first call") {
      val c = new Cached[Int](5)
      c() should equal(5)
    }
    
    it("should only call the passed function once for subsequent calls") {
      var count = 0
      val c = new Cached[Int]( count )
      c() should equal(0)
      count+=1
      c() should equal(0)
      count should equal(1)
    }
    
    it("should not evaluate the cached function until called") {
      var count = 0
      val c = new Cached[Int]( count )
      count+=1
      c() should equal(1)
    }
    
    it("should reset the cached value so that future calls see updates") {
      var count = 0
      val c = new Cached[Int]( count )
      val first = c()
      count+=1
      
      c.reset()
      c() should equal(1)
    }
    
    it("should be consistent under multithread usage") {
      var count = 0
      val c = new Cached[Int]( count ) 
      val results = (1 to 1000 par).map { i =>
        count+=1
        c.reset()
        c()
      }
      
      results.length should equal(1000)
    }
  }

}
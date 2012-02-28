package freezer.serialisers
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec


@RunWith(classOf[JUnitRunner])
class ShortSerialiserSpec extends FunSpec with ShouldMatchers with RoundTrip[Short]{
  val serialiser = new ShortSerialiser
  
  describe("A short serialiser") {
    it("should store a 0") {
      roundTrip(0.shortValue)
    }
    
    it("should store a 1") {
      roundTrip(1.shortValue)
    }
    
    it("should store a -1") {
      roundTrip((-1).shortValue)
    }
    
    it("should store max short") {
      roundTrip(Short.MaxValue)
    }
    
    it("should store min short") {
      roundTrip(Short.MinValue)
    }
  }
}
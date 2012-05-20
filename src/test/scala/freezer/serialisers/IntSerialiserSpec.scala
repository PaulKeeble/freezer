package freezer.serialisers
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class IntSerialiserSpec extends FunSpec with ShouldMatchers with RoundTrip[Int]{
  val serialiser = new IntSerialiser
  
  describe("An IntSerialiser") {
    
    it("should save 0") {
      roundTrip(0)
    }
    
    it("should save 1") {
      roundTrip(1)
    }
    
    it("should save -1") {
      roundTrip(-1)
    }
    
    it("should save Integer max value") {
      roundTrip(Int.MaxValue)
    }
    
    it("should save Integer min value") {
      roundTrip(Int.MinValue)		
    }
    
    it("should save 01010101 hex") {
      roundTrip(0x01010101)
    }
    
    it("should save 80808080 hex") {
      roundTrip(0x80808080)
    }
  }
}
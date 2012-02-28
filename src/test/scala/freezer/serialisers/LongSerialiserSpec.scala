package freezer.serialisers
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LongSerialiserSpec extends FunSpec with RoundTrip[Long] with ShouldMatchers {
  val serialiser = new LongSerialiser
  
  describe("A LongSerialiser") {
    it("should save 0") {
      roundTrip(0)
    }
    
    it("should save 1") {
      roundTrip(1)
    }
    
    it("should save -1") {
      roundTrip(-1)
    }
    
    it("should save max positive long") {
      roundTrip(Long.MaxValue)
    }
    
    it("should save max negative long") {
      roundTrip(Long.MinValue)
    }
  }
}
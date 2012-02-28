package freezer.serialisers
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ByteSerialiserSpec extends FunSpec with ShouldMatchers with RoundTrip[Byte] {
  val serialiser = new ByteSerialiser
  
  describe("A ByteSerialiser") {
    it("should store 0") {
      roundTrip(0.byteValue)
    }
    
    it("should store 1") {
      roundTrip(1.byteValue)
    }
    
    it("should store -1 byte value") {
      roundTrip(-1.byteValue)
    }
    
    it("should store min byte value") {
      roundTrip(Byte.MinValue)
    }
    
    it("should store max byte value") {
      roundTrip(Byte.MaxValue)
    }
  }
}
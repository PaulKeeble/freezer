package freezer.serialisers
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DefaultInlineSerialisersSpec extends FunSpec with ShouldMatchers {
  val serialisers = DefaultInlineSerialisers.serialisers
  val deserialisers = DefaultInlineSerialisers.deserialisers
  
  describe("The Default Inline Serialisers") {
    describe("when serialising") {
      it("should be definied for byte") {
        serialisers.isDefinedAt(1.byteValue) should be(true)
        serialisers(1.byteValue) should have length (1)
      }
      
      it("should be defined for int") {
        serialisers.isDefinedAt(1) should be(true)
        serialisers(1) should have length (4)
      }

      it("should be defined for long") {
        serialisers.isDefinedAt(Long.MaxValue) should be(true)
        serialisers(Long.MaxValue) should have length (8)
      }
    }
    
    describe("when deserialising") {
      it("should read last int") {
        val stored = Array[Byte](0,0,0,1)
        val serialiserType = classOf[Int].getName()
        
        deserialisers.isDefinedAt(serialiserType,stored) should be(true)
        
        val loaded = deserialisers(serialiserType,stored)
        loaded.result should equal (1)
        loaded.remaining should equal (Array())
      }
    }
  }
}
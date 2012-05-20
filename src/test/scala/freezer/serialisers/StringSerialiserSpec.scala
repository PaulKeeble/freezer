package freezer.serialisers
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class StringSerialiserSpec  extends FunSpec with ShouldMatchers with RoundTrip[String]{
  val serialiser = new StringSerialiser()
  describe("A NullTerminatedStringSerialiser") {
    it("should produce only 1 byte for empty string") {
      
     roundTrip("")
    }
    
    it("should reproduce a string as input") {
      val str = "aString"
      roundTrip(str)
    }
    
    it("should accept nulls within the string") {
      val str = "ab\u0000cd"
      roundTrip(str) 
    }
    
    it("should not consume the following field value") {
       val str = "ab"
       val stored = serialiser.store(str)
      
       val loaded = serialiser.load( stored ++ Array[Byte](0) )
       loaded.result should equal (str)
       loaded.remaining ===Array(0)
    }
  }
}
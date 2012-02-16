package freezer.serialisers
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class IntSerialiserSpec extends FunSpec with ShouldMatchers{
  val serialiser = new IntSerialiser
  
  describe("An IntSerialiser") {
    
    it("should save 0") {
      roundtrip(0)
    }
    
    it("should save 1") {
      roundtrip(1)
    }
    
    it("should save -1") {
      roundtrip(-1)
    }
    
    it("should save Integer max value") {
      roundtrip(Int.MaxValue)
    }
    
    it("should save Intger min value") {
      roundtrip(Int.MinValue)		
    }
  }
  
  def roundtrip(i : Int) {
    val stored = serialiser.store(i)
    val loaded = serialiser.load(stored)
    
    loaded.result should equal (i)
    loaded.remaining should equal (Array())
  }
  
  //type information read and written by something else?
}
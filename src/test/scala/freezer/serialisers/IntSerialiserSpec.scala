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
    
    it("should save Integer min value") {
      roundtrip(Int.MinValue)		
    }
    
    it("should save 01010101 hex") {
      roundtrip(0x01010101)
    }
    
    it("should save 80808080 hex") {
      val pow2 = pow(2,_:Int)
      
      roundtrip(0x80808080)
    }
  }
  
  def pow(n:Int,e:Int) : Int = {
    if(e==0) 1 
    else if (e==1) n 
    else n*pow(n,e-1)
  }
  
  def roundtrip(i : Int) {
    val stored = serialiser.store(i)
    val loaded = serialiser.load(stored)
    
    loaded.result should equal (i)
    loaded.remaining should equal (Array())
  }
  
  //type information read and written by something else?
}
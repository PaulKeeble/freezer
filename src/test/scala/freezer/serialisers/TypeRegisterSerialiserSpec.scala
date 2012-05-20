package freezer.serialisers
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import freezer.obj.TypeRegister
import org.scalatest.BeforeAndAfter

@RunWith(classOf[JUnitRunner])
class TypeRegisterSerialiserSpec extends FunSpec with ShouldMatchers with BeforeAndAfter with RoundTrip[TypeRegister]{
  
  var register :TypeRegister = _
  val serialiser = new TypeRegisterSerialiser
  
  before {
    register = new TypeRegister
  }
  
  describe("A TypeRegister Serialiser") {
    
    it("should store and load empty registry") {
      roundTrip(register)
    }
    
    it("should store and load a single entry") {
      register += classOf[Object]
      
      roundTrip(register)
    }
    
    it("should store and load multiple entries") {
      register += classOf[Object]
      register += classOf[String]
      
      roundTrip(register)
    }
    
    it("should only read the bytes of the register") {
      register += classOf[Object]
      
      val stored = serialiser.store(register)
      val loaded = serialiser.load(stored ++ Array[Byte](1,2,3))

      loaded.result should equal(register)
      loaded.remaining ===Array(1,2,3)
    }
    
  }
//  
//  def roundTrip(r : TypeRegister) {
//    val stored = serialiser.store(register)
//    val loaded = serialiser.load(stored)
//    
//    loaded.result should equal (register)
//    loaded.remaining should equal (Array())
//  }
}
package freezer.serialisers
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import freezer.obj.ObjectIndex
import org.scalatest.OneInstancePerTest
import freezer.obj.SystemReference

@RunWith(classOf[JUnitRunner])
class ObjectReferenceSerialiserSpec extends FunSpec with ShouldMatchers with OneInstancePerTest with RoundTrip[AnyRef] {
  val index = new ObjectIndex()
  val serialiser = new ObjectReferenceSerialiser(index)
  
  val obj : AnyRef = new Object
  
  describe("An Object Reference Serialiser") {
    it("should inline and restore null") {
      roundTrip(null)
    }
    
    it("should inline and restore an existing object") {
      indexContaining(obj)
      
      roundTrip(obj)
    }
    
    it("should error if the object isn't indexed on store") {
      intercept[ObjectLookupException] { serialiser.store(obj) }
    }
    
    it("should error if the object isn't indexed on load") {
      indexContaining(obj)
      
      val stored = serialiser.store(obj)
      index.clear()
      
       intercept[ObjectLookupException] { serialiser.load(stored) }
    }
  }
  
  def indexContaining(obj : AnyRef) {
    index+=obj
  }
}
package freezer.serialisers

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.OneInstancePerTest
import freezer.obj.ObjectIndex

@RunWith(classOf[JUnitRunner])
class ObjectsSpec extends FunSpec with ShouldMatchers with OneInstancePerTest with NewRoundTrip {
  val index = new ObjectIndex()
  val obj: AnyRef = new Object

  implicit val serialiser = Objects.referenceSerialiser(index)
  implicit val deserialiser = Objects.referenceDeserialiser(index)
  
  describe("An Objects Serialiser") {
    it("should serialise null") {
      roundTrip[AnyRef](null)
    }
    
    it("should inline and restore an existing object") {
      indexContaining(obj)

      roundTrip(obj)
    }

    it("should error if the object isn't indexed on store") {
      intercept[ObjectLookupException] { serialiser(obj) }
    }

    it("should error if the object isn't indexed on load") {
      indexContaining(obj)

      val stored = serialiser(obj)
      index.clear()

      intercept[ObjectLookupException] { deserialiser(stored) }
    }
    
    it("should select") {
      indexContaining(obj)

      val result= Objects.selector(deserialiser)
      result("anyString") should equal(deserialiser)
    }
  }

  def indexContaining(obj: AnyRef) {
    index += obj
  }
}
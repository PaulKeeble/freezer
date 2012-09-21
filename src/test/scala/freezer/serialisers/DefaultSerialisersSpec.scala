package freezer.serialisers

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import freezer.collection.ArrayView
import freezer.obj.ObjectIndex

@RunWith(classOf[JUnitRunner])
class DefaultSerialisersSpec extends FunSpec with ShouldMatchers with NewRoundTrip {
  describe("A Default Serialisers") {
    it("should select primitives") {
      implicit val s = DefaultSerialisers.serialiseAnyVal
      implicit val dSelector = DefaultSerialisers.deserialiseAnyVal

      val examples =  
        ("byte", 1.toByte) :: ("short", 2.toShort) ::
        ("int", 1) :: ("long", 1L) ::
        ("float", 1.1f) :: ("double", 1.1d) ::
        ("char", 'a') :: ("boolean", true) ::
        ("unit",()) :: Nil

      examples.foreach { (t) =>
        roundTrip(s, dSelector(t._1), t._2)
      }
    }
    
    it("should select objects") {
      val index = new ObjectIndex
      val obj = new Object
      index+= obj
      
      implicit val s = DefaultSerialisers.composeSerialiser(Objects.referenceSerialiser(index))
      implicit val dSelector = DefaultSerialisers.composeDeserialiser(Objects.selector(Objects.referenceDeserialiser(index)))
		
      roundTrip(s,dSelector("java.lang.Object"),obj)
    }
  }
}
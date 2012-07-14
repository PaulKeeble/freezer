package freezer.serialisers

import scala.annotation.implicitNotFound
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.Assertions

@RunWith(classOf[JUnitRunner])
class PrimitivesTest extends FunSpec with ShouldMatchers with NewRoundTrip with Assertions {
  describe("A Primitives Serialiser") {
    it("should serialise bytes") {
      implicit val s = Primitives.serialiseByte
      implicit val d = Primitives.deserialiseByte
      roundTripAll(List(-1, 0, 1, Byte.MaxValue, Byte.MinValue,0x01,0x80), { i: Int => i.toByte })
    }
    
    it("should serialise ints") {
      implicit val s = Primitives.serialiseInt
      implicit val d = Primitives.deserialiseInt
      roundTripAll(List(-1, 0, 1, Int.MaxValue, Int.MinValue,0x010101,0x8080))
    }
    
    it("should serialise shorts") {
      implicit val s = Primitives.serialiseShort
      implicit val d = Primitives.deserialiseShort
      roundTripAll(List(-1, 0, 1, Short.MaxValue, Short.MinValue,0x0101,0x8080),{ i: Int => i.toShort})
    }
    
    it("should serialise longs") {
      implicit val s = Primitives.serialiseLong
      implicit val d = Primitives.deserialiseLong
      roundTripAll(List(-1, 0, 1, Long.MaxValue, Long.MinValue,0x0101010101L,0x80808080L))
    }
    
    it("should serialise floats") {
      implicit val s = Primitives.serialiseFloat
      implicit val d = Primitives.deserialiseFloat
      roundTripAll(List(-1, 0, 1, Float.MaxValue, Float.MinValue,-0.1f,0.1f))
    }

    it("should serialise doubles") {
      implicit val s = Primitives.serialiseDouble
      implicit val d = Primitives.deserialiseDouble
      roundTripAll(List(-1, 0, 1, Double.MaxValue, Double.MinValue,-0.1d,0.1d,
          Double.NegativeInfinity,Double.MinPositiveValue,Double.PositiveInfinity,Double.NegativeInfinity))
      val nan = d(s(Double.NaN))
      assert(java.lang.Double.isNaN(nan._1))
    }
    
    it("should serialise boolean") {
      implicit val s = Primitives.serialiseBoolean
      implicit val d = Primitives.deserialiseBoolean
      roundTripAll(List(true,false))
    }
    
    it("should serialise char") {
      implicit val s = Primitives.serialiseChar
      implicit val d = Primitives.deserialiseChar
      roundTripAll(List(0,'a','A','\u8000','\uFFFF'),{i:Int => i.toChar})
    }
    
    it("should serialise unit") {
      implicit val s = Primitives.serialiseUnit
      implicit val d = Primitives.deserialiseUnit
      roundTrip(())
    }
  }
}
package freezer.serialisers
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PrimitivesTest extends FunSpec with ShouldMatchers with NewRoundTrip {
  describe("A Primitives Serialiser") {
    describe("with a byte") {
      implicit val s = Primitives.serialiseByte
      implicit val d = Primitives.deserialiseByte

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
      
      ignore("should serialise anyVal byte") {
//        roundTrip(Primitives.serialise,Primitives.deserialise,1.toByte)
      }

    }

    describe("An IntSerialiser") {
      implicit val s = Primitives.serialiseInt
      implicit val d = Primitives.deserialiseInt

      it("should save 0") {
        roundTrip(0)
      }

      it("should save 1") {
        roundTrip(1)
      }

      it("should save -1") {
        roundTrip(-1)
      }

      it("should save Integer max value") {
        roundTrip(Int.MaxValue)
      }

      it("should save Integer min value") {
        roundTrip(Int.MinValue)
      }

      it("should save 01010101 hex") {
        roundTrip(0x01010101)
      }

      it("should save 80808080 hex") {
        roundTrip(0x80808080)
      }
    }

    ignore("should compose serialisation to that bytes and ints are done from the same method") {
      val result = Primitives.serialise(1.toByte)
      result(0) should equal(1.toByte)

      val intResult = Primitives.serialise(1)
      result(0) should equal(1)
    }
  }
}
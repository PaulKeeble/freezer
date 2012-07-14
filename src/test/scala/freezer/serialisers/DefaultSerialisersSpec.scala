package freezer.serialisers

import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import freezer.collection.ArrayView

@RunWith(classOf[JUnitRunner])
class DefaultSerialisersSpec extends FunSpec with ShouldMatchers with NewRoundTrip {
  describe("A Default Serialisers") {
    it("should select primitives") {
      import DefaultSerialisers._
      val value = 1
      val serialised = serialiseAnyVal(value)
      val (result, remaining) = deserialiseAnyVal("int")(serialised)
      result should equal(value)
      remaining.toArray should equal(Array())
    }
  }
}
package freezer.serialisers
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import freezer.obj.ObjectIndex
import freezer.obj.TypeRegister
import org.scalatest.BeforeAndAfter

@RunWith(classOf[JUnitRunner])
class ObjectIndexSerialiserSpec extends FunSpec with ShouldMatchers with BeforeAndAfter with RoundTrip[ObjectIndex] {
  val types = new TypeRegister

  val serialiser = new ObjectIndexSerialiser(types)

  val index = new ObjectIndex

  before {
    types.clear()
    index.clear()
  }

  describe("An ObjectIndexSerialiser") {
    it("should serialise an empty index") {
      roundTrip(index)
    }

    it("should serialise a single entry index") {
      val o1 = new Object
      index += o1
      types += o1.getClass()

      roundTrip(index)
    }

    it("should serialise multiple entries") {
      val o1 = new Object
      val o2 = new Object
      index += o1 += o2
      types += o1.getClass += o2.getClass

      roundTrip(index)
    }

    it("should serialise a custom class") {
      val o1 = new HasDefaultConstructor(1)
      index += o1
      types += o1.getClass
      
      roundTrip(index)
    }

    it("should not load an object without a no arg constructor") {
      val o1 = new NoDefaultConstructor(1)
      index += o1
      types += o1.getClass
      val stored = serialiser.store(index)
      intercept[NoDefaultConstructorException] {
        serialiser.load(stored)
      }
    }
  }
  
  override def roundTrip(index:ObjectIndex) {
    val stored = serialiser.store(index)
    val loaded = serialiser.load(stored)
    
    loaded.result.size equals (index.size)
    assert(loaded.result.equivalent(index))
    
    loaded.remaining equals (Array())
  }
}

class HasDefaultConstructor(i:Int) {
  def this() = this(0)
}

class NoDefaultConstructor(i:Int)
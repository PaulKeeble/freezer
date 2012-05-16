package freezer.serialisers
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import freezer.obj.ObjectIndex

@RunWith(classOf[JUnitRunner])
class DefaultInlineSerialisersSpec extends FunSpec with ShouldMatchers {
  val serialisers = DefaultInlineSerialisers.serialisePrimitive
  val deserialisers = DefaultInlineSerialisers.deserialisePrimitive
  
  describe("The Default Inline Primtiive Serialisers") {
    describe("when serialising") {
      it("should be definied for byte") {
        serialisers.isDefinedAt(1.byteValue) should be(true)
        serialisers(1.byteValue) should have length (1)
      }
      
      it("should be defined for int") {
        serialisers.isDefinedAt(1) should be(true)
        serialisers(1) should have length (4)
      }

      it("should be defined for long") {
        serialisers.isDefinedAt(Long.MaxValue) should be(true)
        serialisers(Long.MaxValue) should have length (8)
      }
    }
    
    describe("when deserialising") {
      it("should read last byte") {
        val stored = Array[Byte](1)
        val serialiserType = classOf[Byte].getName()
        
        deserialisers.isDefinedAt(serialiserType,stored) should be(true)
        
        val loaded = deserialisers(serialiserType,stored)
        loaded.result should equal (1.byteValue)
        loaded.remaining should equal (Array())
      }
      
      it("should read last int") {
        val stored = Array[Byte](0,0,0,1)
        val serialiserType = classOf[Int].getName()
        
        deserialisers.isDefinedAt(serialiserType,stored) should be(true)
        
        val loaded = deserialisers(serialiserType,stored)
        loaded.result should equal (1)
        loaded.remaining should equal (Array())
      }
    }
  }
  
  describe("The Inline Object Serialisers") {
    describe("when serialising") {
      it("should convert objects using references in an index") {
        val index = new ObjectIndex
        val o = new Object
        index += o
        
        val serialiseObject : PartialFunction[Any,Array[Byte]] = DefaultInlineSerialisers.serialiseObject(index)
        serialiseObject.isDefinedAt(new Object) should be (true)
        
        serialiseObject(o) should have length (4)
      }
      
      it("should convert null references") {
        val index = new ObjectIndex
        
        val serialiseObject : PartialFunction[Any,Array[Byte]] = DefaultInlineSerialisers.serialiseObject(index)
        serialiseObject.isDefinedAt(null) should be (true)
        
        serialiseObject(null) should have length (4)
      }
    }
    
    describe("when deserialising") {
      it("should read an object") {
        val index = new ObjectIndex
        val o = new Object
        index += o
        
        val stored = Array[Byte](0,0,0,0)
        val serialiserType = classOf[Object].getName()
        
        val deserialiseObject = DefaultInlineSerialisers.deserialiseObject(index)
        
        deserialiseObject.isDefinedAt(serialiserType,stored) should be(true)
        
        val loaded = deserialiseObject(serialiserType,stored)
        loaded.result should equal (o)
        loaded.remaining should equal (Array())
      }
      
      it("should convert null references") {
        val index = new ObjectIndex
        
        val stored = Array[Byte](-1,-1,-1,-1)
        val serialiserType = classOf[Object].getName()
        
        val deserialiseObject = DefaultInlineSerialisers.deserialiseObject(index)
        
        deserialiseObject.isDefinedAt(serialiserType,stored) should be(true)
        
        val loaded = deserialiseObject(serialiserType,stored)
        loaded.result should equal (null)
        loaded.remaining should equal (Array())
      }
    }
  }
}
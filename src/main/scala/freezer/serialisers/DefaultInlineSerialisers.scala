package freezer.serialisers
import freezer.obj.ObjectIndex
import freezer.collection.ArrayView
object DefaultInlineSerialisers {
  
  def serialiseObject(index : ObjectIndex) : PartialFunction[Any,Array[Byte]] = {
    case a : AnyRef => new ObjectReferenceSerialiser(index).store(a)
    case null => new ObjectReferenceSerialiser(index).store(null)
  }
  
  def serialisePrimitive : PartialFunction[Any,Array[Byte]] = {
    case i :Int => new IntSerialiser().store(i)
    case l :Long => new LongSerialiser().store(l)
    case b :Byte => new ByteSerialiser().store(b)
  }
  
  def deserialiseObject(index : ObjectIndex) : PartialFunction[(String,ArrayView[Byte]),LoadResult[Any]] = {
    case (_,a) => new ObjectReferenceSerialiser(index).load(a)
  }
  
  def deserialisePrimitive : PartialFunction[(String,ArrayView[Byte]),LoadResult[Any]] = {
    case ("int",a) => new IntSerialiser().load(a)
    case ("long",a) => new LongSerialiser().load(a)
    case ("byte",a) => new ByteSerialiser().load(a)
  }
}
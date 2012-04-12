package freezer.serialisers
object DefaultInlineSerialisers {
  
  def serialisers : PartialFunction[Any,Array[Byte]] = {
    case i :Int => new IntSerialiser().store(i)
    case l :Long => new LongSerialiser().store(l)
    case b :Byte => new ByteSerialiser().store(b)
  }
  
  def deserialisers : PartialFunction[(String,Array[Byte]),LoadResult[Any]] = {
    case ("int",a) => new IntSerialiser().load(a)
    case ("long",a) => new LongSerialiser().load(a)
    case ("byte",a) => new ByteSerialiser().load(a)
  }
}
package freezer.serialisers

class ByteSerialiser extends Serialiser[Byte]{
  def store(value:Byte) : Array[Byte] = {
    Array[Byte](value)
  }
  
  def load(stored : Array[Byte]) : LoadResult[Byte] = {
    return new LoadResult(stored(0),stored.drop(1))
  }
}
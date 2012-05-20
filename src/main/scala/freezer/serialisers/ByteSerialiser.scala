package freezer.serialisers
import freezer.collection.ArrayView

class ByteSerialiser extends Serialiser[Byte]{
  def store(value:Byte) : Array[Byte] = {
    Array[Byte](value)
  }
  
  def load(stored : ArrayView[Byte]) : LoadResult[Byte] = {
    return new LoadResult(stored(0),stored.drop(1))
  }
}
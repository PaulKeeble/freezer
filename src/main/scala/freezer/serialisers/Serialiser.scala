package freezer.serialisers
import freezer.collection.ArrayView

trait Serialiser[T] {
  def store(v : T) : Array[Byte]
  
  def load(stored : ArrayView[Byte]) : LoadResult[T]
}
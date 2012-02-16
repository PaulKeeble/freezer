package freezer.serialisers

trait Serialiser[T] {
  def store(v : T) : Array[Byte]
  
  def load(stored : Array[Byte]) : LoadResult[T]
}
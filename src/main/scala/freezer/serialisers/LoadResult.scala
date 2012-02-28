package freezer.serialisers

class LoadResult[+T](result : T,remaining : Array[Byte]) extends Tuple2[T,Array[Byte]](result,remaining) {
  def result = _1
  
  def remaining = _2
}
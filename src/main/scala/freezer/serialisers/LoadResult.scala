package freezer.serialisers
import freezer.collection.ArrayView

class LoadResult[+T](result : T,remaining : ArrayView[Byte]) extends Tuple2(result,remaining) {
  def result = _1
  
  def remaining = _2
}
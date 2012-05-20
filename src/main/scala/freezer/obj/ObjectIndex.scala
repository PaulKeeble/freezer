package freezer.obj
import scala.annotation.migration
import scala.collection.mutable.SynchronizedSet
import scala.collection.mutable.LinkedHashSet
import freezer.collection.OrderedConcurrentSet

class ObjectIndex extends OrderedConcurrentSet[SystemReference] {
  def equivalent(that:ObjectIndex) = classes == that.classes
  
  def objs = map { _.obj}
  
  def classes = objs.map {_.getClass}
}
package freezer.obj
import scala.collection.mutable.LinkedHashSet
import scala.collection.mutable.SynchronizedSet

class OrderedConcurrentSet[A] extends LinkedHashSet[A] with SynchronizedSet[A] {
  def indexOf(ref: A): Option[Int] = {
    if (!contains(ref))
      return None

    val index = toList.indexWhere(_==ref)  //Number one bottleneck of depth graph is this toList
    Some(index)
  }
  
  def atIndex(index :Int) : Option[A] = {
    val list = toList
    
    index match {
      case i if i >list.size-1 => None
      case i if i<0 => None
      case i => Some(list(i))
    }
  }

  override def equals(other:Any) :Boolean = other match {
    case that : OrderedConcurrentSet[A] => (that canEqual this) &&
    	sameElements( that)
    case _ => false
  }
  
  override def hashCode():Int = toList.hashCode
  
  override def canEqual(other:Any) :Boolean = other.isInstanceOf[OrderedConcurrentSet[A]] && super.canEqual(other)
}
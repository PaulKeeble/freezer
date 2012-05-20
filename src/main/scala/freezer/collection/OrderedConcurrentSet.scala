package freezer.collection
import scala.collection.mutable.LinkedHashSet
import scala.collection.mutable.ObservableSet
import scala.collection.mutable.Subscriber
import scala.collection.mutable.SynchronizedSet
import scala.collection.mutable.Undoable
import scala.collection.script.Message

class OrderedConcurrentSet[A] extends LinkedHashSet[A] with ObservableSet[A] with SynchronizedSet[A]  {
  val sub = new Sub {
    def notify(pub:Pub,event:Message[A] with Undoable) {
      cache = None
    }
  }
  subscribe(sub)
  
  var cache : Option[IndexedSeq[A]] = None
  
  def cachedToIndexedSeq = {
    cache match {
      case Some(list) => list
      
      case None => {
        val list= toIndexedSeq
        cache = Some(list)
        list
      }
    }
  }
  
  def indexOf(ref: A): Option[Int] = {
    if (!contains(ref))
      return None

    val index = cachedToIndexedSeq.indexWhere(_==ref)  //Number one bottleneck of depth graph is this toList
    Some(index)
  }
  
  def atIndex(index :Int) : Option[A] = {
    val list = cachedToIndexedSeq
    
    index match {
      case i if i >list.size-1 => None
      case i if i<0 => None
      case i => Some(list(i))
    }
  }

  override def equals(other:Any) :Boolean = other match {
    case that : OrderedConcurrentSet[_] => (that canEqual this) &&
    	sameElements( that)
    case _ => false
  }
  
  override def hashCode():Int = cachedToIndexedSeq.hashCode
  
  override def canEqual(other:Any) :Boolean = other.isInstanceOf[OrderedConcurrentSet[_]] && super.canEqual(other)
}
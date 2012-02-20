package freezer.obj
import java.lang.reflect.Field
import scala.collection.mutable

class ObjectGraph(val root:AnyRef) {
  def getAll = {
    var q = new mutable.Queue[AnyRef]
	var foundObjects = mutable.LinkedHashSet[AnyRef]()

	q.enqueue(root)
    
    while(!q.isEmpty) {
      val current = q.dequeue()
      if(!foundObjects.contains(current)) {
	      foundObjects += current
	      
	      q ++= objectFields(current)
      }
    }
      
    foundObjects.toList
  }
  
  private def objectFields(obj : AnyRef) : Seq[AnyRef] = {
    val anyRefFieldValues = obj.getClass().getDeclaredFields().filter { field => isObject(field, obj)}.map { f =>
      f.setAccessible(true)
      f.get(obj)
    }
    anyRefFieldValues.filter(_!=null)
    
  }
  
  private def isObject(field :Field, obj : AnyRef) : Boolean = {
    field.setAccessible(true)
    val value = field.get(obj)
    
    val isObject = !primitives.isDefinedAt(value)
    isObject
  }
  
  private def primitives :PartialFunction[Any,Boolean]=  {
    case z : Boolean => true
    case b : Byte => true
    case s : Short => true
    case i : Int => true
    case l : Long => true
    case f : Float => true
    case d : Double => true
    case c : Char => true
    case s : String => true
  }
  
}
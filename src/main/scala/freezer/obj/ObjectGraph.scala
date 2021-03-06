package freezer.obj
import java.lang.reflect.Field
import scala.collection.mutable
import scala.collection.mutable.ArrayBuilder
import freezer.serialisers.TypeRegisterSerialiser
import freezer.serialisers.ObjectIndexSerialiser

class ObjectGraph(val root:AnyRef) {
  def allObjects = {
    var q = new mutable.Queue[AnyRef]
	var foundObjects = mutable.LinkedHashSet[SystemReference]()

	q.enqueue(root)
    
    while(!q.isEmpty) {
      val current = q.dequeue()
      if(!foundObjects.contains(current)) {
	      foundObjects += current
	      
	      q ++= objectFields(current)
      }
    }
      
    foundObjects.toList.map{_.obj}
  }
  
  def types = new TypeRegister ++= allObjects.map { _.getClass}
  
  def index = new ObjectIndex ++= allObjects.map { SystemReference(_)}
  
  def freeze(serialisationFunction : Any => Array[Byte]): Array[Byte] = {
    val builder = ArrayBuilder.make[Byte]

    builder ++= new TypeRegisterSerialiser().store(types)
    builder ++= new ObjectIndexSerialiser(types).store(index)
    builder ++= serialiseObjects(serialisationFunction).flatten
    builder.result()
  }
  
  private def serialiseObjects(serialisationFunction: Any => Array[Byte]) = {
    index.objs.toArray.par.map { sr => serialiseObject(sr, serialisationFunction) }.seq
  }
  
  private def serialiseObject(obj: AnyRef, serialisationFunction: Any => Array[Byte]): Array[Byte] = {
    val fields = obj.getClass().getDeclaredFields()
    fields.flatMap(field => serialiseField(serialisationFunction,field, obj))
  }
  
  private def objectFields(obj : AnyRef) : Seq[AnyRef] = {
    val anyRefFieldValues = obj.getClass().getDeclaredFields().filter { field => isObject(field, obj)}.map { f =>
      f.setAccessible(true)
      f.get(obj)
    }
    anyRefFieldValues.filter(_!=null)
  }
  
  private def serialiseField(serialisationFunction: Any => Array[Byte],f: Field, obj: Any): Array[Byte] = {
    f.setAccessible(true)
    val value = f.get(obj)

    serialisationFunction(value)
  }
  
  private def isObject(field :Field, obj : AnyRef) : Boolean = {
    field.setAccessible(true)
    val value = field.get(obj)
    
    !primitives(value)
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
    case _ => false
  }
}
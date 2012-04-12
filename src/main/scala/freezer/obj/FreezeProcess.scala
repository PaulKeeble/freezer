package freezer.obj
import freezer.serialisers.DefaultInlineSerialisers
import java.lang.reflect.Field

class FreezeProcess(val serialisers : Function[Any,Option[Array[Byte]]]) {
  def serialiseField(f : Field, obj : Any) : Array[Byte] = {
    f.setAccessible(true)
    val value = f.get(obj)
    
    serialisers(value) match {
      case Some(x) => x
      case None => throw new RuntimeException("Not supported yet")
    }
  }
  
  def serialiseObject(root: AnyRef) : Array[Array[Byte]] = {
    val fields = root.getClass().getDeclaredFields()
    fields.map {f => serialiseField(f,root)}
  }
}
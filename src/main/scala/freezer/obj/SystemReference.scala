package freezer.obj

class SystemReference(val obj: AnyRef) {
  override def hashCode = System.identityHashCode(obj)

  override def equals(o: Any) = {
    val rhsObj = o.asInstanceOf[SystemReference].obj

    obj eq rhsObj
  }
}

object SystemReference {
  implicit def wrap(o:AnyRef) = new SystemReference(o)
  
  implicit def unwrap(ref:SystemReference) :AnyRef = ref.obj
  
  implicit def wrapOption(o:AnyRef) = Some(wrap(o))
  
  def apply(o : AnyRef) = wrap(o)
}
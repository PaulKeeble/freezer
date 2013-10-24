package freezer.collection

import math.{max,min}
import scala.compat.Platform
import scala.reflect.ClassTag
import scala.language.implicitConversions

class ArrayView[T](val array:Array[T],val start:Int, val end:Int) {
  
  lazy val length = end - start
  
  def this(array:Array[T]) = this(array,0,array.length)
  
  def apply(index:Int) = array(start+index)
  
  def drop(x:Int) : ArrayView[T] = new ArrayView(array,start+x,end)
  
  def take(x:Int) : ArrayView[T] = new ArrayView(array,start,start+x)
  
  def splitAt(idx:Int) : (ArrayView[T],ArrayView[T]) =(take(idx),drop(idx))
  
  def toArray(implicit mf :ClassTag[T]) : Array[T] = {
    val len = length
    val clone = new Array[T](len)
    Platform.arraycopy(array,start,clone,0,len)
    clone
  }
}

object ArrayView {
  implicit def arrayToView[T](a:Array[T]) :ArrayView[T] = new ArrayView(a)
  
  implicit def viewToArray[T](view:ArrayView[T])(implicit mf : ClassTag[T]) : Array[T] = view.toArray
}